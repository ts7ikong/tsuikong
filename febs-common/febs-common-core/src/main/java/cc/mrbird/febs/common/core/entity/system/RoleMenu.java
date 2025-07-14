package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MrBird
 */
@TableName("t_role_menu")
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -7573904024872252113L;
    /**
     * Id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @TableField(value = "ROLE_ID")
    private Long roleId;
    @TableField(value = "MENU_ID")
    private Long menuId;
    @TableField(value = "PROJECT_ID")
    private Long projectId;
    @TableField(exist = false)
    private String projectName;
    @TableField(exist = false)
    private String menuName;
    @TableField(exist = false)
    private Integer classType;
}