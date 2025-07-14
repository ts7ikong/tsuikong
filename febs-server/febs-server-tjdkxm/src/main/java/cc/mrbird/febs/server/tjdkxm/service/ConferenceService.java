package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Conference;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 会议表ID(Conference)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
@Service("ConferenceService")
public interface ConferenceService extends IService<Conference> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param conference 会议表ID实体类
    * @return IPage<Conference>
    */
    MyPage<Conference> findConferences(QueryRequest request, Conference.Params conference);

    
    /**
    * 新增
    *
    * @param conference 会议表ID实体类
    */
    void createConference(Conference conference) throws FebsException;
    
    /**
    * 修改
    *
    * @param conference 会议表ID实体类
    */
    void updateConference(Conference conference) throws FebsException;

    void conferenceStart(Long conferenceId) throws FebsException;

    /**
    * 删除
    *
    * @param conference 会议表ID实体类
    */
    void deleteConference(Conference conference) throws FebsException;

    void conferenceEnd(Long conferenceId) throws FebsException;
}
