package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class JSONPath_set_test6 extends TestCase {
    public void test_jsonpath_1() throws Exception {
        JSONObject aa= new JSONObject();
        aa.put("app-a", "haj ");
        JSONPath.set(aa, "$.app\\-a\\.x", "123");
        assertEquals("haj ", aa.getString("app-a"));
        assertEquals("123", aa.getString("app-a.x"));
    }
}
