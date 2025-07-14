package cc.mrbird.febs.common.core.utils;


import cc.mrbird.febs.common.core.entity.constant.RSAConstant;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSAUtils {

    public static final String PUBLIC_KEY = "public_key";

    public static final String PRIVATE_KEY = "private_key";


    public static Map<String, String> generateRasKey() {
        Map<String, String> rs = new HashMap<>();
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = null;
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥 公钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
            // 将公钥和私钥保存到Map
            rs.put(PUBLIC_KEY, publicKeyString);
            rs.put(PRIVATE_KEY, privateKeyString);
        } catch (Exception e) {
            log.error("RsaUtils invoke genKeyPair failed.", e);
            throw new IllegalArgumentException("RsaUtils invoke genKeyPair failed.");
        }
        return rs;
    }

    /**
     * 加密
     *
     * @param str 字符串
     * @return {@link String}
     */
    public static String encrypt(String str) {
        return encrypt(str, RSAConstant.PUBLIC_KEY);
    }
    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
	private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 加密
     *
     * @param str       字符串
     * @param publicKey 公钥
     * @return {@link String}
     */
    public static String encrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//            return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
            // 分段加密
            byte[] data = str.getBytes(StandardCharsets.UTF_8);
            // 加密时超过117字节就报错。为此采用分段加密的办法来加密
            byte[] enBytes = null;
            for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
                // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
                enBytes = ArrayUtils.addAll(enBytes, doFinal);
            }
            return  Base64.encodeBase64String(enBytes);
        } catch (Exception e) {
            log.error("RsaUtils invoke encrypt failed.", e);
            throw new IllegalArgumentException("RsaUtils invoke encrypt failed.");
        }
    }

    /**
     * 解密
     *
     * @param str 加密后的值
     * @return {@link java.lang.String}
     */
    public static String decrypt(String str) {
        return decrypt(str, RSAConstant.PRIVATE_KEY);
    }

    /**
     * 解密
     *
     * @param str        加密后的值
     * @param privateKey 私钥
     * @return {@link java.lang.String}
     */
    public static String decrypt(String str, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
//            return new String(cipher.doFinal(inputByte));
            // 分段解密
            // 64位解码加密后的字符串
            // 解密时超过字节报错。为此采用分段解密的办法来解密
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inputByte.length; i += MAX_DECRYPT_BLOCK) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(inputByte, i, i + MAX_DECRYPT_BLOCK));
                sb.append(new String(doFinal));
            }
            return sb.toString();

        } catch (Exception e) {
            log.error("RsaUtils invoke decrypt failed.", e);
            throw new IllegalArgumentException("RsaUtils invoke decrypt failed.");
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = RSAUtils.generateRasKey();
        System.out.println("随机生成的公钥为:" + map.get(RSAUtils.PUBLIC_KEY));
        System.out.println("随机生成的私钥为:" + map.get(RSAUtils.PRIVATE_KEY));
        JSONObject jsonObject = JSONObject.parseObject("{\"code\":\"7486\",\"grant_type\":\"password\",\"key\":\"123456\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"sign\":\"NzNxtQSZuVUTm4nyme8N4r0+/kFCIssZytWgttk455CIiQoRq6DF7I0CCK7h/MpRRGbOVNXymmWcEiy4YMbDt4N2gSEgkGIsyBU3foLOxaTdRitMXDCeJ1LiBeiKU2AnNdQMz8H40Qxtj3B35+Is3MHL/u1TKz2FfrFJSjMicV+Cs5uegPZF2uwP/QKlrRj2z2+l92lRV6Ck/ObxF4sZ6jcprpDIw6bzqn6xv4T286G+YCYkQDrgE+cevMv/eqdZB/2k+UJqMf9XO2FU+/uPZYlFB4RR+6iGHXrJ++IQm199WNvDoLeFHEiKm9GTJBrJ9auyc5/kxxsoMC6FHH7eKQ==\",\"username\":\"zoro\"}");
        String str = jsonObject.toJSONString();
        String encrypt = RSAUtils.encrypt(str, map.get(RSAUtils.PUBLIC_KEY));
        System.out.println(encrypt);
        String decrypt = RSAUtils.decrypt(encrypt, map.get(RSAUtils.PRIVATE_KEY));
        System.out.println(decrypt);
    }
}

