package com.alibaba.json.bvt.stream;

import java.io.StringWriter;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONWriterTest_4 extends TestCase {

    public void test_writer() throws Exception {
        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);
        writer.config(SerializerFeature.UseSingleQuotes, true);
        writer.writeObject(Collections.emptyMap());
        writer.close();

        Assert.assertEquals("{}", out.toString());
    }
}
