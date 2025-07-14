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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@TableName("p_parcel")
@ApiModel("分部表")
//解决 No serializer found for class
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Parcel implements Serializable {
    private static final long serialVersionUID = -99380477058337124L;

    /**
     * 分部ID
     */
    @TableId(value = "PARCEL_ID", type = IdType.AUTO)
    @ApiModelProperty("分部ID")
    private Long parcelId;


    /**
     * 项目ID
     */
    @TableField("PARCEL_PROJECTID")
    @ApiModelProperty("项目ID")
    private Long parcelProjectid;
    /**
     * 单位项目ID
     */
    @TableField("PARCEL_UNITENGINEID")
    @ApiModelProperty("单位项目ID")
    private Long parcelUnitengineid;

    @ApiModelProperty("单位项目名称")
    @TableField(exist = false)
    private String parcelUnitengineName;

    /**
     * 分部名称
     */
    @TableField("PARCEL_NAME")
    @ApiModelProperty("分部名称")
    @NotNull(message = "分部名称不能为空")
    private String parcelName;

    /**
     * 分部内容
     */
    @TableField("PARCEL_CONTENT")
    @ApiModelProperty("分部内容")
    private String parcelContent;

    /**
     * 分部单位id
     */
    @TableField("PARCEL_UNITID")
    @ApiModelProperty("分部单位id")
    private Long parcelUnitId;
    /**
     * 分部单位名称
     */
    @TableField(exist = false)
    @ApiModelProperty("分部单位名称")
    private String parcelUnitName;

    /**
     * 负责人
     */
    @TableField("PARCEL_PERSON")
    @ApiModelProperty("分部负责人")
    private String parcelPerson;

    /**
     * 联系电话
     */
    @TableField("PARCEL_LINK")
    @ApiModelProperty("联系电话")
    private String parcelLink;


    @TableField(exist = false)
    @ApiModelProperty(value = "项目信息")
    private String projectName;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("创建人")
//    @JsonIgnore
    private Long createUserid;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JsonIgnore
    private Date createTime;
    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;
}
