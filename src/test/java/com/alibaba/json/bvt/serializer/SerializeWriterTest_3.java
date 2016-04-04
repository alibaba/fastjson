package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_3 extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString("jobs");
        Assert.assertEquals(",\"name\":\"jobs\"", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, false);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString("jobs");
        Assert.assertEquals(",name:\"jobs\"", out.toString());
    }
    
    public void test_null() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString(null);
        Assert.assertEquals(",\"name\":null", out.toString());
    }

    public void test_null_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.config(SerializerFeature.QuoteFieldNames, false);
        out.write(',');
        out.writeFieldName("name", false);
        out.writeString(null);
        Assert.assertEquals(",name:null", out.toString());
    }
    

}
