package cc.mrbird.febs.common.core.entity.tjdkxm;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 16:28
 */
@TableName(value = "p_parcelunit",autoResultMap = true)
@ApiModel("分部单位表")
//解决 No serializer found for class
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ParcelUnit implements Serializable {
    @TableId(value = "PARCELUNIT_ID", type = IdType.AUTO)
    @ApiModelProperty("分部单位ID")
    private Long parcelUnitId;

    @TableField("PARCELUNIT_PROJECTID")
    @ApiModelProperty("项目id")
    private Long parcelUnitProjectid;

    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String parcelUnitProjectname;

    @TableField("PARCELUNIT_NAME")
    @ApiModelProperty("分部单位名称")
    @NotNull(message ="分部单位名称不能为空")
    private String parcelUnitName;

    @TableField("PARCELUNIT_ADDR")
    @ApiModelProperty("分部单位地址")
    @NotNull(message ="分部单位地址不能为空")
    private String parcelUnitAddr;

    @TableField("PARCELUNIT_PERSON")
    @ApiModelProperty("分部单位资料负责人")
    private String parcelUnitPerson;

    @ApiModelProperty("分部单位资料负责人电话")
    @TableField("PARCELUNIT_PERSONNUMBER")
    private String parcelUnitPersonnumber;

    @ApiModelProperty("分部单位附件")
    @TableField(value = "PARCELUNIT_ANNEX", typeHandler = JacksonTypeHandler.class)
    private Object parcelUnitAnnex;

    @TableField("PARCELUNIT_ANNEXTYPE")
    @ApiModelProperty("分部单位附件类型")
    private Integer parcelUnitAnnextype = null;
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
     *
     * */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;



}
