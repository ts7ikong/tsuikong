package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.ParcelUnit;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.QualityplanUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.QualityplanMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.IUserService;
import cc.mrbird.febs.server.tjdkxm.service.QualityplanService;
import cc.mrbird.febs.server.tjdkxm.service.QualityplanUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * QualityplanService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:02
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QualityplanServiceImpl extends ServiceImpl<QualityplanMapper, Qualityplan> implements QualityplanService {
    @Autowired
    private QualityplanMapper qualityplanMapper;
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;
    @Autowired
    private QualityplanUserService qualityplanUserService;
    @Autowired
    private IUserService userService;

    @Override
    public MyPage<Qualityplan> findQualityplans(QueryRequest request, Qualityplan.Params qualityplan, String type)
        throws FebsException {
        QueryWrapper<Qualityplan> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(userId, MenuConstant.INSPECTION_PLAN_ID);
        getSqlParams(request, qualityplan, queryWrapper, userAuth, userId, type);
        return getQualityplanMyPage(request, queryWrapper, userAuth);
    }

    private MyPage<Qualityplan> getQualityplanMyPage(QueryRequest request, QueryWrapper<Qualityplan> queryWrapper,
        AuthUserModel userAuth) {
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        Integer integer = this.qualityplanMapper.selectCount(queryWrapper.select("1"));
        if (integer == null || integer == 0) {
            return new MyPage<>();
        }
        queryWrapper.groupBy("QUALITYPLAN_ID");
        IPage<Qualityplan> page = this.qualityplanMapper
            .selectPageInfo(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper);
        page.setTotal(integer);
        return new MyPage<>(page);
    }

    @Override
    public MyPage<?> findSafeplanRecords(QueryRequest request, Qualityplan.Params qualityplan, String type)
        throws FebsException {
        QueryWrapper<Qualityplan> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        AuthUserModel userAuth = cacheableService.getAuthUser(userId, MenuConstant.INSPECTION_RECORD_ID);
        getSqlParamRecords(request, qualityplan, queryWrapper, userAuth, userId, type);
        return getQualityplanMyPage(request, queryWrapper, userAuth);
    }

    private void getSqlParamRecords(QueryRequest request, Qualityplan.Params qualityplan,
        QueryWrapper<Qualityplan> queryWrapper, AuthUserModel userAuth, Long userId, String type) throws FebsException {
        getParam(request, qualityplan, queryWrapper, userAuth);
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isNotBlank(type)) {
                switch (type) {
                    case "0":
                        queryWrapper.and(wapper -> wapper.in("QUALITYPLAN_CHECKSTATE", 2, 3));
                        break;
                    case "1":
                        queryWrapper.eq("QUALITYPLAN_MAKERUSERID", userId);
                        break;
                    case "3":
                        queryWrapper.and(wapper -> {
                            wapper.in("QUALITYPLAN_CHECKSTATE", "2", "3").eq("QUALITYPLAN_ACCEPTANCEUSERID", userId);
                        });
                        break;
                    case "2":
                        queryWrapper.and(wapper -> {
                            wapper.in("QUALITYPLAN_CHECKSTATE", "2", "3").inSql("QUALITYPLAN_ID",
                                "select TABLE_ID from  p_qualityplan_user where USER_ID=" + userId);
                        });
                        break;
                    default:
                        throw new FebsException("参数不合法");
                }
            } else {
                throw new FebsException("参数不能为null");
            }
        } else {
            throw new FebsException("参数不能为null");
        }
    }

    private void getSqlParams(QueryRequest request, Qualityplan.Params qualityplan,
        QueryWrapper<Qualityplan> queryWrapper, AuthUserModel userAuth, Long userId, String type) throws FebsException {
        getParam(request, qualityplan, queryWrapper, userAuth);
        String state = qualityplan.getQualityplanCheckstate();
        if (userAuth.getKey() != 1 && userAuth.getKey() != 2) {
            if (StringUtils.isNotBlank(state)) {
                if ("0".equals(state)) {
                    queryWrapper.inSql("QUALITYPLAN_ID",
                        "select TABLE_ID from  p_qualityplan_user where USER_ID=" + userId);
                }
                if ("1".equals(state)) {
                    queryWrapper.eq("QUALITYPLAN_MAKERUSERID", userId);
                }
            }
        }
        if (StringUtils.isNotBlank(type)) {
            // 全部代实施 0; 全部待验收 1; 我的代实施 2;我的代验收 3;全部 4 ;我的全部 5
            switch (type) {
                case "0":
                    queryWrapper.and(wapper -> wapper.eq("QUALITYPLAN_CHECKSTATE", "0"));
                    break;
                case "1":
                    queryWrapper.and(wapper -> wapper.eq("QUALITYPLAN_CHECKSTATE", "1"));
                    break;
                case "2":
                    queryWrapper.and(wapper -> {
                        wapper.eq("QUALITYPLAN_CHECKSTATE", "0").inSql("QUALITYPLAN_ID",
                            "select TABLE_ID from  p_qualityplan_user where USER_ID=" + userId);
                    });
                    break;
                case "3":
                    queryWrapper.and(wapper -> {
                        wapper.eq("QUALITYPLAN_CHECKSTATE", "1").eq("QUALITYPLAN_ACCEPTANCEUSERID", userId);
                    });
                    break;
                case "4":
                    queryWrapper.in("QUALITYPLAN_CHECKSTATE", "0", "1");
                    break;
                case "5":
                    queryWrapper.and(wapper -> {
                        wapper.and(wapper1 -> wapper1.eq("QUALITYPLAN_CHECKSTATE", "0").inSql("QUALITYPLAN_ID",
                            "select TABLE_ID from  p_qualityplan_user where USER_ID=" + userId));
                        wapper.or(
                            wapper1 -> wapper1.eq("QUALITYPLAN_CHECKSTATE", "1").eq("QUALITYPLAN_MAKERUSERID", userId));
                    });
                    break;
                default:
                    throw new FebsException("参数不合法");
            }
        } else {
            if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
                queryWrapper.and(wapper -> {
                    wapper.eq("QUALITYPLAN_MAKERUSERID", userId).or().inSql("QUALITYPLAN_ID",
                        "select TABLE_ID from  p_qualityplan_user where USER_ID=" + userId);

                });
            }
        }
    }

    private void getParam(QueryRequest request, Qualityplan.Params qualityplan, QueryWrapper<Qualityplan> queryWrapper,
        AuthUserModel userAuth) {
        queryWrapper.orderByAsc("QUALITYPLAN_CHECKSTATE");
        queryWrapper.orderByDesc("QUALITYPLAN_ID");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        Set<Long> projectIds = userAuth.getProjectIds();
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            queryWrapper.in("QUALITYPLAN_PROJECTID", projectIds);
        }
        queryWrapper.eq("IS_DELETE", 0);
        if (StringUtils.isNotBlank(qualityplan.getStartChecktime())) {
            queryWrapper.ge("QUALITYPLAN_CHECKTIME", qualityplan.getStartChecktime());
        }
        if (StringUtils.isNotBlank(qualityplan.getEndChecktime())) {
            queryWrapper.le("QUALITYPLAN_CHECKTIME", qualityplan.getEndChecktime());
        }
        if (StringUtils.isNotBlank(qualityplan.getQualityplanChecktype())) {
            queryWrapper.eq("QUALITYPLAN_CHECKTYPE", qualityplan.getQualityplanChecktype());
        }
        if (StringUtils.isNotBlank(qualityplan.getStartActualtime())) {
            queryWrapper.ge("QUALITYPLAN_ACTUALTIME", qualityplan.getStartActualtime());
        }
        if (StringUtils.isNotBlank(qualityplan.getEndActualtime())) {
            queryWrapper.le("QUALITYPLAN_ACTUALTIME", qualityplan.getEndActualtime());
        }
        if (StringUtils.isNotBlank(qualityplan.getQualityplanCheckstate())) {
            queryWrapper.eq("QUALITYPLAN_CHECKSTATE", qualityplan.getQualityplanCheckstate());
        }
        if (StringUtils.isNotBlank(qualityplan.getQualityplanCheckusername())) {
            queryWrapper.and(wrapper -> {
                String s = qualityplan.getQualityplanCheckusername().replaceAll("\\,", "");
                wrapper.inSql("QUALITYPLAN_ID",
                    "select q.TABLE_ID from t_user t LEFT JOIN p_qualityplan_user q on q.USER_ID=t.USER_ID "
                        + "where t.`STATUS`<> 2 and REALNAME LIKE CONCAT('%','" + s + "','%')");
                wrapper.or().like("QUALITYPLAN_CHECKFOREIGNUSER", s);
            });
        }
        if (StringUtils.isNotBlank(qualityplan.getPscName())) {
            String pscName = qualityplan.getPscName();
            queryWrapper.and(wrapper -> {
                // 级联项目名称
                wrapper.or().inSql("QUALITYPLAN_PROJECTID",
                    "select PROJECT_ID from p_project where PROJECT_NAME " + "LIKE CONCAT('%','" + pscName + "','%')");
                // wrapper.inSql("QUALITYPLAN_UNITENGINEID",
                // "select UNIT_ID from p_unitengine where IS_DELETE=0 and UNIT_NAME like CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().inSql("QUALITYPLAN_PARCELID",
                // "SELECT PARCEL_ID from p_parcel where IS_DELETE=0 and PARCEL_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
                // wrapper.or().inSql("QUALITYPLAN_SUBITEMID",
                // "SELECT SUBITEM_ID from p_subitem where IS_DELETE=0 and SUBITEM_NAME LIKE CONCAT('%','" + pscName +
                // "','%')");
            });
        }
    }

    @Override
    public List<Qualityplan> findQualityplans(Qualityplan qualityplan) {
        LambdaQueryWrapper<Qualityplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Qualityplan::getIsDelete, 0);
        List<Qualityplan> qualityplans = this.qualityplanMapper.selectList(queryWrapper);
        // 取出其中的整改人用户ID
        List<Long> userIds = qualityplans.stream().map(Qualityplan::getQualityplanLocaluserid).map(Long::valueOf)
            .collect(Collectors.toList());

        // 根据用户ID查询用户信息
        List<SystemUser> systemUsers = userService.listByIds(userIds);
        // 把用户信息放入到qualityplans中
        qualityplans.forEach(qualityplan1 -> {
            systemUsers.forEach(systemUser -> {
                if (qualityplan1.getQualityplanLocaluserid().equals(systemUser.getUserId())) {
                    qualityplan1.setQualityplanLocalusername(systemUser.getRealname());
                }
            });
        });

        return qualityplans;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createQualityplan(Qualityplan qualityplan) throws FebsException {
        FebsUtil.isProjectNotNull(qualityplan.getQualityplanProjectid());
        String qualityplanCheckuserid = qualityplan.getQualityplanCheckuserid();
        cacheableService.hasPermission(null, MenuConstant.INSPECTION_PLAN_ID, ButtonConstant.BUTTON_156_ID,
            qualityplan.getQualityplanProjectid());
        String[] split = null;
        if (!StringUtils.isEmpty(qualityplanCheckuserid)) {
            split = qualityplanCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("新增失败");
                }
            }
        }
        qualityplan.setQualityplanMakeruserid(FebsUtil.getCurrentUserId());
        qualityplan.setQualityplanMakertime(new Date());
        qualityplan.setQualityplanCheckstate("0");
        if (qualityplan.getQualityplanChecktime().compareTo(new Date(System.currentTimeMillis() - 1000 * 60)) <= 0) {
            throw new FebsException("计划检查时间应在当前时间之后");
        }
        this.save(qualityplan);
        List<QualityplanUser> qualityplanUsers = new ArrayList<>();
        if (split != null) {
            for (String s : split) {
                QualityplanUser qualityplanUser = new QualityplanUser();
                qualityplanUser.setTableId(qualityplan.getQualityplanId());
                qualityplanUser.setUserId(Long.valueOf(s));
                qualityplanUsers.add(qualityplanUser);
            }
            qualityplanUserService.saveBatch(qualityplanUsers, qualityplanUsers.size());
        }
        logRecordContext.putVariable("id", qualityplan.getQualityplanId());
        logRecordContext.putVariable("projectId", qualityplan.getQualityplanProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQualityplan(Qualityplan qualityplan) throws FebsException {
        Qualityplan qualityplan1 = qualityplanMapper.selectById(qualityplan.getQualityplanId());
        if (qualityplan1 == null) {
            throw new FebsException("选择记录");
        }
        if ("3".equals(qualityplan.getQualityplanCheckstate())) {
            qualityplan.setQualityplanCheckstate("0");
            qualityplan.setQualityplanCheckimg("");
        }
        String safeproblenCheckuserid = qualityplan.getQualityplanCheckuserid();
        if (!StringUtils.isEmpty(safeproblenCheckuserid)) {
            String[] split = safeproblenCheckuserid.split(",");
            for (String s : split) {
                if (!StringUtils.isNumeric(s)) {
                    log.error(s + "不是数字");
                    throw new FebsException("修改失败");
                }
            }
            qualityplanUserService.remove(new LambdaQueryWrapper<QualityplanUser>().eq(QualityplanUser::getTableId,
                qualityplan.getQualityplanId()));
            List<QualityplanUser> qualityproblemUsers = new ArrayList<>();
            for (String s : split) {
                QualityplanUser qualityproblemUser = new QualityplanUser();
                qualityproblemUser.setTableId(qualityplan.getQualityplanId());
                qualityproblemUser.setUserId(Long.valueOf(s));
                qualityproblemUsers.add(qualityproblemUser);
            }
            qualityplanUserService.saveBatch(qualityproblemUsers, qualityproblemUsers.size());
        }
        if ("1".equals(qualityplan.getQualityplanCheckstate())) {
            if (qualityplan.getQualityplanIslocal().equals("1")) {
                qualityplan.setQualityplanLocaluserid(FebsUtil.getCurrentUserId());
            }
        }
        this.updateById(qualityplan);
        logRecordContext.putVariable("projectId", qualityplan1.getQualityplanProjectid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQualityplan(Qualityplan qualityplan) throws Exception {
        if (qualityplan.getQualityplanId() == null) {
            throw new Exception("");
        }
        Qualityplan qualityplan1 = qualityplanMapper.selectById(qualityplan.getQualityplanId());
        if (qualityplan1 == null) {
            throw new FebsException("选择记录");
        }
        cacheableService.hasPermission(qualityplan1.getQualityplanMakeruserid(), MenuConstant.INSPECTION_PLAN_ID,
            ButtonConstant.BUTTON_158_ID);
        this.update(new LambdaUpdateWrapper<Qualityplan>()
            .eq(Qualityplan::getQualityplanId, qualityplan.getQualityplanId()).set(Qualityplan::getIsDelete, 1));
        logRecordContext.putVariable("projectId", qualityplan1.getQualityplanProjectid());
    }

    @Override
    public Integer notChecked(Long userId, Long projectId) {
        LambdaQueryWrapper<Qualityplan> eq = new LambdaQueryWrapper<Qualityplan>().select(Qualityplan::getQualityplanId)
            .eq(Qualityplan::getQualityplanProjectid, projectId).eq(Qualityplan::getIsDelete, 0)
            // 0未检查
            .eq(Qualityplan::getQualityplanCheckstate, 0);
        if (userId == null) {
            eq.eq(Qualityplan::getQualityplanCheckuserid, FebsUtil.getCurrentUserId());
        }
        return qualityplanMapper.selectCount(eq);
    }

}
