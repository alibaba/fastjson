package com.alibaba.json.bvt.annotation;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import junit.framework.TestCase;

public class CustomSerializerTest extends TestCase {
    public void test_0() throws Exception {
        Model model = new Model();
        model.id = 1001;
        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"ID\":1001}", text);
    }
    
    @JSONType(serializer=ModelSerializer.class)
    public static class Model {
        public int id;
    }
    
    public static class ModelSerializer implements ObjectSerializer {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            Model model = (Model) object;
            SerializeWriter out = serializer.getWriter();
            out.writeFieldValue('{', "ID", model.id);
            out.write('}');
        }
        
    }
}
