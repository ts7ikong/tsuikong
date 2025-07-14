package cc.mrbird.febs.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author MrBird
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "系统管理员用户对象")
public class CurrentUser implements Serializable {

    private static long serialVersionUID = 761748087824726463L;

    @JsonIgnore
    private String password;
    @ApiModelProperty("用户名")
    private String username;
    private String realname;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long userId;
    private String avatar;
    private String email;
    private String man;
    private String mobile;
    private Integer sex;
    private String roleId;
    private String roleName;
    @JsonIgnore
    private Date lastLoginTime;
    private String loginType;
    private String description;
    private String status;
    private Integer type;
    private Long supId;
    private Integer level;
    @ApiModelProperty("地址")
    private String addr;
    @ApiModelProperty("是否领导班子 1是 0 不是")
    private Integer leadershipTeam;
    @ApiModelProperty("是否党员 1是 0 不是")
    private Integer partyMember;
}
