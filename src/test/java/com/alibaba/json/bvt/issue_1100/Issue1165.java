package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 27/04/2017.
 */
public class Issue1165 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.__v = 3;

        String json = JSON.toJSONString(model);
        assertEquals("{\"__v\":3}", json);
    }

    public static class Model {
        public Number __v;
    }
}
