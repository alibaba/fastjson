package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSONArray;
import junit.framework.TestCase;

public class Issue2150 extends TestCase {
    public void test_for_issue() throws Exception {
        int [][][] arr = new int[100][100][100];
        JSONArray jsonObj = (JSONArray) JSONArray.toJSON(arr);
        assertNotNull(jsonObj);
        assertNotNull(jsonObj.getJSONArray(0));
    }
}
