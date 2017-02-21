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

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;

public class JSONObjectTest extends TestCase {

    public void test_toJSONObject() throws Exception {
        {
            Assert.assertNull(JSONObject.parse(null));
        }
    }

    public void test_writeJSONString() throws Exception {
        {
            StringWriter out = new StringWriter();
            new JSONObject().writeJSONString(out);
            Assert.assertEquals("{}", out.toString());
        }
    }

    public void test_getLong() throws Exception {
        JSONObject json = new JSONObject(true);
        json.put("A", 55L);
        json.put("B", 55);
        json.put("K", true);
        Assert.assertEquals(json.getLong("A").longValue(), 55L);
        Assert.assertEquals(json.getLong("B").longValue(), 55L);
        Assert.assertEquals(json.getLong("C"), null);
        Assert.assertEquals(json.getBooleanValue("K"), true);
        Assert.assertEquals(json.getBoolean("K"), Boolean.TRUE);
    }

    public void test_getLong_1() throws Exception {
        JSONObject json = new JSONObject(false);
        json.put("A", 55L);
        json.put("B", 55);
        Assert.assertEquals(json.getLong("A").longValue(), 55L);
        Assert.assertEquals(json.getLong("B").longValue(), 55L);
        Assert.assertEquals(json.getLong("C"), null);
    }

    public void test_getDate() throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        json.put("A", new Date(currentTimeMillis));
        json.put("B", currentTimeMillis);
        Assert.assertEquals(json.getDate("A").getTime(), currentTimeMillis);
        Assert.assertEquals(json.getDate("B").getTime(), currentTimeMillis);
        Assert.assertEquals(json.getLong("C"), null);
    }

    public void test_getBoolean() throws Exception {
        JSONObject json = new JSONObject();
        json.put("A", true);
        Assert.assertEquals(json.getBoolean("A").booleanValue(), true);
        Assert.assertEquals(json.getLong("C"), null);
    }

    public void test_getInt() throws Exception {
        JSONObject json = new JSONObject();
        json.put("A", 55L);
        json.put("B", 55);
        Assert.assertEquals(json.getInteger("A").intValue(), 55);
        Assert.assertEquals(json.getInteger("B").intValue(), 55);
        Assert.assertEquals(json.getInteger("C"), null);
    }

    public void test_order() throws Exception {
        JSONObject json = new JSONObject(true);
        json.put("C", 55L);
        json.put("B", 55);
        json.put("A", 55);
        Assert.assertEquals("C", json.keySet().toArray()[0]);
        Assert.assertEquals("B", json.keySet().toArray()[1]);
        Assert.assertEquals("A", json.keySet().toArray()[2]);

        Assert.assertEquals(0, json.getIntValue("D"));
        Assert.assertEquals(0L, json.getLongValue("D"));
        Assert.assertEquals(false, json.getBooleanValue("D"));
    }

    public void test_all() throws Exception {
        JSONObject json = new JSONObject();
        Assert.assertEquals(true, json.isEmpty());
        json.put("C", 51L);
        json.put("B", 52);
        json.put("A", 53);
        Assert.assertEquals(false, json.isEmpty());
        Assert.assertEquals(true, json.containsKey("C"));
        Assert.assertEquals(false, json.containsKey("D"));
        Assert.assertEquals(true, json.containsValue(52));
        Assert.assertEquals(false, json.containsValue(33));
        Assert.assertEquals(null, json.remove("D"));
        Assert.assertEquals(51L, json.remove("C"));
        Assert.assertEquals(2, json.keySet().size());
        Assert.assertEquals(2, json.values().size());
        Assert.assertEquals(new BigDecimal("53"), json.getBigDecimal("A"));

        json.putAll(Collections.singletonMap("E", 99));
        Assert.assertEquals(3, json.values().size());
        json.clear();
        Assert.assertEquals(0, json.values().size());
        json.putAll(Collections.singletonMap("E", 99));
        Assert.assertEquals(99L, json.getLongValue("E"));
        Assert.assertEquals(99, json.getIntValue("E"));
        Assert.assertEquals("99", json.getString("E"));
        Assert.assertEquals(null, json.getString("F"));
        Assert.assertEquals(null, json.getDate("F"));
        Assert.assertEquals(null, json.getBoolean("F"));
    }

    public void test_all_2() throws Exception {
        JSONObject array = new JSONObject();
        array.put("0", 123);
        array.put("1", "222");
        array.put("2", 3);
        array.put("3", true);
        array.put("4", "true");
        array.put("5", "2.0");

        Assert.assertEquals(123, array.getIntValue("0"));
        Assert.assertEquals(123, array.getLongValue("0"));
        Assert.assertEquals(new BigDecimal("123"), array.getBigDecimal("0"));

        Assert.assertEquals(222, array.getIntValue("1"));
        Assert.assertEquals(3, array.getByte("2").byteValue());
        Assert.assertEquals(3, array.getByteValue("2"));
        Assert.assertEquals(3, array.getShort("2").shortValue());
        Assert.assertEquals(3, array.getShortValue("2"));
        Assert.assertEquals(new Integer(222), array.getInteger("1"));
        Assert.assertEquals(new Long(222), array.getLong("1"));
        Assert.assertEquals(new BigDecimal("222"), array.getBigDecimal("1"));

        Assert.assertEquals(true, array.getBooleanValue("4"));
        Assert.assertTrue(2.0F == array.getFloat("5").floatValue());
        Assert.assertTrue(2.0F == array.getFloatValue("5"));
        Assert.assertTrue(2.0D == array.getDouble("5").doubleValue());
        Assert.assertTrue(2.0D == array.getDoubleValue("5"));
    }

    public void test_getObject_null() throws Exception {
        JSONObject json = new JSONObject();
        json.put("obj", null);

        Assert.assertTrue(json.getJSONObject("obj") == null);
    }
    
    public void test_bytes () throws Exception {
        JSONObject object = new JSONObject();
        Assert.assertNull(object.getBytes("bytes"));
    }

    public void test_getObject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("obj", new JSONObject());

        Assert.assertEquals(0, json.getJSONObject("obj").size());
    }

    public void test_getObject_map() throws Exception {
        JSONObject json = new JSONObject();
        json.put("obj", new HashMap());

        Assert.assertEquals(0, json.getJSONObject("obj").size());
    }
}
