package cc.mrbird.febs.common.core.entity.tjdkxm;


import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@TableName("p_zzaqglryrz")
@ApiModel("专职安全管理人员日志表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Zzaqglryrz implements Serializable {
    private static final long serialVersionUID = 322703919203282403L;

    /**
     * 专职安全管理人员日志ID
     */
    @TableId(value = "ZZAQGLRYRZ_ID", type = IdType.AUTO)
    @ApiModelProperty("专职安全管理人员日志ID")
    private Long zzaqglryrzId;


    /**
     * 对应项目ID
     */
    @TableField("ZZAQGLRYRZ_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long zzaqglryrzProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 记录时间
     */
    @TableField("ZZAQGLRYRZ_CREATETIME")
    @ApiModelProperty("记录时间")

    private Date zzaqglryrzCreatetime;

    /**
     * 记录时间 start
     */
    @TableField(exist = false)
    @ApiModelProperty("记录时间start")
    private String startCreateTime;
    /**
     * 记录时间 end
     */
    @TableField(exist = false)
    @ApiModelProperty("记录时间end")
    private String endCreateTime;
    /**
     * 记录人
     */
    @TableField("ZZAQGLRYRZ_MAN")
    @ApiModelProperty("记录人")
    private String zzaqglryrzMan;
    /**
     * 记录人
     */
    @TableField("ZZAQGLRYRZ_MANID")
    @ApiModelProperty("记录人id")
    private Long zzaqglryrzManid;

    /**
     * 工程施工部位及进展情况
     */
    @TableField("ZZAQGLRYRZ_CASE")
    @ApiModelProperty("工程施工部位及进展情况")
    private String zzaqglryrzCase;

    /**
     * 当日主要危险性项目作业内容
     */
    @TableField("ZZAQGLRYRZ_WXZYNR")
    @ApiModelProperty("当日主要危险性项目作业内容")
    private String zzaqglryrzWxzynr;

    /**
     * 施工项目安全教育与安全交底情况
     */
    @TableField("ZZAQGLRYRZ_AQJYJDQK")
    @ApiModelProperty("施工项目安全教育与安全交底情况")
    private String zzaqglryrzAqjyjdqk;

    /**
     * 施工作业队伍班前施工安全活动情况
     */
    @TableField("ZZAQGLRYRZ_AQHDQK")
    @ApiModelProperty("施工作业队伍班前施工安全活动情况")
    private String zzaqglryrzAqhdqk;

    /**
     * 现场施工安全巡视与检查情况
     */
    @TableField("ZZAQGLRYRZ_AQXSJCQK")
    @ApiModelProperty("现场施工安全巡视与检查情况")
    private String zzaqglryrzAqxsjcqk;

    /**
     * 季节施工防寒、防暑等措施实施情况
     */
    @TableField("ZZAQGLRYRZ_JJSGCSSSQK")
    @ApiModelProperty("季节施工防寒、防暑等措施实施情况")
    private String zzaqglryrzJjsgcsssqk;

    /**
     * 监理通知或有关部门安全检查情况
     */
    @TableField("ZZAQGLRYRZ_JLTZAQJCQK")
    @ApiModelProperty("监理通知或有关部门安全检查情况")
    private String zzaqglryrzJltzaqjcqk;

    /**
     * 停工、加班情况
     */
    @TableField("ZZAQGLRYRZ_TGJBQK")
    @ApiModelProperty("停工、加班情况")
    private String zzaqglryrzTgjbqk;

    /**
     * 其他应记录的安全与文明施工事项
     */
    @TableField("ZZAQGLRYRZ_QTSGSX")
    @ApiModelProperty("其他应记录的安全与文明施工事项")
    private String zzaqglryrzQtsgsx;

    /**
     * 最高气温
     */
    @TableField("ZZAQGLRYRZ_ZGQW")
    @ApiModelProperty("最高气温")
    private Object zzaqglryrzZgqw;

    /**
     * 最低气温
     */
    @TableField("ZZAQGLRYRZ_ZDQW")
    @ApiModelProperty("最低气温")
    private Object zzaqglryrzZdqw;

    /**
     * 气候 1晴2阴3雨4雪
     */
    @TableField("ZZAQGLRYRZ_QH")
    @ApiModelProperty("气候 1晴2阴3雨4雪")
    private String zzaqglryrzQh;


    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    /**
     * 附件
     */
    @TableField(value = "fjUrl", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("附件（可上传多个）[{''name'':'',''addr'':''}]")
    private Object fjUrl;
}
