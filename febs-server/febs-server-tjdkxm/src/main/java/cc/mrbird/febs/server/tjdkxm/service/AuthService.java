package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;

import java.util.List;
import java.util.Map;

public interface AuthService {
    /**
     * 获取用户的数据权限 角色类型
     *
     * @param userId
     * @return {@link AuthUserModel}
     */
    AuthUserModel getUserAuth(Long userId);

    /**
     * 获取用户的数据权限 角色类型
     *
     * @return {@link AuthUserModel}
     */
    AuthUserModel getUserAuth();

    /**
     * 用户拥有的权限
     *
     * @param userId 用户id
     * @return {@link List <String>}
     */
    List<String> getAuthority(Long userId);

    /**
     * 所有用户拥有的按钮权限
     *
     * @return "BUTTON_ID,BUTTON_NAME,count,userIds"
     */
    List<Map<String, Object>> getButtonAuthority();

    /**
     * 用户是否没有该权限
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @return {@link boolean}
     */
    boolean hasNotPermission(Integer menuId);

    /**
     * 用户是否拥有该权限
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param userAuth {@link AuthUserModel}
     * @return {@link boolean}
     */
    boolean hasPermission(Integer menuId, AuthUserModel userAuth);

    /**
     * 用户是否没有权限
     *
     * @param menuId {@link cc.mrbird.febs.common.core.entity.constant.MenuConstant}
     * @param userAuth {@link AuthUserModel}
     * @return {@link boolean}
     */
    boolean hasNotPermission(Integer menuId, AuthUserModel userAuth);

    /**
     * 是否可以删除 --用于非项目
     *
     * @return {@link java.lang.Integer}
     */
    boolean isDeleteAuth(Long createUserId);

    /**
     * 是否可以删除 --用于非项目
     *
     * @return {@link java.lang.Integer}
     */
    boolean isDeleteAuth();

    /**
     * 是否可以删除 -- 用于项目
     *
     * @return {@link java.lang.Integer}
     */
    boolean isProjectDeleteAuth(Long createUserId, Long projectId);
}
