package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue1739 extends TestCase {
    public void test_for_issue() throws Exception {
        M0 model = new M0();
        model.data = new JSONObject();

        String json = JSON.toJSONString(model);
        assertEquals("{\"data\":{}}", json);
    }

    public void test_for_issue_1() throws Exception {
        M1 model = new M1();
        model.data = new JSONObject();

        String json = JSON.toJSONString(model);
        assertEquals("{}", json);
    }

    public static class M0 {
        private JSONObject data;

        @JSONField(deserialize = false)
        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }
    }

    public static class M1 {
        private JSONObject data;

        @JSONField(serialize = false)
        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }
    }
}
