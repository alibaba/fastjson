package com.alibaba.json.bvt.issue_3700;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3725 {
    @Test
    public void test_for_issue_1() {
        short shortValue = 1;
        byte byteValue = 1;
        List<Object> list = new ArrayList<Object>();
        list.add("xxxx");
        list.add(1L);
        list.add(1);
        list.add(shortValue);
        list.add(byteValue);
        list.add(1F);
        list.add(1D);
        String outputjsonWithClassName = JSON.toJSONString(list, SerializerFeature.WriteClassName);
        String outputjsonWithOutClassName = JSON.toJSONString(list);
        String json = "[\"xxxx\",1L,1,1S,1B,1.0F,1.0D]";
        Assert.assertEquals(json, outputjsonWithClassName);

        JSONValidator from2 = JSONValidator.from(outputjsonWithOutClassName);
        boolean validate2 = from2.validate();
        System.out.println(validate2); // true
        Assert.assertTrue(validate2);

        JSONValidator from = JSONValidator.from(json);
        boolean validate = from.validate();
        System.out.println(validate); //false
        String json2 = JSON.toJSONString(list);
        Assert.assertTrue(validate);

    }
}
