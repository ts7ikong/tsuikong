package cc.mrbird.febs.common.datasource.starter.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;

import cc.mrbird.febs.common.datasource.starter.inteceptor.DataPermissionInterceptor;

/**
 * @author MrBird
 */
@Configuration
public class FebsDataSourceAutoConfigure {

    /**
     * 注册数据权限
     */
    @Bean
    @Order(-1)
    public DataPermissionInterceptor dataPermissionInterceptor() {
        return new DataPermissionInterceptor();
    }
    // /**
    // * 注册分页插件
    // */
    // @Order(-2)
    // @Bean
    // public MybatisPlusInterceptor mybatisPlusInterceptor() {
    // MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    // interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    // return interceptor;
    // }

    @Order(-2)
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
