package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_builder extends TestCase {

    public void test_for_longBuilderMethod() throws Exception {
        VO vo = JSON.parseObject("{\"id\":123}", VO.class);
    }

    public static class VO {

        private long id;

        public long getId() {
            return id;
        }

        public VO setId(long id) {
            this.id = id;
            return this;
        }

    }
}
