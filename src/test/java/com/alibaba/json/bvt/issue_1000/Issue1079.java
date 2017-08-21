package com.alibaba.json.bvt.issue_1000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by wenshao on 17/03/2017.
 */
public class Issue1079 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\n" +
                "\t\"Response\": [{\n" +
                "\t\t\"Status\": {\n" +
                "\t\t\t\"StatusCode\": {\n" +
                "\t\t\t\t\"Value\": \"urn:oasis:names:tc:xacml:1.0:status:ok\"\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"Decision\": \"NotApplicable\"\n" +
                "\t}]\n" +
                "}";

        JSON.parseObject(text, PdpResponse.class);

    }

    public static class PdpResponse {

        @JSONField(name ="Response")
        public List<Response> response;

        public static class Response {
            public List<InnerObject> innerObjects;
        }

        public static class InnerObject {
            @JSONField(name = "Status")
            public Status status;
            @JSONField(name = "Decision")
            public String decision;
        }


        public static class Status {
            @JSONField(name = "StatusCode")
            public StatusCode statusCode;
        }

        public static class StatusCode {
            @JSONField(name = "Value")
            public String value;
        }

        @JSONField(deserialize = false)
        public String retrieveDecision(){
            return this.response.get(0).innerObjects.get(0).decision;
        }
    }
}
