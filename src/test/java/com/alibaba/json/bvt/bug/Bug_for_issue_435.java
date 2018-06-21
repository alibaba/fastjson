package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_435 extends TestCase {

    public void test_for_issue() throws Exception {
        JSON.parseObject("\ufeff{\"value\":null}", Model.class);
    }

    public void test_for_issue_Float() throws Exception {
        JSON.parseObject("\ufeff{\"value\":null}", ModelFloat.class);
    }
    
    public static class Model {
        public float value;
    }
    
    public static class ModelFloat {
        public Float value;
    }
}
