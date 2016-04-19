package com.alibaba.json.bvt.parser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.junit.Assert;

import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.UTF8Decoder;

import junit.framework.TestCase;

public class TestUTF8_4 extends TestCase {

    public void test_error_0() throws Exception {
        byte[] bytes = decodeHex("FBB0B0B0B0B0".toCharArray());

        UTF8Decoder charsetDecoder = new UTF8Decoder();
        int scaleLength = (int) (bytes.length * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = IOUtils.getChars(scaleLength);

        CharBuffer charBuffer = CharBuffer.wrap(chars);

        Exception error = null;
        try {
            IOUtils.decode(charsetDecoder, ByteBuffer.wrap(bytes), charBuffer);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        byte[] bytes = decodeHex("FCB0B0B0B0B0".toCharArray());

        UTF8Decoder charsetDecoder = new UTF8Decoder();
        int scaleLength = (int) (bytes.length * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = IOUtils.getChars(scaleLength);

        CharBuffer charBuffer = CharBuffer.wrap(chars);

        Exception error = null;
        try {
            IOUtils.decode(charsetDecoder, ByteBuffer.wrap(bytes), charBuffer);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        byte[] bytes = decodeHex("C0".toCharArray());

        UTF8Decoder charsetDecoder = new UTF8Decoder();
        int scaleLength = (int) (bytes.length * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = IOUtils.getChars(scaleLength);

        CharBuffer charBuffer = CharBuffer.wrap(chars);

        Exception error = null;
        try {
            IOUtils.decode(charsetDecoder, ByteBuffer.wrap(bytes), charBuffer);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            UTF8Decoder.malformedN(null, 5);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        byte[] bytes = decodeHex("20".toCharArray());

        UTF8Decoder charsetDecoder = new UTF8Decoder();
        char[] chars = new char[0];

        CharBuffer charBuffer = CharBuffer.wrap(chars);

        Exception error = null;
        try {
            IOUtils.decode(charsetDecoder, ByteBuffer.wrap(bytes), charBuffer);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static byte[] decodeHex(char[] data) throws Exception {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    protected static int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
