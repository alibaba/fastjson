package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.Date;

public class Issue1482 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{\"date\":\"2017-06-28T07:20:05.000+05:30\"}", Model.class);
    }

    public static class Model {
        public Date date;
    }
}
