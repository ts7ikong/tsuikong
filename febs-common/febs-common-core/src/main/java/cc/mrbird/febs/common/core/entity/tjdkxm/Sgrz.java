package cc.mrbird.febs.common.core.entity.tjdkxm;


import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@TableName("p_sgrz")
@ApiModel("施工日志表")
public class Sgrz implements Serializable {
    private static final long serialVersionUID = 594629093186829184L;

    /**
     * 施工日志ID
     */
    @TableId(value = "SGRZ_ID", type = IdType.AUTO)
    @ApiModelProperty("施工日志ID")
    private Long sgrzId;


    /**
     * 对应项目ID
     */
    @TableField("SGRZ_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long sgrzProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 记录人
     */
    @TableField("SGRZ_CREATEMAN")
    @ApiModelProperty("记录人")
    private String sgrzCreateman;
    /**
     * 记录人
     */
    @TableField("SGRZ_CREATEMANID")
    @ApiModelProperty("记录人id")
    private Long sgrzCreatemanid;

    /**
     * 记录时间
     */
    @TableField("SGRZ_CREATETIME")
    @ApiModelProperty("记录时间")

    private Date sgrzCreatetime;
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
     * 最高气温
     */
    @TableField("SGRZ_MAXQW")
    @ApiModelProperty("最高气温")
    private Object sgrzMaxqw;

    /**
     * 最低气温
     */
    @TableField("SGRZ_MINQW")
    @ApiModelProperty("最低气温")
    private Object sgrzMinqw;

    /**
     * 上午天气 1晴2阴3雨4雪
     */
    @TableField("SGRZ_SWTQ")
    @ApiModelProperty("上午天气 1晴2阴3雨4雪")
    private String sgrzSwtq;

    /**
     * 下午天气 1晴2阴3雨4雪
     */
    @TableField("SGRZ_XWTX")
    @ApiModelProperty("下午天气 1晴2阴3雨4雪")
    private String sgrzXwtx;

    /**
     * 晚上天气 1晴2阴3雨4雪
     */
    @TableField("SGRZ_WSTQ")
    @ApiModelProperty("晚上天气 1晴2阴3雨4雪")
    private String sgrzWstq;

    /**
     * 施工事项 施工部位
     */
    @TableField("SGRZ_SGSGBW")
    @ApiModelProperty("施工事项 施工部位")
    private String sgrzSgsgbw;

    /**
     * 施工事项 施工人数
     */
    @TableField("SGRZ_SGSGRS")
    @ApiModelProperty("施工事项 施工人数")
    private Integer sgrzSgsgrs;

    /**
     * 施工事项 事项内容
     */
    @TableField("SGRZ_SGSXNR")
    @ApiModelProperty("施工事项 事项内容")
    private String sgrzSgsxnr;

    /**
     * 施工事项 进度进展
     */
    @TableField("SGRZ_SGJDJZ")
    @ApiModelProperty("施工事项 进度进展")
    private String sgrzSgjdjz;

    /**
     * 施工事项 现场图片
     */
    @TableField("SGRZ_SGXCTP")
    @ApiModelProperty("施工事项 现场图片")
    private String sgrzSgxctp;
    @TableField(exist = false)
    @ApiModelProperty("")
    private List<String> sgrzSgxctpList;

    /**
     * 安全事项 施工部位
     */
    @TableField("SGRZ_AQSGBW")
    @ApiModelProperty("安全事项 施工部位")
    private String sgrzAqsgbw;

    /**
     * 安全事项 施工人数
     */
    @TableField("SGRZ_AQSGRS")
    @ApiModelProperty("安全事项 施工人数")
    private Integer sgrzAqsgrs;

    /**
     * 安全事项 事项内容
     */
    @TableField("SGRZ_AQSXNR")
    @ApiModelProperty("安全事项 事项内容")
    private String sgrzAqsxnr;

    /**
     * 安全事项 解决举措
     */
    @TableField("SGRZ_AQJJJC")
    @ApiModelProperty("安全事项 解决举措")
    private String sgrzAqjjjc;

    /**
     * 安全事项 现场图片
     */
    @TableField("SGRZ_AQXCTP")
    @ApiModelProperty("安全事项 现场图片")
    private String sgrzAqxctp;
    @TableField(exist = false)
    @ApiModelProperty("")
    private List<String> sgrzAgxctpList;

    /**
     * 质量事项 施工部位
     */
    @TableField("SGRZ_ZLSGBW")
    @ApiModelProperty("质量事项 施工部位")
    private String sgrzZlsgbw;

    /**
     * 质量事项 施工人数
     */
    @TableField("SGRZ_ZLSGRS")
    @ApiModelProperty("质量事项 施工人数")
    private Integer sgrzZlsgrs;

    /**
     * 质量事项 事项内容
     */
    @TableField("SGRZ_ZLSXNR")
    @ApiModelProperty("质量事项 事项内容")
    private String sgrzZlsxnr;

    /**
     * 质量事项 解决举措
     */
    @TableField("SGRZ_ZLJJJC")
    @ApiModelProperty("质量事项 解决举措")
    private String sgrzZljjjc;

    /**
     * 质量事项 现场图片
     */
    @TableField("SGRZ_ZLXCTP")
    @ApiModelProperty("质量事项 现场图片")
    private String sgrzZlxctp;
    /**
     * 质量事项 现场图片
     */
    @TableField(exist = false)
    @ApiModelProperty("质量事项 现场图片")
    private List<String> sgrzZlxctpList;

    /**
     * 其他事项 施工部位
     */
    @TableField("SGRZ_QTSGBW")
    @ApiModelProperty("其他事项 施工部位")
    private String sgrzQtsgbw;

    /**
     * 其他事项 施工人数
     */
    @TableField("SGRZ_QTSGRS")
    @ApiModelProperty("其他事项 施工人数")
    private Integer sgrzQtsgrs;

    /**
     * 其他事项 事项内容
     */
    @TableField("SGRZ_QTSXNR")
    @ApiModelProperty("其他事项 事项内容")
    private String sgrzQtsxnr;

    /**
     * 其他事项 解决举措
     */
    @TableField("SGRZ_QTJJJC")
    @ApiModelProperty("其他事项 解决举措")
    private String sgrzQtjjjc;

    /**
     * 其他事项 现场图片
     */
    @TableField("SGRZ_QTXCTP")
    @ApiModelProperty("其他事项 现场图片")
    private String sgrzQtxctp;
    @TableField(exist = false)
    @ApiModelProperty("")
    private List<String> sgrzQtxctpList;

    @TableField(exist = false)
    @ApiModelProperty("气候")
    private String climate;


    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
