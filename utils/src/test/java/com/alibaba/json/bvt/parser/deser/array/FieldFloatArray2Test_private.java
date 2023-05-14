package com.alibaba.json.bvt.parser.deser.array;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 11/01/2017.
 */
public class FieldFloatArray2Test_private extends TestCase {
    public void test_intArray() throws Exception {
        Model model = JSON.parseObject("{\"value\":[[1,2.1,-0.3]]}", Model.class);
        assertNotNull(model.value);
        assertEquals(1, model.value.length);
        assertEquals(3, model.value[0].length);
        assertEquals(1f, model.value[0][0]);
        assertEquals(2.1f, model.value[0][1]);
        assertEquals(-0.3f, model.value[0][2]);
    }

    private static class Model {
        public float[][] value;
    }
}
