package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class Issue2792 extends TestCase {
    public void test_for_issue() throws Exception {
        String jsonpath = "$.sku[?((@.quantity != 0)&&(@.is_onsale == 1))].sku_id";

        JSONObject root = JSON.parseObject("{\"sku\":{\"quantity\":12,\"is_onsale\":1,\"sku_id\":42356}}");

        assertEquals(42356, JSONPath.eval(root, jsonpath));
    }
}
