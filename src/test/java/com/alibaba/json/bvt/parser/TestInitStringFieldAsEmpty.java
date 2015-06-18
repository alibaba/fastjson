package com.alibaba.json.bvt.parser;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class TestInitStringFieldAsEmpty extends TestCase {

    public void test_private() throws Exception {
        VO1 vo1 = JSON.parseObject("{}", VO1.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("", vo1.getValue());
    }

    public void test_public() throws Exception {
        VO2 vo2 = JSON.parseObject("{}", VO2.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("", vo2.getValue());
    }
    
    private static class VO1 {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    public static class VO2 {

        private String value;
        
        public VO2() {
            
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
