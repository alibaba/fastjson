package com.alibaba.json.bvt.issue_2400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

public class Issue2488 extends TestCase {
    public void testForIssue_1() {
        String a = "{\"$a_b\":\"a1_b2\",\"_c_d\":\"c3_d4\",\"aaaa\":\"CC\",\"__flag\":\"true\",\"$flag\":\"true\"}";
        JSONObject obj = (JSONObject) JSONObject.parse(a);
        TestJsonObj2 stu = JSONObject.toJavaObject(obj, TestJsonObj2.class);
        assertEquals("TestJsonObj2{$a_b=\"a1_b2\",_c_d=\"c3_d4\",aaaa=\"CC\",__flag=true,$flag=true}", stu.toString());
    }

    public void testForIssue_2() {
        String a = "{\"$a_b\":\"aa3_bb4\",\"_c_d\":\"cc1_dd2\",\"aaaa\":\"BB\",\"__flag\":\"true\",\"$flag\":\"true\"}";
        TestJsonObj2 stu = JSON.parseObject(a, TestJsonObj2.class);
        assertEquals("TestJsonObj2{$a_b=\"aa3_bb4\",_c_d=\"cc1_dd2\",aaaa=\"BB\",__flag=true,$flag=true}",
                stu.toString());
    }

    public void testForIssue_3() {
        TestJsonObj2 vo = new TestJsonObj2("aa_bb", "cc_dd", "AA", true, true);
        String text = JSON.toJSONString(vo);
        assertEquals("{\"$a_b\":\"aa_bb\",\"$flag\":true,\"__flag\":true,\"_c_d\":\"cc_dd\",\"aaaa\":\"AA\"}", text);
    }

    public static class TestJsonObj2 {
        private String $a_b;
        private String _c_d;
        private String aaaa;
        private boolean __flag;
        private boolean $flag;

        public TestJsonObj2() {
        }

        public TestJsonObj2(String $a_b, String _c_d, String aaaa, boolean __flag, boolean $flag) {
            this.$a_b = $a_b;
            this._c_d = _c_d;
            this.aaaa = aaaa;
            this.__flag = __flag;
            this.$flag = $flag;
        }

        public String get$a_b() {
            return $a_b;
        }

        public void set$a_b(String $a_b) {
            this.$a_b = $a_b;
        }

        public String get_c_d() {
            return _c_d;
        }

        public void set_c_d(String _c_d) {
            this._c_d = _c_d;
        }

        public String getaaaa() {
            return aaaa;
        }

        public void setaaaa(String aaaa) {
            this.aaaa = aaaa;
        }

        public boolean is__flag() {
            return __flag;
        }

        public void set__flag(boolean __flag) {
            this.__flag = __flag;
        }

        public boolean is$flag() {
            return $flag;
        }

        public void set$flag(boolean $flag) {
            this.$flag = $flag;
        }

        @Override
        public String toString() {
            return String.format("TestJsonObj2{$a_b=\"%s\",_c_d=\"%s\",aaaa=\"%s\",__flag=%b,$flag=%b}", this.$a_b,
                    this._c_d, this.aaaa, this.__flag, this.$flag);
        }
    }
}
