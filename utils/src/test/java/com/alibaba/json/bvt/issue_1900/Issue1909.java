package com.alibaba.json.bvt.issue_1900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.List;

public class Issue1909 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONArray params = new JSONArray();
        params.add("val1");
        params.add(2);
        ParamRequest pr = new ParamRequest("methodName", "stringID", params);
        System.out.println(JSON.toJSONString(pr));
        Request paramRequest = JSON.parseObject(JSON.toJSONString(pr), ParamRequest.class);
    }

    public static class ParamRequest extends Request {
        private String methodName;

        @JSONField(name = "id", ordinal = 3, serialize = true, deserialize = true)
        private Object id;

        private List<Object> params;

        public ParamRequest(String methodName, Object id, List<Object> params) {
            this.methodName = methodName;
            this.id = id;
            this.params = params;
        }

        public String getMethodName() {
            return methodName;
        }

        public Object getId() {
            return id;
        }

        public List<Object> getParams() {
            return params;
        }
    }

    public static class Request {

    }
}
