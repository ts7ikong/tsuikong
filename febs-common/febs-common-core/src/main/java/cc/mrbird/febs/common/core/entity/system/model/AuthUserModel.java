package cc.mrbird.febs.common.core.entity.system.model;

import cc.mrbird.febs.common.core.entity.system.ButtonDto;
import cc.mrbird.febs.common.core.entity.system.MenuUserAuthDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/8 17:50
 */
@Data
@Accessors(chain = true)
public class AuthUserModel {
    /**
     * 超级管理员
     */
    public static final Integer KEY_ADMIN = 1;
    /**
     * 项目负责人
     */
    public static final Integer KEY_PROJECT_ADMIN = 2;
    /**
     * 员工
     */
    public static final Integer KEY_EMPLOYEE = 3;
    /**
     * 临时
     */
    public static final Integer KEY_TEMP = 4;
    /**
     * 无角色无权限
     */
    public static final Integer KEY_NONE = 5;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * * 1 超级管理员 * 2 projectIds 项目负责人 * 3 projectIds 员工 * 4 projectIds 临时 * 5 无角色无权限
     */
    private Integer key;
    /**
     * 项目id
     */
    private Set<Long> projectIds = Collections.emptySet();
    /**
     * 项目对应信息
     */
    private List<MenuUserAuthDto.MenuButtonDto> auths = new ArrayList<>();
    /**
     * 按钮对应信息
     */
    private List<ButtonDto> buttonPermissions = Collections.emptyList();

    @Data
    public static class UserProjectAuth {
        private Long userId;
        /**
         * 是否是该项目负责人
         */
        private Integer isProjectAuth = 0;
        private Long projectId;
    }
}
