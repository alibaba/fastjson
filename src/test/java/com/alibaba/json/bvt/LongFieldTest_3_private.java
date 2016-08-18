package com.alibaba.json.bvt;

import java.util.Random;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class LongFieldTest_3_private extends TestCase {

    public void test_min() throws Exception {
        Random random = new Random();
        Model[] array = new Model[8192];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new Model();
            array[i].value = random.nextLong();
        }

        String text = JSON.toJSONString(array);

        Model[] array2 = JSON.parseObject(text, Model[].class);

        Assert.assertEquals(array.length, array2.length);
        for (int i = 0; i < array.length; ++i) {
            Assert.assertEquals(array[i].value, array2[i].value);
        }
    }


    private static class Model {

        public long value;

    }
}
