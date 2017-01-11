package com.alibaba.json.bvt.parser.deser.array;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 11/01/2017.
 */
public class FieldIntArrayTest_private extends TestCase {
    public void test_intArray() throws Exception {
        Model model = JSON.parseObject("{\"value\":[1,2,3]}", Model.class);
        assertNotNull(model.value);
        assertEquals(3, model.value.length);
        assertEquals(1, model.value[0]);
        assertEquals(2, model.value[1]);
        assertEquals(3, model.value[2]);
    }

    private static class Model {
        public int[] value;
    }
}
