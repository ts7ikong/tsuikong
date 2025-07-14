package cc.mrbird.febs.common.core.entity.tjdkxm;

import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@Data
@TableName("p_datamanagement")
@ApiModel("资料管理")
public class Datamanagement implements Serializable {
    private static final long serialVersionUID = 977258378891411636L;

    /**
     * 资料id
     */
    @TableId(value = "DATAMANAGEMENT_ID", type = IdType.AUTO)
    @ApiModelProperty("资料id")
    private Long datamanagementId;

    /**
     * 项目id
     */
    @TableField("DATAMANAGEMENT_PID")
    @ApiModelProperty("项目id")
    private Long datamanagementPid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 单位项目id
     */
    @TableField("DANGER_UNITENGINEID")
    @ApiModelProperty("单位项目id")
    private Long dangerUnitengineid;
    /**
     * 单位项目name
     */
    @TableField(exist = false)
    @ApiModelProperty("单位项目name")
    private String unitengineName;

    /**
     * 分部id
     */
    @TableField("DANGER_PARCELID")
    @ApiModelProperty("分部id")
    private Long dangerParcelid;

    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("DANGER_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long dangerSubitemid;

    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 资料名称
     */
    @TableField("DATAMANAGEMENT_NAME")
    @ApiModelProperty("资料名称")
    @NotNull(message = "资料名称不能为空")
    private String datamanagementName;

    /**
     * 上传人用户ID
     */
    @TableField("DATAMANAGEMENT_USERID")
    @ApiModelProperty("上传人用户ID")
    private Long datamanagementUserid;
    /**
     * 上传人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("上传人用户ID")
    private String datamanagementUserName;

    /**
     * 上传时间
     */
    @TableField("DATAMANAGEMENT_TIME")
    @ApiModelProperty("上传时间")

    private Date datamanagementTime;

    /**
     * 文件地址
     */
    @TableField("DATAMANAGEMENT_ADDR")
    @ApiModelProperty("文件地址")
    private String datamanagementAddr;

    /**
     * 类型 [1 党建资料、2 合同模板、3 合同档案库、 4 施工档案、5 技术台账、6 技术标准、7 技术交底、 8 交接资料、 9 设计变更、10 工程确认单、11 工程联络单、12 测绘资料、13 竣工资料，14
     * 数字化中心，15 质量规范标准资料]
     */
    @TableField("DATAMANAGEMENT_TYPE")
    @ApiModelProperty("类型 [1 党建资料、2 合同模板、3 合同档案库、4施工档案、5技术台账、6技术标准、" + "7技术交底、8交接资料、 9设计变更、10工程确认单、11工程联络单、12测绘资料、"
        + "13竣工资料，14数字化中心，15质量规范标准资料,16 照片管理  +100为模板 正常传]")
    @NotNull(message = "资料类型不能为空")
    private String datamanagementType;
    /**
     * 发表人用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty("发表人用户信息")
    private Map<String, Object> userInfo;

    @TableField(exist = false)
    @ApiModelProperty("类型为党建资料时：是否能修改删除 0 不能,1 可以删除")
    private Integer modify;

    @TableField("DATAMANAGEMENT_CLASSID")
    @ApiModelProperty("资料分类id")
    private Long datamanagementClassid;
    @TableField(exist = false)
    @ApiModelProperty("资料分类类名称")
    private String datamanagementClassName;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("新增")
    public static class Add {
        /**
         * 类型 [1 党建资料、2 合同模板、3 合同档案库、 4 施工档案、5 技术台账、6 技术标准、7 技术交底、 8 交接资料、 9 设计变更、10 工程确认单、11 工程联络单、12 测绘资料、13 竣工资料，14
         * 数字化中心，15 质量规范标准资料]
         */
        @TableField("DATAMANAGEMENT_TYPE")
        @ApiModelProperty("类型 [1 党建资料、2 合同模板、3 合同档案库、4施工档案、5技术台账、6技术标准、" + "7技术交底、8交接资料、 9设计变更、10工程确认单、11工程联络单、12测绘资料、"
            + "13竣工资料，14数字化中心，15质量规范标准资料,16 照片管理  +100为模板 正常传]")
        @NotNull(message = "资料类型不能为空")
        private String datamanagementType;
        /**
         * 资料名称
         */
        @TableField("DATAMANAGEMENT_NAME")
        @ApiModelProperty("资料名称")
        @NotNull(message = "资料名称不能为空")
        private String datamanagementName;
        /**
         * 文件地址
         */
        @TableField("DATAMANAGEMENT_ADDR")
        @ApiModelProperty("文件地址")
        private String datamanagementAddr;

        @TableField("DATAMANAGEMENT_CLASSID")
        @ApiModelProperty("资料分类id")
        @NotNull(message = "资料名称不能为空")
        private Long datamanagementClassid;
        /**
         * 项目id
         */
        @TableField("DATAMANAGEMENT_PID")
        @ApiModelProperty("项目id")
        private Long datamanagementPid;

        /**
         * 单位项目id
         */
        @TableField("DANGER_UNITENGINEID")
        @ApiModelProperty("单位项目id")
        private Long dangerUnitengineid;
        /**
         * 单位项目name
         */
        @TableField(exist = false)
        @ApiModelProperty("单位项目name")
        private String unitengineName;

        /**
         * 分部id
         */
        @TableField("DANGER_PARCELID")
        @ApiModelProperty("分部id")
        private Long dangerParcelid;

        @TableField(exist = false)
        @ApiModelProperty("分部 name")
        private String parcelName;

        /**
         * 分项id
         */
        @TableField("DANGER_SUBITEMID")
        @ApiModelProperty("分项id")
        private Long dangerSubitemid;

        @TableField(exist = false)
        @ApiModelProperty("分项 name")
        private String subitemName;
    }

    @Data
    @ApiModel("查询")
    public static class Params {
        /**
         * 类型 [1 党建资料、2 合同模板、3 合同档案库、 4 施工档案、5 技术台账、6 技术标准、7 技术交底、 8 交接资料、 9 设计变更、10 工程确认单、11 工程联络单、12 测绘资料、13 竣工资料，14
         * 数字化中心，15 质量规范标准资料]
         */
        @TableField("DATAMANAGEMENT_TYPE")
        @ApiModelProperty("类型 [1 党建资料、2 合同模板、3 合同档案库、4施工档案、5技术台账、6技术标准、" + "7技术交底、8交接资料、 9设计变更、10工程确认单、11工程联络单、12测绘资料、"
            + "13竣工资料，14数字化中心，15质量规范标准资料,16 照片管理  +100为模板 正常传]")
        @NotNull(message = "资料类型不能为空")
        private String datamanagementType;
        /**
         * 资料名称
         */
        @TableField("DATAMANAGEMENT_NAME")
        @ApiModelProperty("资料名称")
        @NotNull(message = "资料名称不能为空")
        private String datamanagementName;
        @TableField("DATAMANAGEMENT_CLASSID")
        @ApiModelProperty("资料分类id")
        private Long datamanagementClassid;

        /**
         * 单位项目id
         */
        @TableField("DANGER_UNITENGINEID")
        @ApiModelProperty("单位项目id")
        private Long dangerUnitengineid;
        /**
         * 单位项目name
         */
        @TableField(exist = false)
        @ApiModelProperty("单位项目name")
        private String unitengineName;

        /**
         * 分部id
         */
        @TableField("DANGER_PARCELID")
        @ApiModelProperty("分部id")
        private Long dangerParcelid;

        @TableField(exist = false)
        @ApiModelProperty("分部 name")
        private String parcelName;

        /**
         * 分项id
         */
        @TableField("DANGER_SUBITEMID")
        @ApiModelProperty("分项id")
        private Long dangerSubitemid;

        @TableField(exist = false)
        @ApiModelProperty("分项 name")
        private String subitemName;

    }
}
