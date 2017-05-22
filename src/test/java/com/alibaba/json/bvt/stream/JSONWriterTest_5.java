package com.alibaba.json.bvt.stream;

import java.io.StringWriter;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONWriterTest_5 extends TestCase {

    public void test_writer() throws Exception {
        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);

        writer.startObject();
        writer.writeKey("value");
        writer.writeObject((String) null);
        writer.endObject();

        writer.close();

        Assert.assertEquals("{\"value\":null}", out.toString());
    }
}
