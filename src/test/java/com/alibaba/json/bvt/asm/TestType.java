package com.alibaba.json.bvt.asm;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.asm.Type;
import com.alibaba.fastjson.util.ASMUtils;

public class TestType extends TestCase {

    public void test_getType() throws Exception {
        Assert.assertEquals(Type.VOID_TYPE, Type.getType(ASMUtils.desc(void.class)));

        Assert.assertEquals(Type.BOOLEAN_TYPE, Type.getType(ASMUtils.desc(boolean.class)));

        Assert.assertEquals(Type.CHAR_TYPE, Type.getType(ASMUtils.desc(char.class)));

        Assert.assertEquals(Type.BYTE_TYPE, Type.getType(ASMUtils.desc(byte.class)));

        Assert.assertEquals(Type.SHORT_TYPE, Type.getType(ASMUtils.desc(short.class)));

        Assert.assertEquals(Type.INT_TYPE, Type.getType(ASMUtils.desc(int.class)));

        Assert.assertEquals(Type.LONG_TYPE, Type.getType(ASMUtils.desc(long.class)));

        Assert.assertEquals(Type.FLOAT_TYPE, Type.getType(ASMUtils.desc(float.class)));

        Assert.assertEquals(Type.DOUBLE_TYPE, Type.getType(ASMUtils.desc(double.class)));
        
        Assert.assertEquals("[D", Type.getType(ASMUtils.desc(double[].class)).getInternalName());
        Assert.assertEquals("[[D", Type.getType(ASMUtils.desc(double[][].class)).getInternalName());
        Assert.assertEquals("[Ljava/lang/Double;", Type.getType(ASMUtils.desc(Double[].class)).getInternalName());
    }

}
