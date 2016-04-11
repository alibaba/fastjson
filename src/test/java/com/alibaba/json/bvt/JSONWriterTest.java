package com.alibaba.json.bvt;

import java.io.StringWriter;

import org.junit.Assert;

import com.alibaba.fastjson.JSONWriter;

import junit.framework.TestCase;

@SuppressWarnings("deprecation")
public class JSONWriterTest extends TestCase {

    public void test_0() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.startObject();
        writer.endObject();
        writer.flush();

        Assert.assertEquals("{}", out.toString());
    }

    public void test_1() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.startObject();
        writer.writeKey("id");
        writer.writeValue(33);
        writer.endObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33}", out.toString());
    }

    public void test_2() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.startObject();

        writer.writeKey("id");
        writer.writeValue(33);

        writer.writeKey("name");
        writer.writeValue("jobs");

        writer.endObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33,\"name\":\"jobs\"}", out.toString());
    }

    public void test_3() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);
        writer.startObject();

        writer.writeKey("id");
        writer.writeValue(33);

        writer.writeKey("name");
        writer.writeValue("jobs");

        writer.writeKey("children");
        writer.startArray();

        writer.startObject();
        writer.endObject();

        writer.startObject();
        writer.endObject();

        writer.endArray();

        writer.endObject();
        writer.flush();

        Assert.assertEquals("{\"id\":33,\"name\":\"jobs\",\"children\":[{},{}]}", out.toString());
    }

    public void test_4() throws Exception {
        StringWriter out = new StringWriter();

        JSONWriter writer = new JSONWriter(out);

        writer.startArray();

        writer.startObject();
        writer.endObject();

        writer.startObject();
        writer.endObject();

        writer.startArray();
        writer.endArray();
        {
            writer.startArray();

            writer.startArray();
            writer.endArray();

            writer.startArray();
            writer.endArray();

            writer.endArray();
            
            writer.writeValue(1);
        }

        writer.endArray();

        writer.flush();

        Assert.assertEquals("[{},{},[],[[],[]],1]", out.toString());
    }
}
