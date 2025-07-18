package cc.mrbird.febs.server.tjdkxm.runner;

import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.util.Holidays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @author MrBird
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        LocalDate now = LocalDate.now();
        Holidays.isHolidays(now.toString());
        if (context.isActive()) {
            FebsUtil.printSystemUpBanner(environment);
        }
    }
}
