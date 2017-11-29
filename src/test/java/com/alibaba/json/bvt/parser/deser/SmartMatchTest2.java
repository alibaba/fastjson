package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class SmartMatchTest2 extends TestCase {
    
    public void f_test_0 () throws Exception {
        String text = "{\"_id\":1001}";
        
        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(1001, vo.getId());
    }
    
    public void test_vo2 () throws Exception {
        String text = "{\"_id\":1001}";
        
        VO2 vo = JSON.parseObject(text, VO2.class);
        Assert.assertEquals(1001, vo.getId());
    }

    private static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
    
    public static class VO2 {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
