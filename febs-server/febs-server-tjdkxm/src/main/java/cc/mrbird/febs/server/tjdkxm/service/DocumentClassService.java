package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资料管理(DocumentClass)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
public interface DocumentClassService extends IService<DocumentClass> {

    /**
     * 查询 根据菜单
     *
     * @return IPage<DocumentClass>
     */
    List<DocumentClass> findDocumentClasss(String documentclassMenu,String documentclassName);

    List<DocumentClass> findDocumentTempClasss(String documentclassMenu, String documentclassName) throws FebsException;



    /**
     * 新增
     *
     * @param documentClass 资料管理实体类
     */
    void createDocumentClass(DocumentClass.Add documentClass) throws FebsException;


    void createDocumenTempClass(DocumentClass.Add documentClass);

    /**
     * 修改
     *
     * @param documentClass 资料管理实体类
     */
    void updateDocumentClass(DocumentClass documentClass) throws FebsException;

    /**
     * 删除
     *
     * @param documentClass 资料管理实体类
     */
    void deleteDocumentClass(DocumentClass documentClass) throws FebsException;

}
