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

@Data
@TableName("p_usersysnotify")
@ApiModel("用户系统通知阅读表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class UserSysnotify implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("用户项目ID")
    private Long id;

    /**
     * 项目ID
     */

    @TableField("USER_ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    @TableField("PROJECT_ID")
    @ApiModelProperty("项目ID")
    private Long projectId;

    @TableField("SYSNOTIFY_ID")
    @ApiModelProperty("系统通知id")
    private Long sysnotifyId;

    @TableField("IS_READ")
    @ApiModelProperty("是否已读 是1，否0")
    private Integer isRead = null;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

}
