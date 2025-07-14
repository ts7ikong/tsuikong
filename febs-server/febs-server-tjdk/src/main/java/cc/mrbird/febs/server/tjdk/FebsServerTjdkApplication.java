package cc.mrbird.febs.server.tjdk;

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
@MapperScan("cc.mrbird.febs.server.tjdk.mapper")
@EnableFeignClients
public class FebsServerTjdkApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FebsServerTjdkApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
