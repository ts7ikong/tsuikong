package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/6 10:55
 */
@Data
@TableName("p_unitengine")
@ApiModel("单位项目表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class UnitEngine implements Serializable {
    /**
     * 分项ID
     */
    @TableId(value = "UNIT_ID", type = IdType.AUTO)
    @ApiModelProperty("单位项目id")
    private Long unitId;
    /**
     * 项目id
     */
    @TableField("UNIT_PROJECTID")
    @ApiModelProperty("项目项目id")
    private Long unitProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;
    /**
     * 名称
     */
    @TableField("UNIT_NAME")
    @ApiModelProperty("单位项目名称")
    private String unitName;
    /**
     * 地址
     */
    @TableField("UNIT_ADDR")
    @ApiModelProperty("单位项目地址")
    private String unitAddr;
    /**
     * 合同编码
     */
    @TableField("UNIT_CONTRACTCODE")
    @ApiModelProperty("项目合同编码")
    private String unitContractcode;
    /**
     * 施工许可证号
     */
    @TableField("UNIT_PERMITNUMBER")
    @ApiModelProperty("项目施工许可证号")
    private String unitPermitnumber;
    /**
     * 资质标准技术指标
     */
    @TableField("UNIT_STANDARDS")
    @ApiModelProperty("项目资质标准技术指标")
    private String unitStandards;
    /**
     * 资质申报技术指标
     */
    @TableField("UNIT_INDICATORS")
    @ApiModelProperty("项目资质申报技术指标")
    private String unitIndicators;
    /**
     * 单项合同价（万元）
     */
    @TableField("UNIT_SINGLECONTRACTPRICE")
    @ApiModelProperty("项目单项合同价（万元）")
    private BigDecimal unitSinglecontractprice;
    /**
     * 单项结算价（万元）
     */
    @TableField("UNIT_SINGlETLEMENTPRICE")
    @ApiModelProperty("项目单项结算价（万元）")
    private BigDecimal unitSingletlementprice;
    /**
     * 工程承包方式
     */
    @TableField("UNIT_CONTRACTMETHOD")
    @ApiModelProperty("项目工程承包方式")
    private String unitContractmethod;
    /**
     * 施工组织方式
     */
    @TableField("UNIT_CONSTRUCTION")
    @ApiModelProperty("项目施工组织方式")
    private String unitConstruction;
    /**
     * 计划开工日期
     */
    @TableField("UNIT_PLANSTARTTIME")
    @ApiModelProperty("项目计划开工日期")
    private Date unitPlanstarttime;
    /**
     * 计划竣工日期
     */
    @TableField("UNIT_PLANENDTIME")
    @ApiModelProperty("项目计划竣工日期")
    private Date unitPlanendtime;
    /**
     * 计划工期（天）
     */
    @TableField("UNIT_PLANDAYS")
    @ApiModelProperty("项目计划工期（天）根据计划开工和竣工自动计算")
    private String unitPlandays;
    /**
     * 实际开工日期
     */
    @TableField("UNIT_ACTUALSTARTTIME")
    @ApiModelProperty("项目实际开工日期")
    private Date unitActualstarttime;
    /**
     * 实际竣工日期
     */
    @TableField("UNIT_ACTUALENDTIME")
    @ApiModelProperty("项目实际竣工日期")
    private Date unitActualendtime;
    /**
     * 实际工期（天）
     */
    @TableField("UNIT_ACUALEDAYS")
    @ApiModelProperty("项目实际工期（天）根据开竣工自动计算")
    private String unitAcualedays;
    /**
     * 其它说明
     */
    @TableField("UNIT_OTHERINFO")
    @ApiModelProperty("项目其它说明")
    private String unitOtherinfo;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("创建人")
    private Long createUserid;
    /**
     * 创建人
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建人")
    private Date createTime;
    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;
}
