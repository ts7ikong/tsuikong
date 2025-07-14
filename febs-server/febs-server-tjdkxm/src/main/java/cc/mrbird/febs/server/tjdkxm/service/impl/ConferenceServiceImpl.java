package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceService;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ConferenceService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ConferenceServiceImpl extends ServiceImpl<ConferenceMapper, Conference> implements ConferenceService {

    private final ConferenceMapper conferenceMapper;
    private final CacheableService cacheableService;
    private final ConferenceUserService conferenceUserService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Conference> findConferences(QueryRequest request, Conference.Params conference) {
        QueryWrapper<Conference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.orderByAsc("CONFERENCE_TYPE");
        queryWrapper.orderByAsc("CONFERENCE_TIME");
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.MANAGEMENT_MEETINGS_ID);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.and(wapper -> {
                // 创建人是自己 参加会议中有自己
                wapper.eq("CONFERENCE_CREATEUSERID", userId);
                wapper.or().inSql("CONFERENCE_ID",
                    "select CONFERENCE_ID from p_conference_user where USER_ID=" + userId);
            });
        }
        if (StringUtils.isNotEmpty(conference.getConferenceTheme())) {
            queryWrapper.and(wapper -> wapper.like("CONFERENCE_THEME", conference.getConferenceTheme()).or()
                .like("CONFERENCE_THEME", conference.getConferenceTheme()));
        }
        if (StringUtils.isNotEmpty(conference.getStartTime())) {
            queryWrapper.ge("CONFERENCE_TIME", conference.getStartTime());
        }
        if (StringUtils.isNotEmpty(conference.getEndTime())) {
            queryWrapper.le("CONFERENCE_TIME", conference.getEndTime());
        }
        Integer integer = this.conferenceMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("CONFERENCE_ID");
        IPage<Conference> page = this.conferenceMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        return new MyPage<>(page);
    }

    /**
     * 自己的（或管理员）才能删除
     *
     * @param record {@link Partylearn}
     */
    private void isNotModify(Conference record) {
        Conference byId = this.getById(record.getConferenceId());
        cacheableService.hasPermission(byId.getConferenceCreateuserid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createConference(Conference conference) throws FebsException {
        String conferenceUserids = conference.getConferenceUserids();

        String[] split = null;
        if (!StringUtils.isEmpty(conferenceUserids)) {
            split = conferenceUserids.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        conference.setConferenceCreateuserid(FebsUtil.getCurrentUserId());
        conference.setConferenceType("0");
        this.save(conference);
        List<ConferenceUser> conferences = new ArrayList<>();
        if (split != null && split.length > 0) {
            for (String s : split) {
                ConferenceUser conferenceUser = new ConferenceUser();
                conferenceUser.setTableId(conference.getConferenceId());
                conferenceUser.setUserId(Long.valueOf(s));
                conferences.add(conferenceUser);
            }
            conferenceUserService.saveBatch(conferences, conferences.size());
        }
        logRecordContext.putVariable("id", conference.getConferenceId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConference(Conference conference) throws FebsException {
        isNotModify(conference);
        String safeproblenCheckuserid = conference.getConferenceUserids();
        if (!StringUtils.isEmpty(safeproblenCheckuserid)) {
            String[] split = safeproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            conferenceUserService.remove(
                new LambdaQueryWrapper<ConferenceUser>().eq(ConferenceUser::getTableId, conference.getConferenceId()));
            List<ConferenceUser> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                ConferenceUser qualityproblemUser = new ConferenceUser();
                qualityproblemUser.setTableId(conference.getConferenceId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            conferenceUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        this.updateById(conference);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void conferenceStart(Long conferenceId) throws FebsException {
        Conference conference = conferenceMapper
            .selectOne(new LambdaQueryWrapper<Conference>().eq(Conference::getConferenceId, conferenceId)
                .eq(Conference::getIsDelete, 0).notIn(Conference::getConferenceType, "2", "3"));
        if (conference != null) {
            if (!"1".equals(conference.getConferenceType())) {
                conference.setConferenceType("1");
                this.updateById(conference);
            }
        } else {
            throw new FebsException("会议已结束/已过期");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void conferenceEnd(Long conferenceId) throws FebsException {
        Conference conference = conferenceMapper
            .selectOne(new LambdaQueryWrapper<Conference>().eq(Conference::getConferenceId, conferenceId)
                .eq(Conference::getIsDelete, 0).notIn(Conference::getConferenceType, "2", "3"));
        if (conference != null) {
            conference.setConferenceType("2");
            this.updateById(conference);
        } else {
            throw new FebsException("会议已经结束");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConference(Conference conference) throws FebsException {
        isNotModify(conference);
        this.update(null, new LambdaUpdateWrapper<Conference>()
            .eq(Conference::getConferenceId, conference.getConferenceId()).set(Conference::getIsDelete, 1));
    }
}
