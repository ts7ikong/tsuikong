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
@TableName("p_reporting")
@ApiModel("工作汇报审批表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})

public class Reporting implements Serializable {
    private static final long serialVersionUID = 551425702167928625L;

    /**
     * 工作汇报ID
     */
    @TableId(value = "REPORTING_ID", type = IdType.AUTO)
    @ApiModelProperty("工作汇报ID")
    private Long reportingId;


    /**
     * 对应项目ID
     */
    @TableField("REPORTING_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long reportingProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 汇报标题
     */
    @TableField("REPORTING_TITLE")
    @ApiModelProperty("汇报标题")
    @NotNull(message = "汇报标题不能为空")
    private String reportingTitle;

    /**
     * 汇报人用户ID
     */
    @TableField("REPORTING_USERID")
    @ApiModelProperty("汇报人用户ID")
    private Long reportingUserid;
    /**
     * 汇报人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("汇报人用户ID")
    private String reportingUsername;

    /**
     * 汇报内容
     */
    @TableField("REPORTING_CONTENT")
    @ApiModelProperty("汇报内容")
    @NotNull(message = "汇报内容不能为空")
    private String reportingContent;

    /**
     * 汇报时间
     */
    @TableField("REPORTING_TIME")
    @ApiModelProperty("汇报时间")

    private Date reportingTime;
    /**
     * 汇报时间 start
     */
    @TableField(exist = false)
    @ApiModelProperty("汇报时间start")
    private String startTime;
    /**
     * 汇报时间 end
     */
    @TableField(exist = false)
    @ApiModelProperty("汇报时间end")
    private String endTime;

    /**
     * 审批人
     */
    @TableField("REPORTING_CHECKUSERID")
    @ApiModelProperty("汇报对象/审批人 id")
    private Long reportingCheckuserid;
    /**
     * 审批人
     */
    @TableField(exist = false)
    @ApiModelProperty("汇报对象/审批人 名称")
    private String reportingCheckusername;

    /**
     * 审批时间
     */
    @TableField("REPORTING_CHECKTIME")
    @ApiModelProperty("审批时间")

    private Date reportingChecktime;

    /**
     * 审批状态 0待审批 1已审批
     */
    @TableField("REPORTING_STATE")
    @ApiModelProperty("审批状态 0待审批 1已审批")
    private String reportingState;

    /**
     * 审批批语
     */
    @TableField("REPORTING_RESULT")
    @ApiModelProperty("审批批语")
    private String reportingResult;


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
