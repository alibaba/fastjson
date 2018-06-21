package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.StringWriter;

public class SerializeWriterTest_BrowserSecure_5_script_model extends TestCase {

    public void test_0() throws Exception {
        Model object = new Model();
        object.value = "<script>alert(1);</script>";
        String text = JSON.toJSONString(object, SerializerFeature.BrowserSecure);
//        assertEquals("{\"value\":\"&lt;script&gt;alert(1);&lt;\\/script&gt;\"}", text);
        assertEquals("{\"value\":\"\\u003Cscript\\u003Ealert\\u00281\\u0029;\\u003C/script\\u003E\"}", text);
        Model object1 = JSON.parseObject(text, Model.class);
        assertEquals(object.value, object1.value);
    }

    public void test_1() throws Exception {
        Model object = new Model();
        object.value = "<";
        String text = JSON.toJSONString(object, SerializerFeature.BrowserSecure);
//        assertEquals("{\"value\":\"&lt;script&gt;alert(1);&lt;\\/script&gt;\"}", text);
        assertEquals("{\"value\":\"\\u003C\"}", text);
        Model object1 = JSON.parseObject(text, Model.class);
        assertEquals(object.value, object1.value);
    }

    public void test_2() throws Exception {
        Model object = new Model();
        object.value = "<script>";
        String text = JSON.toJSONString(object, SerializerFeature.BrowserSecure);
//        assertEquals("{\"value\":\"&lt;script&gt;alert(1);&lt;\\/script&gt;\"}", text);
        assertEquals("{\"value\":\"\\u003Cscript\\u003E\"}", text);
        Model object1 = JSON.parseObject(text, Model.class);
        assertEquals(object.value, object1.value);
    }
//
    public void test_3() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            buf.append("<script>");
        }

        StringBuilder buf1 = new StringBuilder();
        buf1.append("{\"value\":\"");
        for (int i = 0; i < 500; i++) {
            buf1.append("\\u003Cscript\\u003E");
        }
        buf1.append("\"}");

        Model object = new Model();
        object.value = buf.toString();

        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);
        writer.config(SerializerFeature.BrowserSecure, true);
        writer.writeObject(object);
        writer.flush();

        assertEquals(buf1.toString(), out.toString());
    }
//
    public void test_4() throws Exception {
        String text = JSON.toJSONString(new Model("("), SerializerFeature.BrowserSecure);
        assertEquals("{\"value\":\"\\u0028\"}", text);
    }

    public void test_5() throws Exception {
        String text = JSON.toJSONString(new Model(")"), SerializerFeature.BrowserSecure);
        assertEquals("{\"value\":\"\\u0029\"}", text);
    }

    public static class Model {
        public String value;

        public Model() {

        }

        public Model(String value) {
            this.value = value;
        }
    }
}
