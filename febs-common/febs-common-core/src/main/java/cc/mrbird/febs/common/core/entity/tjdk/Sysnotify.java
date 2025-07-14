package cc.mrbird.febs.common.core.entity.tjdk;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("p_sysnotify")
@ApiModel("系统通知")
public class Sysnotify implements Serializable {
    private static final long serialVersionUID = 337638300295779751L;
    /**
     * 所有人
     */
    public static final String TYPE_ALL = "1";
    /**
     * 部门
     */
    public static final String TYPE_DEPT = "2";
    /**
     * 领导班子
     */
    public static final String TYPE_LEADERSHIP_TEAM = "3";
    /**
     * 项目
     */
    public static final String TYPE_PROJECT = "4";
    /**
     * 特定的人
     */
    public static final String TYPE_USER = "5";

    /**
     * 系统通知id
     */
    @TableId(value = "SYSNOTIFY_ID", type = IdType.AUTO)
    @ApiModelProperty("系统通知id")
    private Long sysnotifyId;


    /**
     * 通知标题
     */
    @TableField("SYSNOTIFY_TITLE")
    @ApiModelProperty("通知标题")
    private String sysnotifyTitle;

    /**
     * 通知内容
     */
    @TableField("SYSNOTIFY_CONTENT")
    @ApiModelProperty("通知内容")
    private String sysnotifyContent;
    /**
     * 通知附件
     */
    @TableField(value = "SYSNOTIFY_ANNX", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("通知附件 json [{\"addr\": \"/date/20220328/505e6cd0d0e04901a3f95394fa956639409.png\", \"name\": " +
            "\"12\"}]")
    private Object sysnotifyAnnx;

    /**
     * 下发时间
     */
    @TableField("SYSNOTIFY_TIME")
    @ApiModelProperty("下发时间")
    private Date sysnotifyTime;

//    /**
//     * 项目名称
//     */
//    @TableField(exist = false)
//    @ApiModelProperty("名称 ','")
//    private String projectName;
    /**
     * 项目名称
     */
    @TableField("SYSNOTITY_TYPE")
    @ApiModelProperty("通知对象 类型 1所有人 2部门 3领导班子 4项目（可选多个）5选择特定的人(可选多个)")
    private String sysnotityType;
    /**
     * 通知对象
     */
    @TableField("SYSNOTITY_CONTRANT")
    @ApiModelProperty("通知对象")
    private String sysnotityContrant;

    @TableField(exist = false)
    @ApiModelProperty("是否已读")
    private Integer isRead;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;
    @Data
    @ApiModel("系统通知查询")
    public static class Params {
        /**
         * 通知标题
         */
        @TableField("SYSNOTIFY_TITLE")
        @ApiModelProperty("通知标题")
        private String sysnotifyTitle;
        /**
         * 项目名称
         */
        @TableField(exist = false)
        @ApiModelProperty("名称 ','分割")
        private String sysnotityContrant;

        /**
         * 通知内容
         */
        @TableField("SYSNOTIFY_CONTENT")
        @ApiModelProperty("通知内容")
        private String sysnotifyContent;

        /**
         * 查询用 下发开始时间
         */
        @TableField(exist = false)
        @ApiModelProperty("下发开始时间")
        private String sysnotifyStartTime;
        /**
         * 查询用 下发结束时间
         */
        @TableField(exist = false)
        @ApiModelProperty("下发结束时间")
        private String sysnotifyEndTime;
        @TableField(exist = false)
        @ApiModelProperty("是否已读")
        private Integer isRead;
    }

}
