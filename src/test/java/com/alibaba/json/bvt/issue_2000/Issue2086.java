package com.alibaba.json.bvt.issue_2000;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2086 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{\"id\":123}", Model.class);
        JSON.toJSONString(new Model());
    }

    public static class Model {
        public void set() {

        }
    }
}
