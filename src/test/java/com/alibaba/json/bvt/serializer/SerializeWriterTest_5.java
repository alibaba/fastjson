package com.alibaba.json.bvt.serializer;

import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_5 extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.writeFieldValue(',', "name", (Enum) null);
        Assert.assertEquals(",\"name\":null", out.toString());
    }
    
    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.writeFieldValue(',', "name", (BigDecimal) null);
        Assert.assertEquals(",\"name\":null", out.toString());
    }
    
    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.writeFieldValue(',', "name", (String) null);
        Assert.assertEquals(",\"name\":null", out.toString());
    }
    
    public void test_3() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.writeFieldValue(',', "name", (String) null);
        Assert.assertEquals(",'name':null", out.toString());
    }
    
    public void test_4() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.writeFieldValue(',', "name", (String) null);
        Assert.assertEquals(",'name':null", out.toString());
    }

}
