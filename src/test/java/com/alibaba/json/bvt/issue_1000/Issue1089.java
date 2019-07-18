package com.alibaba.json.bvt.issue_1000;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 20/03/2017.
 */
public class Issue1089 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "{\"ab\":123,\"a_b\":456}";
        String json2 = "{\"a_b\":456, \"ab\":123,}";
        TestBean tb = JSON.parseObject(json, TestBean.class);
        TestBean tb2 = JSON.parseObject(json2, TestBean.class);
        assertEquals(123, tb.getAb());
        assertEquals(123, tb2.getAb());
    }

    public static class TestBean {
        private int ab;

        public int getAb() {
            return ab;
        }

        public void setAb(int ab) {
            this.ab = ab;
        }
    }

}
