package cc.mrbird.febs.auth.service.impl;

import cc.mrbird.febs.auth.manager.UserManager;
import cc.mrbird.febs.auth.mapper.UserProjectMapper;
import cc.mrbird.febs.auth.mapper.UserRoleMapper;
import cc.mrbird.febs.common.core.entity.FebsAuthUser;
import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.core.entity.constant.SocialConstant;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

/**
 * @author MrBird
 */
@Service
@RequiredArgsConstructor
public class FebsUserDetailServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserManager userManager;
    private final RedisService redisService;
    private final UserProjectMapper userProjectMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest httpServletRequest = FebsUtil.getHttpServletRequest();
        SystemUser systemUser = userManager.findByName(username);
        //pc,app
        if (systemUser != null) {
//            //查询用户的按钮权限id
            String loginTypeHeader = httpServletRequest.getHeader(ParamsConstant.LOGIN_TYPE);
//            Long projectId = null;
//            String key = FebsUtil.getUserProjectRedisKey(systemUser.getUserId());
//            HashSet<Long> comOrProjects = userRoleMapper.getComOrProjects(systemUser.getUserId());
//            if (loginTypeHeader != null) {
//                //PC端
//                if (loginTypeHeader.equalsIgnoreCase(FebsConstant.PC)) {
//                    projectId = 0L;
//                    if (comOrProjects.size() > 0) {
//                        if (!comOrProjects.contains(0L)) {
//                            throw new UsernameNotFoundException("");
//                        }
//                        if (systemUser.getLevel() == 2 || systemUser.getLevel() == 1) {
//                            throw new UsernameNotFoundException("");
//                        }
//                    }
//                }
//            }
//            if (projectId == null) {
//                boolean isProjectRole = false;
//                if (comOrProjects.size() > 0) {
//                    for (Long comOrProject : comOrProjects) {
//                        if (comOrProject == 0L) {
//                            if (systemUser.getLevel() == 2 || systemUser.getLevel() == 1) {
//                                isProjectRole = true;
//                                break;
//                            }
//                        } else {
//                            isProjectRole = true;
//                            break;
//                        }
//                    }
//                }
//                if (!isProjectRole) {
//                    throw new UsernameNotFoundException("");
//                }
//                HashSet<Long> projectNoDelete = userRoleMapper.getProjectNoDelete(systemUser.getUserId());
//
//                if (projectNoDelete == null || projectNoDelete.size() == 0) {
//                    redisService.set(key, -1);
//                } else {
//                    redisService.del(key);
//                    List<UserProject> userProjects = userProjectMapper.selectList(
//                            new LambdaQueryWrapper<UserProject>()
//                                    .eq(UserProject::getUserId, systemUser.getUserId())
//                                    .eq(UserProject::getIsDelete, 0)
//                                    .orderByDesc(UserProject::getIsDefaultproject)
//                    );
//                    if (userProjects != null && userProjects.size() > 0) {
//                        projectId = userProjects.get(0).getProjectId();
//                        redisService.set(key, projectId);
//                    }
//                }
//            }
//            String permissions = userManager.findUserPermissions(systemUser.getUsername());
            boolean notLocked = false;
            if (SystemUser.STATUS_VALID.equals(systemUser.getStatus())) {
                notLocked = true;
            }
            String password = systemUser.getPassword();
            String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);
            if (StringUtils.equals(loginType, SocialConstant.SOCIAL_LOGIN)) {
                password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());
            }

            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
//            if (StringUtils.isNotBlank(permissions)) {
//                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
//            }
            FebsAuthUser authUser = new FebsAuthUser(systemUser.getUsername(), password, true, true, true, notLocked,
                    grantedAuthorities);

            BeanUtils.copyProperties(systemUser, authUser);
            authUser.setLoginType(FebsUtil.getKey(loginTypeHeader));
            return authUser;
        } else {
            throw new UsernameNotFoundException("");
        }
    }


}
