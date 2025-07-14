package cc.mrbird.febs.common.core.entity.system;

import java.util.Date;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (TRoleButton)实体类
 *
 * @author makejava
 * @since 2021-04-22 10:48:03
 */
@Data
@TableName("t_role_button")
public class TRoleButton implements Serializable {
    private static final long serialVersionUID = 976664066448700023L;

    /**
     * Id
     */

    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("Id")
    private Long id;
    /**
     * 角色Id
     */

    @TableField(value = "ROLE_ID")
    @ApiModelProperty("角色Id")
    private Long roleId;

    /**
     * 按钮ID
     */
    @TableField("BUTTON_ID")
    @ApiModelProperty("按钮ID")
    private Long buttonId;
    @TableField(value = "PROJECT_ID")
    private Long projectId;

}
