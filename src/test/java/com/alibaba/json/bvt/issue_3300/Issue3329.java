package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author ：Nanqi
 * @Date ：Created in 19:40 2020/7/6
 */
public class Issue3329 extends TestCase {
    public void test_for_issue() throws Exception {
        Integer count = 0;
        while (count < 100 * 10000) {
            parse("{\"id\":1001,\"values\":[{}]}", User.class);
            count++;
        }
        System.out.println("No OOM");
    }

    public <T> Response<T> parse(String text, Class<T> clazz) {
        ParameterizedTypeImpl type = new ParameterizedTypeImpl(new Type[] { User.class }, null, Response.class);
        return JSON.parseObject(text, type);
    }

    public static class Response<T> {

        public long    id;
        public List<T> values;
    }

    public static class User {

    }
}
