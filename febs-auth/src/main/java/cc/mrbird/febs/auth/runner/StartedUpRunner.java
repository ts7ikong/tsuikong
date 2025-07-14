package cc.mrbird.febs.auth.runner;

import cc.mrbird.febs.auth.mapper.MenuMapper;
import cc.mrbird.febs.common.core.entity.constant.MenuConstant;
import cc.mrbird.febs.common.core.entity.constant.RSAConstant;
import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.redis.service.RedisService;
import com.sun.glass.ui.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;
    private final RedisService redisService;
    private final MenuMapper menuMapper;

    @Override
    public void run(ApplicationArguments args) {
        menuIdsByType();
        String publicKey = RSAConstant.PUBLIC_KEY;
        if (context.isActive()) {
            FebsUtil.printSystemUpBanner(environment);
        }
    }

    /**
     * 公共菜单 和 项目菜单
     */
    private void menuIdsByType() {
        String key = RedisKey.MENU_BUTTON_TYPE;
        List<Map<String, String>> typeMenuIds = menuMapper.getTypeMenuIds();
        String menuIds0 = typeMenuIds.get(0).get("menuIds");
        String menuIds1 = typeMenuIds.get(1).get("menuIds");
        Map<String, Object> map = new HashMap<>(2);
        map.put(MenuConstant.PUBLIC_MENU, Arrays.stream(menuIds0.split(",")).collect(Collectors.toList()));
        map.put(MenuConstant.PROJECT_MENU, Arrays.stream(menuIds1.split(",")).collect(Collectors.toList()));
        redisService.hmset(key, map);
    }
}
