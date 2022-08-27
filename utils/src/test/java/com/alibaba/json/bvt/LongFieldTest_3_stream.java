package com.alibaba.json.bvt;

import java.io.StringReader;
import java.util.Random;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;

import junit.framework.TestCase;

public class LongFieldTest_3_stream extends TestCase {

    public void test_min() throws Exception {
        Random random = new Random();
        Model[] array = new Model[8192];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new Model();
            array[i].value = random.nextLong();
        }

        String text = JSON.toJSONString(array);

        JSONReader reader = new JSONReader(new StringReader(text));
        Model[] array2 = reader.readObject(Model[].class);

        Assert.assertEquals(array.length, array2.length);
        for (int i = 0; i < array.length; ++i) {
            Assert.assertEquals(array[i].value, array2[i].value);
        }
        reader.close();
    }


    public static class Model {

        public long value;

    }
}
