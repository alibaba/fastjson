package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue1879 extends TestCase {
//    public void test_for_issue() throws Exception {
//        String json = "{\n" +
//                "   \"ids\" : \"1,2,3\"\n" +
//                "}";
//        M1 m = JSON.parseObject(json, M1.class);
//    }

    public void test_for_issue_2() throws Exception {
        String json = "{\n" +
                "   \"ids\" : \"1,2,3\"\n" +
                "}";
        M2 m = JSON.parseObject(json, M2.class);
    }

    public static class M1 {
        private List<Long> ids;

        @JSONCreator
        public M1(@JSONField(name = "ids") String ids) {
            this.ids = new ArrayList<Long>();
            for(String id : ids.split(",")) {
                this.ids.add(Long.valueOf(id));
            }
        }

        public List<Long> getIds() {
            return ids;
        }

        public void setIds(List<Long> ids) {
            this.ids = ids;
        }
    }

    public static class M2 {
        private List<Long> ids;

        @JSONCreator
        public M2(@JSONField(name = "ids") Long id) {
            this.ids = new ArrayList<Long>();
            this.ids.add(id);
        }

        public List<Long> getIds() {
            return ids;
        }

        public void setIds(List<Long> ids) {
            this.ids = ids;
        }
    }
}
