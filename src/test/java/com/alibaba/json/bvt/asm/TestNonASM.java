package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.ParserConfig;

public class TestNonASM extends TestCase {

    public void test_no_asm() throws Exception {
        ParserConfig mapping = new ParserConfig();
        
        
        Assert.assertEquals(false, mapping.isAsmEnable());
        
        
        Assert.assertEquals(true, mapping.isAsmEnable());
    }
    
    public void test_error() throws Exception {
        
    }
}
