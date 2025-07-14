package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.constant.ServerConstant;
import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ServerConstant.SYSTEM)
public interface SystemService {
    @GetMapping("/user/auth/{userId}")
    AuthUserModel userAuth(@PathVariable("userId") Long userId);

    @GetMapping("/system/config")
    List<SystemConfig> findSystemConfigs(@SpringQueryMap SystemConfig systemConfig);
}
