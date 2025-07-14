package cc.mrbird.febs.common.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
public class FebsAuthUser extends User {

    private static final long serialVersionUID = -6411066541689297219L;

    private Long userId;

    private String avatar;

    private String email;

    private String man;

    private String mobile;

    private String sex;

    private String roleId;

    private String roleName;

    private Date lastLoginTime;

    private String description;

    private String status;

    private Integer type;

    private Long supId;

    private String addr;

    private Integer level;

    private String realname;
    private String loginType;
    @ApiModelProperty("是否领导班子 1是 0 不是")
    private Integer leadershipTeam;
    @ApiModelProperty("是否党员 1是 0 不是")
    private Integer partyMember;


    public FebsAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public FebsAuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
