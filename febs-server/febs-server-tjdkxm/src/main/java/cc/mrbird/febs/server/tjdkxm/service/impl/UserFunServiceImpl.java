package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.tjdkxm.UserFun;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.UserFunMapper;
import cc.mrbird.febs.server.tjdkxm.service.UserFunService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 20:35
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserFunServiceImpl extends ServiceImpl<UserFunMapper, UserFun> implements UserFunService {
    private final UserFunMapper userFunMapper;

    @Override
    public UserFun findUserFun() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserFun crateUserFun(UserFun userFun) throws FebsException {
        FebsUtil.isProjectNotNull(userFun.getProjectId());
        userFun.setFeatures(JSONObject.parseObject(userFun.getFeaturesString()));
        Long userId = FebsUtil.getCurrentUserId();
        userFun.setUserId(userId);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProject(UserFun userFun) {
        userFun.setFeatures(JSONObject.parseObject(userFun.getFeaturesString()));
        userFunMapper.update(userFun, new LambdaQueryWrapper<UserFun>()
                .eq(UserFun::getUserId, FebsUtil.getCurrentUserId())
        );
    }
}
