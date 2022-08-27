package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeWriterTest_13 extends TestCase {

    public void test_default() throws Exception {
        Assert.assertEquals("{\"\":\"\"}", //
                            JSON.toJSONStringZ(Collections.singletonMap("", ""), //
                                               SerializeConfig.getGlobalInstance()));
    }

    public void test_single() throws Exception {
        Assert.assertEquals("{'':''}", //
                            JSON.toJSONStringZ(Collections.singletonMap("", ""), //
                                               SerializeConfig.getGlobalInstance(), SerializerFeature.UseSingleQuotes));
    }

    public void test_writer() throws Exception {
        SerializeWriter out = new SerializeWriter(3);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            serializer.write(Collections.singletonMap("", ""));
            Assert.assertEquals("{\"\":\"\"}", out.toString());
        } finally {
            out.close();
        }
    }

    public void test_writer_single() throws Exception {
        SerializeWriter out = new SerializeWriter(3);
        out.config(SerializerFeature.UseSingleQuotes, true);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            serializer.write(Collections.singletonMap("", ""));
            Assert.assertEquals("{'':''}", out.toString());
        } finally {
            out.close();
        }
    }
    
}
