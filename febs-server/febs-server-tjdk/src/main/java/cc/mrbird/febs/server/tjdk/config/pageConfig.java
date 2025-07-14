package cc.mrbird.febs.server.tjdk.config;

import cc.mrbird.febs.common.datasource.starter.configure.MyPaginationInnerInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
