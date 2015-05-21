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
        Assert.assertEquals("\"\\u4E2D\\u56FD\"", JSON.toJSONString("中国", SerializerFeature.BrowserSecure));
    }

    public void test_all() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";
        String expect = "\".,_\\u007E\\u0021\\u0040\\u003C\\u003E\\u0027\\u0022\\u005C\\u002Fhello\\u0020world\\u00200123\\u003B\\u6C49\\u5B57\\uFF1B\\u2028\\u2028\\u000D\\u000A\\u003Cscript\\u003E\\u003C\\u002Fscirpt\\u003E\"";
        Assert.assertEquals(expect, JSON.toJSONString(value, SerializerFeature.BrowserSecure));
    }

    public void test_all_map() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";

        Map<String, String> map = new HashMap<String, String>();
        map.put("value", value);

        String expect = "{\"value\":\".,_\\u007E\\u0021\\u0040\\u003C\\u003E\\u0027\\u0022\\u005C\\u002Fhello\\u0020world\\u00200123\\u003B\\u6C49\\u5B57\\uFF1B\\u2028\\u2028\\u000D\\u000A\\u003Cscript\\u003E\\u003C\\u002Fscirpt\\u003E\"}";
        String json = JSON.toJSONString(map, SerializerFeature.BrowserSecure);
        Assert.assertEquals(expect, json);

        Assert.assertEquals(value, JSON.parseObject(json).get("value"));
    }

    public void test_all_entity() throws Exception {
        String value = ".,_~!@<>'\"\\/hello world 0123;汉字；\u2028\u2028\r\n<script></scirpt>";

        VO vo = new VO();
        vo.setValue(value);

        String expect = "{\"value\":\".,_\\u007E\\u0021\\u0040\\u003C\\u003E\\u0027\\u0022\\u005C\\u002Fhello\\u0020world\\u00200123\\u003B\\u6C49\\u5B57\\uFF1B\\u2028\\u2028\\u000D\\u000A\\u003Cscript\\u003E\\u003C\\u002Fscirpt\\u003E\"}";
        String json = JSON.toJSONString(vo, SerializerFeature.BrowserSecure);
        Assert.assertEquals(expect, json);

        Assert.assertEquals(value, JSON.parseObject(json, VO.class).getValue());
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
