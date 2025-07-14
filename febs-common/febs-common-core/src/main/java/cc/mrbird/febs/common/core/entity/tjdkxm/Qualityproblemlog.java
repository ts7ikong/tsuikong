package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("p_qualityproblemlog")
@ApiModel("质量问题清单")
public class Qualityproblemlog implements Serializable {
    private static final long serialVersionUID = -35617934255160920L;

    /**
     * 质量整改记录操作日志ID
     */
    @TableId(value = "QUALITYPROBLENLOG_ID", type = IdType.AUTO)
    @ApiModelProperty("质量整改记录操作日志ID")
    private Long qualityproblenlogId;


    /**
     * 质量问题清单ID
     */
    @TableField("QUALITYPROBLEN_ID")
    @ApiModelProperty("质量问题清单ID")
    private Long qualityproblenId;

    /**
     * 操作人姓名
     */
    @TableField("QUALITYPROBLENLOG_NAME")
    @ApiModelProperty("操作人姓名")
    private String qualityproblenlogName;

    /**
     * 操作了什么事
     */
    @TableField("QUALITYPROBLENLOG_DO")
    @ApiModelProperty("操作了什么事 1上报 2分配 3整改  4验收合格 5验收不通过")
    private String qualityproblenlogDo;

    /**
     * 操作时间
     */
    @TableField("QUALITYPROBLENLOG_ACCEPTTIME")
    @ApiModelProperty("操作时间")

    private Date qualityproblenlogAccepttime;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
