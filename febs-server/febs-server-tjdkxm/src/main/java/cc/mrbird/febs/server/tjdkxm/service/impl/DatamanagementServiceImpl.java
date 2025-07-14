package cc.mrbird.febs.server.tjdkxm.service.impl;

import java.util.*;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.DatamanagementMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.DocumentClassMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.DatamanagementService;
import lombok.RequiredArgsConstructor;

/**
 * DatamanagementService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DatamanagementServiceImpl extends ServiceImpl<DatamanagementMapper, Datamanagement>
    implements DatamanagementService {

    private final DatamanagementMapper datamanagementMapper;
    private final DocumentClassMapper documentClassMapper;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;
    private static final List<String> TEMP_NOT_VERIFY = Lists.newArrayList("117", "118", "119");

    @Override
    public MyPage<Datamanagement> findDatamanagements(QueryRequest request, Datamanagement.Params datamanagement) {
        return getPage(request, datamanagement, false);
    }

    private MyPage<Datamanagement> getPage(QueryRequest request, Datamanagement.Params datamanagement, boolean isTemp) {
        Page<Datamanagement> objectPage = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<Datamanagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DATAMANAGEMENT_TYPE", datamanagement.getDatamanagementType());
        Map<String, Integer> extracted = extracted(false, datamanagement.getDatamanagementType());
        AuthUserModel userAuth = cacheableService.getAuthUser(extracted.get("menuId"));
        queryWrapper.eq("IS_DELETE", 0);
        if (AuthUserModel.KEY_NONE.equals(userAuth.getKey())) {
            return new MyPage<>();
        }
        if (!AuthUserModel.KEY_ADMIN.equals(userAuth.getKey())) {
            if (!isTemp) {
                queryWrapper.in("DATAMANAGEMENT_PID", userAuth.getProjectIds());
            }
        }
        queryWrapper.orderByDesc("DATAMANAGEMENT_TIME");
        OrderUtils.setQuseryOrder(queryWrapper, request);
        if (StringUtils.isNotBlank(datamanagement.getDatamanagementName())) {
            queryWrapper.and(wapper -> {
                wapper.like("DATAMANAGEMENT_NAME", datamanagement.getDatamanagementName());
                if (!isTemp) {
                    wapper.or().inSql("DATAMANAGEMENT_PID", "SELECT PROJECT_ID from p_project where IS_DELETE=0 "
                        + "and PROJECT_NAME like CONCAT('%','" + datamanagement.getDatamanagementName() + "','%')");
                }
            });
        }
        // 分类类型
        if (datamanagement.getDatamanagementClassid() != null) {
            queryWrapper.eq("DATAMANAGEMENT_CLASSID", datamanagement.getDatamanagementClassid());
        }
        // 单位项目id
        if (datamanagement.getDangerUnitengineid() != null) {
            queryWrapper.eq("DANGER_UNITENGINEID", datamanagement.getDangerUnitengineid());
        }
        // 单位分部id
        if (datamanagement.getDangerParcelid() != null) {
            queryWrapper.eq("DANGER_PARCELID", datamanagement.getDangerParcelid());
        }
        // 单位分项id
        if (datamanagement.getDangerSubitemid() != null) {
            queryWrapper.eq("DANGER_SUBITEMID", datamanagement.getDangerSubitemid());
        }
        IPage<Datamanagement> page = this.datamanagementMapper.selectPageInfo(objectPage, queryWrapper);
        return new MyPage<>(page);
    }

    /**
     * 查询（分页） 模板
     *
     * @param request QueryRequest
     * @param datamanagement 资料管理实体类
     * @return IPage<Datamanagement>
     */
    @Override
    public MyPage<Datamanagement> findDatamanagementTemps(QueryRequest request, Datamanagement.Params datamanagement) {
        return getPage(request, datamanagement, true);
    }

    /**
     * 自己的（或管理员）
     */
    private void isModify(boolean isDelete, String... ids) {
        List<Datamanagement> datamanagements = datamanagementMapper.selectBatchIds(Arrays.asList(ids));
        for (Datamanagement datamanagement : datamanagements) {
            Map<String, Integer> extracted = extracted(isDelete, datamanagement.getDatamanagementType());
            cacheableService.hasPermission(datamanagement.getDatamanagementUserid(), extracted.get("menuId"),
                extracted.get("buttonId"), datamanagement.getDatamanagementPid());
        }
    }

    private Map<String, Integer> extracted(boolean isDelete, String type) {
        Integer menuId = null, buttonId = null;
        switch (type) {
            // 党建资料
            case "1":
                menuId = MenuConstant.PARTY_MATERIALS_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_86_ID : ButtonConstant.BUTTON_84_ID;
                break;
            // 合同模板
            case "2":
            case "102":
                menuId = MenuConstant.CONTRACTTEMPLATE1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_35_ID : ButtonConstant.BUTTON_32_ID;
                break;
            // 合同档案库
            case "3":
                menuId = MenuConstant.CONTRACT_ARCHIVE_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_294_ID : ButtonConstant.BUTTON_291_ID;
                break;
            // 施工档案
            case "4":
                menuId = MenuConstant.CONSTRUCTION_ARCHIVES_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_111_ID : ButtonConstant.BUTTON_108_ID;
                break;
            // 技术台账
            case "5":
                menuId = MenuConstant.TECHNICAL_ACCOUNT_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_115_ID : ButtonConstant.BUTTON_112_ID;
                break;
            // 技术标准
            case "6":
                menuId = MenuConstant.TECHNICAL_STANDARD_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_119_ID : ButtonConstant.BUTTON_116_ID;
                break;
            // 技术交底
            case "7":
                menuId = MenuConstant.TECHNICAL_DISCLOSURE_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_123_ID : ButtonConstant.BUTTON_120_ID;
                break;
            // 交接资料
            case "8":
                menuId = MenuConstant.HANDOVER_DATA_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_128_ID : ButtonConstant.BUTTON_124_ID;
                break;
            // 设计变更
            case "9":
                menuId = MenuConstant.DESIGN_CHANGE_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_139_ID : ButtonConstant.BUTTON_136_ID;
                break;
            // 工程确认单
            case "10":
                menuId = MenuConstant.CONFIRMATION_SHEET_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_131_ID : ButtonConstant.BUTTON_128_ID;
                break;
            // 工程联络单
            case "11":
                menuId = MenuConstant.LIAISON_SHEET_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_136_ID : ButtonConstant.BUTTON_132_ID;
                break;
            // 测绘资料
            case "12":
                menuId = MenuConstant.SURVEYING_MAPPING_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_143_ID : ButtonConstant.BUTTON_140_ID;
                break;
            // 竣工资料
            case "13":
                menuId = MenuConstant.COMPLETION_DATA_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_35_ID : ButtonConstant.BUTTON_32_ID;
                break;
            // 数字化中心
            case "14":
            case "114":
                menuId = MenuConstant.DIGITAL1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_83_ID : ButtonConstant.BUTTON_80_ID;
                break;
            // 质量规范标准资料
            case "15":
                menuId = MenuConstant.STANDARD_DATA_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_155_ID : ButtonConstant.BUTTON_152_ID;
                break;
            // 照片管理
            case "16":
                menuId = MenuConstant.PHOTO_MANAGEMENT_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_151_ID : ButtonConstant.BUTTON_148_ID;
                break;
            // 施工档案
            case "104":
                menuId = MenuConstant.CONSTRUCTIONARCHIVES1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_43_ID : ButtonConstant.BUTTON_40_ID;
                break;
            // 技术台账
            case "105":
                menuId = MenuConstant.TECHNICALACCOUNT1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_47_ID : ButtonConstant.BUTTON_44_ID;
                break;
            // 技术标准
            case "106":
                menuId = MenuConstant.TECHNICALSTANDARD1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_51_ID : ButtonConstant.BUTTON_48_ID;
                break;
            // 技术交底
            case "107":
                menuId = MenuConstant.TECHNICALDISCLOSURE1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_55_ID : ButtonConstant.BUTTON_52_ID;
                break;
            // 交接资料
            case "108":
                menuId = MenuConstant.HANDOVERDATA1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_59_ID : ButtonConstant.BUTTON_56_ID;
                break;
            // 设计变更
            case "109":
                menuId = MenuConstant.DESIGNCHANGE1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_71_ID : ButtonConstant.BUTTON_68_ID;
                break;
            // 工程确认单
            case "110":
                menuId = MenuConstant.CONFIRMATIONSHEET1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_63_ID : ButtonConstant.BUTTON_60_ID;
                break;
            // 工程联络单
            case "111":
                menuId = MenuConstant.LIAISONSHEET1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_67_ID : ButtonConstant.BUTTON_64_ID;
                break;
            // 测绘资料
            case "112":
                menuId = MenuConstant.SURVEYINGMAPPING1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_75_ID : ButtonConstant.BUTTON_72_ID;
                break;
            // 竣工资料
            case "113":
                menuId = MenuConstant.COMPLETIONDATA1_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_79_ID : ButtonConstant.BUTTON_76_ID;
                break;
            // 上级下发文件
            case "117":
                menuId = MenuConstant.SUPERIOR_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_302_ID : ButtonConstant.BUTTON_299_ID;
                break;
            // 院务文件
            case "118":
                menuId = MenuConstant.CONFERENCE_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_228_ID : ButtonConstant.BUTTON_226_ID;
                break;
            // 三重一大专项
            case "119":
                menuId = MenuConstant.THREE_CONFERENCE_ID;
                buttonId = isDelete ? ButtonConstant.BUTTON_306_ID : ButtonConstant.BUTTON_304_ID;
                break;
            default:
                break;
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("menuId", menuId);
        map.put("buttonId", buttonId);
        return map;
    }

    @Override
    public List<Datamanagement> findDatamanagements(Datamanagement datamanagement) {
        LambdaQueryWrapper<Datamanagement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Datamanagement::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDatamanagement(Datamanagement datamanagement) throws FebsException {
        FebsUtil.isProjectNotNull(datamanagement.getDatamanagementPid());
        datamanagement.setDatamanagementUserid(FebsUtil.getCurrentUserId());
        datamanagement.setDatamanagementTime(new Date());
        this.save(datamanagement);
        logRecordContext.putVariable("id", datamanagement.getDatamanagementId());
        logRecordContext.putVariable("projectId", datamanagement.getDatamanagementPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDatamanagementTemp(Datamanagement datamanagement) throws FebsException {
        FebsUtil.isProjectNotNull(datamanagement.getDatamanagementPid());
        datamanagement.setDatamanagementUserid(FebsUtil.getCurrentUserId());
        datamanagement.setDatamanagementTime(new Date());
        this.save(datamanagement);
        logRecordContext.putVariable("id", datamanagement.getDatamanagementId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDatamanagementTemp(List<Datamanagement.Add> datamanagements) throws FebsException {
        for (Datamanagement.Add datamanagement : datamanagements) {
            if (!TEMP_NOT_VERIFY.contains(datamanagement.getDatamanagementType())) {
                FebsUtil.isProjectNotNull(datamanagement.getDatamanagementClassid());
            }
        }
        if (datamanagements == null || datamanagements.isEmpty()) {
            throw new FebsException("请选择数据");
        }
        ArrayList<Datamanagement> list = new ArrayList<>();
        datamanagements.forEach(datamanagement -> {
            Datamanagement datamanagement1 = new Datamanagement();
            datamanagement1.setDatamanagementType(datamanagement.getDatamanagementType());
            datamanagement1.setDatamanagementName(datamanagement.getDatamanagementName());
            datamanagement1.setDatamanagementClassid(datamanagement.getDatamanagementClassid());
            datamanagement1.setDatamanagementAddr(datamanagement.getDatamanagementAddr());
            datamanagement1.setDatamanagementUserid(FebsUtil.getCurrentUserId());
            datamanagement1.setDatamanagementTime(new Date());
            list.add(datamanagement1);
        });
        this.saveBatch(list, list.size());
        logRecordContext.putVariable("id", list.get(0).getDatamanagementId());
    }

    @Override
    public void createDatamanagement(List<Datamanagement.Add> datamanagements) throws FebsException {
        if (datamanagements == null || datamanagements.isEmpty()) {
            return;
        }
        for (Datamanagement.Add datamanagement : datamanagements) {
            FebsUtil.isProjectNotNull(datamanagement.getDatamanagementPid());
            FebsUtil.isProjectNotNull(datamanagement.getDatamanagementClassid());
        }
        ArrayList<Datamanagement> list = new ArrayList<>();
        datamanagements.forEach(datamanagement -> {
            Datamanagement datamanagement1 = new Datamanagement();
            datamanagement1.setDatamanagementType(datamanagement.getDatamanagementType());
            datamanagement1.setDatamanagementName(datamanagement.getDatamanagementName());
            datamanagement1.setDatamanagementPid(datamanagement.getDatamanagementPid());
            datamanagement1.setDatamanagementClassid(datamanagement.getDatamanagementClassid());
            datamanagement1.setDatamanagementAddr(datamanagement.getDatamanagementAddr());
            datamanagement1.setDatamanagementUserid(FebsUtil.getCurrentUserId());
            datamanagement1.setDangerParcelid(datamanagement.getDangerParcelid());
            datamanagement1.setDangerSubitemid(datamanagement.getDangerSubitemid());
            datamanagement1.setDangerUnitengineid(datamanagement.getDangerUnitengineid());
            datamanagement1.setDatamanagementTime(new Date());
            list.add(datamanagement1);
        });
        this.saveBatch(list, list.size());
        logRecordContext.putVariable("id", list.get(0).getDatamanagementId());
        logRecordContext.putVariable("projectId", list.get(0).getDatamanagementPid());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDatamanagement(Datamanagement datamanagement) {
        Datamanagement byId = datamanagementMapper.selectById(datamanagement.getDatamanagementId());
        isModify(false, byId.getDatamanagementId().toString());
        this.updateById(datamanagement);
        logRecordContext.putVariable("projectId", byId.getDatamanagementPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDatamanagement(Datamanagement datamanagement) throws FebsException {
        isModify(true, datamanagement.getDatamanagementId().toString());
        this.update(new LambdaUpdateWrapper<Datamanagement>()
            .eq(Datamanagement::getDatamanagementId, datamanagement.getDatamanagementId())
            .set(Datamanagement::getIsDelete, 1));
        Datamanagement byId = datamanagementMapper.selectById(datamanagement.getDatamanagementId());
        logRecordContext.putVariable("projectId", byId.getDatamanagementPid());
    }

    /**
     * 删除
     *
     * @param datamanagementIds 资料管理实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDatamanagements(String datamanagementIds) throws FebsException {
        String[] split = datamanagementIds.split(",");
        isModify(true, split);
        List<String> strings = Arrays.asList(split);
        this.update(new LambdaUpdateWrapper<Datamanagement>().in(Datamanagement::getDatamanagementId, strings)
            .set(Datamanagement::getIsDelete, 1));
    }
}
