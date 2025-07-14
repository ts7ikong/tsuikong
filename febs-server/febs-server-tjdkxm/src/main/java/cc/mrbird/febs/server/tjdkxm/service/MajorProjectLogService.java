package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.MajorProjectLog;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.processing.FilerException;
import java.util.List;

/**
 * 党建学习表(MajorProjectLog)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface MajorProjectLogService extends IService<MajorProjectLog> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param majorProjectLog 党建学习表实体类
     * @return IPage<MajorProjectLog>
     */
    MyPage<MajorProjectLog> findMajorProjectLogs(QueryRequest request, MajorProjectLog.Params majorProjectLog);

    /**
     * 查询（所有）
     *
     * @param majorProjectLog 党建学习表实体类
     * @return List<MajorProjectLog>
     */
    List<MajorProjectLog> findMajorProjectLogs(MajorProjectLog majorProjectLog);

    /**
     * 新增
     *
     * @param majorProjectLog 党建学习表实体类
     */
    void createMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException;

    /**
     * 修改
     *
     * @param majorProjectLog 党建学习表实体类
     */
    void updateMajorProjectLog(MajorProjectLog majorProjectLog) throws FebsException;

    /**
     * 删除
     *
     * @param majorProjectLog 党建学习表实体类
     */
    void deleteMajorProjectLog(MajorProjectLog majorProjectLog) throws  FebsException;
}
