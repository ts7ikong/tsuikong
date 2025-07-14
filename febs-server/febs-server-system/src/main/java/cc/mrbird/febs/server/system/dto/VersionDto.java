package cc.mrbird.febs.server.system.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/28 17:54
 */
@Data
public class VersionDto implements Serializable {
    /**
     * 版本信息ID
     */
    @ApiModelProperty("版本信息ID")
    private Long id;
    /**
     * 版本
     */
    @ApiModelProperty("版本号")
    private String version;
    /**
     * 安卓下载路径
     */
    @ApiModelProperty("安卓下载路径")
    private String androidPath;
    /**
     * ios下载路径
     */
    @ApiModelProperty("ios下载路径")
    private String iosPath;
    /**
     * 状态 0 弃用 1启用
     */
    @ApiModelProperty("状态 0 弃用 1 启用")
    private Integer state;
    /**
     * 是否强制更新 1强制 0不强制
     */
    @ApiModelProperty("是否强制更新 1强制 0不强制")
    private Integer isForce;
    /**
     * 类型 android 1 ios 2 暂时未用
     */
    @ApiModelProperty("类型 android 1 ios 2 暂时未用")
    private Integer type;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Data
    public static class VersionDtoAdd {
        /**
         * 版本
         */
        @ApiModelProperty("版本号")
        private String version;
        /**
         * 安卓下载路径
         */
        @ApiModelProperty("安卓下载路径")
        private String androidPath;
        /**
         * ios下载路径
         */
        @ApiModelProperty("ios下载路径")
        private String iosPath;
        /**
         * 状态 0 弃用 1启用
         */
        @ApiModelProperty("状态 0 弃用 1 启用")
        private Integer state;
        /**
         * 是否强制更新 1强制 0不强制
         */
        @ApiModelProperty("是否强制更新 1强制 0不强制")
        private Integer isForce;
    }
}
