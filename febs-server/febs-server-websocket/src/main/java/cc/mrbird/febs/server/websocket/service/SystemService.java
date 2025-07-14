package cc.mrbird.febs.server.websocket.service;

import cc.mrbird.febs.common.core.entity.constant.ServerConstant;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/8 21:05
 */
@FeignClient(ServerConstant.SYSTEM)
public interface SystemService {
    @GetMapping("/user/auth/{userId}")
    AuthUserModel userAuth(@PathVariable("userId") Long userId);
}
