package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ASMUtils;


public class Bug_for_issue_676 extends TestCase {
    public void test_for_issue() throws Exception {
        JSON.parseObject("{\"modelType\":\"\"}", MenuExpend.class);
    }
    
    public static class MenuExpend {
        public ModelType modelType;
    }
    
    public static enum ModelType {
        A, B, C
    }
}
