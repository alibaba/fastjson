package com.alibaba.json.bvt.parser.deser.arraymapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class ArrayMapping_long extends TestCase {
    public void test_for_error() throws Exception {
        JSON.parseObject("[1001,\"wenshao\"]", Model.class, Feature.SupportArrayToBean);
    }
    
    public static class Model {
        public long id;
        public String name;
        
    }
}
