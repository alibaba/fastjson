package com.alibaba.json.bvt.asm;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.util.ASMUtils;

public class ASMUtilsTest extends TestCase {

    public void test_isAnroid() throws Exception {
        Assert.assertTrue(ASMUtils.isAndroid("Dalvik"));
    }

    public void test_getDescs() throws Exception {
        Assert.assertEquals("Lcom/alibaba/fastjson/parser/ParseContext;", ASMUtils.getDesc(ParseContext.class));
    }

    public void test_getType_null() throws Exception {
        Assert.assertNull(ASMUtils.getMethodType(ParseContext.class, "XX"));
    }
    
    public void test_getFieldType_null() throws Exception {
        Assert.assertNull(ASMUtils.getFieldType(ParseContext.class, "XX"));
    }
}
