package com.alibaba.json.bvt.serializer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_19 extends TestCase {

    public void test_writer_1() throws Exception {
        SerializeWriter out = new SerializeWriter(14);
        out.config(SerializerFeature.QuoteFieldNames, true);
        out.config(SerializerFeature.UseSingleQuotes, true);
        try {
            JSONSerializer serializer = new JSONSerializer(out);

            VO vo = new VO();
            vo.getValues().add("#");
            serializer.write(vo);

            Assert.assertEquals("{'values':['#']}", out.toString());
        } finally {
            out.close();
        }

    }

    public static class VO {

        private List<String> values = new ArrayList<String>();

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

    }
}
