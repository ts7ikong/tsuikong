package cc.mrbird.febs.common.redis.configure;

import cc.mrbird.febs.common.redis.properties.FebsLettuceRedisProperties;
import cc.mrbird.febs.common.redis.service.RedisService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Lettuce Redis配置
 *
 * @author MrBird
 */
@EnableConfigurationProperties(FebsLettuceRedisProperties.class)
@ConditionalOnProperty(value = "febs.lettuce.redis.enable", havingValue = "true", matchIfMissing = true)
@EnableCaching
public class FebsLettuceRedisAutoConfigure extends CachingConfigurerSupport {

    @Bean(name = "redisTemplate")
    @ConditionalOnClass(RedisOperations.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @ConditionalOnBean(name = "redisTemplate")
    public RedisService redisService() {
        return new RedisService();
    }

    // 自定义key生成器
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName()); // 类目
            sb.append(method.getName()); // 方法名
            for (Object param : params) {
                sb.append(param.toString()); // 参数名
            }
            return sb.toString();
        };
    }

    // 配置缓存管理器
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        return RedisCacheManager.RedisCacheManagerBuilder
                // Redis 连接工厂
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                .withCacheConfiguration("cache_project_user", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_project_ps", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_project_bigScreen", getCacheConfigurationWithTtl(redisTemplate, 2))
                .withCacheConfiguration("cache_app_project", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_user_project", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_user_project_project", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_user_project_agency", getCacheConfigurationWithTtl(redisTemplate, 1))
                .withCacheConfiguration("cache_user_parcel_unit", getCacheConfigurationWithTtl(redisTemplate, 60))
                .withCacheConfiguration("cache_user_fun_project", getCacheConfigurationWithTtl(redisTemplate, 24 * 60))
                .withCacheConfiguration("cache_project_user_auth", getCacheConfigurationWithTtl(redisTemplate,  60))
                // 配置同步修改或删除 put/evict
                .transactionAware()
                .build();
    }

    private RedisCacheConfiguration getCacheConfigurationWithTtl(RedisTemplate<String, Object> template, long minute) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                // 设置key为String
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getStringSerializer()))
                // 设置value 为自动转Json的Object
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getValueSerializer()))
                // 不缓存null
                .disableCachingNullValues()
                // 缓存数据保存分钟
                .entryTtl(Duration.ofMinutes(minute));
    }

}
