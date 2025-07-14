package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "p_majorprojectlog", autoResultMap = true)
@ApiModel("重大项目日志")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class MajorProjectLog implements Serializable {
    private static final long serialVersionUID = -20515202770104348L;

    /**
     * 用户党建学习记录id
     */
    @TableId(value = "MAJOR_ID", type = IdType.AUTO)
    @ApiModelProperty("重大项目日志id")
    private Long majorId;
    /**
     * 党建学习id
     */
    @TableField(value = "MAJOR_TITLE")
    @ApiModelProperty("标题")
    private String majorTitle;

    /**
     * 项目id
     */
    @TableField("MAJOR_PROJECTID")
    @ApiModelProperty("项目id")
    private Long majorProjectid;
    /**
     * 项目id
     */
    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String majorProjectname;
    /**
     * 内容
     */
    @TableField("MAJOR_CONTENT")
    @ApiModelProperty("内容")
    private String majorContent;


    /**
     * 图片 “,”分割
     */
    @TableField("MAJOR_IMG")
    @ApiModelProperty("图片 “,”分割")
    private String majorImg;

    /**
     * 用户id
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("用户id")
    private Long createUserid;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    @ApiModelProperty("用户名称")
    private String createUsername;
    /**
     * 创建时间
     */
    @TableField("MAJOR_TIME")
    @ApiModelProperty("事件发生时间")
    private Date majorTime;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("查询")
    public static class Params {
        /**
         * 项目id
         */
        @TableField("MAJOR_PROJECTID")
        @ApiModelProperty("项目id")
        private Long majorProjectid;
        /**
         * 项目id
         */
        @TableField(exist = false)
        @ApiModelProperty("项目名称")
        private String majorProjectname;
        /**
         * 党建学习id
         */
        @TableField(value = "MAJOR_TITLE")
        @ApiModelProperty("标题")
        private String majorTitle;

        /**
         * 创建时间
         */
        @ApiModelProperty("事件发生时间 start")
        private String majorStartTime;
        /**
         * 创建时间
         */
        @ApiModelProperty("事件发生时间 start")
        private String majorEndTime;
    }
}
