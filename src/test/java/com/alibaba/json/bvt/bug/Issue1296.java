package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

/**
 * Created by wenshao on 02/07/2017.
 */
public class Issue1296 extends TestCase {
    public void test_for_issue() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("1");
        } catch (JSONException e) {
            error = e;
        }
        assertNotNull(error);
    }
}
