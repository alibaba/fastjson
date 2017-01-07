package com.alibaba.json.bvt.parser.deser.floatDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class FloatArrayTest3 extends TestCase {
    public void test_float_array() throws Exception {
        String text = "{\"matrix\":[6.01855,77.0,-105.473,4.69116,-6.59919,-68.714,20.4158,28.0552,-63.0301,18.3684,28.9993]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
        assertEquals(Float.parseFloat("6.01855"), ((float) 601855)/100000);
        System.out.println(6.01855f == ((float) 601855)/100000);
    }

    public static class Model {
        public float[] matrix;
    }
}
