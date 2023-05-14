package com.alibaba.json.bvt.serializer.stream;

import java.io.StringWriter;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class StreamWriterTest_writeString extends TestCase {

    public void test_0() throws Exception {
        StringWriter out = new StringWriter();

        SerializeWriter writer = new SerializeWriter(out, 10);
        Assert.assertEquals(10, writer.getBufferLength());

        int blockSize = 21;
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            char[] chars = new char[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                chars[i] = ch;
            }
            writer.write(new String(chars), 0, chars.length);
        }
        writer.close();

        String text = out.toString();
        Assert.assertEquals(26 * blockSize, text.length());

        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < blockSize; ++j) {
                Assert.assertEquals(text.charAt(i * blockSize + j), (char) ('a' + i));
            }
        }
    }
}
