package com.alibaba.json.bvt.annotation;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class JsonSeeAlsoTest extends TestCase {

    public void test_seeAlso_dog() throws Exception {
        Dog dog = new Dog();
        dog.dogName = "dog1001";

        String text = JSON.toJSONString(dog, SerializerFeature.WriteClassName);
        Assert.assertEquals("{\"@type\":\"dog\",\"dogName\":\"dog1001\"}", text);

        Dog dog2 = (Dog) JSON.parseObject(text, Animal.class);

        Assert.assertEquals(dog.dogName, dog2.dogName);
    }

    public void test_seeAlso_cat() throws Exception {
        Cat cat = new Cat();
        cat.catName = "cat2001";

        String text = JSON.toJSONString(cat, SerializerFeature.WriteClassName);
        Assert.assertEquals("{\"@type\":\"cat\",\"catName\":\"cat2001\"}", text);

        Cat cat2 = (Cat) JSON.parseObject(text, Animal.class);

        Assert.assertEquals(cat.catName, cat2.catName);
    }
    
    public void test_seeAlso_tidy() throws Exception {
        Tidy tidy = new Tidy();
        tidy.dogName = "dog2001";
        tidy.tidySpecific = "tidy1001";

        String text = JSON.toJSONString(tidy, SerializerFeature.WriteClassName);
        Assert.assertEquals("{\"@type\":\"tidy\",\"dogName\":\"dog2001\",\"tidySpecific\":\"tidy1001\"}", text);

        Tidy tidy2 = (Tidy) JSON.parseObject(text, Animal.class);

        Assert.assertEquals(tidy.dogName, tidy2.dogName);
    }

    @JSONType(seeAlso = { Dog.class, Cat.class })
    public static class Animal {
    }

    @JSONType(typeName = "dog", seeAlso={Tidy.class, Labrador.class})
    public static class Dog extends Animal {
        public String dogName;
    }

    @JSONType(typeName = "cat")
    public static class Cat extends Animal {
        public String catName;
    }

    @JSONType(typeName = "tidy")
    public static class Tidy extends Dog {
        public String tidySpecific;
    }
    
    @JSONType(typeName = "labrador")
    public static class Labrador extends Dog {
        public String tidySpecific;
    }
}
