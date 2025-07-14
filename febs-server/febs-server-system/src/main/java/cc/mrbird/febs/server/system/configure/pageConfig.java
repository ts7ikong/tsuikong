package cc.mrbird.febs.server.system.configure;

import cc.mrbird.febs.common.datasource.starter.configure.MyPaginationInnerInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: corgi
 * @since: 2021/2/1
 */
@Configuration
public class pageConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new MyPaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
