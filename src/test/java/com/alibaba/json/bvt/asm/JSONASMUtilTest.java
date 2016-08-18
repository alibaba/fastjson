package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.util.ASMUtils;

public class JSONASMUtilTest extends TestCase {

    public void test_1() throws Exception {
        Assert.assertEquals("V", ASMUtils.desc(Void.TYPE));
        Assert.assertEquals("J", ASMUtils.desc(Long.TYPE));
        Assert.assertEquals("[J", ASMUtils.desc(long[].class));
        Assert.assertEquals("[Ljava/lang/Long;", ASMUtils.desc(Long[].class));
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
