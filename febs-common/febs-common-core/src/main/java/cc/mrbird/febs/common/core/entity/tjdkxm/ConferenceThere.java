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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 11:24
 */
@Data
@TableName(value = "p_conferencethree",autoResultMap = true)
@ApiModel("三重一大会议（主要是用于用户学习，所以以记录为主）")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ConferenceThere implements Serializable {
    @TableId(value = "CONFERENCE_ID", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long conferenceId;

    @TableField("CONFERENCE_THEME")
    @ApiModelProperty("会议主题")
    @NotNull(message = "会议主题不能为空")
    private String conferenceTheme;

    @TableField("CONFERENCE_PROJECTID")
    @ApiModelProperty("项目id")
    private Long conferenceProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    @TableField("CONFERENCE_DATE")
    @ApiModelProperty("会议日期")
    @NotNull(message = "会议日期")
    private Date conferenceDate;

    @TableField(exist = false)
    @ApiModelProperty("查询 会议日期 start")
    private String conferenceDateStart;

    @TableField(exist = false)
    @ApiModelProperty("查询 会议日期 end")
    private String conferenceDateEnd;

    @TableField("CONFERENCE_PLACE")
    @ApiModelProperty("会议地点")
    @NotNull(message = "会议地点不能为空")
    private String conferencePlace;

    @TableField("CONFERENCE_PERSON")
    @ApiModelProperty("记录人")
    private String conferencePerson;

    @TableField("CONFERENCE_ATTENDE")
    @ApiModelProperty("出席对象 选择")
    @NotNull(message = "出席对象 ','")
    private String conferenceAttende;
    @TableField(exist = false)
    @ApiModelProperty("出席对象 输入")
    private String conferenceAttendeName;

    @TableField(value = "CONFERENCE_ANNX", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("附件 json [{'name':'','addr':''}]")
    @NotNull(message = "附件 输入不能为空")
    private Object conferenceAnnx;

    @TableField("CONFERENCE_MINUTE")
    @ApiModelProperty("会议纪要 富文本")
//    @NotNull
    private String conferenceMinute;

    @TableField("CONFERENCE_CREATETIME")
    @ApiModelProperty("创建时间")
    private Date conferenceCreatetime;

    @TableField("CONFERENCE_CREATEID")
    @ApiModelProperty("创建人")
    private Long conferenceCreateid;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
