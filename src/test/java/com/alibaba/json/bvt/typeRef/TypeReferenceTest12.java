package com.alibaba.json.bvt.typeRef;

import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wuwen on 2016/12/7.
 */
public class TypeReferenceTest12 extends TestCase {

    public void test_same() throws Exception {
        ParameterizedType type1 = getType(Integer.class);
        ParameterizedType type2 = getType();

        assertEquals(type1.getRawType(), type2.getRawType());
        assertSame(type1.getRawType(), type2.getRawType());
    }

    <T> ParameterizedType getType(Type type) {
        return (ParameterizedType)new TypeReference<Model<T>>(type) {}.getType();
    }

    ParameterizedType getType() {
        return (ParameterizedType)new TypeReference<Model<Integer>>() {}.getType();
    }

    public static class Model<T> {
        public T value;
    }
}
