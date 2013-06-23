package com.alibaba.json.bvt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class JSONFromObjectTest extends TestCase {

    public void test_0() throws Exception {
        User user = new User();
        user.setId(3);
        user.setName("周访");

        JSONObject json = (JSONObject) JSON.toJSON(user);

        Assert.assertEquals(new Long(3), json.getLong("id"));
        Assert.assertEquals("周访", json.getString("name"));
    }

    public void test_1() throws Exception {
        JSONObject user = new JSONObject();
        user.put("id", 3);
        user.put("name", "周访");

        JSONObject json = (JSONObject) JSON.toJSON(user);

        Assert.assertEquals(new Long(3), json.getLong("id"));
        Assert.assertEquals("周访", json.getString("name"));
    }

    public void test_2() throws Exception {
        HashMap user = new HashMap();
        user.put("id", 3);
        user.put("name", "周访");

        JSONObject json = (JSONObject) JSON.toJSON(user);

        Assert.assertEquals(new Long(3), json.getLong("id"));
        Assert.assertEquals("周访", json.getString("name"));
    }

    public void test_3() throws Exception {
        List users = new ArrayList();
        HashMap user = new HashMap();
        user.put("id", 3);
        user.put("name", "周访");
        users.add(user);

        JSONArray array = (JSONArray) JSON.toJSON(users);
        JSONObject json = array.getJSONObject(0);

        Assert.assertEquals(new Long(3), json.getLong("id"));
        Assert.assertEquals("周访", json.getString("name"));
    }

    public void test_error() throws Exception {
        C c = new C();

        JSONException error = null;
        try {
            JSON.toJSON(c);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
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

    public static class C {

        public int getId() {
            throw new UnsupportedOperationException();
        }

        public void setId(int id) {
            throw new UnsupportedOperationException();
        }
    }
}
