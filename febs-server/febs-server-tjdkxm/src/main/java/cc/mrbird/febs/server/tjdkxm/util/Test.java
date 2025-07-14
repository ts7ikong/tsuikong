package cc.mrbird.febs.server.tjdkxm.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/6 16:09
 */
class A {
    static {
        System.out.print("1");
    }

    public A() {
        System.out.print("2");
    }
}

class B extends A {
    static {
        System.out.print("a");
    }

    public B() {
        System.out.print("b");
    }
}

public class Test {
    public static void main(String[] args) {
        A ab = new B();
        ab = new B();
    }
}