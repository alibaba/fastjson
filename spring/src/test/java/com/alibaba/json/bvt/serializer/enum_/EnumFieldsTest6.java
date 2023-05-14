package com.alibaba.json.bvt.serializer.enum_;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class EnumFieldsTest6 extends TestCase {
    public void test_enum() throws Exception {
        Model model = new Model();
        model.types = new Object[]{Type.A, null};
        
        String text = JSON.toJSONString(model, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"types\":[\"A\",null]}", text);
    }
    
    public static class Model {
        public Object[] types;
    }

    private static enum Type {
                             A, B, C
    }
}
