package com.alibaba.json.bvt.parser.deser.doubleDeser;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 07/01/2017.
 */
public class DoubleTest extends TestCase {
    public void test_double() throws Exception {
        String text = "{\"value\":0.999969}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_1() throws Exception {
        String text = "{\"value\":-0.999969}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_2() throws Exception {
        String text = "{\"value\":-0.999969}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_3() throws Exception {
        String text = "{\"value\":5.8E-4}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_4() throws Exception {
        String text = "{\"value\":6.41E-4}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_5() throws Exception {
        String text = "{\"value\":6.1E-5}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public void test_double_6() throws Exception {
        String text = "{\"value\":-6.1E-5}";
        assertEquals(text, JSON.toJSONString(JSON.parseObject(text, Model.class)));
    }

    public static class Model {
        public double value;
    }
}
