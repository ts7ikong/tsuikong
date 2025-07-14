package cc.mrbird.febs.server.system.utils;


import org.springframework.util.DigestUtils;


public class Md5Util {

    public static String getMd5(String password){
        String str=password;
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }



}
