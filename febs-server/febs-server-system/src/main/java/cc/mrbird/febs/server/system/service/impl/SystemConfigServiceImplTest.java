package cc.mrbird.febs.server.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.server.system.dto.SystemConfigDto;
import cc.mrbird.febs.server.system.service.SystemConfigService;
import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/2 17:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemConfigServiceImplTest {
    @Resource
    SystemConfigService systemConfigService;

    @Test
    public void findSystemConfigs() {
        List<SystemConfig> systemConfigs =
            systemConfigService.findSystemConfigs(new SystemConfigDto().setKey("TEST_KEY"));
        systemConfigs.forEach(s -> {
            try {
                JSONObject jsonObject = JSON.parseObject(s.getContent());
                System.out.println(jsonObject);
            } catch (Exception e) {
                try {
                    List<String> jsonArray = JSON.parseArray(s.getContent(), String.class);
                    System.out.println(jsonArray);
                } catch (Exception e1) {
                    System.out.println(s.getContent());
                }
            }

        });
        System.out.println(systemConfigs);
    }

    @Test
    public void createSystemConfig() {
        SystemConfigDto systemConfigDto = new SystemConfigDto();
        systemConfigDto.setKey("TEST_KEY_2");
        JSONObject put = new JSONObject();
        put.put("test", "aa");
        put.put("name", "123456");
        JSONArray jsonArray = new JSONArray();
        HashSet<String> strings = new HashSet<>();
        strings.add("2022-06-03");
        strings.add("2022-06-04");
        strings.add("2022-06-05");
        systemConfigDto.setContent(strings.toString());
        systemConfigDto.setType(0);
        systemConfigDto.setExplain("测试");

        systemConfigService.createSystemConfig(systemConfigDto);
    }
}