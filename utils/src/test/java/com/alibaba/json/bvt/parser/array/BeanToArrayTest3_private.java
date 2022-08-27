package com.alibaba.json.bvt.parser.array;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class BeanToArrayTest3_private extends TestCase {

    public void test_array() throws Exception {
        Model model = new Model();
        model.item = new Item();
        model.item.id = 1001;
        
        String text = JSON.toJSONString(model);
        
        Assert.assertEquals("{\"item\":[1001]}", text);
        
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model2.item.id, model.item.id);
    }
   
    private static class Model {
        @JSONField(serialzeFeatures=SerializerFeature.BeanToArray, parseFeatures=Feature.SupportArrayToBean)
        public Item item;
    }
    
    private static class Item {
        public int id;
    }
}
