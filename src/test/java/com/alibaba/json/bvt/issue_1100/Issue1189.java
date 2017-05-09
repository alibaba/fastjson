package com.alibaba.json.bvt.issue_1100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import junit.framework.TestCase;

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

    public static class JsonBean {
        private Map<String, String> body;
        private int headertemplateno;
        private Map<String, String> headerdestination;
        private String headernotificationtype;
        private String notificationType;



        public Map<String, String> getBody() {
            return body;
        }
        public void setBody(Map<String, String> body) {
            this.body = body;
        }
        public int getHeadertemplateno() {
            return headertemplateno;
        }
        public void setHeadertemplateno(int headertemplateno) {
            this.headertemplateno = headertemplateno;
        }
        public Map<String, String> getHeaderdestination() {
            return headerdestination;
        }
        public void setHeaderdestination(Map<String, String> headerdestination) {
            this.headerdestination = headerdestination;
        }
        public String getHeadernotificationtype() {
            return headernotificationtype;
        }
        public void setHeadernotificationtype(String headernotificationtype) {
            this.headernotificationtype = headernotificationtype;
        }
        public String getNotificationType() {
            return notificationType;
        }
        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }
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
        public JsonBean() {
            super();
            // TODO Auto-generated constructor stub
        }

    }
}
