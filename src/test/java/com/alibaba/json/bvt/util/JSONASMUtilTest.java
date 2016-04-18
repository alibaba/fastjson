package com.alibaba.json.bvt.util;

import java.util.HashMap;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.util.ASMUtils;

public class JSONASMUtilTest extends TestCase {

    public void test_0() throws Exception {
        Assert.assertEquals("()I", ASMUtils.desc(HashMap.class.getMethod("size")));
        Assert.assertEquals("(Ljava/lang/Object;)Ljava/lang/Object;", ASMUtils.desc(HashMap.class.getMethod("get", Object.class)));
        Assert.assertEquals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ASMUtils.desc(HashMap.class.getMethod("put", Object.class, Object.class)));
    }

    public void test_1() throws Exception {
        Assert.assertEquals("I", ASMUtils.type(int.class));
        Assert.assertEquals("java/lang/Integer", ASMUtils.type(Integer.class));
    }

    public void test_2() throws Exception {
        Assert.assertEquals("[I", ASMUtils.type(int[].class));
        Assert.assertEquals("[Ljava/lang/Integer;", ASMUtils.type(Integer[].class));
    }
}
