package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue3652 {

    @Test
    public void test_SerializeConfig_different_Class_Annotation() {
        Object[] models = new Object[]{
                new Model1("hello,world"),
                new Model2("hello,world"),
                new Model3("hello,world"),
                new Model4("hello,world"),
        };
        for (int i = 0; i < 4; i++) {
            String[] toStrings = new String[PropertyNamingStrategy.values().length];
            for (int j = 0; j < toStrings.length; j++) {
                SerializeConfig config = new SerializeConfig();
                config.propertyNamingStrategy = PropertyNamingStrategy.values()[j];
                toStrings[j] = JSON.toJSONString(models[i], config);
            }
            for (int j = 1; j < toStrings.length; j++) {
                Assert.assertEquals(toStrings[j], toStrings[j - 1]);
                System.out.println(toStrings[j - 1]);
            }
        }
    }

    @Test
    public void test_different_Class_Annotation() {
        Object[] models = new Object[]{
                new Model1("hello,world"),
                new Model2("hello,world"),
                new Model3("hello,world"),
                new Model4("hello,world"),
        };
        String[] JsonStrings = new String[]{
                "{\"goodBoy\":\"hello,world\"}",
                "{\"GoodBoy\":\"hello,world\"}",
                "{\"good_boy\":\"hello,world\"}",
                "{\"good-boy\":\"hello,world\"}"};
        /* PS: Order is
         CamelCase,
         PascalCase,
         SnakeCase,
         KebabCase,*/
        for (int i = 0; i < models.length; i++) {
            String[] toStrings = new String[PropertyNamingStrategy.values().length];
            toStrings[i] = JSON.toJSONString(models[i]);
            Assert.assertEquals(JsonStrings[i], toStrings[i]);
        }
    }

    @JSONType(naming = PropertyNamingStrategy.CamelCase)
    @Data
    @AllArgsConstructor
    public class Model1 {
        private String goodBoy;
    }

    @JSONType(naming = PropertyNamingStrategy.PascalCase)
    @Data
    @AllArgsConstructor
    public class Model2 {
        private String goodBoy;
    }

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    @Data
    @AllArgsConstructor
    public class Model3 {
        private String goodBoy;
    }

    @JSONType(naming = PropertyNamingStrategy.KebabCase)
    @Data
    @AllArgsConstructor
    public class Model4 {
        private String goodBoy;
    }

}
