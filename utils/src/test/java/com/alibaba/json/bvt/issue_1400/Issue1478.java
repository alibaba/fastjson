package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue1478 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.md5 = "xxx";

        String json = JSON.toJSONString(model);
        assertEquals("{\"MD5\":\"xxx\"}", json);
    }

    public static class Model {
        @JSONField(name = "MD5")
        private String md5;

        public String getMD5() throws Exception {
            return md5;
        }
    }
}
