package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 04/08/2017.
 */
public class FloatTest extends TestCase {
    public void test_0() throws Exception {
        String json = "{\"v1\":-0.012671709,\"v2\":0.6042485,\"v3\":0.13231707,\"v4\":0.80090785,\"v5\":0.6192943}";
        String json2 = "{\"v1\":\"-0.012671709\",\"v2\":\"0.6042485\",\"v3\":\"0.13231707\",\"v4\":\"0.80090785\",\"v5\":\"0.6192943\"}";

        Model m1 = JSON.parseObject(json, Model.class);
        Model m2 = JSON.parseObject(json2, Model.class);

        assertNotNull(m1);
        assertNotNull(m2);

        assertEquals(-0.012671709f, m1.v1);
        assertEquals(0.6042485f, m1.v2);
        assertEquals(0.13231707f, m1.v3);
        assertEquals(0.80090785f, m1.v4);
        assertEquals(0.6192943f, m1.v5);

        assertEquals(-0.012671709f, m2.v1);
        assertEquals(0.6042485f, m2.v2);
        assertEquals(0.13231707f, m2.v3);
        assertEquals(0.80090785f, m2.v4);
        assertEquals(0.6192943f, m2.v5);
    }

    public void test_array_mapping() throws Exception {
        String json = "[-0.012671709,0.6042485,0.13231707,0.80090785,0.6192943]";
        String json2 = "[\"-0.012671709\",\"0.6042485\",\"0.13231707\",\"0.80090785\",\"0.6192943\"]";

        Model m1 = JSON.parseObject(json, Model.class, Feature.SupportArrayToBean);
        Model m2 = JSON.parseObject(json2, Model.class, Feature.SupportArrayToBean);

        assertNotNull(m1);
        assertNotNull(m2);

        assertEquals(-0.012671709f, m1.v1);
        assertEquals(0.6042485f, m1.v2);
        assertEquals(0.13231707f, m1.v3);
        assertEquals(0.80090785f, m1.v4);
        assertEquals(0.6192943f, m1.v5);

        assertEquals(-0.012671709f, m2.v1);
        assertEquals(0.6042485f, m2.v2);
        assertEquals(0.13231707f, m2.v3);
        assertEquals(0.80090785f, m2.v4);
        assertEquals(0.6192943f, m2.v5);
    }

    public static class Model {
        public float v1;
        public float v2;
        public float v3;
        public float v4;
        public float v5;
    }
}
