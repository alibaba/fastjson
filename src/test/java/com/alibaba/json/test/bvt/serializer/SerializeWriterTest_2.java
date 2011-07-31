package com.alibaba.json.test.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_2 extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.writeString("\t\n \b\n\r\f\\ \"");
        Assert.assertEquals("\"\\t\\n \\b\\n\\r\\f\\\\ \\\"\"", out.toString());
    }
    
    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.writeString("\t\n \b\n\r\f\\ \"");
        Assert.assertEquals("'\\t\\n \\b\\n\\r\\f\\\\ \"'", out.toString());
    }
}
