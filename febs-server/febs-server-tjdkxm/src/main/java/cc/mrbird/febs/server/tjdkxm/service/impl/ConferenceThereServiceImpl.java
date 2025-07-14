package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThereUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceThereMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceThereService;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceThereUserService;
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
import java.util.Date;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 12:04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ConferenceThereServiceImpl extends ServiceImpl<ConferenceThereMapper, ConferenceThere>
    implements ConferenceThereService {

    private final ConferenceThereMapper conferenceThereMapper;
    private final CacheableService cacheableService;
    private final ConferenceThereUserService conferenceThereUserService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<ConferenceThere> findConferenceTheres(QueryRequest request, ConferenceThere conferenceThere) {
        QueryWrapper<ConferenceThere> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("IS_DELETE", 0);
        // queryWrapper.groupBy("CONFERENCE_ID");
        queryWrapper.orderByDesc("CONFERENCE_CREATETIME");
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.THREE_CONFERENCE_ID);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new Page<>();
        }
        if (StringUtils.isNotEmpty(conferenceThere.getConferenceTheme())) {
            queryWrapper.like("CONFERENCE_THEME", conferenceThere.getConferenceTheme());
        }
        if (StringUtils.isNotEmpty(conferenceThere.getConferenceDateStart())) {
            queryWrapper.ge("CONFERENCE_DATE", conferenceThere.getConferenceDateStart());
        }
        if (StringUtils.isNotEmpty(conferenceThere.getConferenceDateEnd())) {
            queryWrapper.lt("CONFERENCE_DATE", conferenceThere.getConferenceDateEnd());
        }
        return this.conferenceThereMapper.selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize()),
            queryWrapper);
    }

    @Override
    public List<ConferenceThere> findConferenceTheres(ConferenceThere conferenceThere) {
        LambdaQueryWrapper<ConferenceThere> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConferenceThere::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createConferenceThere(ConferenceThere conferenceThere) throws FebsException {
        Long userId = FebsUtil.getCurrentUserId();
        conferenceThere.setConferencePerson(FebsUtil.getCurrentRealname());
        conferenceThere.setConferenceCreateid(userId);
        conferenceThere.setConferenceCreatetime(new Date());

        this.save(conferenceThere);

        logRecordContext.putVariable("id", conferenceThere.getConferenceId());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConferenceThere(ConferenceThere conferenceThere) throws FebsException {
        ConferenceThere byId = conferenceThereMapper.selectById(conferenceThere.getConferenceId());
        if (conferenceThere.getConferenceId() == null || byId == null) {
            throw new FebsException("请选择记录");
        }
        cacheableService.hasPermission(byId.getConferenceCreateid());
        this.updateById(conferenceThere);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConferenceThere(ConferenceThere conferenceThere) throws FebsException {
        ConferenceThere byId = this.getById(conferenceThere.getConferenceId());
        if (byId == null) {
            throw new FebsException("");
        }
        cacheableService.hasPermission(byId.getConferenceCreateid());
        this.update(null,
            new LambdaUpdateWrapper<ConferenceThere>()
                .eq(ConferenceThere::getConferenceId, conferenceThere.getConferenceId())
                .set(ConferenceThere::getIsDelete, 1));

    }
}
