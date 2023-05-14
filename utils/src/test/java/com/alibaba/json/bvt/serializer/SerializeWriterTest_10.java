package com.alibaba.json.bvt.serializer;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.serializer.SerializeWriter;

public class SerializeWriterTest_10 extends TestCase {

    public void test_erro_0() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Exception error = null;
        try {
            out.write(new char[0], -1, 0);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }

    public void test_erro_1() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Exception error = null;
        try {
            out.write(new char[0], 1, 0);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }

    public void test_erro_2() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Exception error = null;
        try {
            out.write(new char[0], 0, -1);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }

    public void test_erro_3() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Exception error = null;
        try {
            out.write(new char[] { '0', '0' }, 1, 2);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }
    
    public void test_erro_4() throws Exception {
        SerializeWriter out = new SerializeWriter();
        Exception error = null;
        try {
            out.write(new char[] { '0', '0' }, 1, Integer.MAX_VALUE);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
        out.close();
    }
}
