package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_BrowserSecure extends TestCase {

    public void test_0() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 1024; ++i) {
            buf.append('a');
        }
        buf.append("中国");
        buf.append("\0");
        JSON.toJSONString(buf.toString(), SerializerFeature.BrowserSecure);
    }

    public void test_1() throws Exception {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 1024; ++i) {
            buf.append('a');
        }
        buf.append("中国");
        buf.append("\0");

        StringWriter out = new StringWriter();
        JSON.writeJSONStringTo(buf.toString(), out, SerializerFeature.BrowserSecure);
    }

    public void test_zh() throws Exception {
        Assert.assertEquals("\"中国\"", JSON.toJSONString("中国", SerializerFeature.BrowserSecure));
    }

    public void test_all() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";
        String expect = "\".,_~!@\\u003C\\u003E'\\\"\\\\/hello world 0123;汉字；\\u2028\\u2028\\r\\n\\u003Cscript\\u003E\\u003C/scirpt\\u003E\"";
        Assert.assertEquals(expect, JSON.toJSONString(value, SerializerFeature.BrowserSecure));
    }

    public void test_all_map() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";

        Map<String, String> map = new HashMap<String, String>();
        map.put("value", value);

        String expect = "{\"value\":\".,_~!@\\u003C\\u003E'\\\"\\\\/hello world 0123;汉字；\\u2028\\u2028\\r\\n\\u003Cscript\\u003E\\u003C/scirpt\\u003E\"}";
        String json = JSON.toJSONString(map, SerializerFeature.BrowserSecure);
        assertEquals(expect, json);

        assertEquals(value, JSON.parseObject(json).get("value"));
    }

    public void test_all_entity() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";

        VO vo = new VO();
        vo.setValue(value);

        String expect = "{\"value\":\".,_~!@\\u003C\\u003E'\\\"\\\\/hello world 0123;汉字；\\u2028\\u2028\\r\\n\\u003Cscript\\u003E\\u003C/scirpt\\u003E\"}";
        String json = JSON.toJSONString(vo, SerializerFeature.BrowserSecure);
        assertEquals(expect, json);

        assertEquals(value, JSON.parseObject(json, VO.class).value);
    }

    public static class VO {

        String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
