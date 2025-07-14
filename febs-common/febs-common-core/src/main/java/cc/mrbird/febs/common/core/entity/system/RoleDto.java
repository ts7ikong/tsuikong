package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 17:08
 */
@Data
public class RoleDto {
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @TableField(value = "ROLE_NAME")
    @NotBlank(message = "{required}")
    @Size(message = "{noMoreThan}")
    @ExcelField(value = "角色名称")
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @TableField(value = "REMARK")
    @Size(message = "{noMoreThan}")
    @ExcelField(value = "角色描述")
    @ApiModelProperty(value = "角色描述")
    private String remark;

    @TableField(value = "CREATE_TIME")
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @TableField(value = "MODIFY_TIME")
    @ApiModelProperty(value = "修改时间")
    private String modifyTime;

    @ApiModelProperty(value = "角色类型 0 默认 1 特殊 2 通用")
    @NotEmpty(message = "类型不能为null")
    private Integer type;

    @ApiModelProperty(value = "菜单按钮信息")
    private List<RoleDtoInfo> menus;

    @Data
    @ApiModel("参数")
    public static class Params {
        @TableId(value = "ROLE_ID", type = IdType.AUTO)
        @ApiModelProperty(value = "角色ID")
        private Long roleId;

        @TableField(value = "ROLE_NAME")
        @NotBlank(message = "{required}")
        @ApiModelProperty(value = "角色名称")
        private String roleName;
        @TableField(value = "REMARK")
        @ApiModelProperty(value = "角色描述")
        private String remark;

        @ApiModelProperty(value = "角色类型 0 默认 1 特殊 2 通用")
        @NotNull(message = "类型不能为null")
        private Integer type;

        @ApiModelProperty(value = "菜单按钮信息")
        private List<RoleDtoInfo.Params> menus;
    }
}
