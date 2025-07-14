package cc.mrbird.febs.gateway.enhance.auth;

import cc.mrbird.febs.common.core.entity.constant.FebsConstant;
import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("暂不支持");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        HttpHeaders headers = request.getHeaders();
        List<String> strings = headers.get(ParamsConstant.LOGIN_TYPE);
        // String loginType = request.getHeaders().get(ParamsConstant.LOGIN_TYPE);
        if (StringUtils.isNotBlank(authHeader) && StringUtils.startsWith(authHeader, FebsConstant.OAUTH2_TOKEN_TYPE)) {
            String authToken = StringUtils.substringAfter(authHeader, FebsConstant.OAUTH2_TOKEN_TYPE).trim();
            String s = "";
            if (strings != null && !strings.isEmpty()) {
                s = strings.get(0);
                if (Boolean.TRUE.equals(redisService.hasKey(authToken))) {
                    Object o1 = redisService.get(authToken);
                    if (o1.equals(s)) {
                        redisService.expire(authToken, 30 * 60L);
                    } else {
                        redisService.del(authToken);
                    }
                } else {
                    redisService.set(authToken, s, 5L);
                }
            } else {
                return Mono.empty();
            }
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
        } else {
            return Mono.empty();
        }
    }
}
