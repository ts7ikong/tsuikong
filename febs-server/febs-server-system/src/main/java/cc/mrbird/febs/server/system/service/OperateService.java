package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.scheduling.annotation.Async;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:13
 */
public interface OperateService extends IService<Operate> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param operate 分部表实体类
     * @return IPage<Operate>
     */
    IPage<Operate> findOperates(QueryRequest request, Operate operate);
    Operate create(JSONObject json, String type, String id, Long currentUserId);


    /**
     * 修改
     *
     * @param operate 分部表实体类
     */
    void updateOperate(Operate operate,String id,Object projectId);

    /**
     * 删除
     *
     * @param operate 分部表实体类
     */
    void deleteOperate(Operate operate);

}
