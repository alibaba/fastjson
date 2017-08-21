package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import junit.framework.TestCase;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kimmking on 15/06/2017.
 */
public class Issue1271 extends TestCase {
    public void test_for_issue() throws Exception {

        String json = "{\"a\":1,\"b\":2}";

        final AtomicInteger count = new AtomicInteger(0);
        ExtraProcessor extraProcessor = new ExtraProcessor() {
            public void processExtra(Object object, String key, Object value) {
                System.out.println("setter not found, class " + object.getClass().getName() + ", property " + key);
                count.incrementAndGet();
            }
        };


        A a = JSON.parseObject(json,A.class,extraProcessor);
        assertEquals(1,a.a);
        assertEquals(1, count.intValue());

        B b = JSON.parseObject(json,B.class,extraProcessor);
        assertEquals(1,b.a);
        assertEquals(2, count.intValue());

    }

    public static class A {
        public int a;
    }

    public static class B {
        private int a;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }
    }
}
