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
@TableName("p_safeproblemlog")
@ApiModel("安全隐患清单")
public class Safeproblemlog implements Serializable {
    private static final long serialVersionUID = 704064149669467720L;

    /**
     * 安全整改记录操作日志ID
     */
    @TableId(value = "SAFEPROBLENLOG_ID", type = IdType.AUTO)
    @ApiModelProperty("安全整改记录操作日志ID")
    private Long safeproblenlogId;


    /**
     * 安全整改记录ID
     */
    @TableField("SAFEPROBLEN_ID")
    @ApiModelProperty("安全整改记录ID")
    private Long safeproblenId;

    /**
     * 操作人姓名
     */
    @TableField("SAFEPROBLENLOG_NAME")
    @ApiModelProperty("操作人姓名")
    private String safeproblenlogName;

    /**
     * 操作了什么事
     */
    @TableField("SAFEPROBLENLOG_DO")
    @ApiModelProperty("操作了什么事 2分配 3整改 4验收合格 5验收不通过")
    private String safeproblenlogDo;

    /**
     * 操作时间
     */
    @TableField("SAFEPROBLENLOG_ACCEPTTIME")
    @ApiModelProperty("操作时间")

    private Date safeproblenlogAccepttime;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
