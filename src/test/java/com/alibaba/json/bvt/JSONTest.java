/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONTest extends TestCase {

    public void test_number() throws Exception {
        Assert.assertEquals("3", JSON.parse("3").toString());
        Assert.assertEquals("34", JSON.parse("34").toString());
        Assert.assertEquals("922337203685477580755", JSON.parse("922337203685477580755").toString());
        Assert.assertEquals("-34", JSON.parse("-34").toString());
        Assert.assertEquals(new BigDecimal("9.223372036854776E18"), new BigDecimal(JSON.parse("9.223372036854776E18").toString()));
        Assert.assertEquals(new BigDecimal("9.223372036854776E+18"), new BigDecimal(JSON.parse("9.223372036854776E+18").toString()));
        Assert.assertEquals(new BigDecimal("9.223372036854776E-18"), new BigDecimal(JSON.parse("9.223372036854776E-18").toString()));
    }

    public void test_string() throws Exception {
        Assert.assertEquals("", JSON.parse("\"\"").toString());
        Assert.assertEquals("3", JSON.parse("\"3\"").toString());
        Assert.assertEquals("34", JSON.parse("\"34\"").toString());
        Assert.assertEquals("3\\4", JSON.parse("\"3\\\\4\"").toString());
        Assert.assertEquals("3\"4", JSON.parse("\"3\\\"4\"").toString());
        Assert.assertEquals("3\\b4", JSON.parse("\"3\\\\b4\"").toString());
        Assert.assertEquals("3\\f4", JSON.parse("\"3\\\\f4\"").toString());
        Assert.assertEquals("3\\n4", JSON.parse("\"3\\\\n4\"").toString());
        Assert.assertEquals("3\\r4", JSON.parse("\"3\\\\r4\"").toString());
        Assert.assertEquals("3\\t4", JSON.parse("\"3\\\\t4\"").toString());
        Assert.assertEquals("中国", JSON.parse("\"中国\"").toString());
        Assert.assertEquals("中国", JSON.parse("\"\\u4E2D\\u56FD\"").toString());
        Assert.assertEquals("\u001F", JSON.parse("\"\\u001F\"").toString());
    }
    
    public void test_for_jh() throws Exception {
        String text = "[{\"I.13\":\"XEMwXFMweGEuMHhjOFxGy87M5VxUxOO6ww==\",\"I.18\":\"MA==\"},{\"I.13\":\"XEMwXFMweGEuMHhjOFxGy87M5VxUxOO6ww==\",\"I.18\":\"MA==\"}]";
        JSON.parse(text);
        JSON.parseArray(text);
    }

    public void test_value() throws Exception {
        Assert.assertEquals(Boolean.TRUE, JSON.parse("true"));
        Assert.assertEquals(Boolean.FALSE, JSON.parse("false"));
        Assert.assertEquals(null, JSON.parse("null"));
    }

    public void test_object() throws Exception {
        Assert.assertTrue(JSON.parseObject("{}").size() == 0);
        Assert.assertEquals(1, JSON.parseObject("{\"K\":3}").size());
        Assert.assertEquals(3, ((Number) JSON.parseObject("{\"K\":3}").get("K")).intValue());
        Assert.assertEquals(2, JSON.parseObject("{\"K1\":3,\"K2\":4}").size());
        Assert.assertEquals(3, ((Number) JSON.parseObject("{\"K1\":3,\"K2\":4}").get("K1")).intValue());
        Assert.assertEquals(4, ((Number) JSON.parseObject("{\"K1\":3,\"K2\":4}").get("K2")).intValue());
        Assert.assertEquals(1, JSON.parseObject("{\"K\":{}}").size());
        Assert.assertEquals(1, JSON.parseObject("{\"K\":[]}").size());
    }

    public void test_array() throws Exception {
        Assert.assertEquals(0, JSON.parseArray("[]").size());
        Assert.assertEquals(1, JSON.parseArray("[1]").size());
        Assert.assertEquals(1, ((Number) JSON.parseArray("[1]").get(0)).intValue());
        Assert.assertEquals(3, JSON.parseArray("[1,2, 3]").size());
        Assert.assertEquals(1, ((Number) JSON.parseArray("[1,2, 3]").get(0)).intValue());
        Assert.assertEquals(2, ((Number) JSON.parseArray("[1,2, 3]").get(1)).intValue());
        Assert.assertEquals(3, ((Number) JSON.parseArray("[1,2, 3]").get(2)).intValue());
    }

    public void test_all() throws Exception {
        Assert.assertEquals(null, JSON.parse(null));
        Assert.assertEquals("{}", JSON.toJSONString(new HashMap<String, Object>()));
        Assert.assertEquals("{}", JSON.toJSONString(new HashMap<String, Object>(), true));
        Assert.assertEquals("{}", JSON.toJSONString(new HashMap<String, Object>(), true));
        Assert.assertEquals(null, JSON.parseObject(null));
        Assert.assertEquals(null, JSON.parseArray(null));
        Assert.assertEquals(null, JSON.parseObject(null, Object.class));
        Assert.assertEquals(null, JSON.parseArray(null, Object.class));
    }

    public void test_writeTo_0() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONObject json = new JSONObject();
        json.writeJSONString(out);

        Assert.assertEquals("{}", out.toString());
    }

    public void test_writeTo_1() throws Exception {
        StringWriter out = new StringWriter();

        JSONObject json = new JSONObject();
        json.writeJSONString(out);

        Assert.assertEquals("{}", out.toString());
    }

    public void test_writeTo_2() throws Exception {
        StringBuffer out = new StringBuffer();

        JSONObject json = new JSONObject();
        json.writeJSONString(out);

        Assert.assertEquals("{}", out.toString());
    }

    public void test_writeTo_error() throws Exception {
        JSONException error = null;
        try {
            JSONObject json = new JSONObject();
            json.writeJSONString(new ErrorAppendable());
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_fromJavaObject_null() throws Exception {
        Assert.assertEquals(null, JSON.toJSON(null));
    }

    private final class ErrorAppendable implements Appendable {

        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            throw new IOException("");
        }

        public Appendable append(char c) throws IOException {
            throw new IOException("");
        }

        public Appendable append(CharSequence csq) throws IOException {
            throw new IOException("");
        }
    }
}
