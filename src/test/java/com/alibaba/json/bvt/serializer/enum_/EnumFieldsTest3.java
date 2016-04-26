package com.alibaba.json.bvt.serializer.enum_;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class EnumFieldsTest3 extends TestCase {
    public void test_enum() throws Exception {
        Model model = new Model();
        model.types.add(Type.A);
        model.types.add(null);
        
        String text = JSON.toJSONString(model, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"types\":[\"A\",null]}", text);
    }
    
    public static class Model {
        public List<Type> types = new ArrayList<Type>();
    }

    private static enum Type {
                             A, B, C
    }
}
