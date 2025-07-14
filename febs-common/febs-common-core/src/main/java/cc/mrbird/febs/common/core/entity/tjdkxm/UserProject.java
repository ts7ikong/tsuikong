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

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 19:36
 */

@Data
@TableName("p_userproject")
@ApiModel("用户项目表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class UserProject implements Serializable {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("用户项目ID")
    private Long id;

    /**
     * 项目ID
     */
    @TableField("PROJECT_ID")
    @ApiModelProperty("项目ID")
    private Long projectId;

    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String projectName;

    @TableField(exist = false)
    @ApiModelProperty("是否是用户项目 是1，否0")
    private Integer isUserProject;
    @TableField(exist = false)
    @ApiModelProperty("roleId 可以多个 ‘,’分割")
    private String roleIds;
    /**
     * 用户ID
     */
    @TableField("USER_ID")
    @ApiModelProperty("用户ID")
    private Long userId;
    /**
     * 默认： 0 不是 1 是
     */
    @TableField("IS_DEFAULTPROJECT")
    @ApiModelProperty("默认： 0 不是 1 是")
    private int isDefaultproject;
    /**
     * 默认： 0 不是 1 是
     */
    @TableField(exist = false)
    @ApiModelProperty("当前登录的项目 用户是否是管理员")
    private int isProject;
    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("添加")
    public static class Add {
        @TableField(exist = false)
        @ApiModelProperty("userId 可以多个 ‘,’分割")
        private String userIds;
        /**
         * 项目ID
         */
        @TableField("PROJECT_ID")
        @ApiModelProperty("项目ID")
        private Long projectId;
    }

    @Data
    @ApiModel("查询")
    public static class UserModel {
        @ApiModelProperty("用户id")
        private Long userId;
        @ApiModelProperty("username")
        private String username;
        @ApiModelProperty("姓名")
        private String realname;
        @ApiModelProperty("电话")
        private String mobile;
        @ApiModelProperty("部门id")
        private Long deptId;
        @ApiModelProperty("部门名称")
        private String deptName;
        @ApiModelProperty("项目id")
        private Long projectId;
        @ApiModelProperty("项目名称")
        private String projectName;
    }
}
