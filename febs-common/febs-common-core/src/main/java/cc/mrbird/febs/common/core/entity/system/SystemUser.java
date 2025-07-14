package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Data
@TableName("t_user")
@Excel("用户信息表")
@ApiModel(value = "系统管理员用户对象")
public class SystemUser implements Serializable {

    /**
     * 用户状态：有效
     */
    public static final Integer STATUS_VALID = 1;
    /**
     * 用户状态：锁定
     */
    public static final Integer STATUS_LOCK = 0;
    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "default.jpg";
    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";
    /**
     * 性别男
     */
    public static final Integer SEX_MALE = 0;
    /**
     * 性别女
     */
    public static final Integer SEX_FEMALE = 1;
    /**
     * 性别保密
     */
    public static final Integer SEX_UNKNOW = 2;
    public static final Integer IS_PARTY_MEMBER = 1;
    public static final Integer IS_NO_PARTY_MEMBER = 0;
    private static final long serialVersionUID = -4352868070794165001L;
    /**
     * 用户 ID
     */
    @TableId(value = "USER_ID", type = IdType.AUTO)
    @ApiModelProperty("用户 ID")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    @ExcelField(value = "用户名")
    @ApiModelProperty("用户名")
    private String username;

    @TableField("REALNAME")
    @ApiModelProperty("真实姓名")
    private String realname;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    @JsonIgnore
    private String password;


    /**
     * 邮箱
     */
    @TableField("EMAIL")
    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    @ExcelField(value = "邮箱")
    @ApiModelProperty("邮箱")
    private String email;


    /**
     * 联系人
     */
    @TableField("MAN")
    @ApiModelProperty("联系人")
    private String man;


    /**
     * 联系电话
     */
    @TableField("MOBILE")
//    @IsMobile(message = "{mobile}")
    @ExcelField(value = "联系电话")
    @ApiModelProperty("联系电话")
    private String mobile;

    /**
     * 状态 0锁定 1有效 2删除
     */
    @TableField("STATUS")
    @ExcelField(value = "状态", writeConverterExp = "0=锁定,1=有效,2=删除")
    @ApiModelProperty("状态 0锁定 1有效 2删除")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private String modifyTime;

    /**
     * 最近访问时间
     */
    @TableField("LAST_LOGIN_TIME")
    @ExcelField(value = "最近访问时间", writeConverter = TimeConverter.class)
    private String lastLoginTime;

    /**
     * 性别 0男 1女 2 保密
     */
    @TableField("SSEX")
    @ExcelField(value = "性别", writeConverterExp = "0=男,1=女,2=保密")
    @ApiModelProperty("性别 0男 1女 2 保密")
    private Integer sex;

    /**
     * 头像
     */
    @TableField("AVATAR")
    @ApiModelProperty("TODO-头像")
    private String avatar;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "个人描述")
    @ApiModelProperty("个人描述")
    private String description;

    @TableField(exist = false)
    private String createTimeFrom;

    @TableField(exist = false)
    private String createTimeTo;
    /**
     * 角色 ID
     */
    @TableField(exist = false)
    @ApiModelProperty("角色ID")
    private String roleId;

    /**
     * 项目IDs
     */
    @TableField(exist = false)
    @ApiModelProperty("项目IDs")
    private String projectIds;

    /**
     * 角色 LIST
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "角色List", hidden = true)
    private List<UserRole> roleList = new ArrayList<>();

    @TableField(exist = false)
    @ApiModelProperty("角色")
    private String roleName;


    /**
     * 0公司1项目
     */
    @TableField("TYPE")
    @ApiModelProperty("0公司1项目")
    private Integer type;


    @TableField("LEADERSHIP_TEAM")
    @ApiModelProperty("是否领导班子 1是 0 不是")
    private Integer leadershipTeam;
    @TableField("PARTY_MEMBER")
    @ApiModelProperty("是否党员 1是 0 不是")
    private Integer partyMember;

    /**
     * 上级ID
     */
    @TableField("SUP_ID")
    @ApiModelProperty("上级ID")
    private Long supId;

    /**
     * 部门ID
     */
    @TableField("DEPT_ID")
    @ApiModelProperty("部门ID")
    private Long deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 地址
     */
    @TableField("ADDR")
    @ApiModelProperty("地址")
    private String addr;

    /**
     * 1超级管理员2公司员工等级3公司管理用户 等级
     */
    @TableField("LEVEL")
    @ApiModelProperty("等级 1超级管理员 2员工 3临时")
    private Integer level;
    @TableField("POST")
    @ApiModelProperty("职位")
    private String post;

    @TableField(exist = false)
    @ApiModelProperty("输入框搜索")
    private String search;

    @TableField(exist = false)
    @ApiModelProperty("旧密码")
    private String oldPassword;

}
