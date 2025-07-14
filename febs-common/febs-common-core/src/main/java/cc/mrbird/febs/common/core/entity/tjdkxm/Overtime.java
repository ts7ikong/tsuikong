package cc.mrbird.febs.common.core.entity.tjdkxm;


import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;


@Data
@TableName("p_overtime")
@ApiModel("加班申请审批表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Overtime implements Serializable {
    private static final long serialVersionUID = 648932693432786558L;

    /**
     * 加班申请审批ID
     */
    @TableId(value = "OVERTIME_ID", type = IdType.AUTO)
    @ApiModelProperty("加班申请审批ID")
    private Long overtimeId;


    /**
     * 对应项目ID
     */
    @TableField("OVERTIME_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long overtimeProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 加班原因
     */
    @TableField("OVERTIME_CAUSE")
    @ApiModelProperty("加班原因")
    @NotNull(message = "加班原因不能为空")
    private String overtimeCause;

    /**
     * 加班时长（小时）
     */
    @TableField("OVERTIME_DURATION")
    @ApiModelProperty("加班时长（小时）")
    @NotNull(message = "加班时长（小时）不能为空")
    private Double overtimeDuration;

    /**
     * 申请时间
     */
    @TableField("OVERTIME_CREATETIME")
    @ApiModelProperty("申请时间")
    private Date overtimeCreatetime;
    /**
     * 汇报时间 start
     */
    @TableField(exist = false)
    @ApiModelProperty("申请时间start")
    private String startCreateTime;
    /**
     * 汇报时间 end
     */
    @TableField(exist = false)
    @ApiModelProperty("申请时间end")
    private String endCreateTime;

    /**
     * 申请人用户ID
     */
    @TableField("OVERTIME_CREATEUSERID")
    @ApiModelProperty("申请人用户ID")
    private Long overtimeCreateuserid;
    /**
     * 申请人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("申请人用户姓名")
    private String overtimeCreateusername;

    /**
     * 审批时间
     */
    @TableField("OVERTIME_CHECKTIME")
    @ApiModelProperty("审批时间")

    private Date overtimeChecktime;

    /**
     * 审批人用户ID
     */
    @TableField("OVERTIME_CHECKUSERID")
    @ApiModelProperty("审批人用户ID")
    private Long overtimeCheckuserid;
    /**
     * 审批人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("审批人用户姓名")
    private String overtimeCheckusername;

    /**
     * 审批状态 0未审批1已通过2未通过
     */
    @TableField("OVERTIME_CHECKSTATE")
    @ApiModelProperty("审批状态 0未审批1已通过2未通过")
    private String overtimeCheckstate;

    /**
     * 审批未通过原因
     */
    @TableField("OVERTIME_CHECKREASON")
    @ApiModelProperty("审批未通过原因")
    private String overtimeCheckreason;


    @TableField(exist = false)
    @ApiModelProperty("是否能修改删除 0 不能,1 可以删除")
    private Integer modify;
    @TableField(exist = false)
    @ApiModelProperty("我的任务跳转传 1")
    private String task;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
