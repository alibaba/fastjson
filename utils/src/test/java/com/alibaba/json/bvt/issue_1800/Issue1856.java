package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.Labels;
import junit.framework.TestCase;

public class Issue1856 extends TestCase {
    public void test_excludes() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("wenshao");
        vo.setPassword("ooxxx");
        vo.setInfo("fofo");

        String text = JSON.toJSONString(vo, Labels.excludes("AuditIdEntity"));
        assertEquals("{\"id\":123,\"info\":\"fofo\",\"name\":\"wenshao\"}", text);
    }

    public static class VO {

        private int    id;
        private String name;
        private String password;
        private String info;

        @JSONField(label = "LogicDeleteEntity")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @JSONField(label = "LogicDeleteEntity")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JSONField(label = "AuditIdEntity")
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

    }
}
