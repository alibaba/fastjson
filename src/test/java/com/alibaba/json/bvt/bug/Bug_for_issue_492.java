package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSONPath;

import junit.framework.TestCase;

public class Bug_for_issue_492 extends TestCase {

    public void test_for_issue() throws Exception {
       String json = "{\"name\":\"zhugw\"}";
       Assert.assertEquals("zhugw", JSONPath.read(json, "/name"));
    }

   
    public static class Model {
        public String name;
    }
}
