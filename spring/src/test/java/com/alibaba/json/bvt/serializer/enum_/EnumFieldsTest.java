package com.alibaba.json.bvt.serializer.enum_;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class EnumFieldsTest extends TestCase {
    public void test_enum() throws Exception {
        Model model = new Model();
        model.t1 = Type.A;
        model.t2 = null;
        
        String text = JSON.toJSONString(model, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals("{\"t1\":\"A\",\"t2\":null}", text);
    }
    
    public static class Model {
        public Type t1;
        public Type t2;
    }

    private static enum Type {
                             A, B, C
    }
}
