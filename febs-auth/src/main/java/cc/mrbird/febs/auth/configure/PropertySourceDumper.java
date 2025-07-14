package cc.mrbird.febs.auth.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractPropertyResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class PropertySourceDumper {

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void dumpPropertySources() {
        System.out.println("===== Dumping all property sources =====");

        environment.getPropertySources().forEach(ps -> {
            if (ps.getSource() instanceof Map) {
                System.out.println("PropertySource: " + ps.getName());
                ((Map<?, ?>) ps.getSource()).forEach((k, v) -> {
                    if (k.toString().contains("port") || k.toString().contains("info")) {
                        System.out.printf("  %s = %s%n", k, v);
                    }
                });
            }
        });

        System.out.println("=======================================");
    }
}