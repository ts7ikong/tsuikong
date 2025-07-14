package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:24
 */
public interface PunchAreaService extends IService<PunchArea> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param punchArea 项目打卡区域
     * @return IPage<PunchArea>
     */
    // IPage<PunchArea> findPunchAreas(QueryRequest request, PunchArea punchArea);

    /**
     * 查询（所有）
     *
     * @return List<PunchArea>
     */
    List<PunchArea> findPunchAreas();

    /**
     * 新增
     *
     * @param punchArea 党建学习表实体类
     */
    void createPunchArea(PunchArea punchArea) throws FebsException;

    /**
     * 修改
     *
     * @param punchArea 党建学习表实体类
     */
    void updatePunchArea(PunchArea punchArea) throws FebsException;

    /**
     * 删除
     *
     * @param punchArea 党建学习表实体类
     */
    void deletePunchArea(PunchArea punchArea);

    IPage<?> findPunchAreas(QueryRequest request, PunchArea punchArea);

    /**
     * 人员配置
     *
     * @param userIds 用户ids
     * @param punchAreaId 区域id
     */
    void upPunchAreaUser(String userIds, Long punchAreaId) throws FebsException;

    /**
     * 规则配置
     *
     * @param object 规则
     */
    void upPunchAreaRule(String object, Long tableId) throws FebsException;

    /**
     * 获取没在打卡区域的用户 {userId:用户id,username:用户名,realname:用户姓名,deptName:用户部门,projectName:用户项目名称}
     * 
     * @return {@link List< Map< String, Object>>}
     */
    IPage<Map<String, Object>> getPunchAreaUser(Long punchAreaId, String username, Long deptId, Long projectId,
        QueryRequest request, Integer type);

    /**
     * 添加人员
     *
     * @param userIds 用户ids
     * @param punchAreaId 区域id
     */
    void addPunchAreaUser(String userIds, Long punchAreaId) throws FebsException;

    /**
     * 删除人员
     *
     * @param userIds 用户ids
     * @param punchAreaId 区域id
     */
    void delPunchAreaUser(String userIds, Long punchAreaId) throws FebsException;
}
