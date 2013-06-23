package com.alibaba.json.bvt.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("rawtypes")
public class TypeUtilsToJSONTest extends TestCase {

    public void test_0() throws Exception {
        HashMap map = new HashMap();

        JSONObject json = (JSONObject) JSON.toJSON(map);

        Assert.assertEquals(map.size(), json.size());
    }

    public void test_1() throws Exception {
        List list = new ArrayList();

        JSONArray json = (JSONArray) JSON.toJSON(list);

        Assert.assertEquals(list.size(), json.size());
    }

    public void test_null() throws Exception {
        Assert.assertEquals(null, JSON.toJSON(null));
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
