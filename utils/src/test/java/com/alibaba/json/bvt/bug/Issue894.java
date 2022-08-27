package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

/**
 * Created by wenshao on 10/03/2017.
 */
public class Issue894 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = String.valueOf(Double.MAX_VALUE);
        Throwable error = null;
        try {
            JSON.parseObject(str, short.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }
}
