package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.constant.ButtonConstant;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.tjdkxm.Datamanagement;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.DocumentClassMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.DocumentClassService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * DocumentClassService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DocumentClassServiceImpl extends ServiceImpl<DocumentClassMapper, DocumentClass>
        implements DocumentClassService {
    @Autowired
    private CacheableService cacheableService;
    @Autowired
    private DocumentClassMapper documentClassMapper;
    @Autowired
    private LogRecordContext logRecordContext;

    /**
     * 查询 根据菜单
     *
     * @param documentclassMenu
     * @return IPage<DocumentClass>
     */
    @Override
    public List<DocumentClass> findDocumentClasss(String documentclassMenu, String documentclassName) {
        if (StringUtils.isBlank(documentclassMenu)) {
            return Collections.emptyList();
        }
        List<DocumentClass> documentClass = cacheableService.getDocumentClass(documentclassMenu);
        List<DocumentClass> classArrayList = new ArrayList<>();
        if (StringUtils.isNotBlank(documentclassName)) {
            for (DocumentClass aClass : documentClass) {
                String name = aClass.getDocumentclassName();
                if (StringUtils.isNotBlank(name)) {
                    if (name.contains(documentclassName)) {
                        classArrayList.add(aClass);
                    }
                }
            }
        } else {
            classArrayList = documentClass;
        }
        return classArrayList;
    }

    /**
     * 查询 根据菜单
     *
     * @param documentclassMenu
     * @return IPage<DocumentClass>
     */
    @Override
    public List<DocumentClass> findDocumentTempClasss(String documentclassMenu, String documentclassName)
            throws FebsException {
        if (StringUtils.isBlank(documentclassMenu)) {
            return Collections.emptyList();
        }
        try {
            List<DocumentClass> documentClass = cacheableService.getDocumentTempClass(documentclassMenu);
            List<DocumentClass> classArrayList = new ArrayList<>();
            if (StringUtils.isNotBlank(documentclassName)) {
                for (DocumentClass aClass : documentClass) {
                    String name = aClass.getDocumentclassName();
                    if (StringUtils.isNotBlank(name)) {
                        if (name.contains(documentclassName)) {
                            classArrayList.add(aClass);
                        }
                    }
                }
            } else {
                classArrayList = documentClass;
            }
            return classArrayList;
        } catch (Exception e) {
            throw new FebsException("类型错误");
        }
    }

    /**
     * 自己的（或管理员）
     */
    private void isModify(Long pid, Long projectId, boolean isAdd) {
        Integer menuId = null;
        Integer buttonId = null;
        switch (pid.toString()) {
            // 合同模板
            case "2":
            case "102":
                menuId = MenuConstant.CONTRACTTEMPLATE1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_234_ID : ButtonConstant.BUTTON_235_ID;
                break;
            // 合同档案库
            case "3":
                menuId = MenuConstant.CONTRACT_ARCHIVE_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_295_ID : ButtonConstant.BUTTON_296_ID;
                break;
            // 施工档案
            case "4":
                menuId = MenuConstant.CONSTRUCTION_ARCHIVES_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_260_ID : ButtonConstant.BUTTON_261_ID;
                break;
            // 技术台账
            case "5":
                menuId = MenuConstant.TECHNICAL_ACCOUNT_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_262_ID : ButtonConstant.BUTTON_263_ID;
                break;
            // 技术标准
            case "6":
                menuId = MenuConstant.TECHNICAL_STANDARD_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_264_ID : ButtonConstant.BUTTON_265_ID;
                break;
            // 技术交底
            case "7":
                menuId = MenuConstant.TECHNICAL_DISCLOSURE_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_266_ID : ButtonConstant.BUTTON_267_ID;
                break;
            // 交接资料
            case "8":
                menuId = MenuConstant.HANDOVER_DATA_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_267_ID : ButtonConstant.BUTTON_268_ID;
                break;
            // 设计变更
            case "9":
                menuId = MenuConstant.DESIGN_CHANGE_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_274_ID : ButtonConstant.BUTTON_275_ID;
                break;
            // 工程确认单
            case "10":
                menuId = MenuConstant.CONFIRMATION_SHEET_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_270_ID : ButtonConstant.BUTTON_271_ID;
                break;
            // 工程联络单
            case "11":
                menuId = MenuConstant.LIAISON_SHEET_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_272_ID : ButtonConstant.BUTTON_273_ID;
                break;
            // 测绘资料
            case "12":
                menuId = MenuConstant.SURVEYING_MAPPING_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_276_ID : ButtonConstant.BUTTON_267_ID;
                break;
            // 竣工资料
            case "13":
                menuId = MenuConstant.COMPLETION_DATA_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_278_ID : ButtonConstant.BUTTON_279_ID;
                break;
            // 数字化中心
            case "14":
            case "114":
                menuId = MenuConstant.DIGITAL1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_258_ID : ButtonConstant.BUTTON_259_ID;
                break;
            // 照片管理
            case "16":
                menuId = MenuConstant.PHOTO_MANAGEMENT_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_280_ID : ButtonConstant.BUTTON_281_ID;
                break;
            // 施工档案
            case "104":
                menuId = MenuConstant.CONSTRUCTIONARCHIVES1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_238_ID : ButtonConstant.BUTTON_239_ID;
                break;
            // 技术台账
            case "105":
                menuId = MenuConstant.TECHNICALACCOUNT1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_240_ID : ButtonConstant.BUTTON_241_ID;
                break;
            // 技术标准
            case "106":
                menuId = MenuConstant.TECHNICALSTANDARD1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_242_ID : ButtonConstant.BUTTON_243_ID;
                break;
            // 技术交底
            case "107":
                menuId = MenuConstant.TECHNICALDISCLOSURE1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_244_ID : ButtonConstant.BUTTON_245_ID;
                break;
            // 交接资料
            case "108":
                menuId = MenuConstant.HANDOVERDATA1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_246_ID : ButtonConstant.BUTTON_247_ID;
                break;
            // 设计变更
            case "109":
                menuId = MenuConstant.DESIGNCHANGE1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_252_ID : ButtonConstant.BUTTON_253_ID;
                break;
            // 工程确认单
            case "110":
                menuId = MenuConstant.CONFIRMATIONSHEET1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_248_ID : ButtonConstant.BUTTON_249_ID;
                break;
            // 工程联络单
            case "111":
                menuId = MenuConstant.LIAISONSHEET1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_250_ID : ButtonConstant.BUTTON_251_ID;
                break;
            // 测绘资料
            case "112":
                menuId = MenuConstant.SURVEYINGMAPPING1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_254_ID : ButtonConstant.BUTTON_255_ID;
                break;
            // 竣工资料
            case "113":
                menuId = MenuConstant.COMPLETIONDATA1_ID;
                buttonId = isAdd ? ButtonConstant.BUTTON_256_ID : ButtonConstant.BUTTON_257_ID;
                break;
            default:
                break;
        }
        cacheableService.hasPermission(null, menuId, buttonId, projectId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDocumentClass(DocumentClass.Add add) throws FebsException {
        //重名校验
        LambdaQueryWrapper<DocumentClass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DocumentClass::getDocumentclassName, add.getDocumentclassName()).eq(DocumentClass::getIsDelete, 0);
        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new FebsException("文件类别已存在！");
        }
        isModify(add.getDocumentclassPid(), null, true);
        DocumentClass documentClass = new DocumentClass();
        documentClass.setDocumentclassPid(add.getDocumentclassPid());
        documentClass.setDocumentclassName(add.getDocumentclassName());
        documentClass.setDocumentclassTime(new Date());
        documentClass.setDocumentclassMenu(null);
        documentClass.setDocumentclassProjectid(0L);
        this.save(documentClass);
        logRecordContext.putVariable("id", documentClass.getDocumentclassId());
        logRecordContext.putVariable("projectId", documentClass.getDocumentclassPid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDocumenTempClass(DocumentClass.Add add) {
        isModify(add.getDocumentclassPid(), null, true);
        DocumentClass documentClass = new DocumentClass();
        documentClass.setDocumentclassPid(add.getDocumentclassPid());
        documentClass.setDocumentclassName(add.getDocumentclassName());
        documentClass.setDocumentclassProjectid(-1L);
        documentClass.setDocumentclassTime(new Date());
        documentClass.setDocumentclassMenu(null);
        this.save(documentClass);
        logRecordContext.putVariable("id", documentClass.getDocumentclassId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocumentClass(DocumentClass documentClass) throws FebsException {
        //重名校验
        LambdaQueryWrapper<DocumentClass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DocumentClass::getDocumentclassName, documentClass.getDocumentclassName()).ne(DocumentClass::getDocumentclassId, documentClass.getDocumentclassId()).eq(DocumentClass::getIsDelete, 0);
        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new FebsException("文件类别已存在！");
        }
        DocumentClass byId = getById(documentClass.getDocumentclassId());
        if (byId == null) {
            throw new IllegalArgumentException("请选择数据");
        }
        isModify(byId.getDocumentclassPid(), null, true);
        this.updateById(documentClass);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocumentClass(DocumentClass documentClass) throws FebsException {
        this.update(null,
                new LambdaUpdateWrapper<DocumentClass>()
                        .eq(DocumentClass::getDocumentclassId, documentClass.getDocumentclassId())
                        .set(DocumentClass::getIsDelete, 1));
    }

}
