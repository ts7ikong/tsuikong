package cc.mrbird.febs.server.system.runner;

import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.system.dto.SystemConfigDto;
import cc.mrbird.febs.server.system.service.SystemConfigService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import cc.mrbird.febs.common.core.utils.FebsUtil;
import lombok.RequiredArgsConstructor;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;
    private final SystemConfigService systemConfigService;
    private final RedisService redisService;
    private final ThreadPoolTaskExecutor executor;

    @Override
    public void run(ApplicationArguments args) {
        // executor.execute(this::setSystemConfig);
        if (context.isActive()) {
            FebsUtil.printSystemUpBanner(environment);
        }
    }

    private void setSystemConfig() {
        List<SystemConfig> systemConfigs = systemConfigService.findSystemConfigs(new SystemConfigDto());
        Map<String, Object> map = new ConcurrentHashMap<>(8);
        Map<String, Optional<SystemConfig>> collect = systemConfigs.parallelStream().collect(
            Collectors.groupingBy(SystemConfig::getKey, Collectors.maxBy(Comparator.comparing(SystemConfig::getId))));
        collect.forEach((k, v) -> map.put(k, v.orElseGet(SystemConfig::new)));
        redisService.hmset(RedisKey.SYSTEM_CONFIG, map);
    }
}
