package cc.mrbird.febs.server.tjdk.service;

import cc.mrbird.febs.common.core.entity.tjdk.Feedback;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 反馈信息(Feedback)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
public interface FeedbackService extends IService<Feedback> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param feedback 反馈信息实体类
    * @return IPage<Feedback>
    */
    IPage<Feedback> findFeedbacks(QueryRequest request, Feedback feedback);
    
    /**
    * 查询（所有）
    *
    * @param feedback 反馈信息实体类
    * @return List<Feedback>
    */
    List<Feedback> findFeedbacks(Feedback feedback);
    
    /**
    * 新增
    *
    * @param feedback 反馈信息实体类
    */
    void createFeedback(Feedback feedback);
    
    /**
    * 修改
    *
    * @param feedback 反馈信息实体类
    */
    void updateFeedback(Feedback feedback);
    
    /**
    * 删除
    *
    * @param feedback 反馈信息实体类
    */
    void deleteFeedback(Feedback feedback);
}
