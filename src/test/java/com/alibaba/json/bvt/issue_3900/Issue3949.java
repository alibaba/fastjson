package com.alibaba.json.bvt.issue_3900;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

public class Issue3949 extends TestCase {
    //CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/3949
    public void test1() throws Exception {
        Model1 m = new Model1();
        String s = JSON.toJSONString(m);
        JSONObject jsonObject = JSON.parseObject(s);
        assertEquals(888, jsonObject.get("integer"));
    }
    //CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/3949
    public void test2() throws Exception {
        Model2 m = new Model2();
        String s = JSON.toJSONString(m);
        JSONObject jsonObject = JSON.parseObject(s);
        assertEquals("888", jsonObject.get("string"));
    }

    public static class Model1 {
        @JSONField(defaultValue = "888")
        private Integer integer;

        public Model1(Integer integer) {
            this.integer = integer;
        }

        public Model1() {
        }

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }
    }

    public static class Model2 {
        @JSONField(defaultValue = "888")
        private String string;
        public Model2(String string) {
            this.string = string;
        }

        public Model2() {
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}
