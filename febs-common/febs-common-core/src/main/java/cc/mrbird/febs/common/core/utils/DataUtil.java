package cc.mrbird.febs.common.core.utils;

import java.util.Random;
import java.util.UUID;

/**
 * 数据工具类
 */
public class DataUtil {

    //如果字符种类不够，可以自己再添加一些
    private static String range = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static synchronized String getRandomString(int num) {

        Random random = new Random();

        StringBuffer result = new StringBuffer();
        //要生成几位，就把这里的数字改成几
        for (int i = 0; i < num; i++) {

            result.append(range.charAt(random.nextInt(range.length())));

        }

        return result.toString();
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }

}
