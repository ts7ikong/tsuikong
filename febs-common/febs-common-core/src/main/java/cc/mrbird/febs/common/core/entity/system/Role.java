package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author MrBird
 */
@Data
@TableName("t_role")
@Excel("角色信息表")
@ApiModel("重大危险源记录表")
public class Role implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;
    /**
     * 超级管理员
     */
    public static final Long ADMIN_ROLE = 1L;
    /**
     * 项目负责人
     */
    public static final Long PROJECT_ADMIN_ROLE = 2L;
    /**
     * 临时人员
     */
    public static final Long TEMP_ROLE = 4L;
    /**
     * 项目默认角色
     */
    public static final Long PROJECT_DEFAULT_ROLE = 3L;
    /**
     * 党员
     */
    public static final Long PARTY_MEMBER = 19L;
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
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

    @TableField(value = "TYPE")
    @NotEmpty(message = "{noMoreThan}")
    @ExcelField(value = "角色类型 1 特殊 2 通用")
    @ApiModelProperty(value = "角色类型 1 特殊 2 通用")
    private Integer type;

    @TableField(value = "CREATE_TIME")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @TableField(value = "IS_DELETE")
    @ApiModelProperty(value = "是否删除 1删除 0 未删除")
    @JsonIgnore
    private Integer isDelete;

    @TableField(value = "MODIFY_TIME")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    @ApiModelProperty(value = "修改时间")
    private String modifyTime;

    @TableField(exist = false)
    private String ids;

    private transient String menuIds;

    private transient String buttonIds;

    private transient String menuNames;

    @TableField(exist = false)
    private List<RoleDtoInfo> menus;

}