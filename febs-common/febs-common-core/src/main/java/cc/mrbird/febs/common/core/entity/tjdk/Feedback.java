package cc.mrbird.febs.common.core.entity.tjdk;


import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@TableName("p_feedback")
@ApiModel("反馈信息")
public class Feedback implements Serializable {
    private static final long serialVersionUID = 186879417342148881L;

    /**
     * 反馈id
     */
    @TableId(value = "FEEDBACK_ID", type = IdType.AUTO)
    @ApiModelProperty("反馈id")
    private Long feedbackId;


    /**
     * 反馈类型 1.系统漏洞 2.投诉 3. 其他
     */
    @TableField("FEEDBACK_TYPE")
    @ApiModelProperty("反馈类型 1.系统漏洞 2.投诉 3. 其他")
    private String feedbackType;

    /**
     * 反馈内容
     */
    @TableField("FEEDBACK_CONTENT")
    @ApiModelProperty("反馈内容")
    private String feedbackContent;

    /**
     * 联系方式
     */
    @TableField("FEEDBACK_NUMBER")
    @ApiModelProperty("联系方式")
    private String feedbackNumber;

    /**
     * 联系邮箱
     */
    @TableField("FEEDBACK_EMAL")
    @ApiModelProperty("联系邮箱")
    private String feedbackEmal;
    /**
     * 反馈时间
     */
    @TableField("FEEDBACK_TIME")
    @ApiModelProperty("反馈时间")

    private Date feedbackTime;

    /**
     * 附件 【图片地址】
     */
    @TableField("FEEDBACK_ANNEX")
    @ApiModelProperty("附件 【图片地址】")
    private String feedbackAnnex;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
