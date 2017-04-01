package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class Issue1112 extends TestCase {
    public void test_for_issue_1() throws Exception {
        JSONObject object = new JSONObject();
        object.put("123", "abc");

        assertEquals("abc", JSONPath.eval(object, "$.123"));
    }

    public void test_for_issue_2() throws Exception {
        JSONObject object = new JSONObject();
        object.put("345_xiu", "abc");

        assertEquals("abc", JSONPath.eval(object, "$.345_xiu"));
    }


    public void test_for_issue_3() throws Exception {
        JSONObject object = new JSONObject();
        object.put("345.xiu", "abc");

        assertEquals("abc", JSONPath.eval(object, "$.345\\.xiu"));
    }
}
