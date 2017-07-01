package com.alibaba.json.bvt;

import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 01/07/2017.
 */
public class TypeReferenceTest13 extends TestCase {
    public void test_typeRef() throws Exception {
        Type type = new TypeReference<Integer>(){}.getType();
        assertSame(Integer.class, type);
    }

    public <T> void test_typeRef_1() throws Exception {
        Type type1 = new TypeReference<T>(){}.getType();
        Type type2 = new TypeReference<T>(){}.getType();
        assertSame(type1, type2);
    }
}
