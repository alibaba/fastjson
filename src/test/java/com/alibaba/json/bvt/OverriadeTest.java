package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OverriadeTest extends TestCase {

    public void test_override() throws Exception {
        JSON.parseObject("{}", B.class);
    }

    public static class A {

        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }

    public static class B extends A {
        public void setId(String id) {
            setId(Long.parseLong(id));
        }
    }
}
