package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 招投标文件
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 11:44
 */
@Data
@TableName(value = "p_bidd", autoResultMap = true)
@ApiModel("招投标文件")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Bidd implements Serializable {
    /**
     * 招投标文件ID
     */
    @TableId(value = "BIDD_ID", type = IdType.AUTO)
    @ApiModelProperty("招投标文件ID")
    private Long biddId;
    /**
     * 招标单位
     */
    @TableField("BIDD_UNIT")
    @ApiModelProperty("招标单位")
    private String biddUnit;
    // /**
    // * 招标项目
    // */
    // @TableField("BIDD_PROJECTID")
    // @ApiModelProperty("招标项目")
    // private Long biddProjectid;
    /**
     * 招标项目
     */
    @TableField("BIDD_PROJECTNAME")
    @ApiModelProperty("招标项目 名称")
    private String biddProjectName;
    /**
     * 招标时间
     */
    @TableField("BIDD_TIME")
    @ApiModelProperty("招标时间")
    private Date biddTime;

    /**
     * 项目地址
     */
    @TableField("BIDD_ADDR")
    @ApiModelProperty("项目地址")
    private String biddAddr;
    /**
     * 保证金
     */
    @TableField("BIDD_BOND")
    @ApiModelProperty("保证金")
    private String biddBond;
    /**
     * 是否退保证金 0否 1是
     */
    @TableField("BIDD_TUIBOND")
    @ApiModelProperty("是否退保证金 0否 1是 ")
    private Integer biddTuibond;
    /**
     * 招标文件（可上传多个）[{''name'':'',''addr'':''}]
     */
    @TableField(value = "BIDD_ANNX", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("招标文件（可上传多个）[{''name'':'',''addr'':''}]")
    private Object biddAnnx;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("创建人")
    private Long createUserid;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JsonIgnore
    private Date createTime;
    /**
     * 是否删除
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("查询")
    public static class Params {
        /**
         * 招标单位
         */
        @TableField("BIDD_UNIT")
        @ApiModelProperty("招标单位")
        private String biddUnit;
        /**
         * 招标项目
         */
        @TableField("BIDD_PROJECTNAME")
        @ApiModelProperty("招标项目 名称")
        private String biddProjectName;
        /**
         * 招标时间
         */
        @TableField(exist = false)
        @ApiModelProperty("招标时间 查询 start")
        private String biddStartTime;
        /**
         * 招标时间
         */
        @TableField(exist = false)
        @ApiModelProperty("招标时间 查询 end")
        private String biddEndTime;
        /**
         * 是否退保证金 0否 1是
         */
        @TableField("BIDD_TUIBOND")
        @ApiModelProperty("是否退保证金 0否 1是 ")
        private Integer biddTuibond;
    }
}
