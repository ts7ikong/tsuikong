package cc.mrbird.febs.server.system;

import cc.mrbird.febs.common.security.starter.annotation.EnableFebsCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication
@EnableFebsCloudResourceServer
@EnableTransactionManagement
@MapperScan("cc.mrbird.febs.server.system.mapper")
@EnableFeignClients
public class FebsServerSystemApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FebsServerSystemApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

