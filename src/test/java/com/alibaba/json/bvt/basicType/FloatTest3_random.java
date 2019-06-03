package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class FloatTest3_random extends TestCase {
    public void test_ran() throws Exception {
        Random rand = new Random();

        for (int i = 0; i < 1000 * 1000 * 1; ++i) {
            float val = rand.nextFloat();

            String str = JSON.toJSONString(new Model(val));
            Model m = JSON.parseObject(str, Model.class);

            assertEquals(val, m.value);
        }
    }

    public void test_ran_2() throws Exception {
        Random rand = new Random();

        for (int i = 0; i < 1000 * 1000 * 1; ++i) {
            float val = rand.nextFloat();

            String str = JSON.toJSONString(new Model(val), SerializerFeature.BeanToArray);
            Model m = JSON.parseObject(str, Model.class, Feature.SupportArrayToBean);

            assertEquals(val, m.value);
        }
    }

    public void test_ran_3() throws Exception {
        Random rand = new Random();

        for (int i = 0; i < 1000 * 1000 * 1; ++i) {
            float val = rand.nextFloat();

            String str = JSON.toJSONString(Collections.singletonMap("val", val));
            float val2 = JSON.parseObject(str).getFloatValue("val");

            assertEquals(val, val2);
        }
    }

    public void test_ran_4() throws Exception {
        Random rand = new Random();

        for (int i = 0; i < 1000 * 1000 * 1; ++i) {
            float val = rand.nextFloat();

            HashMap map = new HashMap();
            map.put("val", val);
            String str = JSON.toJSONString(map);
            float val2 = JSON.parseObject(str).getFloatValue("val");

            assertEquals(val, val2);
        }
    }

    public static class Model {
        public float value;

        public Model() {

        }

        public Model(float value) {
            this.value = value;
        }
    }
}
