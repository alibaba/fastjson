package com.alibaba.json.bvt;


import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * 在小米4/c, android-4.4.4；华为P6 android-4.2.2, 会出现反序列化不稳定的问题
 */
public class DupFileNameTest extends TestCase {

    public static class A extends B {
        public String extra;
    }

    public static class B {
        public String extra;

        public String getB() {
            return extra;
        }
    }

    public static class C extends B {
        public String extra;
    }

    public void test() {

        String text = "{extra:\"s\"}";

        A a = JSON.parseObject(text, A.class);
        C c = JSON.parseObject(text, C.class);

        Assert.assertEquals(a.getB(), c.getB());
    }

}
