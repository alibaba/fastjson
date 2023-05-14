package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.StringWriter;

public class SerializeWriterTest_BrowserSecure_6_name_script extends TestCase {

    public void test_0() throws Exception {
        JSONObject object = new JSONObject();
        object.put("<script>alert(1);</script>", "value");
        String text = JSON.toJSONString(object, SerializerFeature.BrowserSecure);
//        assertEquals("{\"value\":\"&lt;script&gt;alert(1);&lt;\\/script&gt;\"}", text);
        assertEquals("{\"\\u003Cscript\\u003Ealert\\u00281\\u0029;\\u003C/script\\u003E\":\"value\"}", text);
        JSONObject object1 = JSON.parseObject(text);
        assertEquals(object.get("<script>alert(1);</script>"), object1.get("<script>alert(1);</script>"));
    }
//
//    public void test_1() throws Exception {
//        String text = JSON.toJSONString("<", SerializerFeature.BrowserSecure);
//        assertEquals("\"\\u003C\"", text);
//    }
//
//    public void test_2() throws Exception {
//        String text = JSON.toJSONString("<script>", SerializerFeature.BrowserSecure);
//        assertEquals("\"\\u003Cscript\\u003E\"", text);
//    }
//
//    public void test_3() throws Exception {
//        StringBuilder buf = new StringBuilder();
//        for (int i = 0; i < 500; i++) {
//            buf.append("<script>");
//        }
//
//        StringBuilder buf1 = new StringBuilder();
//        buf1.append('"');
//        for (int i = 0; i < 500; i++) {
//            buf1.append("\\u003Cscript\\u003E");
//        }
//        buf1.append('"');
//
//        StringWriter out = new StringWriter();
//        JSONWriter writer = new JSONWriter(out);
//        writer.config(SerializerFeature.BrowserSecure, true);
//        writer.writeObject(buf.toString());
//        writer.flush();
//
//        assertEquals(buf1.toString(), out.toString());
//    }
//
//
}
