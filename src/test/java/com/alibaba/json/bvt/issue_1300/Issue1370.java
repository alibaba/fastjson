package com.alibaba.json.bvt.issue_1300;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.sql.Timestamp;

/**
 * Created by wenshao on 04/08/2017.
 */
public class Issue1370 extends TestCase {
    public void test_0() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("val", "2017-08-04 15:16:41.000000000");

        Model model = obj.toJavaObject(Model.class);
        assertNotNull(model.val);
    }

    public static class Model {
        public Timestamp val;
    }
}
