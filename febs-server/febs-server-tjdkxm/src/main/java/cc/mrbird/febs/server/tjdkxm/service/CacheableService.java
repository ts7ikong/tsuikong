package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdkxm.DocumentClass;
import cc.mrbird.febs.common.core.exception.FebsException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:05
 */
public interface CacheableService {

    /**
     * 查询（项目中所有的单位工程、分部、分项）
     *
     * @return List<Project>
     */
    List<Map<String, Object>> getAllProjectChooses(Integer menuId, Integer buttonId);

    /**
     * 查询 所有拥有项目
     *
     * @return List<Project>
     */
    List<Map<String, Object>> getAllProject(Integer menuId, Integer buttonId);

    /**
     * 更新（项目中所有的单位工程、分部、分项）
     *
     * @param projectId 项目id
     */
    void upgetAllProjectChooses(Long projectId);

    /**
     * 更新（项目中所有的单位工程、分部、分项）
     *
     * @param projectId 项目id
     */
    void deletePorject(Long projectId);

    /**
     * 获取具体的项目id
     *
     * @param menuId 菜单id
     * @param buttonId 按钮id
     * @return {@link AuthUserModel}
     */
    AuthUserModel getAuthUser(Integer menuId, Integer buttonId) throws FebsException;

    AuthUserModel getUserAuth(Long userId);

    /**
     * 获取具体的项目id
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @return {@link AuthUserModel}
     */
    AuthUserModel getAuthUser(Integer menuId);

    /**
     * 获取具体的项目id
     *
     * @param userId 用户id
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @return {@link AuthUserModel}
     */
    AuthUserModel getAuthUser(Long userId, Integer menuId);

    /**
     * 获取具体的项目id
     *
     * @param userId 用户id
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param buttonId {@link cc.mrbird.febs.common.core.entity.constant.ButtonConstant}
     * @return {@link AuthUserModel}
     */
    AuthUserModel getAuthUser(Long userId, Integer menuId, Integer buttonId);

    /**
     * 更新用户的数据权限 角色类型
     *
     * @param userId
     * @return {@link AuthUserModel}
     */
    void upUserAuth(Long userId);

    /**
     * 查询所有用户
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getAllUser();

    /**
     * 查询所有用户
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param buttonId {@link cc.mrbird.febs.common.core.entity.constant.ButtonConstant}
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getAllUser(Integer menuId, Integer buttonId);

    /**
     * 查询（项目中所有的用户 )
     *
     * @return List<Project>
     */
    List<Map<String, Object>> getAllUserByProject(Set<Long> projectId);

    /**
     * 项目中拥有该角色的用户信息
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param buttonId {@link cc.mrbird.febs.common.core.entity.constant.ButtonConstant}
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> getUserByMenuIdAndBtnIdAndProjectIds(Integer menuId, Integer buttonId);

    /**
     * 更新项目用户
     *
     * @param projectId 项目id
     */
    void upProjectUser(Long projectId);

    /**
     * 更新项目用户
     */
    void upProjectUser();

    /**
     * 菜单待审批数量
     *
     * @param userId 用户id
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> getAppMenuAgencyNum(Long userId);

    /**
     * 供应商单位
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> findParcelUnits();

    /**
     * 更新供应商单位
     */
    void upFindParcelUnits();

    /**
     * 用户拥有的权限
     *
     * @param userId 用户id
     * @return {@link List<String>}
     */
    List<MenuUserAuthDto.MenuButtonDto> getAuthority(Long userId);

    /**
     * 项目资料分类
     *
     * @param documentclassMenu 菜单类型
     * @return {@link List< DocumentClass>}
     */
    List<DocumentClass> getDocumentClass(String documentclassMenu);

    /**
     * 资料模板分类
     *
     * @param documentclassMenu 菜单类型
     * @return {@link List< DocumentClass>}
     */
    List<DocumentClass> getDocumentTempClass(String documentclassMenu);

    /**
     * 用户是否有该权限
     *
     * @param createUserId 用户id
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param buttonId {@link cc.mrbird.febs.common.core.entity.constant.ButtonConstant}
     * @param projectId 项目id 可不传
     * @return {@link boolean}
     */
    void hasPermission(Long createUserId, Integer menuId, Integer buttonId, Long projectId);

    /**
     * 用户是否有该权限
     *
     * @param createUserId 用户id
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @return {@link boolean}
     */
    void hasPermission(Long createUserId, Integer... menuId);

    /**
     * 用户是否有该权限
     *
     * @param createUserId 用户id
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param projectId 项目id 可不传
     * @return {@link boolean}
     */
    void hasPermission(Long createUserId, Integer menuId, Long projectId);

    List<Map<String, Object>> getAllUserNew();

    Set<Long> getUserAllProjectId(Long currentUserId);
}
