package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.util.ASMUtils;

public class JSONASMUtilTest extends TestCase {

    public void test_1() throws Exception {
        Assert.assertEquals("V", ASMUtils.getDesc(Void.TYPE));
        Assert.assertEquals("J", ASMUtils.getDesc(Long.TYPE));
        Assert.assertEquals("[J", ASMUtils.getDesc(long[].class));
        Assert.assertEquals("[Ljava/lang/Long;", ASMUtils.getDesc(Long[].class));
    }

    public void test_error_1() throws Exception {
        new ASMUtils();

        Exception error = null;
        try {
            ASMUtils.getPrimitiveLetter(Long.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
}
