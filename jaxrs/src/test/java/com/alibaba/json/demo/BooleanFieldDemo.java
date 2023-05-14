package com.alibaba.json.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 15/02/2017.
 */
public class BooleanFieldDemo extends TestCase {
    public void test_boolean() throws Exception {
        Model model = new Model();
        String json = JSON.toJSONString(model, SerializerFeature.IgnoreNonFieldGetter);
        System.out.println(json);


    }

    public static class Model {


        public boolean isAvailable() {
            return true;
        }
    }
}
