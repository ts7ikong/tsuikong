package cc.mrbird.febs.common.core.entity.tjdkxm;

import java.util.Date;
import java.math.BigDecimal;

import cc.mrbird.febs.common.core.entity.system.Log;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("p_aqjlrz")
@ApiModel("安全监理日志表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Aqjlrz implements Serializable {
    private static final long serialVersionUID = -10499040107606609L;

    /**
     * 安全监理日志ID
     */
    @TableId(value = "AQJLRZ_ID", type = IdType.AUTO)
    @ApiModelProperty("安全监理日志ID")
    private Long aqjlrzId;


    /**
     * 对应项目表
     */
    @TableField("AQJLRZ_PROJECTID")
    @ApiModelProperty("对应项目表")
    private Long aqjlrzProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 记录人
     */
    @TableField("AQJLRZ_CREATEMAN")
    @ApiModelProperty("记录人")
    private String aqjlrzCreateman;
    /**
     * 记录人id
     */
    @TableField("AQJLRZ_CREATEMANID")
    @ApiModelProperty("记录人id")
    private Long aqjlrzCreatemanid;

    /**
     * 记录时间
     */
    @TableField("AQJLRZ_CREATETIME")
    @ApiModelProperty("记录时间")

    private Date aqjlrzCreatetime;
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
     * 施工情况-施工部位
     */
    @TableField("AQJLRZ_SGBW")
    @ApiModelProperty("施工情况-施工部位")
    private String aqjlrzSgbw;

    /**
     * 施工情况-施工内容
     */
    @TableField("AQJLRZ_SGNR")
    @ApiModelProperty("施工情况-施工内容")
    private String aqjlrzSgnr;

    /**
     * 监理工作及处理情况-安全生产方面巡视检查记录-人员方面
     */
    @TableField("AQJLRZ_RYFM")
    @ApiModelProperty("监理工作及处理情况-安全生产方面巡视检查记录-人员方面")
    private String aqjlrzRyfm;

    /**
     * 监理工作及处理情况-安全生产方面巡视检查记录-设施
     */
    @TableField("AQJLRZ_SS")
    @ApiModelProperty("监理工作及处理情况-安全生产方面巡视检查记录-设施")
    private String aqjlrzSs;

    /**
     * 监理工作及处理情况-安全生产方面巡视检查记录-设备及用电
     */
    @TableField("AQJLRZ_SBJAQSGYD")
    @ApiModelProperty("监理工作及处理情况-安全生产方面巡视检查记录-设备及用电")
    private String aqjlrzSbjaqsgyd;

    /**
     * 监理工作及处理情况-安全生产方面巡视检查记录-其他
     */
    @TableField("AQJLRZ_QTQK")
    @ApiModelProperty("监理工作及处理情况-安全生产方面巡视检查记录-其他")
    private String aqjlrzQtqk;

    /**
     * 监理工作及处理情况-存在的问题及复查情况
     */
    @TableField("AQJLRZ_QZDWTJFCQK")
    @ApiModelProperty("监理工作及处理情况-存在的问题及复查情况")
    private String aqjlrzQzdwtjfcqk;

    /**
     * 验收情况
     */
    @TableField("AQJLRZ_YSQK")
    @ApiModelProperty("验收情况")
    private String aqjlrzYsqk;

    /**
     * 4、见证及送检情况
     */
    @TableField("AQJLRZ_JZJSJQK")
    @ApiModelProperty("4、见证及送检情况")
    private String aqjlrzJzjsjqk;

    /**
     * 5、旁站情况
     */
    @TableField("AQJLRZ_PZQK")
    @ApiModelProperty("5、旁站情况")
    private String aqjlrzPzqk;

    /**
     * 6、审核审批情况
     */
    @TableField("AQJLRZ_SHSPQK")
    @ApiModelProperty("6、审核审批情况")
    private String aqjlrzShspqk;

    /**
     * 来往函件
     */
    @TableField("AQJLRZ_LWHJ")
    @ApiModelProperty("来往函件")
    private String aqjlrzLwhj;

    /**
     * 其他
     */
    @TableField("AQJLRZ_QT")
    @ApiModelProperty("其他")
    private String aqjlrzQt;

    /**
     * 监理工程师
     */
    @TableField("AQJLRZ_JLGCS")
    @ApiModelProperty("监理工程师")
    private String aqjlrzJlgcs;

    /**
     * 总监理工程师
     */
    @TableField("AQJLRZ_ZJLGCS")
    @ApiModelProperty("总监理工程师")
    private String aqjlrzZjlgcs;

    /**
     * 最高气温
     */
    @TableField("AQJLRZ_MAXQW")
    @ApiModelProperty("最高气温")
    private Object aqjlrzMaxqw;

    /**
     * 最低气温
     */
    @TableField("AQJLRZ_MINQW")
    @ApiModelProperty("最低气温")
    private Object aqjlrzMinqw;

    /**
     * 气候 1晴2阴3雨4雪
     */
    @TableField("AQJLRZ_QH")
    @ApiModelProperty("气候 1晴2阴3雨4雪")
    private String aqjlrzQh;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;


}
