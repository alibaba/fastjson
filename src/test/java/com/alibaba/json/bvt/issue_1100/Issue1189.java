package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by wenshao on 10/05/2017.
 */
public class Issue1189 extends TestCase {
    public void test_for_issue() throws Exception {
        String str = new String("{\"headernotificationType\": \"PUSH\",\"headertemplateNo\": \"99\",\"headerdestination\": [{\"target\": \"all\",\"targetvalue\": \"all\"}],\"body\": [{\"title\": \"预约超时\",\"body\": \"您的预约已经超时\"}]}");

        JsonBean objeclt = JSON.parseObject(str, JsonBean.class);
        Map<String, String> list = objeclt.getBody();
        System.out.println(list.get("body"));
    }

    @Data
    @NoArgsConstructor
    public static class JsonBean {
        private Map<String, String> body;
        private int headertemplateno;
        private Map<String, String> headerdestination;
        private String headernotificationtype;
        private String notificationType;

        public JsonBean(Map<String, String> body, int headertemplateno,
                        Map<String, String> headerdestination,
                        String headernotificationtype, String notificationType) {
            super();
            this.body = body;
            this.headertemplateno = headertemplateno;
            this.headerdestination = headerdestination;
            this.headernotificationtype = headernotificationtype;
            this.notificationType = notificationType;
        }
    }
}
