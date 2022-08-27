package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

/**
 * Created by wenshao on 13/04/2017.
 */
public class Issue1144 extends TestCase {
    public void test_issue_1144() throws Exception {
        Model model = new Model();
        String json = JSON.toJSONString(model);
        System.out.println(json);
    }

    @JSONType(alphabetic = false)
    public static class Model {
        public int f2;
        public int f1;
        public int f0;
    }
}
