package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 安全隐患清单(Safeproblem)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface SafeproblemService extends IService<Safeproblem> {

    /**
     * 查询（分页）
     *
     * @param request     QueryRequest
     * @param safeproblem 安全隐患清单实体类
     * @return IPage<Safeproblem>
     */
    MyPage<Safeproblem> findSafeproblems(QueryRequest request, Safeproblem.Params  safeproblem, int type) throws FebsException;

    /**
     * 查询整改记录分页
     *
     * @param request     QueryRequest
     * @param safeproblem 安全隐患清单实体类
     * @return IPage<Safeproblem>
     */
    MyPage<Safeproblem> safeproblemRectList(QueryRequest request, Safeproblem.Params  safeproblem, int type);


    /**
     * 新增
     *
     * @param safeproblem 安全隐患清单实体类
     */
    void createSafeproblem(Safeproblem safeproblem) throws FebsException;

    /**
     * 修改
     *
     * @param safeproblem 安全隐患清单实体类
     */
    void updateSafeproblem(Safeproblem safeproblem) throws FebsException;

    /**
     * 删除
     *
     * @param safeproblem 安全隐患清单实体类
     */
    void deleteSafeproblem(Safeproblem safeproblem) throws FebsException;

    /**
     * 大屏统计数据
     *
     * @return {@link Map<String,Object>}
     */
    Map<String, Object> bigScreen(String type, String date) throws FebsException;

}
