package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import junit.framework.TestCase;

import java.lang.reflect.Type;

public class Issue3693 extends TestCase {

    public void test_for_issue() throws Exception {
        Model<ModelProperties> model = new Model<ModelProperties>("hello 世界", new ModelProperties("红色", 66));
        String json = JSON.toJSONString(model);
        assertEquals("{\"name\":\"hello 世界\",\"properties\":\"{\\\"color\\\":\\\"红色\\\",\\\"size\\\":66}\"}", json);

        Model<ModelProperties> deserializedModel = JSON.parseObject(json, new TypeReference<Model<ModelProperties>>() {
        });
        assertNotNull(deserializedModel);
        assertEquals("hello 世界", deserializedModel.getName());
        assertNotNull(deserializedModel.getProperties());
        assertEquals("红色", deserializedModel.getProperties().getColor());
        assertEquals(66, deserializedModel.getProperties().getSize());
    }


    static class Model<T> {
        private String name;
        @JSONField(serializeUsing = MyCodec.class, deserializeUsing = MyCodec.class)
        private T properties;

        Model() {
        }

        Model(String name, T properties) {
            this.name = name;
            this.properties = properties;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getProperties() {
            return this.properties;
        }

        public void setProperties(T properties) {
            this.properties = properties;
        }
    }


    static class ModelProperties {
        private String color;
        private int size;

        ModelProperties() {
        }

        ModelProperties(String color, int size) {
            this.color = color;
            this.size = size;
        }

        public String getColor() {
            return this.color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getSize() {
            return this.size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }


    public static class MyCodec implements ObjectSerializer, ObjectDeserializer {
        @Override
        public int getFastMatchToken() {
            return 0;
        }

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
            serializer.write(JSON.toJSONString(object));
        }

        @Override
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String json = parser.parseObject(String.class);
            return JSON.parseObject(json, type);
        }
    }
}
