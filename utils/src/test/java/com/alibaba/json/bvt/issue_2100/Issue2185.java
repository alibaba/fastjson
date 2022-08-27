package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Date;
import java.util.HashMap;

public class Issue2185 extends TestCase {

    public void test_for_issue() throws Exception {
        Exception error = null;
        try {
            JSONObject origin = new JSONObject();
            JSONArray jsonArray = new JSONArray().fluentAdd(origin.getInnerMap());
            jsonArray.getJSONObject(0).put("key", "value");
            // now we expect jsonArray is [{"key":"value"}]
            assertEquals(1, jsonArray.getJSONObject(0).size());
            assertTrue(origin.getInnerMap() == jsonArray.getJSONObject(0).getInnerMap());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNull(error);
    }

    /**
     * To prove casting from Map&lt;Object,Object&gt; won't cause exception
     *
     * @throws Exception
     */
    public void test_for_issue_1() throws Exception {
        Exception error = null;
        try {
            HashMap origin = new HashMap();
            origin.put(new Object(), "value");
            JSONArray jsonArray = new JSONArray().fluentAdd(origin);
            jsonArray.getJSONObject(0);
            // now jsonArray is [{{}:"value"}]
            assertEquals(1, jsonArray.getJSONObject(0).size());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNull(error);
    }

    /**
     * To prove casting from Map&lt;primitive type, Object&gt; won't cause exception
     *
     * @throws Exception
     */
    public void test_for_issue_2() throws Exception {
        Exception error = null;
        try {
            HashMap origin = new HashMap();
            origin.put(5.3f, "value");
            JSONArray jsonArray = new JSONArray().fluentAdd(origin);
            jsonArray.getJSONObject(0);
            // now jsonArray is [{5.3:"value"}]
            assertEquals(1, jsonArray.getJSONObject(0).size());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNull(error);
    }

    /**
     * To prove casting from Map&lt;Date, Object&gt; won't cause exception
     *
     * @throws Exception
     */
    public void test_for_issue_3() throws Exception {
        Exception error = null;
        try {
            HashMap origin = new HashMap();
            origin.put(new Date(), "value");
            JSONArray jsonArray = new JSONArray().fluentAdd(origin);
            jsonArray.getJSONObject(0);
            // now jsonArray is [{154xxxxxxxxx:"value"}]
            assertEquals(1, jsonArray.getJSONObject(0).size());
        } catch (JSONException ex) {
            error = ex;
        }
        assertNull(error);
    }

    public static class Model {
        public int value;
    }
}
