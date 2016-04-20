package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerialWriterStringEncoder;

import junit.framework.TestCase;

public class SerialWriterStringEncoderTest2 extends TestCase {

    public void test_error_0() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder charsetEncoder = new MockCharsetEncoder2(charset);
        SerialWriterStringEncoder encoder = new SerialWriterStringEncoder(charset);
        Field field = SerialWriterStringEncoder.class.getDeclaredField("encoder");
        field.setAccessible(true);
        field.set(encoder, charsetEncoder);

        Exception error = null;
        char[] chars = "abc".toCharArray();
        try {
            encoder.encode(chars, 0, chars.length);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder realEncoder = charset.newEncoder();
        
        SerialWriterStringEncoder encoder = new SerialWriterStringEncoder(charset);
        
        CharsetEncoder charsetEncoder = new MockCharsetEncoder(charset, realEncoder);
        Field field = SerialWriterStringEncoder.class.getDeclaredField("encoder");
        field.setAccessible(true);
        field.set(encoder, charsetEncoder);

        Exception error = null;
        char[] chars = "abc".toCharArray();
        try {
            encoder.encode(chars, 0, chars.length);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class MockCharsetEncoder extends CharsetEncoder {
        private CharsetEncoder raw;
        protected MockCharsetEncoder(Charset cs, CharsetEncoder raw){
            super(cs, raw.averageBytesPerChar(), raw.maxBytesPerChar());
            this.raw = raw;
        }

        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            return raw.encode(in, out, false);
        }

        protected CoderResult implFlush(ByteBuffer out) {
            return CoderResult.malformedForLength(1);
            }
    }
    
    public static class MockCharsetEncoder2 extends CharsetEncoder {

        protected MockCharsetEncoder2(Charset cs){
            super(cs, 2, 2);
        }

        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            return CoderResult.OVERFLOW;
        }

    }
}
