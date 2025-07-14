package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.BiddRecord;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 14:55
 */
public interface BiddRecordService extends IService<BiddRecord> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param biddRecord    实体类
     * @return IPage<BiddRecord>
     */
    MyPage<BiddRecord> findBiddRecords(QueryRequest request, BiddRecord biddRecord);

    /**
     * 查询（所有）
     *
     * @param biddRecord 实体类
     * @return List<BiddRecord>
     */
    List<BiddRecord> findBiddRecords(BiddRecord biddRecord);

    /**
     * 新增
     *
     * @param biddRecord 实体类
     */
    void createBiddRecord(BiddRecord biddRecord) throws FebsException;

    /**
     * 修改
     *
     * @param biddRecord 实体类
     */
    void updateBiddRecord(BiddRecord biddRecord) throws FebsException;

    /**
     * 删除
     *
     * @param biddRecord 实体类
     */
    void deleteBiddRecord(BiddRecord biddRecord) throws FebsException;
}