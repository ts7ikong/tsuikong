package cc.mrbird.febs.gateway.common.filter;

import cc.mrbird.febs.common.core.utils.RSAUtils;

import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/24 22:48
 */
public class RSAConstant {
    /**
     * 私钥
     */
    public static final String PRIVATE_KEY;
    /**
     * 公钥
     */
    public static final String PUBLIC_KEY;

    static {
        Map<String, String> map = RSAUtils.generateRasKey();
        PUBLIC_KEY = map.get(RSAUtils.PUBLIC_KEY);
        PRIVATE_KEY = map.get(RSAUtils.PRIVATE_KEY);
        System.out.println("PUBLIC_KEY = " + PUBLIC_KEY);
        System.out.println("PRIVATE_KEY = " + PRIVATE_KEY);
    }
}
