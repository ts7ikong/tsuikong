package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.server.tjdk.mapper.UserMapper;
import cc.mrbird.febs.server.tjdk.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {


    @Override
    public SystemUser findByName(String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }


    @Override
    public IPage<SystemUser> findUserDetailList(SystemUser user, QueryRequest request) {
        LambdaQueryWrapper<SystemUser> queryUser = Wrappers.lambdaQuery();
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        if (Strings.isNotEmpty(user.getUsername())) {
            queryUser.and(wq -> wq.like(SystemUser::getUsername, user.getUsername()).or().like(SystemUser::getMobile, user.getUsername()));
        }
        queryUser.eq(SystemUser::getType, '0').ne(SystemUser::getStatus, '2').ne(SystemUser::getUserId, "1");
        queryUser.orderByAsc(SystemUser::getUserId);
        Page<SystemUser> userPage = this.page(page, queryUser);
        //本次查询所有的用户
        List<SystemUser> userList = userPage.getRecords();
        //记录所有的用户id
        ArrayList<Object> userIds = new ArrayList<>();

        if (userList.size() > 0) {
            userList.forEach(item -> {
                userIds.add(item.getUserId());
            });
        }
        userPage.setRecords(userList);
        return userPage;
    }

    @Override
    public SystemUser findUserDetail(String username) {
        SystemUser param = new SystemUser();
        param.setUsername(username);
        List<SystemUser> users = this.baseMapper.findUserDetail(param);
        return CollectionUtils.isNotEmpty(users) ? users.get(0) : null;
    }



}
