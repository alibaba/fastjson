package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;

public class Issue1443 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"date\":\"3017-08-28T00:00:00+08:00\"}";
        Model model = JSON.parseObject(json, Model.class);

    }

    public static class Model {
        public Date date;
    }
}
