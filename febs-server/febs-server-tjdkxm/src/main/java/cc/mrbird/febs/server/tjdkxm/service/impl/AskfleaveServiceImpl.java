package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.DateUtil;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.AskfleaveMapper;
import cc.mrbird.febs.server.tjdkxm.service.AskfleaveService;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.IUserService;
import cc.mrbird.febs.server.tjdkxm.util.BeanDiffUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AskfleaveService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:05
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AskfleaveServiceImpl extends ServiceImpl<AskfleaveMapper, Askfleave> implements AskfleaveService {

    private final AskfleaveMapper askfleaveMapper;
    private final CacheableService cacheableService;
    private final LogRecordContext logRecordContext;


    @Autowired
    private IUserService userService;

    @Override
    public IPage<Askfleave> findAskfleaves(QueryRequest request, Askfleave askfleave) {
        QueryWrapper<Askfleave> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        getParams(request, askfleave, queryWrapper, userId);
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.LEAVE_APPLICATION_ID);
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.eq("ASKFLEAVE_CREATEUSERID", userId);
        }
        return getAskfleaveMyPage(request, queryWrapper);
    }

    /**
     * 查询待审批 [需要有审批权限]
     *
     * @param request
     * @param askfleave
     * @return {@link IPage<?>}
     */
    @Override
    public IPage<Askfleave> findAskfleaveApprovals(QueryRequest request, Askfleave askfleave) throws FebsException {
        QueryWrapper<Askfleave> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        cacheableService.getAuthUser(userId, MenuConstant.LEAVE_APPROVAL_ID);
        getParams(request, askfleave, queryWrapper, userId);
        // 这里要防止sql注入
        queryWrapper.apply("FIND_IN_SET({0}, ASKFLEAVE_CHECKUSERID)", userId);
        return getAskfleaveMyPage(request, queryWrapper);
    }

    private IPage<Askfleave> getAskfleaveMyPage(QueryRequest request, QueryWrapper<Askfleave> queryWrapper) {
        return this.askfleaveMapper.selectPageInfo1(new Page<>(request.getPageNum(), request.getPageSize()),
                queryWrapper);
    }

    private void getParams(QueryRequest request, Askfleave askfleave, QueryWrapper<Askfleave> queryWrapper,
                           Long userId) {
        queryWrapper.ne("IS_DELETE", 1);
        // 查询需要自己审批的数据
        if (StringUtils.isNotEmpty(askfleave.getAskfleaveCause())) {
            queryWrapper.and(wapper -> {
                wapper.like("ASKFLEAVE_CAUSE", askfleave.getAskfleaveCause());
                wapper.or().inSql("ASKFLEAVE_CREATEUSERID",
                        "SELECT u.USER_ID FROM t_user u WHERE u.REALNAME like CONCAT('%','" + askfleave.getAskfleaveCause()
                                + "','%')");
            });
        }
        if (StringUtils.isNotEmpty(askfleave.getStartCreateTime())) {
            queryWrapper.ge("ASKFLEAVE_CREATETIME", askfleave.getStartCreateTime());
        }
        if (StringUtils.isNotEmpty(askfleave.getEndCreateTime())) {
            queryWrapper.le("ASKFLEAVE_CREATETIME", askfleave.getEndCreateTime());
        }
        if (StringUtils.isNotEmpty(askfleave.getAskfleaveCheckstate())) {
            queryWrapper.eq("ASKFLEAVE_CHECKSTATE", askfleave.getAskfleaveCheckstate());
        }
    }

    @Override
    public List<Askfleave> findAskfleaves(Askfleave askfleave) {
        LambdaQueryWrapper<Askfleave> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Askfleave::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAskfleave(Askfleave askfleave) {
        askfleave.setAskfleaveCreateuserid(FebsUtil.getCurrentUserId());
        askfleave.setAskfleaveCreatetime(new Date());
        askfleave.setAskfleaveCheckstate("0");
        askfleave.setAskfleaveChecknode("1");
        askfleave.setAskfleaveCheckrecord(
                DateUtil.getNowdateTimeToString() + " " + FebsUtil.getCurrentRealname() + "提交了此请假申请。");
        this.save(askfleave);
        logRecordContext.putVariable("id", askfleave.getAskfleaveId());
    }

    /**
     * 修改
     *
     * @param askfleave 请假申请审批表实体类
     * @param approval
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAskfleave(Askfleave askfleave, boolean approval) throws FebsException {
        Askfleave askfleave1 = askfleaveMapper.selectById(askfleave.getAskfleaveId());

        if ("1".equals(askfleave1.getAskfleaveCheckstate())) {
            throw new FebsException("已审批不可修改");
        }

        // 获取当前用户信息
        String currentRealname = FebsUtil.getCurrentRealname();
        String nowTime = DateUtil.getNowdateTimeToString();

        // 使用 Lambda 表达式实现字段格式化逻辑
        BeanDiffUtil.ValueFormatter formatter = (value, fieldName) -> {
            if ("askfleaveCheckuserid".equals(fieldName)) {
                if (value == null) return "空";

                // 先判断类型，再安全转换
                if (value instanceof Number) {
                    Long userId = ((Number) value).longValue();
                    SystemUser user = userService.getById(userId);
                    return user != null ? user.getRealname() : "未知用户";
                } else if (value instanceof String) {
                    try {
                        Long userId = Long.valueOf((String) value);
                        SystemUser user = userService.getById(userId);
                        return user != null ? user.getRealname() : "未知用户";
                    } catch (NumberFormatException e) {
                        return "无效ID";
                    }
                } else {
                    return "未知类型";
                }
            }

            return BeanDiffUtil.defaultFormatValue(value);
        };
        // 比较差异
        Map<String, String> diffMap = BeanDiffUtil.getDiff(askfleave1, askfleave, formatter);

        // 如果有修改内容，则记录到日志中
        if (!diffMap.isEmpty()) {
            StringBuilder modifyDetail = new StringBuilder();
            for (Map.Entry<String, String> entry : diffMap.entrySet()) {
                modifyDetail.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
            }

            String modifyRecord = nowTime + " " + currentRealname + " 修改了此请假申请。修改内容：" + modifyDetail;

            if (askfleave1.getAskfleaveCheckrecord() != null && !askfleave1.getAskfleaveCheckrecord().isEmpty()) {
                askfleave.setAskfleaveCheckrecord(askfleave1.getAskfleaveCheckrecord() + "," + modifyRecord);
            } else {
                askfleave.setAskfleaveCheckrecord(modifyRecord);
            }
        }

        if (approval) {
            Long userId = FebsUtil.getCurrentUserId();
            cacheableService.hasPermission(userId, MenuConstant.LEAVE_APPROVAL_ID, ButtonConstant.BUTTON_211_ID, null);

            if (StringUtils.isNotBlank(askfleave.getAskfleaveCheckstate())) {
                // 处理审批人和审批时间
                if (Strings.isNotEmpty(askfleave1.getAskfleaveCheckuserid())) {
                    askfleave.setAskfleaveCheckuserid(
                            askfleave1.getAskfleaveCheckuserid() + "," + askfleave.getAskfleaveCheckuserid());
                } else {
                    askfleave.setAskfleaveCheckuserid(askfleave.getAskfleaveCheckuserid());
                }

                if (Strings.isNotEmpty(askfleave1.getAskfleaveChecktime())) {
                    askfleave.setAskfleaveChecktime(
                            askfleave1.getAskfleaveChecktime() + "," + nowTime);
                } else {
                    askfleave.setAskfleaveChecktime(nowTime);
                }

                // 记录审批日志
                String reason = askfleave.getAskfleaveCheckreason();
                String approvalRecord = nowTime + " " + currentRealname + " 审批了此请假申请。审批意见:" + (reason != null ? reason : "无");

                askfleave.setAskfleaveCheckrecord(askfleave.getAskfleaveCheckrecord() + "," + approvalRecord);
            } else {
                throw new FebsException("审批出错了");
            }
        } else {
            cacheableService.hasPermission(null, MenuConstant.LEAVE_APPLICATION_ID);
        }

        this.updateById(askfleave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAskfleave(Askfleave askfleave) throws FebsException {
        Askfleave askfleave1 = askfleaveMapper.selectById(askfleave.getAskfleaveId());
        if ("1".equals(askfleave1.getAskfleaveCheckstate())) {
            throw new FebsException("已审批不可删除");
        }
        cacheableService.hasPermission(askfleave1.getAskfleaveCreateuserid(), MenuConstant.LEAVE_APPROVAL_ID,
                MenuConstant.LEAVE_APPLICATION_ID);
        if (askfleave.getIsDelete() == 1) {
            this.update(null, new LambdaUpdateWrapper<Askfleave>()
                    .eq(Askfleave::getAskfleaveId, askfleave.getAskfleaveId()).set(Askfleave::getIsDelete, 2));
        }
        if (askfleave.getIsDelete() == 2) {
            this.update(null,
                    new LambdaUpdateWrapper<Askfleave>().eq(Askfleave::getAskfleaveId, askfleave.getAskfleaveId())
                            .set(Askfleave::getIsDelete, 2).set(Askfleave::getAskfleaveCheckstate, 4)
                            .set(Askfleave::getAskfleaveCheckrecord, askfleave1.getAskfleaveCheckrecord() + ","
                                    + DateUtil.getNowdateTimeToString() + " " + FebsUtil.getCurrentRealname() + "撤销了此请假申请。"));
        }
    }

    /**
     * 未审批
     *
     * @param userId 用户id
     * @return {@link Integer}
     */
    @Override
    public Integer notChecked(Long userId, Long projectId) {
        return askfleaveMapper.selectCount(new LambdaQueryWrapper<Askfleave>().select(Askfleave::getAskfleaveId)
                // 0未检查
                .eq(Askfleave::getAskfleaveCheckstate, 0));
    }
}
