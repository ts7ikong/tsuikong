package cc.mrbird.febs.server.websocket;

import cc.mrbird.febs.common.security.starter.annotation.EnableFebsCloudResourceServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.PostConstruct;
import java.io.File;

@EnableAsync
@SpringBootApplication
@EnableFebsCloudResourceServer
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.server.websocket.mapper")
@EnableFeignClients
@EnableDiscoveryClient
@EnableWebSocket
@Slf4j
public class FebsServerWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerWebsocketApplication.class, args);
    }

    @PostConstruct
    public void test() {
        // 获取jar路径
        ApplicationHome applicationHome = new ApplicationHome(getClass());
        // ==applicationHome.getSource().getParentFile()
        File dir = applicationHome.getDir();
        if (dir == null) {
            dir = applicationHome.getSource();
        }
        log.info(dir.toString());
        log.info(dir.getParent());
    }
}
