package com.alibaba.json.bvt.parser.deser.floatDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class FloatArrayTest2 extends TestCase {
    public void test_float_array() throws Exception {
        String text = "{\"matrix\":[0.999969,5.8E-4,6.41E-4,0.999969,6.1E-5,-6.1E-5]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
    }

    public static class Model {
        public float[] matrix;
    }
}
