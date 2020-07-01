package com.alibaba.json.test.benchmark.decode;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.alibaba.json.test.benchmark.BenchmarkCase;
import com.alibaba.json.test.codec.Codec;

import data.media.MediaContent;

public class EishayDecode2Bytes extends BenchmarkCase {

    public final static EishayDecode2Bytes instance = new EishayDecode2Bytes();

    private final byte[]                  bytes;
    private final char[]                  chars;
    private final String text;

    public byte[] getBytes() {
        return bytes;
    }

    public char[] getChars() {
        return chars;
    }
    
    public String getText() {
        return text;
    }

    public EishayDecode2Bytes(){
        super("EishayDecode2-Byte[]");

        try {
            String resource = "data/media.2.json";
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            text = IOUtils.toString(is);
            is.close();
            
            chars = (text + " ").toCharArray();
            bytes = text.getBytes("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void execute(Codec codec) throws Exception {
        codec.decodeObject(bytes, MediaContent.class);
    }

}
