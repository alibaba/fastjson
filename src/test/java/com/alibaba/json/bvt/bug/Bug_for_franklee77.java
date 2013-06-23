package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_franklee77 extends TestCase {

    public void test_0() throws Exception {
        VO vo = JSON.parseObject("{\"id\":33}", VO.class);
        Assert.assertEquals(33, vo.getId());

    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private VO(){

        }
    }
}
