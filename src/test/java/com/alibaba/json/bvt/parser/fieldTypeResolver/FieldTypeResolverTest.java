package com.alibaba.json.bvt.parser.fieldTypeResolver;

import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;

import junit.framework.TestCase;

public class FieldTypeResolverTest extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"item_0\":{},\"item_1\":{}}";
        
        FieldTypeResolver fieldResolver = new FieldTypeResolver() {

            public Type resolve(Object object, String fieldName) {
                if (fieldName.startsWith("item_")) {
                    return Item.class;
                }
                
                return null;
            }
        };
        
        JSONObject jsonObject = JSON.parseObject(text, JSONObject.class, fieldResolver);
        Assert.assertTrue(jsonObject.get("item_0") instanceof Item);
    }

    public static class Item {
        
    }
}
