package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdk.mapper.SysnotifyMapper;
import cc.mrbird.febs.server.tjdk.mapper.UserMapper;
import cc.mrbird.febs.server.tjdk.mapper.UserSysnotifyMapper;
import cc.mrbird.febs.server.tjdk.service.UserSysnotifyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserSysnotifyServiceImpl extends ServiceImpl<UserSysnotifyMapper, UserSysnotify>
    implements UserSysnotifyService {
    private final SysnotifyMapper sysnotifyMapper;
    private final UserSysnotifyMapper userSysnotifyMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserSysnotify(Long sysnotifyId) {
        Sysnotify sysnotify = sysnotifyMapper.selectById(sysnotifyId);
        if (sysnotify != null) {
            List<UserSysnotify> userSysnotifies = new ArrayList<>();
            String contrant = sysnotify.getSysnotityContrant();
            switch (sysnotify.getSysnotityType()) {
                case Sysnotify.TYPE_ALL:
                    List<SystemUser> systemUsers = userMapper.selectList(new LambdaQueryWrapper<SystemUser>()
                        .select(SystemUser::getUserId).ne(SystemUser::getStatus, 2).gt(SystemUser::getUserId, 1));
                    systemUsers.forEach(user -> {
                        UserSysnotify userSysnotify = new UserSysnotify();
                        userSysnotify.setProjectId(null);
                        userSysnotify.setSysnotifyId(sysnotifyId);
                        userSysnotify.setUserId(user.getUserId());
                        userSysnotifies.add(userSysnotify);
                    });
                    break;
                case Sysnotify.TYPE_DEPT:
                    List<SystemUser> systemUserDepts = userMapper.selectList(new LambdaQueryWrapper<SystemUser>()
                        .select(SystemUser::getUserId).eq(SystemUser::getDeptId, contrant).ne(SystemUser::getStatus, 2)
                        .gt(SystemUser::getUserId, 1));
                    systemUserDepts.forEach(user -> {
                        UserSysnotify userSysnotify = new UserSysnotify();
                        userSysnotify.setProjectId(null);
                        userSysnotify.setSysnotifyId(sysnotifyId);
                        userSysnotify.setUserId(user.getUserId());
                        userSysnotifies.add(userSysnotify);
                    });
                    break;
                case Sysnotify.TYPE_LEADERSHIP_TEAM:
                    List<SystemUser> systemUserLeaderships = userMapper.selectList(new LambdaQueryWrapper<SystemUser>()
                        .select(SystemUser::getUserId).eq(SystemUser::getLeadershipTeam, 1).ne(SystemUser::getStatus, 2)
                        .gt(SystemUser::getUserId, 1));
                    systemUserLeaderships.forEach(user -> {
                        UserSysnotify userSysnotify = new UserSysnotify();
                        userSysnotify.setProjectId(null);
                        userSysnotify.setSysnotifyId(sysnotifyId);
                        userSysnotify.setUserId(user.getUserId());
                        userSysnotifies.add(userSysnotify);
                    });
                    break;
                case Sysnotify.TYPE_PROJECT:
                    String[] split = contrant.split(",");
                    Set<String> collect = Arrays.stream(split).collect(Collectors.toSet());
                    List<Map<String, Object>> projectUserIds = userSysnotifyMapper.getProjectUserIds(collect);
                    projectUserIds.forEach(projectUser -> {
                        UserSysnotify userSysnotify = new UserSysnotify();
                        userSysnotify.setProjectId((Long)projectUser.get("PROJECT_ID"));
                        userSysnotify.setSysnotifyId(sysnotifyId);
                        userSysnotify.setUserId((Long)projectUser.get("USER_ID"));
                        userSysnotifies.add(userSysnotify);
                    });
                    break;
                case Sysnotify.TYPE_USER:
                    String[] split1 = contrant.split(",");
                    Set<String> collect1 = Arrays.stream(split1).collect(Collectors.toSet());
                    List<SystemUser> systemUserUsers = userMapper.selectList(new LambdaQueryWrapper<SystemUser>()
                        .select(SystemUser::getUserId).in(SystemUser::getUserId, collect1).ne(SystemUser::getStatus, 3)
                        .gt(SystemUser::getUserId, 1));
                    systemUserUsers.forEach(user -> {
                        UserSysnotify userSysnotify = new UserSysnotify();
                        userSysnotify.setProjectId(null);
                        userSysnotify.setSysnotifyId(sysnotifyId);
                        userSysnotify.setUserId(user.getUserId());
                        userSysnotifies.add(userSysnotify);
                    });
                    break;
                default:
                    break;
            }

            this.saveBatch(userSysnotifies, userSysnotifies.size());
            // cacheEvictService.getUserSysnotifyNotRead();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserSysnotify(Long sysnotifyId) {
        QueryWrapper<UserSysnotify> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        queryWrapper.eq("USER_ID", userId);
        queryWrapper.eq("SYSNOTIFY_ID", sysnotifyId);
        UserSysnotify userSysnotify = new UserSysnotify();
        userSysnotify.setIsRead(1);
        this.update(userSysnotify, queryWrapper);
        // cacheEvictService.getUserSysnotifyNotRead(userId, projectId);
    }

    @Override
    public IPage<Sysnotify> findSysnotifyNotReads(QueryRequest request, Sysnotify.Params sysnotify) {
        Long userId = FebsUtil.getCurrentUserId();
        QueryWrapper<Sysnotify> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyContent())) {
            queryWrapper.like("s.SYSNOTIFY_CONTENT", sysnotify.getSysnotifyContent());
        }
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyTitle())) {
            queryWrapper.like("s.SYSNOTIFY_TITLE", sysnotify.getSysnotifyTitle());
        }
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyStartTime())) {
            queryWrapper.ge("s.SYSNOTIFY_TIME", sysnotify.getSysnotifyStartTime());
        }
        if (StringUtils.isNotBlank(sysnotify.getSysnotifyEndTime())) {
            queryWrapper.le("s.SYSNOTIFY_TIME", sysnotify.getSysnotifyEndTime());
        }
        if (sysnotify.getIsRead() != null) {
            queryWrapper.eq("us.is_read", sysnotify.getIsRead());
        }
        OrderUtils.setQuseryOrder(queryWrapper, request);
        queryWrapper.eq("us.user_id", userId);
        Page<Sysnotify> page = new Page<>(request.getPageNum(), request.getPageSize());
        queryWrapper.orderByAsc("us.is_read");
        queryWrapper.orderByDesc("s.SYSNOTIFY_TIME");
        queryWrapper.eq("us.IS_DELETE", 0);
        queryWrapper.eq("s.IS_DELETE", 0);
        return userSysnotifyMapper.getUserRead(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserSysnotify(Long sysnotifyId) {
        Long userId = FebsUtil.getCurrentUserId();
        userSysnotifyMapper.update(null, new LambdaUpdateWrapper<UserSysnotify>().eq(UserSysnotify::getUserId, userId)
            .eq(UserSysnotify::getSysnotifyId, sysnotifyId).set(UserSysnotify::getIsDelete, 1));
    }

    @Override
    public Map<String, Object> getNotRead() {
        Map<String, Object> hashMap = new HashMap<>(1);
        Integer count = userSysnotifyMapper.selectCount(new LambdaQueryWrapper<UserSysnotify>()
            .select(UserSysnotify::getUserId).eq(UserSysnotify::getUserId, FebsUtil.getCurrentUserId())
            .eq(UserSysnotify::getIsDelete, 0).eq(UserSysnotify::getIsRead, 0));
        hashMap.put("notification", count);
        return hashMap;
    }

    @Override
    public void readAllSysnotity() {
        QueryWrapper<UserSysnotify> queryWrapper = new QueryWrapper<>();
        Long userId = FebsUtil.getCurrentUserId();
        queryWrapper.eq("USER_ID", userId);
        UserSysnotify userSysnotify = new UserSysnotify();
        userSysnotify.setIsRead(1);
        this.update(userSysnotify, queryWrapper);
    }

    @Override
    public void delAllReadSysnotity() {
        Long userId = FebsUtil.getCurrentUserId();
        userSysnotifyMapper.update(null, new LambdaUpdateWrapper<UserSysnotify>().eq(UserSysnotify::getUserId, userId)
            .set(UserSysnotify::getIsDelete, 1));
    }
}
