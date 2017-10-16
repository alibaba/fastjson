package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.io.StringReader;

/**
 * Created by wenshao on 11/08/2017.
 */
public class LongTest2 extends TestCase {
    public void test_0() throws Exception {
        String json = "{\"v1\":-1883391953414482124,\"v2\":-3019416596934963650,\"v3\":6497525620823745793,\"v4\":2136224289077142499,\"v5\":-2090575024006307745}";
        String json2 = "{\"v1\":\"-1883391953414482124\",\"v2\":\"-3019416596934963650\",\"v3\":\"6497525620823745793\",\"v4\":\"2136224289077142499\",\"v5\":\"-2090575024006307745\"}";

        Model m1 = JSON.parseObject(json, Model.class);
        Model m2 = JSON.parseObject(json2, Model.class);

        assertNotNull(m1);
        assertNotNull(m2);

        assertEquals(-1883391953414482124L, m1.v1);
        assertEquals(-3019416596934963650L, m1.v2);
        assertEquals(6497525620823745793L, m1.v3);
        assertEquals(2136224289077142499L, m1.v4);
        assertEquals(-2090575024006307745L, m1.v5);

        assertEquals(-1883391953414482124L, m2.v1);
        assertEquals(-3019416596934963650L, m2.v2);
        assertEquals(6497525620823745793L, m2.v3);
        assertEquals(2136224289077142499L, m2.v4);
        assertEquals(-2090575024006307745L, m2.v5);
    }

    public void test_1() throws Exception {
        String json = "{\"v1\":-1883391953414482124,\"v2\":-3019416596934963650,\"v3\":6497525620823745793,\"v4\":2136224289077142499,\"v5\":-2090575024006307745}";
        String json2 = "{\"v1\":\"-1883391953414482124\",\"v2\":\"-3019416596934963650\",\"v3\":\"6497525620823745793\",\"v4\":\"2136224289077142499\",\"v5\":\"-2090575024006307745\"}";

        Model m1 = new JSONReader(new StringReader(json)).readObject(Model.class);
        Model m2 = new JSONReader(new StringReader(json2)).readObject(Model.class);

        assertNotNull(m1);
        assertNotNull(m2);

        assertEquals(-1883391953414482124L, m1.v1);
        assertEquals(-3019416596934963650L, m1.v2);
        assertEquals(6497525620823745793L, m1.v3);
        assertEquals(2136224289077142499L, m1.v4);
        assertEquals(-2090575024006307745L, m1.v5);

        assertEquals(-1883391953414482124L, m2.v1);
        assertEquals(-3019416596934963650L, m2.v2);
        assertEquals(6497525620823745793L, m2.v3);
        assertEquals(2136224289077142499L, m2.v4);
        assertEquals(-2090575024006307745L, m2.v5);
    }

    public void test_2() throws Exception {
        String json = "[-1883391953414482124,-3019416596934963650,6497525620823745793,2136224289077142499,-2090575024006307745]";
        String json2 = "[\"-1883391953414482124\",\"-3019416596934963650\",\"6497525620823745793\",\"2136224289077142499\",\"-2090575024006307745\"]";

        Model m1 = new JSONReader(new StringReader(json), Feature.SupportArrayToBean).readObject(Model.class);
        Model m2 = new JSONReader(new StringReader(json2), Feature.SupportArrayToBean).readObject(Model.class);

        assertNotNull(m1);
        assertNotNull(m2);

        assertEquals(-1883391953414482124L, m1.v1);
        assertEquals(-3019416596934963650L, m1.v2);
        assertEquals(6497525620823745793L, m1.v3);
        assertEquals(2136224289077142499L, m1.v4);
        assertEquals(-2090575024006307745L, m1.v5);

        assertEquals(-1883391953414482124L, m2.v1);
        assertEquals(-3019416596934963650L, m2.v2);
        assertEquals(6497525620823745793L, m2.v3);
        assertEquals(2136224289077142499L, m2.v4);
        assertEquals(-2090575024006307745L, m2.v5);
    }

    public static class Model {
        public long v1;
        public long v2;
        public long v3;
        public long v4;
        public long v5;
    }
}
