package com.alibaba.json.bvt.parser.deser;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class SmartMatchTest_boolean_is extends TestCase {
    
    public void test_0() throws Exception {
        String text = "{\"isVisible\":true}";

        VO vo = JSON.parseObject(text, VO.class);
        Assert.assertEquals(true, vo.isVisible());
    }

    public static class VO {

        private boolean visible;

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

    }
}
