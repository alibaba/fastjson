package com.alibaba.json.bvt.bug;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import junit.framework.TestCase;

public class Bug_for_issue_297 extends TestCase {

    public void test_for_issue() throws Exception {
        Response<User> resp = parse("{\"id\":1001,\"values\":[{}]}", User.class);
        Assert.assertEquals(1001, resp.id);
        Assert.assertEquals(1, resp.values.size());
        Assert.assertEquals(User.class, resp.values.get(0).getClass());
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
