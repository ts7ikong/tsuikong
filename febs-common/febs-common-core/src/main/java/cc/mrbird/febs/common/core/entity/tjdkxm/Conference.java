package cc.mrbird.febs.common.core.entity.tjdkxm;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("p_conference")
@ApiModel("会议表ID")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Conference implements Serializable {
    private static final long serialVersionUID = 919114025466417373L;

    /**
     * 会议表ID
     */
    @TableId(value = "CONFERENCE_ID", type = IdType.AUTO)
    @ApiModelProperty("会议表ID")
    private Long conferenceId;

    /**
     * 对应项目ID
     */
    @TableField("CONFERENCE_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long conferenceProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 会议主题
     */
    @TableField("CONFERENCE_THEME")
    @ApiModelProperty("会议主题")
    @NotNull(message = "会议主题不能为空")
    private String conferenceTheme;

    /**
     * 会议时间
     */
    @TableField("CONFERENCE_TIME")
    @ApiModelProperty("会议时间")
    @NotNull(message = "会议时间")
    private Date conferenceTime;

    /**
     * 会议地点
     */
    @TableField("CONFERENCE_SITE")
    @ApiModelProperty("会议地点")
    @NotNull(message = "会议地点不能为空")
    private String conferenceSite;

    /**
     * 参与会议用户IDs ,分割
     */
    @TableField("CONFERENCE_USERIDS")
    @ApiModelProperty("参与会议用户IDs ,分割")
    private String conferenceUserids;
    /**
     * 参与会议用户','分割
     */
    @TableField(exist = false)
    @ApiModelProperty("参与会议用户Name ','分割")
    private String conferenceUserName;
    /**
     * 状态
     */
    @TableField("CONFERENCE_TYPE")
    @ApiModelProperty("状态 0待开始  1进行中 2已结束 3已过期")
    private String conferenceType;

    /**
     * 会议发起人用户ID
     */
    @TableField("CONFERENCE_CREATEUSERID")
    @ApiModelProperty("会议发起人用户ID")
    private Long conferenceCreateuserid;
    /**
     * 会议发起人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("会议发起人用户姓名")
    private String conferenceCreateusername;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @ApiModel("会议查询参数")
    @Data
    public static class Params {
        /**
         * 会议主题
         */
        @TableField("CONFERENCE_THEME")
        @ApiModelProperty("会议主题")
        private String conferenceTheme;
        /**
         * 会议地点
         */
        @TableField("CONFERENCE_SITE")
        @ApiModelProperty("会议地点")
        private String conferenceSite;
        /**
         * 会议时间 start
         */
        @TableField(exist = false)
        @ApiModelProperty("会议时间 start")
        private String startTime;
        /**
         * 会议时间 end
         */
        @TableField(exist = false)
        @ApiModelProperty("会议时间 end")
        private String endTime;
    }
}
