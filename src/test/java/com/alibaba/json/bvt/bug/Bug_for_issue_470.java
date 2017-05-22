package com.alibaba.json.bvt.bug;

import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_470 extends TestCase {
    
    public void test_for_issue() throws Exception {
        List<VO> list = JSON.parseArray("[{\"value\":null}]", VO.class);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(false, list.get(0).value);
    }
    
    public void test_for_issue_private() throws Exception {
        List<V1> list = JSON.parseArray("[{\"value\":null}]", V1.class);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(false, list.get(0).value);
    }
    
    public void test_for_issue_method() throws Exception {
        List<V2> list = JSON.parseArray("[{\"value\":null}]", V2.class);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(false, list.get(0).value);
    }

    public static class VO {
        public boolean value;
    }
    
    private static class V1 {
        public boolean value;
    }
    
    public static class V2 {
        private boolean value;
        
        public boolean isValue() {
            return value;
        }

        
        public void setValue(boolean value) {
            this.value = value;
        }
        
    }
}
