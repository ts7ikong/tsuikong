package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityplan;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 质量检查计划(Qualityplan)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
public interface QualityplanService extends IService<Qualityplan> {

    /**
     * 查询（分页）
     *
     * @param request     QueryRequest
     * @param qualityplan 质量检查计划实体类
     * @return IPage<Qualityplan>
     */
    MyPage<Qualityplan> findQualityplans(QueryRequest request, Qualityplan.Params qualityplan, String type) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param qualityplan 质量检查计划实体类
     * @return List<Qualityplan>
     */
    List<Qualityplan> findQualityplans(Qualityplan qualityplan);

    /**
     * 新增
     *
     * @param qualityplan 质量检查计划实体类
     */
    void createQualityplan(Qualityplan qualityplan) throws FebsException;

    /**
     * 修改
     *
     * @param qualityplan 质量检查计划实体类
     */
    void updateQualityplan(Qualityplan qualityplan) throws FebsException;

    /**
     * 删除
     *
     * @param qualityplan 质量检查计划实体类
     */
    void deleteQualityplan(Qualityplan qualityplan) throws Exception;

    @Deprecated
    Integer notChecked(Long userId, Long projectId);

    MyPage<?> findSafeplanRecords(QueryRequest request, Qualityplan.Params safeplan, String type) throws FebsException;
}
