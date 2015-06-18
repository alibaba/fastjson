package com.alibaba.fastjson.serializer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;

public class SerializeWriterTest {

    private final Logger logger = Logger.getLogger(SerializeWriterTest.class.getSimpleName());

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final SerializeWriter writer = new SerializeWriter(new OutputStreamWriter(baos));

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWriteLiteBasicStr() throws UnsupportedEncodingException {
        String targetStr = new String(IOUtils.DIGITS);
        this.doTestWrite(targetStr);
    }

    private String doTestWrite(String input) throws UnsupportedEncodingException {
        writer.writeString(input, (char) 0);
        writer.flush();
        String result = this.baos.toString("UTF-8");

        Assert.assertEquals(input, JSON.parse(result));

        logger.info(result);

        return result;
    }

    @Test
    public void testWriteLiteSpecilaStr() throws UnsupportedEncodingException {
        this.doTestWrite(this.makeSpecialChars());
    }

    private String makeSpecialChars() {
        StringBuilder strBuilder = new StringBuilder(128);
        for (char c = 128; c <= 160; c++) {
            strBuilder.append(c);
        }
        return strBuilder.toString();
    }

    @Test
    public void testWriteLargeBasicStr() throws UnsupportedEncodingException {
        String tmp = new String(IOUtils.DIGITS);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            builder.append(tmp);
        }
        this.doTestWrite(builder.toString());
    }

    @Test
    public void testWriteLargeSpecialStr() throws UnsupportedEncodingException {

        String tmp = this.makeSpecialChars();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            builder.append(tmp);
        }
        this.doTestWrite(builder.toString());
    }
}
