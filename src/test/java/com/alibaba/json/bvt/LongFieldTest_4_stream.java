package com.alibaba.json.bvt;

import java.io.StringReader;
import java.util.Random;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.LongFieldTest_3_stream.Model;

import junit.framework.TestCase;

public class LongFieldTest_4_stream extends TestCase {

    public void test_min() throws Exception {
        Random random = new Random();
        Model[] array = new Model[2048];
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

    @JSONType(serialzeFeatures = SerializerFeature.BeanToArray, parseFeatures = Feature.SupportArrayToBean)
    public static class Model {

        public long value;

    }
}
