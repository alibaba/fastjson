package com.alibaba.json.bvt.serializer;

import java.math.BigDecimal;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_5 extends TestCase {

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString(null);
        out.config(SerializerFeature.WriteTabAsSpecial, true);
        Assert.assertEquals(",\"name\":null", out.toString());
    }

    public void test_3() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString(null);
        Assert.assertEquals(",'name':null", out.toString());
    }

    public void test_4() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString(null);
        Assert.assertEquals(",'name':null", out.toString());
    }

}
