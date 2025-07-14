package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.UserFun;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 20:19
 */
public interface UserFunService extends IService<UserFun> {
    UserFun findUserFun();

    UserFun crateUserFun(UserFun userFun) throws FebsException;


    void updateUserProject(UserFun userFun);
}
