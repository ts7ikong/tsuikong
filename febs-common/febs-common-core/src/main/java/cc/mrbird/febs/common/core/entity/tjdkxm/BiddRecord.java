package cc.mrbird.febs.common.core.entity.tjdkxm;

import cc.mrbird.febs.common.core.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业绩统计[项目中标记录]
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 14:30
 */
@Data
@TableName(value = "p_biddrecord", autoResultMap = true)
@ApiModel("业绩统计[项目中标记录]")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class BiddRecord implements Serializable {


    /**
     * 业绩统计ID
     */
    @TableId(value = "BIDDRECORD_ID", type = IdType.AUTO)
    @ApiModelProperty("业绩统计ID")
    private Long biddrecordId;

    /**
     * 编号
     */
    @TableField("BIDDRECORD_CODE")
    @ApiModelProperty("编号")
    @ExcelField(value = "序号")
    private String biddrecordCode;
    /**
     * 对应项目名称
     */
    @TableField("BIDDRECORD_PROJECTNAME")
    @ApiModelProperty("对应项目名称")
    @ExcelField(value = "项目名称")
    private String biddrecordProjectName;
    /**
     * 对应招投文件id
     */
    @TableField("BIDDRECORD_BIDDID")
    @ApiModelProperty("对应招投文件id")
    private Long biddrecordBiddid;

    /**
     * 中标金额
     */
    @TableField("BIDDRECORD_AMOUNT")
    @ApiModelProperty("中标金额")
    @ExcelField(value = "中标金额")
    private String biddrecordAmount;
    /**
     * 对应招投文件id
     */
    @TableField(exist = false)
    @ApiModelProperty("对应招投单位")
    @ExcelField(value = "中标单位名称")
    private Long biddrecordBiddUnit;
    /**
     * 项目主要工作内容
     */
    @TableField("BIDDRECORD_WORKLOAD")
    @ApiModelProperty("项目主要工作内容")
    @ExcelField(value = "项目主要工作内容")
    private String biddrecordWorkload;
    /**
     * 项目经理
     */
    @TableField("BIDDRECORD_PROJECTMANAGER")
    @ApiModelProperty("项目经理 ','隔开 userid")
    private String biddrecordProjectmanager;
    /**
     * 项目经理
     */
    @TableField(exist = false)
    @ApiModelProperty("项目经理 ','隔开 username")
    private String biddrecordProjectmanagerName;
    /**
     * 省份
     */
    @TableField("BIDDRECORD_PROVINCE")
    @ApiModelProperty("省份")
    @ExcelField(value = "省份")
    private String biddrecordProvince;
    /**
     * 合同时间
     */
    @TableField(value = "BIDDRECORD_CONTRACTTIME")
    @ApiModelProperty("合同时间")
    @ExcelField(value = "合同时间")
    private Date biddrecordContracttime;
    /**
     * 中标时间
     */
    @TableField("BIDDRECORD_TIME")
    @ApiModelProperty("中标时间")
    @ExcelField(value = "中标时间", writeConverter = TimeConverter.class)
    private Date biddrecordTime;
    /**
     * 验收时间 json
     */
    @ApiModelProperty("验收时间 ")
    @TableField("BIDDRECORD_ACCEPTANCETIME")
    private Date biddrecordAcceptancetime;


    /**
     * 项目资料json [{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDDRECORD_PROJECTINFO", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("项目资料json [{''name'':'',''addr'':''}]")
    private Object biddrecordProjectinfo;
    @TableField(value = "BIDDRECORD_ISINFO")
    @ApiModelProperty("是否有 0无 1有 中标通知书")
    private Integer biddrecordIsInfo;
    /**
     * 中标通知书 json [{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDDRECORD_INFO", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("中标通知书 json [{''name'':'',''addr'':''}]")
    private Object biddrecordInfo;
    @TableField(value = "BIDDRECORD_ISCONTRACTINFO")
    @ApiModelProperty("是否有 0无 1有 合同资料")
    private Integer biddrecordIsContractinfo;
    /**
     * 合同资料 json [{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDDRECORD_CONTRACTINFO", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("合同资料 json [{''name'':'',''addr'':''}]")
    private Object biddrecordContractinfo;

    /**
     * 竣工资料 json [{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDDRECORD_COMPLETIONINFO", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("竣工资料 json [{''name'':'',''addr'':''}]")
    private Object biddrecordCompletioninfo;
    @TableField(value = "BIDDRECORD_ISACCEPTANCEINFO")
    @ApiModelProperty("是否有 0无 1有 验收资料")
    private Integer biddrecordIsAcceptanceinfo;
    /**
     * 验收资料 json [{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDDRECORD_ACCEPTANCEINFO", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("验收资料 json [{''name'':'',''addr'':''}]")
    private Object biddrecordAcceptanceinfo;

    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("创建人")
//    @JsonIgnore
    private Long createUserid;
    /**
     * 创建人
     */
    @TableField(exist = false)
    @ApiModelProperty("创建人")
//    @JsonIgnore
    private String createUserName;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
//    @JsonIgnore
    private Date createTime;
    /**
     * 创建时间
     */
    @TableField(exist = false)
    @ApiModelProperty("创建时间")
//    @JsonIgnore
    private String startTime;
    /**
     * 创建时间
     */
    @TableField(exist = false)
    @ApiModelProperty("创建时间")
//    @JsonIgnore
    private String endTime;
    /**
     * 是否删除
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private Integer isDelete;
}
