package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class Issue1120 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.setReqNo("123");

        assertEquals("{\"REQ_NO\":\"123\"}", JSON.toJSONString(model));
    }

    public static class Model {
        @JSONField(name="REQ_NO")
        private String ReqNo;

        public String getReqNo() {
            return ReqNo;
        }

        public void setReqNo(String reqNo) {
            ReqNo = reqNo;
        }
    }
}
