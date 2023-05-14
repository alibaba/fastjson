package com.alibaba.json.bvt.bug;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class Bug_for_issue_555_setter2 extends TestCase {

    public void test_for_issue() throws Exception {
        JSON.parseObject("{\"list\":[{\"spec\":{}}]}", A.class);
    }

    public static class A {

        public List<B> list;
    }

    public static class B {

        private Spec spec;

        @JSONField(serialize = true, deserialize = false)
        public Spec getSpec() {
            return spec;
        }

        @JSONField(serialize = true, deserialize = false)
        public void setSpec(Spec spec) {
            this.spec = spec;
        }

    }

    public static class Spec {

        private int id;

        public Spec(int id){
            this.id = id;
        }
    }
}
