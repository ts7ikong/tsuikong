package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.yulichang.annotation.EntityMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("p_subitem")
@ApiModel("分项表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Subitem implements Serializable {
    private static final long serialVersionUID = -39085341150154469L;

    /**
     * 分项ID
     */
    @TableId(value = "SUBITEM_ID", type = IdType.AUTO)
    @ApiModelProperty("分项ID")
    private Long subitemId;

    /**
     * 项目ID
     */
    @TableField("SUBITEM_PROJECTID")
    @ApiModelProperty("项目ID")
    private Long subitemProjectid;
    /**
     * 项目信息
     */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    //    @JsonIgnore
    @EntityMapping(thisField = "subitemProjectid", joinField = "projectId")
    private Project project;
    /**
     * 分部ID
     */
    @TableField("SUBITEM_PARCELID")
    @ApiModelProperty("分部ID")
    private Long subitemParcelid;

    /**
     * 分部信息
     */

    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
//    @JsonIgnore
    @EntityMapping(thisField = "subitemParcelid", joinField = "parcelId")
    private Parcel parcel;
    /**
     * 单位项目ID
     */
    @TableField("SUBITEM_UNITENGINEID")
    @ApiModelProperty("单位项目ID")
    private Long subitemUnitengineid;

    @ApiModelProperty("单位项目名称")
    @TableField(exist = false)
    private String subitemUnitengineName;

    /**
     * 分项工程名称
     */
    @TableField("SUBITEM_NAME")
    @ApiModelProperty("分项工程名称")
    @NotNull(message = "分项工程名称不能为空")
    private String subitemName;

    /**
     * 分项内容
     */
    @TableField("SUBITEM_CONTENT")
    @ApiModelProperty("分项内容")
    private String subitemContent;

    /**
     * 分项单位
     */
    @TableField("SUBITEM_UNIT")
    @ApiModelProperty("分项单位")
//    @NotNull
    private String subitemUnit;

    /**
     * 负责人
     */
    @TableField("SUBITEM_PERSON")
    @ApiModelProperty("分包负责人")
    private String subitemPerson;

    /**
     * 联系电话
     */
    @TableField("SUBITEM_LINK")
    @ApiModelProperty("联系电话")
    private String subitemLink;
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
