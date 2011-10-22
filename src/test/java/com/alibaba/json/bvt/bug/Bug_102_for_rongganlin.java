package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

public class Bug_102_for_rongganlin extends TestCase {

    public void test_bug() throws Exception {
        TestBean testProcessInfo = new TestBean();
        com.alibaba.fastjson.JSONObject jo = new com.alibaba.fastjson.JSONObject();
        jo.put("id", 121);
        ParserConfig config = new ParserConfig();
        testProcessInfo = TypeUtils.cast(jo, TestBean.class, config);
    }

    public static class TestBean {

        private double id;
        private double name;

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public double getName() {
            return name;
        }

        public void setName(double name) {
            this.name = name;
        }
    }
}
