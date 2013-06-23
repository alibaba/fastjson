package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class IntArrayEncodeTest extends TestCase {

    public void test_0_s() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(new int[] { 0, 1 });

        Assert.assertEquals("[0,1]", out.toString());
    }

    public void test_1_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(new int[] {});

        Assert.assertEquals("[]", out.toString());
    }

    public void test_2_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(new int[] { -2147483648 });

        Assert.assertEquals("[-2147483648]", out.toString());
    }

    public void test_3_s() throws Exception {
        SerializeWriter out = new SerializeWriter();

        JSONSerializer serializer = new JSONSerializer(out);

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int len = 1000;
        int[] array = new int[len];
        for (int i = 0; i < array.length; ++i) {
            array[i] = i;
            if (i != 0) {
                sb.append(',');
            }
            sb.append(i);
        }
        sb.append(']');

        serializer.write(array);

        Assert.assertEquals(sb.toString(), out.toString());
    }
}
