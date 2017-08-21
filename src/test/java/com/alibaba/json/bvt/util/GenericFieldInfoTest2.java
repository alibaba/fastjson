package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 30/05/2017.
 */
public class GenericFieldInfoTest2 extends TestCase {
    public void test_generic() throws Exception {
        A4 a = JSON.parseObject("{\"data\":[]3}", A4.class);
        assertTrue(a.data instanceof List);
    }


    public static class A <T> {
        public T data;
    }

    public static class A1 <T> extends A<T> {

    }

    public static class A2 <T> extends A1<T> {

    }

    public static class A3 <T> extends A2<List<T>> {

    }

    public static class A4 <M> extends A3<Long> {

    }
}
