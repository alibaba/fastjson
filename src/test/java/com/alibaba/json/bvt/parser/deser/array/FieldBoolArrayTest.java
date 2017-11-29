package com.alibaba.json.bvt.parser.deser.array;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 11/01/2017.
 */
public class FieldBoolArrayTest extends TestCase {
    public void test_intArray() throws Exception {
        Model model = JSON.parseObject("{\"value\":[1,null,true,false,0]}", Model.class);
        assertNotNull(model.value);
        assertEquals(5, model.value.length);
        assertEquals(true, model.value[0]);
        assertEquals(false, model.value[1]);
        assertEquals(true, model.value[2]);
        assertEquals(false, model.value[3]);
        assertEquals(false, model.value[4]);
    }

    public static class Model {
        public boolean[] value;
    }
}
