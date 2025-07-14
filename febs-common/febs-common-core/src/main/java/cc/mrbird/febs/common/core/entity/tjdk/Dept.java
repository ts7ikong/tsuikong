package cc.mrbird.febs.common.core.entity.tjdk;

import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("p_dept")
@ApiModel("部门表")
public class Dept implements Serializable {
    private static final long serialVersionUID = 455072294576019293L;

    /**
     * 部门ID
     */
    @TableId(value = "DEPT_ID", type = IdType.AUTO)
    @ApiModelProperty("部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @TableField("DEPT_NAME")
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 部门负责人
     */
    @TableField("DEPT_PERSON")
    @ApiModelProperty("部门负责人")
    private String deptPerson;

    /**
     * 部门联系电话
     */
    @TableField("DEPT_LINK")
    @ApiModelProperty("部门联系电话")
    private String deptLink;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @TableField(exist = false)
    private List<Map<String, Object>> users;

}
