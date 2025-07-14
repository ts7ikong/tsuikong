package cc.mrbird.febs.common.core.entity.tjdkxm;

import cc.mrbird.febs.common.core.entity.tjdkxm.model.Add;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Delete;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Update;
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

/**
 * 项目打卡区域
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 16:51
 */

@Data
@TableName("p_puncharea")
@ApiModel("项目打卡区域")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class PunchArea implements Serializable {
    /**
     * ID
     */
    @TableId(value = "PUNCHAREA_ID", type = IdType.AUTO)
    @ApiModelProperty("ID")
    @NotNull(message = "请选择区域", groups = {Update.class, Delete.class})
    private Long punchAreaId;

    /**
     * 项目ID
     */
    @TableField("PUNCHAREA_PROJECTID")
    @ApiModelProperty("项目ID")
    @Deprecated
    @JsonIgnore
    private Long punchAreaProjectid;

    /**
     * 打卡区域名称
     */
    @TableField("PUNCHAREA_NAME")
    @ApiModelProperty("打卡区域名称")
    @NotNull(message = "打卡区域名称不能为空", groups = {Add.class})
    private String punchAreaName;

    /**
     * 经度
     */
    @TableField("PUNCHAREA_LATITUDE")
    @ApiModelProperty("经度")
    @NotNull(message = "经度不能为空", groups = {Update.class})
    private String punchAreaLatitude;

    /**
     * 纬度
     */
    @TableField("PUNCHAREA_LONGITUDE")
    @ApiModelProperty("纬度")
    @NotNull(message = "纬度不能为空", groups = {Update.class})
    private String punchAreaLongitude;

    /**
     * 半径
     */
    @TableField("PUNCHAREA_REDIU")
    @ApiModelProperty("半径")
    @NotNull(message = "半径不能为空", groups = {Update.class})
    private String punchAreaRediu;
    /**
     * 用户ids
     */
    @TableField(exist = false)
    @ApiModelProperty("用户ids")
    private String userIds;
    /**
     * 用户名称s
     */
    @TableField(exist = false)
    @ApiModelProperty("用户名称s")
    private String userNames;
    /**
     * 打卡时间段
     */
    @TableField(exist = false)
    @ApiModelProperty("打卡时间段格式 String ['id':1,'startTime':'9:00','endTime':'10:00']")
    private String clockTimes;
    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
