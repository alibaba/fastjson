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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONArrayTest extends TestCase {

    public void test_toString() throws Exception {
        StringWriter out = new StringWriter();
        new JSONArray().writeJSONString(out);
        Assert.assertEquals("[]", out.toString());
        Assert.assertEquals("[]", new JSONArray().toString());
    }

    public void test_toJSONString() throws Exception {
        Assert.assertEquals("null", JSONArray.toJSONString(null));
        Assert.assertEquals("[null]", JSONArray.toJSONString(Collections.singletonList(null)));
    }

    public void test_1() throws Exception {
        JSONArray array = new JSONArray(3);
        Assert.assertEquals(true, array.isEmpty());
        array.add(1);
        Assert.assertEquals(false, array.isEmpty());
        Assert.assertEquals(true, array.contains(1));
        Assert.assertEquals(1, array.toArray()[0]);
        {
            Object[] items = new Object[1];
            array.toArray(items);
            Assert.assertEquals(1, items[0]);
        }
        Assert.assertEquals(true, array.containsAll(Collections.singletonList(1)));
        Assert.assertEquals(true, array.remove(Integer.valueOf(1)));
        Assert.assertEquals(true, array.isEmpty());
        array.addAll(Collections.singletonList(1));
        Assert.assertEquals(1, array.size());
        array.removeAll(Collections.singletonList(1));
        Assert.assertEquals(0, array.size());
        array.addAll(0, Arrays.asList(1, 2, 3));
        Assert.assertEquals(3, array.size());
        array.clear();
        array.addAll(0, Arrays.asList(1, 2, 3));
        Assert.assertEquals(true, array.retainAll(Arrays.asList(1, 2)));
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(true, array.retainAll(Arrays.asList(2, 4)));
        Assert.assertEquals(1, array.size());
        array.set(0, 4);
        Assert.assertEquals(4, array.toArray()[0]);
        array.add(0, 4);
        Assert.assertEquals(4, array.toArray()[0]);
        array.remove(0);
        array.remove(0);
        Assert.assertEquals(0, array.size());
        array.addAll(Arrays.asList(1, 2, 3, 4, 5, 4, 3));
        Assert.assertEquals(2, array.indexOf(3));
        Assert.assertEquals(6, array.lastIndexOf(3));
        {
            AtomicInteger count = new AtomicInteger();
            for (ListIterator<Object> iter = array.listIterator(); iter.hasNext(); iter.next()) {
                count.incrementAndGet();
            }
            Assert.assertEquals(7, count.get());
        }
        {
            AtomicInteger count = new AtomicInteger();
            for (ListIterator<Object> iter = array.listIterator(2); iter.hasNext(); iter.next()) {
                count.incrementAndGet();
            }
            Assert.assertEquals(5, count.get());
        }
        {
            Assert.assertEquals(2, array.subList(2, 4).size());
        }
    }

    public void test_2() throws Exception {
        JSONArray array = new JSONArray();
        array.add(123);
        array.add("222");
        array.add(3);
        array.add(true);
        array.add("true");
        array.add(null);

        Assert.assertEquals(123, array.getByte(0).byteValue());
        Assert.assertEquals(123, array.getByteValue(0));

        Assert.assertEquals(123, array.getShort(0).shortValue());
        Assert.assertEquals(123, array.getShortValue(0));

        Assert.assertTrue(123F == array.getFloat(0).floatValue());
        Assert.assertTrue(123F == array.getFloatValue(0));

        Assert.assertTrue(123D == array.getDouble(0).doubleValue());
        Assert.assertTrue(123D == array.getDoubleValue(0));

        Assert.assertEquals(123, array.getIntValue(0));
        Assert.assertEquals(123, array.getLongValue(0));
        Assert.assertEquals(new BigDecimal("123"), array.getBigDecimal(0));

        Assert.assertEquals(222, array.getIntValue(1));
        Assert.assertEquals(new Integer(222), array.getInteger(1));
        Assert.assertEquals(new Long(222), array.getLong(1));
        Assert.assertEquals(new BigDecimal("222"), array.getBigDecimal(1));

        Assert.assertEquals(true, array.getBooleanValue(4));
        Assert.assertEquals(Boolean.TRUE, array.getBoolean(4));

        Assert.assertEquals(0, array.getIntValue(5));
        Assert.assertEquals(0, array.getLongValue(5));
        Assert.assertEquals(null, array.getInteger(5));
        Assert.assertEquals(null, array.getLong(5));
        Assert.assertEquals(null, array.getBigDecimal(5));
        Assert.assertEquals(null, array.getBoolean(5));
        Assert.assertEquals(false, array.getBooleanValue(5));
    }

    public void test_getObject_null() throws Exception {
        JSONArray array = new JSONArray();
        array.add(null);

        Assert.assertTrue(array.getJSONObject(0) == null);
    }

    public void test_getObject() throws Exception {
        JSONArray array = new JSONArray();
        array.add(new JSONObject());

        Assert.assertEquals(0, array.getJSONObject(0).size());
    }

    public void test_getObject_map() throws Exception {
        JSONArray array = new JSONArray();
        array.add(new HashMap());

        Assert.assertEquals(0, array.getJSONObject(0).size());
    }

    public void test_getArray() throws Exception {
        JSONArray array = new JSONArray();
        array.add(new ArrayList());

        Assert.assertEquals(0, array.getJSONArray(0).size());
    }

    public void test_getArray_1() throws Exception {
        JSONArray array = new JSONArray();
        array.add(new JSONArray());

        Assert.assertEquals(0, array.getJSONArray(0).size());
    }

    public void test_constructor() throws Exception {
        List<Object> list = new ArrayList();
        JSONArray array = new JSONArray(list);
        array.add(3);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(3, list.get(0));
    }

    public void test_getJavaBean() throws Exception {
        JSONArray array = JSON.parseArray("[{id:123, name:'aaa'}]");
        Assert.assertEquals(1, array.size());
        Assert.assertEquals(123, array.getObject(0, User.class).getId());
    }

    public static class User {

        private long   id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
