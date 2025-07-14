package cc.mrbird.febs.server.websocket.config;

import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.common.security.starter.configure.FebsUserInfoTokenServices;
import cc.mrbird.febs.server.websocket.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 18:18
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    private void serRedisService(RedisService redisService, ResourceServerProperties properties) {
        WebSocketServer.redisService = redisService;
        WebSocketServer.userInfoUri = properties.getUserInfoUri();
        WebSocketServer.clientId = properties.getClientId();
    }
}
