package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/03/2017.
 */
public class Issue1074 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "//123456";
        Throwable error = null;
        try {
            JSON.parse(json);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }
}
