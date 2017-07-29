package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 2016/10/11.
 */
public class TypeReferenceTest11 extends TestCase {
    public void test_same() throws Exception {
        Type type1 = getType(Integer.class);
        Type type2 = getType(Integer.class);

        assertEquals(type1, type2);
        assertSame(type1, type2);
    }

    public static <T> Response<T> parseRepsonse(String json, Type type) {
        return JSON.parseObject(json, new TypeReference<Response<T>>() {});
    }

    <T> Type getType(Type type) {
        return new TypeReference<Response<T>>(type) {}.getType();
    }

    public static class Model<T> {
        public T value;
    }

    public static class Response<T> {
        public T data;
    }


}
