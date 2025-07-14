package cc.mrbird.febs.common.core.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 按钮表(TButton)实体类
 *
 * @author makejava
 * @since 2021-04-22 10:48:09
 */
@Data
@TableName("t_button")
@ApiModel("按钮表")
public class TButton implements Serializable {
    private static final long serialVersionUID = 870603952775886966L;

    /**
     * 按钮ID
     */
    @TableId(value = "BUTTON_ID", type = IdType.AUTO)
    @ApiModelProperty("按钮ID")
    private Long buttonId;

    @TableField(exist = false)
    private String id;

    /**
     * 按钮名称
     */
    @TableField("BUTTON_NAME")
    @ApiModelProperty("按钮名称")
    private String buttonName;

    /**
     * 按钮对应菜单ID
     */
    @TableField("BUTTON_MENUID")
    @ApiModelProperty("按钮对应菜单ID")
    private Long buttonMenuid;
    /**
     * 是否启用 1启用 0未启用
     */
    @TableField("BUTTON_TYPE")
    @ApiModelProperty("是否启用 1启用 0未启用")
    private Integer buttonType;
    @TableField(exist = false)
    private Integer type;
    @TableField(exist = false)
    private Integer projectId;

}
