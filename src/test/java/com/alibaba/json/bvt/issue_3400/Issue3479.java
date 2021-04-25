package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

public class Issue3479 {

    @JSONType(seeAlso = {issue3479_Dog.class}, typeKey = "type")
    public static abstract class issue3479_Animal {
    }

    @JSONType(seeAlso = {issue3479_Cat.class}, typeKey = "type")
    //@Getter,add this alone(without setType) do not make it to null
    public static abstract class issue3479_Animal_Data {
        @Getter
        @Setter
        private String type;
    }

    @JSONType(typeName = "dog")
    @Data
    public static class issue3479_Dog extends issue3479_Animal {
        private String dogName;
    }

    @JSONType(typeName = "cat")
    @Data
    public static class issue3479_Cat extends issue3479_Animal_Data {
        private String catName;
    }

    @Test
    public void test1() {
        issue3479_Dog dog = new issue3479_Dog();
        issue3479_Cat cat = new issue3479_Cat();
        dog.dogName = "dog3479";
        cat.catName = "cat3479";

        String text_dog = JSON.toJSONString(dog, SerializerFeature.WriteClassName);
        String text_cat = JSON.toJSONString(cat, SerializerFeature.WriteClassName);
        Assert.assertNotNull(text_dog);
        Assert.assertNotNull(text_cat);
        System.out.println(text_dog);
        System.out.println(text_cat);
        issue3479_Dog dog2 = (issue3479_Dog) JSON.parseObject(text_dog, issue3479_Animal.class);
        issue3479_Cat cat2 = (issue3479_Cat) JSON.parseObject(text_cat, issue3479_Animal_Data.class);
        Assert.assertNotNull(dog2);
        Assert.assertNotNull(cat2);
    }

    @JSONType(seeAlso = {Dollar.class}, typeKey = "country")
    public static abstract class Money{
        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @JSONType(typeName = "America")
    @Data
    public static class Dollar extends Money{
        public int value;
    }

    @Test
    public void test_for_same_name_in_field_and_typeKey() {
        Dollar dollar = new Dollar();
        dollar.value = 12;
        String text_dollar = JSON.toJSONString(dollar, SerializerFeature.WriteClassName);
        System.out.println(text_dollar);
        Dollar dollar1 = (Dollar) JSON.parseObject(text_dollar,Money.class);
        Assert.assertNotNull(dollar1);
        Assert.assertEquals(dollar.value,dollar1.value);

    }
}