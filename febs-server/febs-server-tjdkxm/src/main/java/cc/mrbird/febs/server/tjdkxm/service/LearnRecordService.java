package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.LearnRecord;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.annotation.processing.FilerException;
import java.util.List;
/**
 * 党建学习表(LearnRecord)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface LearnRecordService extends IService<LearnRecord> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param learnRecord 党建学习表实体类
     * @return IPage<LearnRecord>
     */
    IPage<LearnRecord> findLearnRecords(QueryRequest request, LearnRecord learnRecord);

    /**
     * 查询（所有）
     *
     * @param learnRecord 党建学习表实体类
     * @return List<LearnRecord>
     */
    List<LearnRecord> findLearnRecords(LearnRecord learnRecord);

    /**
     * 新增
     *
     * @param learnRecord 党建学习表实体类
     */
    void createLearnRecord(LearnRecord learnRecord);

    /**
     * 修改
     *
     * @param learnRecord 党建学习表实体类
     */
    void updateLearnRecord(LearnRecord learnRecord) throws FilerException;

    /**
     * 删除
     *
     * @param learnRecord 党建学习表实体类
     */
    void deleteLearnRecord(LearnRecord learnRecord) throws FilerException;
}
