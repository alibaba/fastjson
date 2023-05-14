package com.alibaba.json.bvt.typeRef;

import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;
import org.junit.Assert;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 2016/10/11.
 */
public class TypeReferenceTest10 extends TestCase {
    public void test_same() throws Exception {
        Type type1 = getType();
        Type type2 = getType();

        assertEquals(type1, type2);
        assertSame(type1, type2);
    }

    Type getType() {
        return new TypeReference<Model<Integer>>() {}.getType();
    }

    public static class Model<T> {
        public T value;
    }
}
