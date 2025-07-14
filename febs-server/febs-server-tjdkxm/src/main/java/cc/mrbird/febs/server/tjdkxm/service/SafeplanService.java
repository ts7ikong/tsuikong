package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 安全检查计划(Safeplan)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
public interface SafeplanService extends IService<Safeplan> {

    /**
     * 查询（分页）
     *
     * @param request  QueryRequest
     * @param safeplan 安全检查计划实体类
     * @return IPage<Safeplan>
     */
    MyPage<Safeplan> findSafeplans(QueryRequest request, Safeplan.Params safeplan, String type) throws FebsException;

    /**
     * 查询（所有）
     *
     * @param safeplan 安全检查计划实体类
     * @return List<Safeplan>
     */
    List<Safeplan> findSafeplans(Safeplan safeplan);

    /**
     * 新增
     *
     * @param safeplan 安全检查计划实体类
     */
    void createSafeplan(Safeplan safeplan) throws ParseException, FebsException;

    /**
     * 修改
     *
     * @param safeplan 安全检查计划实体类
     */
    void updateSafeplan(Safeplan safeplan) throws FebsException;

    /**
     * 删除
     *
     * @param safeplan 安全检查计划实体类
     */
    void deleteSafeplan(Safeplan safeplan) throws Exception;

    /**
     * 检查记录
     *
     * @param request
     * @param safeplan
     * @param type
     * @return {@link MyPage<?>}
     */
    MyPage<?> findSafeplanRecords(QueryRequest request, Safeplan.Params safeplan, String type) throws FebsException;
}
