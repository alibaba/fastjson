package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest_14 extends TestCase {
    @SuppressWarnings("rawtypes")
    public void test_writer_1() throws Exception {
        StringWriter strOut = new StringWriter();
        SerializeWriter out = new SerializeWriter(strOut, 1);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            Map map = Collections.singletonMap("", "a");
            serializer.write(map);
        } finally {
            out.close();
        }
        Assert.assertEquals("{\"\":\"a\"}", strOut.toString());
    }
    
    
    public void test_writer_2() throws Exception {
        StringWriter strOut = new StringWriter();
        SerializeWriter out = new SerializeWriter(strOut, 1);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            Map map = Collections.singletonMap("ab", "a");
            serializer.write(map);
        } finally {
            out.close();
        }
        Assert.assertEquals("{ab:\"a\"}", strOut.toString());
    }
    
    public void test_writer_3() throws Exception {
        StringWriter strOut = new StringWriter();
        SerializeWriter out = new SerializeWriter(strOut, 1);

        try {
            JSONSerializer serializer = new JSONSerializer(out);

            Map map = Collections.singletonMap("ab\t<", "a");
            serializer.write(map);
        } finally {
            out.close();
        }
        Assert.assertEquals("{\"ab\\t<\":\"a\"}", strOut.toString());
    }
}
