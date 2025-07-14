package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.SafeplanUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.SafeproblemUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.SafeplanMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.IUserService;
import cc.mrbird.febs.server.tjdkxm.service.SafeplanService;
import cc.mrbird.febs.server.tjdkxm.service.SafeplanUserService;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * SafeplanService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SafeplanServiceImpl extends ServiceImpl<SafeplanMapper, Safeplan> implements SafeplanService {
    @Autowired
    private SafeplanMapper safeplanMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private SafeplanUserService safeplanUserService;
    @Autowired
    private IUserService userService;

    @Override
    public MyPage<Safeplan> findSafeplans(QueryRequest request, Safeplan.Params safeplan, String type)
        throws FebsException {
        QueryWrapper<Safeplan> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.SINSPECTION_PLAN_ID);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        getSqlParams(request, safeplan, queryWrapper, userAuth, userId, type);
        return getSafeplanMyPage(request, queryWrapper, userAuth);
    }

    private MyPage<Safeplan> getSafeplanMyPage(QueryRequest request, QueryWrapper<Safeplan> queryWrapper,
        AuthUserModel userAuth) {
        Integer integer = this.safeplanMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("SAFEPLAN_ID");
        IPage<Safeplan> page = this.safeplanMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        return new MyPage<>(page);
    }

    @Override
    public MyPage<?> findSafeplanRecords(QueryRequest request, Safeplan.Params safeplan, String type)
        throws FebsException {
        QueryWrapper<Safeplan> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(MenuConstant.INSPECTION_RECORDQ_ID);
        getSqlParamRecords(request, safeplan, queryWrapper, userAuth, userId, type);
        return getSafeplanMyPage(request, queryWrapper, userAuth);
    }

    private void getSqlParamRecords(QueryRequest request, Safeplan.Params safeplan, QueryWrapper<Safeplan> queryWrapper,
        AuthUserModel userAuth, Long userId, String type) throws FebsException {
        getParam(request, safeplan, queryWrapper, userAuth);
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "0":
                    queryWrapper.and(wapper -> wapper.in("SAFEPLAN_CHECKSTATE", "2", "3"));
                    break;
                case "1":
                    queryWrapper.eq("SAFEPLAN_MAKERUSERID", userId);
                    break;
                case "3":
                    queryWrapper.and(wapper -> {
                        wapper.in("SAFEPLAN_CHECKSTATE", "2", "3").eq("SAFEPLAN_ACCEPTANCEUSERID", userId);
                    });
                    break;
                case "2":
                    queryWrapper.and(wapper -> {
                        wapper.in("SAFEPLAN_CHECKSTATE", "2", "3").inSql("SAFEPLAN_ID",
                            "select TABLE_ID from  p_safeplan_user where USER_ID=" + userId);
                    });
                    break;
                default:
                    throw new FebsException("参数不合法");
            }
        } else {
            throw new FebsException("参数不能为null");
        }
    }

    private void getSqlParams(QueryRequest request, Safeplan.Params safeplan, QueryWrapper<Safeplan> queryWrapper,
        AuthUserModel userAuth, Long userId, String type) throws FebsException {
        getParam(request, safeplan, queryWrapper, userAuth);
        String state = safeplan.getSafeplanCheckstate();
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            if (StringUtils.isNotBlank(state)) {
                if ("0".equals(state)) {
                    queryWrapper.inSql("SAFEPLAN_ID", "select TABLE_ID from  p_safeplan_user where USER_ID=" + userId);
                }
                if ("1".equals(state)) {
                    queryWrapper.eq("SAFEPLAN_MAKERUSERID", userId);
                }
            }
        }
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "0":
                    queryWrapper.and(wapper -> wapper.eq("SAFEPLAN_CHECKSTATE", "0"));
                    break;
                case "1":
                    queryWrapper.and(wapper -> wapper.eq("SAFEPLAN_CHECKSTATE", "1"));
                    break;
                case "2":
                    queryWrapper.and(wapper -> {
                        wapper.eq("SAFEPLAN_CHECKSTATE", "0").inSql("SAFEPLAN_ID",
                            "select TABLE_ID from  p_safeplan_user where USER_ID=" + userId);
                    });
                    break;
                case "3":
                    queryWrapper.and(wapper -> {
                        wapper.eq("SAFEPLAN_CHECKSTATE", "1").eq("SAFEPLAN_ACCEPTANCEUSERID", userId);
                    });
                    break;
                case "4":
                    queryWrapper.in("SAFEPLAN_CHECKSTATE", "0", "1");
                    break;
                case "5":
                    queryWrapper.and(wapper -> {
                        wapper.and(wapper1 -> wapper1.eq("SAFEPLAN_CHECKSTATE", "0").inSql("SAFEPLAN_ID",
                            "select TABLE_ID from  p_safeplan_user where USER_ID=" + userId));
                        wapper.or(wapper1 -> {
                            wapper1.eq("SAFEPLAN_CHECKSTATE", "1").eq("SAFEPLAN_MAKERUSERID", userId);
                        });
                    });
                    break;
                default:
                    throw new FebsException("参数不合法");
            }
        }
    }

    private void getParam(QueryRequest request, Safeplan.Params safeplan, QueryWrapper<Safeplan> queryWrapper,
        AuthUserModel userAuth) {
        queryWrapper.orderByAsc("SAFEPLAN_CHECKSTATE");
        queryWrapper.orderByDesc("SAFEPLAN_ID");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("SAFEPLAN_PROJECTID", userAuth.getProjectIds());
        }
        queryWrapper.eq("IS_DELETE", 0);
        if (StringUtils.isNotBlank(safeplan.getStartChecktime())) {
            queryWrapper.ge("SAFEPLAN_CHECKTIME", safeplan.getStartChecktime());
        }
        if (StringUtils.isNotBlank(safeplan.getEndChecktime())) {
            queryWrapper.le("SAFEPLAN_CHECKTIME", safeplan.getEndChecktime());
        }
        if (StringUtils.isNotBlank(safeplan.getStartActualtime())) {
            queryWrapper.ge("SAFEPLAN_ACTUALTIME", safeplan.getStartActualtime());
        }
        if (StringUtils.isNotBlank(safeplan.getEndActualtime())) {
            queryWrapper.le("SAFEPLAN_ACTUALTIME", safeplan.getEndActualtime());
        }
        if (StringUtils.isNotBlank(safeplan.getSafeplanChecktype())) {
            queryWrapper.eq("SAFEPLAN_CHECKTYPE", safeplan.getSafeplanChecktype());
        }
        if (StringUtils.isNotBlank(safeplan.getSafeplanCheckstate())) {
            queryWrapper.eq("SAFEPLAN_CHECKSTATE", safeplan.getSafeplanCheckstate());
        }
        if (StringUtils.isNotBlank(safeplan.getSafeplanCheckUsername())) {
            queryWrapper.and(wrapper -> {
                String s = safeplan.getSafeplanCheckUsername().replaceAll("\\,", "");
                wrapper.inSql("SAFEPLAN_ID",
                    "select q.TABLE_ID from t_user t LEFT JOIN p_safeplan_user q on q.USER_ID=t.USER_ID "
                        + "where t.`STATUS`<> 2 and REALNAME LIKE CONCAT('%','" + s + "','%')");
                wrapper.or().like("SAFEPLAN_CHECKFOREIGNUSER", s);
            });
        }
        if (StringUtils.isNotBlank(safeplan.getPscName())) {
            String pscName = safeplan.getPscName();
            queryWrapper.and(wrapper -> {
                wrapper.or().inSql("SAFEPLAN_PROJECTID",
                    "select PROJECT_ID from p_project where PROJECT_NAME " + "LIKE CONCAT('%','" + pscName + "','%')");
                // wrapper.inSql("SAFEPLAN_UNITENGINEID",
                // "select UNIT_ID from p_unitengine where IS_DELETE=0 and UNIT_NAME like CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().inSql("SAFEPLAN_PARCELID",
                // "SELECT PARCEL_ID from p_parcel where IS_DELETE=0 and PARCEL_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().inSql("SAFEPLAN_SUBITEMID",
                // "SELECT SUBITEM_ID from p_subitem where IS_DELETE=0 and SUBITEM_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                String s = pscName.replaceAll("\\,", "");
                wrapper.or().like("SAFEPLAN_CHECKFOREIGNUSER", s);
            });
        }
    }

    @Override
    public List<Safeplan> findSafeplans(Safeplan safeplan) {
        LambdaQueryWrapper<Safeplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Safeplan::getSafeplanId, 0);
        List<Safeplan> list = this.list(queryWrapper);
        // 取出其中的整改人Id
        List<Long> userIds = list.stream().map(Safeplan::getSafeplanLocaluserid).collect(Collectors.toList());
        // 根据整改人Id查询整改人信息
        List<SystemUser> systemUsers = userService.listByIds(userIds);
        // 将整改人信息放入整改人Id对应的整改人中
        list.forEach(safeplan1 -> {
            systemUsers.forEach(systemUser -> {
                if (safeplan1.getSafeplanLocaluserid().equals(systemUser.getUserId())) {
                    safeplan1.setSafeplanLocalusername(systemUser.getRealname());
                }
            });
        });

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSafeplan(Safeplan safeplan) throws FebsException {
        FebsUtil.isProjectNotNull(safeplan.getSafeplanProjectid());
        String safeplanSafeplanCheckuserid = safeplan.getSafeplanCheckuserid();
        String[] split = null;
        if (!StringUtils.isEmpty(safeplanSafeplanCheckuserid)) {
            split = safeplanSafeplanCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        safeplan.setSafeplanMakeruserid(FebsUtil.getCurrentUserId());
        safeplan.setSafeplanMakertime(new Date());
        safeplan.setSafeplanCheckstate("0");
        if (safeplan.getSafeplanChecktime().compareTo(new Date(System.currentTimeMillis() - 1000 * 60)) <= 0) {
            throw new FebsException("计划检查时间应在当前时间之后");
        }
        this.save(safeplan);
        List<SafeplanUser> safeplanUsers = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                SafeplanUser safeplanUser = new SafeplanUser();
                safeplanUser.setTableId(safeplan.getSafeplanId());
                safeplanUser.setUserId(Long.valueOf(s));
                safeplanUsers.add(safeplanUser);
            }
        }
        safeplanUserService.saveBatch(safeplanUsers, safeplanUsers.size());
        logRecordContext.putVariable("id", safeplan.getSafeplanId());
        logRecordContext.putVariable("projectId", safeplan.getSafeplanProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSafeplan(Safeplan safeplan) throws FebsException {
        if ("3".equals(safeplan.getSafeplanCheckstate())) {
            safeplan.setSafeplanCheckstate("0");
            safeplan.setSafeplanCheckimg("");
        }
        String safeproblenCheckuserid = safeplan.getSafeplanCheckuserid();
        if (!StringUtils.isEmpty(safeproblenCheckuserid)) {
            String[] split = safeproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            safeplanUserService
                .remove(new LambdaQueryWrapper<SafeplanUser>().eq(SafeplanUser::getTableId, safeplan.getSafeplanId()));
            List<SafeplanUser> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                SafeplanUser qualityproblemUser = new SafeplanUser();
                qualityproblemUser.setTableId(safeplan.getSafeplanId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            safeplanUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        if ("1".equals(safeplan.getSafeplanCheckstate())) {
            if (safeplan.getSafeplanIslocal().equals("1")) {
                safeplan.setSafeplanLocaluserid(FebsUtil.getCurrentUserId());
            }
        }
        this.updateById(safeplan);
        Safeplan safeplan1 = safeplanMapper.selectById(safeplan.getSafeplanId());
        logRecordContext.putVariable("projectId", safeplan1.getSafeplanProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSafeplan(Safeplan safeplan) throws Exception {
        if (safeplan.getSafeplanId() == null) {
            throw new Exception("");
        }
        Safeplan safeplan1 = safeplanMapper.selectById(safeplan.getSafeplanId());
        cacheableService.hasPermission(safeplan1.getSafeplanMakeruserid(), MenuConstant.SINSPECTION_PLAN_ID,
            ButtonConstant.BUTTON_175_ID);
        this.update(new LambdaUpdateWrapper<Safeplan>().eq(Safeplan::getSafeplanId, safeplan.getSafeplanId())
            .set(Safeplan::getIsDelete, 1));
        logRecordContext.putVariable("projectId", safeplan1.getSafeplanProjectid());
    }

}
