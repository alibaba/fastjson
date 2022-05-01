package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class JSONFieldTest_unwrapped_7 extends TestCase {

    public void test_jsonField() throws Exception {
        String str = "{\"s\":\"[\\\"123\\\",\\\"xyz\\\"]\"}";
        System.out.println(str);

        A a = JSON.parseObject(str, A.class);
        System.out.println(a.getS());

    }
    public static class A {
        private List<String> s;

        public List<String> getS() {
            return s;
        }

        @JSONField(unwrapped = true)
        public void setS(List<String> s) {
            this.s = s;
        }
    }
}
