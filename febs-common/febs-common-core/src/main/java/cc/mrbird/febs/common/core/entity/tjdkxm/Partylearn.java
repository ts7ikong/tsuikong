package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.yulichang.annotation.EntityMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@TableName(value = "p_partylearn", autoResultMap = true)
@ApiModel("党建学习表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Partylearn implements Serializable {
    private static final long serialVersionUID = -20515202770104348L;

    /**
     * 党建学习id
     */
    @TableId(value = "PARTYLEARN_ID", type = IdType.AUTO)
    @ApiModelProperty("党建学习id")
    private Long partylearnId;


    @TableField(exist = false)
    @ApiModelProperty("用户学习记录")
    private List<LearnRecord> learnRecords;
    @TableField(exist = false)
    @ApiModelProperty("当前登录用户是否已学习 1已学习 0 未学习")
    private int isLearn = 0;

    /**
     * 党建学习标题
     */
    @TableField("PARTYLEARN_TITLE")
    @ApiModelProperty("党建学习标题")
    private String partylearnTitle;

    /**
     * 党建学习发表日期
     */
    @TableField("PARTYLEARN_TIME")
    @ApiModelProperty("党建学习发表日期")
    private Date partylearnTime;

    /**
     * 发表日期start
     */
    @TableField(exist = false)
    @ApiModelProperty("发表日期start")
    private String startTime;
    /**
     * 发表日期end
     */
    @TableField(exist = false)
    @ApiModelProperty("发表日期end")
    private String endTime;

    /**
     * 党建学习发表人用户ID
     */
    @TableField("PARTYLEARN_USERID")
    @ApiModelProperty("党建学习发表人用户ID")
    private Long partylearnUserid;

    /**
     * 发表人用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty("发表人用户信息")
    private String partylearnUsername;


    /**
     * 党建中心资料附件
     */
    @TableField("PARTYLEARN_CONTENT")
    @ApiModelProperty("党建学习内容（富文本）")
    private String partylearnContent;
    /**
     * 党建中心资料附件
     */
    @TableField("PARTYLEARN_TYPE")
    @ApiModelProperty("type 1党建资料 2 宣传学习 3 综合治理")
    private Integer partylearnType;
    /**
     * 党建学习内容（富文本）
     */
    @TableField(value = "PARTYLEARN_ANNX", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("资料附件 [{ \"addr \": \"/date/20220328/505e6cd0d0e04901a3f95394fa956639409.png \", " +
            " \"name \":\" \"12 \"}]\"")
    private Object partylearnAnnx;


    @TableField(exist = false)
    @ApiModelProperty("是否能修改删除 0 不能,1 可以删除")
    private Integer modify;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;


}
