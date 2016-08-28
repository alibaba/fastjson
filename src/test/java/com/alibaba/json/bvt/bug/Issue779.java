package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/28.
 */
public class Issue779 extends TestCase {
    public void test_for_issue() throws Exception {
        Exception error = null;
        try {
            String aaa = "{'token':'token':'sign':'bb'}";
            JSON.parseObject(aaa);
        } catch (JSONException ex) {
            error = ex;
        }

        assertNotNull(error);
    }

    public void test_for_issue_deser() throws Exception {
        Exception error = null;
        try {
            String aaa = "{'token':'token':'sign':'bb'}";
            JSON.parseObject(aaa, Model.class);
        } catch (JSONException ex) {
            error = ex;
        }

        assertNotNull(error);
    }

    public static class Model {
        public String token;
        public String sign;
    }
}
