package com.alibaba.json.bvt.annotation;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import junit.framework.TestCase;

public class SerializeUsingTest extends TestCase {

    public void test_annotation() throws Exception {
        Model model = new Model();
        model.value = 100;
        String json = JSON.toJSONString(model);
        Assert.assertEquals("{\"value\":\"100元\"}", json);
    }

    public static class Model {

        @JSONField(serializeUsing = ModelValueSerializer.class)
        public int value;
    }

    public static class ModelValueSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            Integer value = (Integer) object;
            String text = value + "元";
            serializer.write(text);
        }
    }
}
