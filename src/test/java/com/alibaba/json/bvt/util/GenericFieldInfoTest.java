package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

/**
 * Created by wenshao on 30/05/2017.
 */
public class GenericFieldInfoTest extends TestCase {
    public void test_generic() throws Exception {
        A a = JSON.parseObject("{\"data\":3}", A4.class);
        assertTrue(a.data instanceof Long);
    }

    public void test_generic_1() throws Exception {
        A a = JSON.parseObject("{\"data\":3}", new TypeReference<A3<Long>>(){});
        assertEquals(a.data.getClass(), Long.class);
    }

    public static class A <T> {
        public T data;
    }

    public static class A1 <T> extends A<T> {

    }

    public static class A2 <T> extends A1<T> {

    }

    public static class A3 <T> extends A2<T> {

    }

    public static class A4 <T> extends A3<Long> {

    }
}
