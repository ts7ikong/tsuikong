package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 12:03
 */
public interface ConferenceThereService extends IService<ConferenceThere> {

    /**
     * 查询（分页）
     *
     * @param request         QueryRequest
     * @param conferenceThere 会议表ID实体类
     * @return IPage<ConferenceThere>
     */
    IPage<ConferenceThere> findConferenceTheres(QueryRequest request, ConferenceThere conferenceThere);

    /**
     * 查询（所有）
     *
     * @param conferenceThere 会议表ID实体类
     * @return List<ConferenceThere>
     */
    List<ConferenceThere> findConferenceTheres(ConferenceThere conferenceThere);

    /**
     * 新增
     *
     * @param conferenceThere 会议表ID实体类
     */
    void createConferenceThere(ConferenceThere conferenceThere) throws FebsException;

    /**
     * 修改
     *
     * @param conferenceThere 会议表ID实体类
     */
    void updateConferenceThere(ConferenceThere conferenceThere) throws FebsException;

    /**
     * 删除
     *
     * @param conferenceThere 会议表ID实体类
     */
    void deleteConferenceThere(ConferenceThere conferenceThere) throws FebsException;
}