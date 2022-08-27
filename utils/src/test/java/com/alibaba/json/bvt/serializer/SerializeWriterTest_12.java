package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.io.Writer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest_12 extends TestCase {

    public void test_erro_0() throws Exception {
        SerializeWriter out = new SerializeWriter(new ErrorWriter());
        Exception error = null;
        try {
            out.flush();
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }

   public static class ErrorWriter extends Writer {

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new IOException();
    }

    @Override
    public void flush() throws IOException {
        throw new IOException();
    }

    @Override
    public void close() throws IOException {
        
    }
       
   }
}
