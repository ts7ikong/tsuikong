package cc.mrbird.febs.server.system.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.util.StringUtils;

/**
 * @Classname PropertiesUtil
 * @Description
 * @Date 2019/8/19 0019 16:26
 * @Author by jtj
 */
public class PropertiesUtil {

    private static Properties prop = new Properties();

    /**
     * 获取指定属性文件的值
     * 
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("key值不能为空!");
        }
        try {
            prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("ftp.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

}
