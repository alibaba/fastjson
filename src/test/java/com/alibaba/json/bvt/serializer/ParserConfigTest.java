package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.parser.ParserConfig;

import junit.framework.TestCase;

public class ParserConfigTest extends TestCase {

    public void test_0() throws Exception {
        new ParserConfig();
    }

//    public void test_error_0() throws Exception {
//        ParserConfig config = new ParserConfig();
//        
//        Exception error = null;
//        try {
//            config.createJavaBeanDeserializer(int.class, int.class);
//        } catch (JSONException ex) {
//            error = ex;
//        }
//        Assert.assertNotNull(error);
//    }
}
