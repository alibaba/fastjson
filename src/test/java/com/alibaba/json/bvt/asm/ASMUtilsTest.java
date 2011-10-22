package com.alibaba.json.bvt.asm;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.util.ASMUtils;


public class ASMUtilsTest extends TestCase {
    public void test_isAnroid() throws Exception {
        Assert.assertTrue(ASMUtils.isAndroid("Dalvik"));
    }
}
