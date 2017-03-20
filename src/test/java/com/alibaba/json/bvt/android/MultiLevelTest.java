package com.alibaba.json.bvt.android;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 24/02/2017.
 */
public class MultiLevelTest extends TestCase {

    public static class A {

    }

    public static class B extends A {

    }

    public static class C extends B {

    }

    public static class D extends C {

    }

    public static class E extends D {

    }


    public static class F extends E {

    }

    public void test_multi_level() throws Exception {
        F f = new F();

        String text = JSON.toJSONString(f);

        for (int i = 0; i < 10; ++i) {
            long startMillis = System.currentTimeMillis();
            f(text);
            long millis = System.currentTimeMillis() - startMillis;
            System.out.println("millis : " + millis);
        }
    }

    private void f(String text) {
        for (int i = 0; i < 1000 * 1000; ++i) {
            JSON.parseObject(text, F.class);
        }
    }
}
