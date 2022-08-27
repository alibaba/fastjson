package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_18 extends TestCase {

    public void test_writer_1() throws Exception {
        SerializeWriter out = new SerializeWriter(14);
        out.config(SerializerFeature.QuoteFieldNames, true);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            VO vo = new VO();
            vo.setValue("#");
            serializer.write(vo);
            
            Assert.assertEquals("{\"value\":\"#\"}", out.toString());
        } finally {
            out.close();
        }
       
    }

    public static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
