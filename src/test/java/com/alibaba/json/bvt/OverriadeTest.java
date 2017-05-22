package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OverriadeTest extends TestCase {

    public void test_override() throws Exception {
        JSON.parseObject("{\"id\":123}", B.class);
    }

    public static class A {

        protected long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            throw new UnsupportedOperationException();
        }

    }

    public static class B extends A {
        public void setId(String id) {
            this.id = Long.parseLong(id);
        }
    }
}
