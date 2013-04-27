package com.alibaba.json.bvt.asm;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.util.ASMUtils;


public class ASMUtilsTest extends TestCase {
    public void test_isAnroid() throws Exception {
        Assert.assertTrue(ASMUtils.isAndroid("Dalvik"));
    }
    
    public void test_getDescs() throws Exception {
        Assert.assertEquals("Lcom/alibaba/fastjson/parser/ParseContext;",ASMUtils.getDesc(ParseContext.class));
    }
}
