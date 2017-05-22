package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

import junit.framework.TestCase;

public class SerialWriterStringEncoderTest2 extends TestCase {

    public void test_error_0() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder charsetEncoder = new MockCharsetEncoder2(charset);
     

        Exception error = null;
        char[] chars = "abc".toCharArray();
        try {
            encode(charsetEncoder, chars, 0, chars.length);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder realEncoder = charset.newEncoder();
        
        CharsetEncoder charsetEncoder = new MockCharsetEncoder(charset, realEncoder);

        Exception error = null;
        char[] chars = "abc".toCharArray();
        try {
            encode(charsetEncoder, chars, 0, chars.length);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public static byte[] encode(CharsetEncoder encoder, char[] chars, int off, int len) throws Exception {
        Method method = SerializeWriter.class.getDeclaredMethod("encode", CharsetEncoder.class, char[].class, int.class, int.class);
        method.setAccessible(true);
        return (byte[]) method.invoke(null, encoder, chars, off, len);
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
