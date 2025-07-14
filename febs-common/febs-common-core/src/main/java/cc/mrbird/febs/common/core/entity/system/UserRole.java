package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MrBird
 */
@Data
@TableName("t_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;
    /**
     * Id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @TableField(value = "USER_ID")
    private Long userId;

    @TableField(value = "ROLE_ID")
    private Long roleId;

    @TableField(value = "PROJECT_ID")
    private Long projectId;

    @TableField(exist = false)
    private Role role;

    @TableField(exist = false)
    private Project project;

    @TableField(exist = false)
    private String projectIds;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserRole() {}
}