package com.alibaba.json.bvt.parser.deser.doubleDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class DoubleArrayTest5 extends TestCase {
    public void test_double_array_1() throws Exception {
        String text = "{\"faces\":[-0.551408,-0.076876]}";
        Model model = JSON.parseObject(text, Model.class);
        assertEquals(text, JSON.toJSONString(model));
        assertEquals(-0.076876d, model.faces[1]);
    }

    public static class Model {
        public double[] faces;
    }
}
