package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_17 extends TestCase {

    public void test_writer_1() throws Exception {
        StringWriter strOut = new StringWriter();
        SerializeWriter out = new SerializeWriter(strOut, 6);
        out.config(SerializerFeature.QuoteFieldNames, true);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            VO vo = new VO();
            vo.setValue(123456789);
            serializer.write(vo);
        } finally {
            out.close();
        }
        Assert.assertEquals("{\"value\":123456789}", strOut.toString());
    }
    
    public void test_direct() throws Exception {
        SerializeWriter out = new SerializeWriter(6);
        out.config(SerializerFeature.QuoteFieldNames, true);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            VO vo = new VO();
            vo.setValue(123456789);
            serializer.write(vo);
            
            Assert.assertEquals("{\"value\":123456789}", out.toString());
        } finally {
            out.close();
        }
        
    }

    public static class VO {

        private long value;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

    }
}
