package com.alibaba.json.bvt;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TestDeprecate extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(123);

        String text = JSON.toJSONString(vo);
    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        /**
         * @deprecated
         * @return
         */
        public int getId2() {
            return this.id;
        }

        @Deprecated
        public int getId3() {
            return this.id;
        }
    }

}
