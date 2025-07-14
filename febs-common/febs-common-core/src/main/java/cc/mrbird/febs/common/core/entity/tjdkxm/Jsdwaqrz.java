package cc.mrbird.febs.common.core.entity.tjdkxm;


import java.util.Date;
import java.math.BigDecimal;

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
@TableName("p_jsdwaqrz")
@ApiModel("建设单位安全日志")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Jsdwaqrz implements Serializable {
    private static final long serialVersionUID = -29922008417210114L;

    /**
     * 建设单位安全日志ID
     */
    @TableId(value = "JSDWAQRZ_ID", type = IdType.AUTO)
    @ApiModelProperty("建设单位安全日志ID")
    private Long jsdwaqrzId;


    /**
     * 对应项目ID
     */
    @TableField("JSDWAQRZ_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long jsdwaqrzProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 记录时间
     */
    @TableField("JSDWAQRZ_CREATETIME")
    @ApiModelProperty("记录时间")

    private Date jsdwaqrzCreatetime;
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
    @TableField("JSDWAQRZ_CREATEMAN")
    @ApiModelProperty("记录人")
    private String jsdwaqrzCreateman;
    /**
     * 记录人
     */
    @TableField("JSDWAQRZ_CREATEMANID")
    @ApiModelProperty("记录人id")
    private Long jsdwaqrzCreatemanid;

    /**
     * 建设单位项目负责人
     */
    @TableField("JSDWAQRZ_JSDWXMFZR")
    @ApiModelProperty("建设单位项目负责人")
    private String jsdwaqrzJsdwxmfzr;

    /**
     * 施工人数
     */
    @TableField("JSDWAQRZ_SGRS")
    @ApiModelProperty("施工人数")
    private Integer jsdwaqrzSgrs;

    /**
     * 考勤记录-项目经理
     */
    @TableField("JSDWAQRZ_XMJL")
    @ApiModelProperty("考勤记录-项目经理")
    private String jsdwaqrzXmjl;
    /**
     * 考勤记录-项目经理
     */
    @TableField(exist = false)
    @ApiModelProperty("考勤记录-项目经理名称")
    private String jsdwaqrzXmjlname;

    /**
     * 考勤记录-技术负责人
     */
    @TableField("JSDWAQRZ_JSFZR")
    @ApiModelProperty("考勤记录-技术负责人")
    private String jsdwaqrzJsfzr;
    /**
     * 考勤记录-技术负责人
     */
    @TableField(exist = false)
    @ApiModelProperty("考勤记录-技术负责人名称")
    private String jsdwaqrzJsfzrname;

    /**
     * 考勤记录-专职安全员
     */
    @TableField("JSDWAQRZ_ZZAQY")
    @ApiModelProperty("考勤记录-专职安全员")
    private String jsdwaqrzZzaqy;
    /**
     * 考勤记录-专职安全员
     */
    @TableField(exist = false)
    @ApiModelProperty("考勤记录-专职安全员名称")
    private String jsdwaqrzZzaqyname;

    /**
     * 考勤记录-总监
     */
    @TableField("JSDWAQRZ_ZJ")
    @ApiModelProperty("考勤记录-总监")
    private String jsdwaqrzZj;
    /**
     * 考勤记录-总监
     */
    @TableField(exist = false)
    @ApiModelProperty("考勤记录-总监名称")
    private String jsdwaqrzZjname;

    /**
     * 考勤记录-专业监理
     */
    @TableField("JSDWAQRZ_ZYJL")
    @ApiModelProperty("考勤记录-专业监理")
    private String jsdwaqrzZyjl;
    /**
     * 考勤记录-专业监理
     */
    @TableField(exist = false)
    @ApiModelProperty("考勤记录-专业监理名称")
    private String jsdwaqrzZyjlname;

    /**
     * 当日施工主要记录 工程进度、操作部位、形象进度情况
     */
    @TableField("JSDWAQRZ_JDQK")
    @ApiModelProperty("当日施工主要记录 工程进度、操作部位、形象进度情况")
    private String jsdwaqrzJdqk;

    /**
     * 当日施工主要记录 当天协调问题，是否有结果；0无1有
     */
    @TableField("JSDWAQRZ_XTWTFLAG")
    @ApiModelProperty("当日施工主要记录 当天协调问题，是否有结果；0无1有")
    private String jsdwaqrzXtwtflag;

    /**
     * 当日施工主要记录 工地会议简况
     */
    @TableField("JSDWAQRZ_GDHYJK")
    @ApiModelProperty("当日施工主要记录  工地会议简况")
    private String jsdwaqrzGdhyjk;

    /**
     * 各级检查情况；
     */
    @TableField("JSDWAQRZ_GJJCQK")
    @ApiModelProperty("当日施工主要记录 各级检查情况；")
    private String jsdwaqrzGjjcqk;

    /**
     * 危大工程编制、审查、交底
     */
    @TableField("JSDWAQRZ_WDGCJD")
    @ApiModelProperty("危大工程编制、审查、交底")
    private String jsdwaqrzWdgcjd;

    /**
     * 危大工程实施情况
     */
    @TableField("JSDWAQRZ_WDGCSSQK")
    @ApiModelProperty("危大工程实施情况")
    private String jsdwaqrzWdgcssqk;

    /**
     * 危大工程检查验收情况；
     */
    @TableField("JSDWAQRZ_WDGCYSQK")
    @ApiModelProperty("危大工程检查验收情况；")
    private String jsdwaqrzWdgcysqk;

    /**
     * 超过危大工程论证情况；
     */
    @TableField("JSDWAQRZ_CGWDGCLZQK")
    @ApiModelProperty("超过危大工程论证情况；")
    private String jsdwaqrzCgwdgclzqk;

    /**
     * 危大工程监测情况
     */
    @TableField("JSDWAQRZ_WDGCJCQK")
    @ApiModelProperty("危大工程监测情况")
    private String jsdwaqrzWdgcjcqk;

    /**
     * 工程监理履职情况
     */
    @TableField("JSDWAQRZ_GCJLLZQK")
    @ApiModelProperty("工程监理履职情况")
    private String jsdwaqrzGcjllzqk;

    /**
     * 监理报告和处理情况
     */
    @TableField("JSDWAQRZ_JLBG")
    @ApiModelProperty("监理报告和处理情况")
    private String jsdwaqrzJlbg;

    /**
     * 未处理完毕、待解决的情况
     */
    @TableField("JSDWAQRZ_QT")
    @ApiModelProperty("未处理完毕、待解决的情况")
    private String jsdwaqrzQt;

    /**
     * 最高气温
     */
    @TableField("JSDWAQRZ_MAXQW")
    @ApiModelProperty("最高气温")
    private Object jsdwaqrzMaxqw;

    /**
     * 最低气温
     */
    @TableField("JSDWAQRZ_MINQW")
    @ApiModelProperty("最低气温")
    private Object jsdwaqrzMinqw;

    /**
     * 气候 1晴2阴3雨4雪
     */
    @TableField("JSDWAQRZ_QH")
    @ApiModelProperty("气候 1晴2阴3雨4雪")
    private String jsdwaqrzQh;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;


}
