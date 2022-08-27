package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class Issue176 extends TestCase {

    public void test_for_parent() throws Exception {
        String text = "{\"content\":\"result\"}";
        
        ParentClass parentClass = JSON.parseObject(text, ParentClass.class);

        Assert.assertEquals(parentClass.getTest(), "result");
        
        String text2 = JSON.toJSONString(parentClass);
        Assert.assertEquals(text, text2);
    }
    
    public void test_for_sub() throws Exception {
        String text = "{\"content\":\"result\"}";
        
        SubClass  parentClass = JSON.parseObject(text, SubClass.class);
        
        Assert.assertEquals(parentClass.getTest(), "result");
        String text2 = JSON.toJSONString(parentClass);
        Assert.assertEquals(text, text2);
    }

    public static class ParentClass {

        @JSONField(name = "content")
        protected String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

    }

    public static class SubClass extends ParentClass {

    }
}
