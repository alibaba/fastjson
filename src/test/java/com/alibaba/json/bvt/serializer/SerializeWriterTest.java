package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.StringWriter;

public class SerializeWriterTest extends TestCase {

    public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.write('a');
        out.write('b');
        out.write('c');
        Assert.assertEquals("abc", out.toString());

        StringWriter writer = new StringWriter();
        out.writeTo(writer);
        Assert.assertEquals("abc", writer.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.write((int) 'a');
        out.write((int) 'b');
        out.write((int) 'c');
        out.write(new char[0], 0, 0);
        Assert.assertEquals("abc", out.toString());

        StringWriter writer = new StringWriter();
        out.writeTo(writer);
        Assert.assertEquals("abc", writer.toString());

        out.expandCapacity(128);
    }

    public void test_12() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.append("abc");
        Assert.assertEquals("abc", out.toString());
        Assert.assertEquals(3, out.toCharArray().length);
        Assert.assertEquals(3, out.size());
        out.reset();
        Assert.assertEquals("", out.toString());
        Assert.assertEquals(0, out.toCharArray().length);
        Assert.assertEquals(0, out.size());
        out.writeInt(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.toString(Integer.MIN_VALUE), out.toString());
        out.flush();
        out.close();
    }

    public void test_13() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeInt(Integer.MIN_VALUE);
        Assert.assertEquals(Integer.toString(Integer.MIN_VALUE), out.toString());
    }

    public void test_13_long() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeLong(Long.MIN_VALUE);
        Assert.assertEquals(Long.toString(Long.MIN_VALUE), out.toString());
    }

    public void test_13_long_browser() throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.BrowserCompatible);
        out.writeLong(Long.MIN_VALUE + 1);
        Assert.assertEquals("\"" + Long.toString(Long.MIN_VALUE + 1) + "\"", out.toString());
    }

    public void test_13_long_browser2() throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.BrowserCompatible);
        out.writeLong(Long.MIN_VALUE);
        Assert.assertEquals("\"" + Long.toString(Long.MIN_VALUE) + "\"", out.toString());
    }

    public void test_14() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeInt(Integer.MAX_VALUE);
        Assert.assertEquals(Integer.toString(Integer.MAX_VALUE), out.toString());
    }

    public void test_14_long() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeLong(Long.MAX_VALUE);
        Assert.assertEquals(Long.toString(Long.MAX_VALUE), out.toString());
    }

    public void test_15() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeIntAndChar(Integer.MAX_VALUE, ',');
        Assert.assertEquals(Integer.toString(Integer.MAX_VALUE) + ",", out.toString());
    }

    public void test_15_long() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeLongAndChar(Long.MAX_VALUE, ',');
        Assert.assertEquals(Long.toString(Long.MAX_VALUE) + ",", out.toString());
    }

    public void test_16() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeIntAndChar(Integer.MIN_VALUE, ',');
        Assert.assertEquals(Integer.toString(Integer.MIN_VALUE) + ",", out.toString());
    }

    public void test_16_long() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.writeLongAndChar(Long.MIN_VALUE, ',');
        Assert.assertEquals(Long.toString(Long.MIN_VALUE) + ",", out.toString());
    }

    public void test_16_long_browser() throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.BrowserCompatible);
        out.writeLongAndChar(Long.MIN_VALUE + 1, ',');
        Assert.assertEquals("\"" + Long.toString(Long.MIN_VALUE + 1) + "\",", out.toString());
    }

    public void test_16_long_browser2() throws Exception {
        SerializeWriter out = new SerializeWriter(SerializerFeature.BrowserCompatible);
        out.writeLongAndChar(Long.MIN_VALUE, ',');
        Assert.assertEquals("\"" + Long.toString(Long.MIN_VALUE) + "\",", out.toString());
    }

    public void test_17() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.append(null);
        Assert.assertEquals("null", out.toString());
    }

    public void test_18() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.append(null, 0, 4);
        Assert.assertEquals("null", out.toString());
    }

    public void test_19() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.append("abcd", 0, 4);
        Assert.assertEquals("abcd", out.toString());
    }

    public void test_20() throws Exception {
        SerializeWriter out = new SerializeWriter(1);
        out.write("abcd".toCharArray(), 0, 4);
        Assert.assertEquals("abcd", out.toString());
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            new SerializeWriter(-1);
        } catch (IllegalArgumentException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        Exception error = null;
        try {
            SerializeWriter out = new SerializeWriter(16);
            out.write(new char[0], -1, 0);
        } catch (IndexOutOfBoundsException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        Exception error = null;
        try {
            SerializeWriter out = new SerializeWriter(16);
            out.write(new char[0], 2, 0);
        } catch (IndexOutOfBoundsException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        Exception error = null;
        try {
            SerializeWriter out = new SerializeWriter(16);
            out.write(new char[0], 0, -1);
        } catch (IndexOutOfBoundsException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_5() throws Exception {
        Exception error = null;
        try {
            SerializeWriter out = new SerializeWriter(16);
            out.write(new char[0], 0, 1);
        } catch (IndexOutOfBoundsException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_6() throws Exception {
        Exception error = null;
        try {
            SerializeWriter out = new SerializeWriter(16);
            out.write("abcdefg".toCharArray(), 1, 1 + Integer.MAX_VALUE);
        } catch (IndexOutOfBoundsException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

}
