package com.alibaba.json.bvt.parser.deser;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;

public class TestASM_primitive extends TestCase {

    public void test_asm() throws Exception {
        ASMDeserializerFactory factory = new ASMDeserializerFactory();
        Exception error = null;
        try {
            factory.createJavaBeanDeserializer(ParserConfig.getGlobalInstance(), int.class, int.class);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

}
