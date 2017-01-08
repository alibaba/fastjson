package com.alibaba.json.bvt.parser.deser.floatDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class FloatArrayTest extends TestCase {
    public void test_float_array() throws Exception {
        String text = "{\"matrix\":[0.172129,0.723352,0.177995,0.721544,0.182355,0.730706,0.176919,0.732701,0.186608,0.720192,0.190068,0.729511,0.192888]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
    }

    public void test_float_array_empty() throws Exception {
        String text = "{\"matrix\":[]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
    }

    public void test_float_array_16() throws Exception {
        String text = "{\"matrix\":[0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09,0.1,0.11,0.12,0.13,0.14,0.15,0.16]}";
        Model model = JSON.parseObject(text, Model.class);
        String text2 = JSON.toJSONString(model);
        assertEquals(text, text2);
    }

    public static class Model {
        public float[] matrix;
    }
}
