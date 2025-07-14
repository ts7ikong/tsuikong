package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 19:35
 */
@Service
public interface UserProjectService extends IService<UserProject> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param userProject 分项表实体类
     * @return IPage<UserProject>
     */
    IPage<UserProject> findUserProject(QueryRequest request, UserProject userProject);

    IPage<Map<String, Object>> findUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException;

    /**
     * 查询（所有）
     *
     * @return List<UserProject>
     */
    List<UserProject> findUserProject();

    /**
     * 新增
     *
     * @param userProject 分项表实体类
     */
    void createUserProject(UserProject userProject) throws FebsException;

    /**
     * 批量新增
     *
     * @param userProjects
     */
    void createUserProjectList(UserProject.Add userProjects) throws FebsException;

    /**
     * 修改
     *
     * @param userProject 分项表实体类
     */
    void updateUserProject(UserProject userProject);

    /**
     * 删除
     *
     * @param userProject 分项表实体类
     */
    void deleteUserProject(UserProject.Add userProject) throws FebsException;

    /**
     * 切换项目
     *
     * @param projectId 项目id
     * @return {@link Project}
     */
    public Map<String, Object> toggleProject(Long projectId, String type, String password) throws FebsException;

    /**
     * 获取所有项目以及用户用户拥有的项目
     *
     * @param userId 用户id
     * @return {@link List< UserProject>}
     */
    List<UserProject> getAllProject(Long userId);

    /**
     * 获取用户用户拥有的项目
     *
     * @return {@link java.util.List<cc.mrbird.febs.common.core.entity.tjdkxm.UserProject>}
     */
    @Deprecated
    List<UserProject> getAllUserProject();

    /**
     * 获取所有项目以及用户用户拥有的项目--更新
     *
     * @param userProjects 集合
     * @return {@link String}
     */

    String updateUserAllProject(List<UserProject> userProjects);

    /**
     * 获取所有项目以及用户用户拥有的项目---更新--new
     *
     * @param projectIds 项目ids
     */
    void updateAllProjectNew(String projectIds, Long userId) throws FebsException;

    /**
     * 项目关系 (单位分部分项)
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getAllProjectChooses(String menuId, Integer buttonId);

    /**
     * 项目人员 查询--质量检查计划检查人
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getAllUserByProject();

    /**
     * 所有人员
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getAllUser();

    List<Map<String, Object>> getAllUserNew();

    /**
     * 用户拥有的项目只包含 项目id 项目名称
     *
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> getUserAllProject(String menuId);

    IPage<Map<String, Object>> findNotUserByProjectId(QueryRequest request, UserProject.UserModel userProject)
        throws FebsException;

    /**
     * 查询--质量检查计划、质量问题 检查人
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> projectUserInfoListQualityPlan();

    /**
     * 查询--质量问题整改人
     *
     * @return {@link java.lang.Object}
     */
    List<Map<String, Object>> projectUserInfoListQualityRectify();

    /**
     * 查询--质量问题验收人
     *
     * @return {@link java.lang.Object}
     */
    List<Map<String, Object>> projectUserInfoListQualityAcceptance();

    /**
     * 查询--安全检查计划、安全隐患 检查人
     *
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> projectUserInfoListSafePlan();

    /**
     * 查询--安全隐患整改人
     *
     * @return {@link java.lang.Object}
     */
    List<Map<String, Object>> projectUserInfoListSafeRectify();

    /**
     * 查询--安全隐患验收人
     *
     * @return {@link java.lang.Object}
     */
    List<Map<String, Object>> projectUserInfoListSafeAcceptance();

    /**
     * 查询--拥有工作审批的用户
     *
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> userInfoWorkApproval();

    /**
     * 查询--拥有会议管理的用户
     *
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> userInfoManagement();

}
