package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;


public class Issue117 extends TestCase {
    public void test_for_issue() throws Exception {
        VO vo = JSON.parseObject("{\"id\":123}", VO.class);
        Assert.assertEquals(123, vo.getId());
        vo.setId(124);
        vo.equals(null);
        vo.hashCode();
        Assert.assertEquals("{\"id\":124}", vo.toString());
    }
    
    public static interface VO {
        public int getId();
        public void setId(int val);
    }
}
