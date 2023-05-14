package com.alibaba.json.bvt.stream;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONWriter;

public class JSONWriterTest_0 extends TestCase {

    public void test_writer() throws Exception {
        StringWriter out = new StringWriter();
        JSONWriter writer = new JSONWriter(out);
        writer.startArray();
        writer.writeObject("1");
        writer.writeObject("2");
        writer.writeObject("3");
        writer.endArray();
        writer.close();

        Assert.assertEquals("[\"1\",\"2\",\"3\"]", out.toString());
    }
}
