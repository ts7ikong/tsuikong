package cc.mrbird.febs.server.tjdk.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/23 17:57
 */
public interface UserSysnotifyService extends IService<UserSysnotify> {
    void createUserSysnotify(Long sysnotifyId);

    void updateUserSysnotify(Long sysnotifyId);

    IPage<Sysnotify> findSysnotifyNotReads(QueryRequest queryRequest, Sysnotify.Params sysnotify);

    void deleteUserSysnotify(Long sysnotifyId);

    Map<String, Object> getNotRead();

    void readAllSysnotity();

    void delAllReadSysnotity();
}
