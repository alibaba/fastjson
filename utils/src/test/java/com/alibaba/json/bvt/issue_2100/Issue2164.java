package com.alibaba.json.bvt.issue_2100;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

public class Issue2164 extends TestCase {
    public void test_for_issue() throws Exception {
        java.sql.Timestamp ts = new java.sql.Timestamp(-65001600000L);
        String json = JSON.toJSONString(ts);
        assertEquals("-65001600000", json);
        java.sql.Timestamp ts2 = JSON.parseObject(json, java.sql.Timestamp.class);
        assertEquals(ts.getTime(), ts2.getTime());
    }

    public void test_for_issue_1() throws Exception {
        Model m = new Model(-65001600000L);
        String json = JSON.toJSONString(m);
        assertEquals("{\"time\":-65001600000}", json);
        Model m2 = JSON.parseObject(json, Model.class);
        assertEquals(m.time.getTime(), m2.time.getTime());
    }

    public static class Model {
        public java.sql.Timestamp time;

        public Model() {

        }

        public Model(long ts) {
            this.time = new java.sql.Timestamp(ts);
        }
    }
}
