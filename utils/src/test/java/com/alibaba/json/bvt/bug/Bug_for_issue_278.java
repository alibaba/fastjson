package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_278 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.setTest(true);
        
        String text = JSON.toJSONString(vo);
        Assert.assertEquals("{\"test\":true}", text);
    }
    
    public void test_for_issue_decode() throws Exception {
        VO vo = JSON.parseObject("{\"isTest\":true}", VO.class);
        Assert.assertTrue(vo.isTest);
    }

    public static class VO {

        private boolean isTest;

        public boolean isTest() {
            return isTest;
        }

        public void setTest(boolean isTest) {
            this.isTest = isTest;
        }

    }
}
