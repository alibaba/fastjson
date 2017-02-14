package com.alibaba.json.bvt.typeRef;

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
