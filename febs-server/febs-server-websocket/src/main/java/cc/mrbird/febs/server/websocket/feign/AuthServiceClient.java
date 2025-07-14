package cc.mrbird.febs.server.websocket.feign;

import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "febs-auth-service")
public interface AuthServiceClient {

    @GetMapping("/user")
    Map<String, Object> getUser(@RequestHeader(ParamsConstant.LOGIN_TYPE) String type);
}