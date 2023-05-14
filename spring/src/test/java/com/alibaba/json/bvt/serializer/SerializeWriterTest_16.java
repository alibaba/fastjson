package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_16 extends TestCase {

    public void test_writer_1() throws Exception {
        StringWriter strOut = new StringWriter();
        SerializeWriter out = new SerializeWriter(strOut, 14);
        out.config(SerializerFeature.BrowserCompatible, true);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            VO vo = new VO();
            vo.setValue("abcd\t");
            serializer.write(vo);
        } finally {
            out.close();
        }
        Assert.assertEquals("{value:\"abcd\\t\"}", strOut.toString());
    }

    private static class VO {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
