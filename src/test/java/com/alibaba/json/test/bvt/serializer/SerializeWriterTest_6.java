package com.alibaba.json.test.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_6 extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.writeFieldValue(',', "name", (Enum) null);
        Assert.assertEquals(",'name':null", out.toString());
    }
    

}
