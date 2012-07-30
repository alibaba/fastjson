package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.serializer.*;
import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;

public class FieldSerializerTest2 extends TestCase {

    public void test_writeNull() throws Exception {
        String text = toJSONString(new Entity());
        Assert.assertEquals("{\"value\":\"xxx\"}", text);
    }
    
    public static final String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.getPropertyFilters().add(new PropertyFilter() {
                
                public boolean apply(Object source, String name, DelayObject value) {
                    if ("id".equals(name)) {
                        return false;
                    }
                    return true;
                }
            });
            serializer.getNameFilters().add(new NameFilter() {

                public String process(Object source, String name, DelayObject value) {
                    if ("v".equals(name)) {
                        return "value";
                    }
                    return name;
                }
                
            });
            serializer.getValueFilters().add(new ValueFilter() {
                
                public Object process(Object source, String name, Object value) {
                    if ("v".endsWith(name)) {
                        return "xxx";
                    }
                    
                    return value;
                }
            });
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }

    private static class Entity {

        private int id;
        @JSONField(name = "v", serialzeFeatures = { SerializerFeature.WriteMapNullValue })
        private String        value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
