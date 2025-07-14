package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 质量问题清单(Qualityproblem)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface QualityproblemService extends IService<Qualityproblem> {

    /**
     * 查询（分页）
     *
     * @param request        QueryRequest
     * @param qualityproblem 质量问题清单实体类
     * @return IPage<Qualityproblem>
     */
    MyPage<Qualityproblem> findQualityproblems(QueryRequest request, Qualityproblem.Params qualityproblem) throws FebsException;

    /**
     * 查询（分页）整改记录
     *
     * @param request        QueryRequest
     * @param qualityproblem 质量问题清单实体类
     * @return IPage<Qualityproblem>
     */
    MyPage<Qualityproblem> findQualityproblemRectList(QueryRequest request, Qualityproblem.Params qualityproblem) throws FebsException;

    /**
     * 新增
     *
     * @param qualityproblem 质量问题清单实体类
     */
    void createQualityproblem(Qualityproblem qualityproblem) throws FebsException;

    /**
     * 修改
     *
     * @param qualityproblem 质量问题清单实体类
     */
    void updateQualityproblem(Qualityproblem qualityproblem) throws FebsException;

    /**
     * 删除
     *
     * @param qualityproblem 质量问题清单实体类
     */
    void deleteQualityproblem(Qualityproblem qualityproblem) throws FebsException;

    /**
     * 大屏统计数据
     *
     * @return {@link Map<String,Object>}
     */
    Map<String, Object> bigScreen(String type, String date) throws FebsException;

    MyPage<Qualityproblem> findQualityproblemRectifiedList(QueryRequest request, Qualityproblem.Params qualityproblem);
}
