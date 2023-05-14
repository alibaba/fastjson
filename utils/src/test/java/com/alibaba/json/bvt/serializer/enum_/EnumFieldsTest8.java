package com.alibaba.json.bvt.serializer.enum_;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;

import junit.framework.TestCase;

public class EnumFieldsTest8 extends TestCase {

    public void test_enum() throws Exception {
        Model model = new Model();
        model.t1 = Type.A;
        model.t2 = null;
        
        ValueFilter valueFilter = new ValueFilter() {

            public Object process(Object object, String name, Object value) {
                return value;
            }
            
        };
        
        SerializeFilter[] filters = {valueFilter};
        String text = JSON.toJSONString(model, SerializeConfig.getGlobalInstance(), // 
                                        filters, 
                                        null,
                                        0, // 
                                        SerializerFeature.QuoteFieldNames, // 
                                        SerializerFeature.BrowserCompatible, // 
                                        SerializerFeature.WriteEnumUsingName);
        Assert.assertEquals("{\"t1\":\"A\"}", text);
    }

    public static class Model {

        public Type t1;
        public Type t2;
    }

    public static enum Type {
                             A, B, C
    }
}
