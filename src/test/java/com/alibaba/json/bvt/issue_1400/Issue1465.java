package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue1465 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"id\":3,\"hasSth\":true}";
        Model model = JSON.parseObject(json, Model.class);
        assertEquals(0, model.hasSth);
        assertEquals(3, model.id);
    }

    public static class Model {
        private int id;
        @JSONField(deserialize = false)
        private int hasSth;

        public int getHasSth() {
            return hasSth;
        }

        @JSONField(deserialize = false)
        public void setHasSth(int hasSth) {
            this.hasSth = hasSth;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
