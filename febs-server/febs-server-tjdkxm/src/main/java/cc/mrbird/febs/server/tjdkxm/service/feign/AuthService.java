package cc.mrbird.febs.server.tjdkxm.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import cc.mrbird.febs.common.core.entity.constant.ServerConstant;

@FeignClient(ServerConstant.AUTH)
public interface AuthService {
    @PostMapping("/auth/remove-token")
    String logout();
}
