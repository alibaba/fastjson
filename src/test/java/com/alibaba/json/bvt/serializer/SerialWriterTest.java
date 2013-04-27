package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerialWriterTest extends TestCase {

    public void test_0() throws Exception {
        for (int i = 0; i < 3; ++i) {
            {
                String text = "abc";
                String charset = "UTF-8";
                SerializeWriter writer = new SerializeWriter();
                writer.append(text);
                byte[] bytes = writer.toBytes(charset);
                Assert.assertArrayEquals(text.getBytes(charset), bytes);
            }
            
            {
                String text = "efg";
                String charset = "UTF-8";
                SerializeWriter writer = new SerializeWriter();
                writer.append(text);
                byte[] bytes = writer.toBytes(charset);
                Assert.assertArrayEquals(text.getBytes(charset), bytes);
            }
        }
    }
}
