package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

/**
 * Created by wenshao on 18/06/2017.
 */
public class Issue1272 extends TestCase {
    public void test_for_issue() throws Exception {
        Exception exception = null;

        try {
            JSON.toJSONString(new Point());
        }catch (JSONException ex) {
            exception = ex;
        }
        assertNotNull(exception);
        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    public static class Point {

        private Long userId;

        public long getUserId() {
            return userId;
        }
    }
}
