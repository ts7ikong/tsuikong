package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/28 16:33
 */
@Data
@TableName("p_version")
@ApiModel("版本信息")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Version {
    /**
     * 状态 启用
     */
    public static final Integer STATE_ONE = 1;
    /**
     * 状态 弃用
     */
    public static final Integer STATE_ZERO = 0;

    /**
     * 版本信息ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("版本信息ID")
    private Long id;
    /**
     * 版本
     */
    @TableField("VERSION")
    @ApiModelProperty("版本号")
    private String version;
    /**
     * 安卓下载路径
     */
    @TableField("ANDROID_PATH")
    @ApiModelProperty("安卓下载路径")
    private String androidPath;
    /**
     * ios下载路径
     */
    @TableField("IOS_PATH")
    @ApiModelProperty("ios下载路径")
    private String iosPath;
    /**
     * 状态 0 弃用 1启用
     */
    @TableField("STATE")
    @ApiModelProperty("状态 0 弃用 1 启用")
    private Integer state;
    /**
     * 是否强制更新 1强制 0不强制
     */
    @TableField("IS_FORCE")
    @ApiModelProperty("是否强制更新 1强制 0不强制")
    private Integer isForce;
    /**
     * 类型 android 1 ios 2 暂时未用
     */
    @TableField("TYPE")
    @ApiModelProperty("类型 android 1 ios 2 暂时未用")
    private Integer type;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JsonIgnore
    private Data createTime;
    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    @ApiModelProperty("修改时间")
    @JsonIgnore
    private Data modifyTime;
    /**
     * 删除 1；0未删
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("删除 1；0未删")
    @JsonIgnore
    private Integer isDelete;

}
