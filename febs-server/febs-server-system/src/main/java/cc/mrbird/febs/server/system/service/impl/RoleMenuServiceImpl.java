package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.system.RoleMenu;
import cc.mrbird.febs.common.core.entity.system.TRoleButton;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.system.mapper.RoleMenuMapper;
import cc.mrbird.febs.server.system.mapper.TRoleButtonMapper;
import cc.mrbird.febs.server.system.service.IRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author MrBird
 */
@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {
    @Autowired
    private TRoleButtonMapper tRoleButtonMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenusByRoleId(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        LambdaUpdateWrapper<RoleMenu> objectLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        objectLambdaUpdateWrapper.in(RoleMenu::getRoleId, list);
        baseMapper.delete(objectLambdaUpdateWrapper);
        LambdaUpdateWrapper<TRoleButton> tRoleButtonLambdaQueryWrapper = new LambdaUpdateWrapper<>();
        tRoleButtonLambdaQueryWrapper.in(TRoleButton::getRoleId, list);
        tRoleButtonMapper.delete(tRoleButtonLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenusByMenuId(String[] menuIds) {
        List<String> list = Arrays.asList(menuIds);
        baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getMenuId, list));
    }

    @Override
    public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
        return baseMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
    }

    @Override
    public List<RoleMenu> listById(Long roleId) {
        return baseMapper.listById(roleId);
    }

}
