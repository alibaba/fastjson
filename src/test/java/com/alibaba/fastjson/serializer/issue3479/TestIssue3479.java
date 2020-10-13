package com.alibaba.fastjson.serializer.issue3479;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestIssue3479 {

    @JSONType(seeAlso = {Dog.class, Cat.class}, typeKey = "typeKey")
    public static abstract class Animal {

        private String typeKey;

        public String getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(String typeKey) {
            this.typeKey = typeKey;
        }
    }

    @JSONType(typeName = "dog")
    public static class Dog extends Animal {
        private String dogName;

        public String getDogName() {
            return dogName;
        }

        public void setDogName(String dogName) {
            this.dogName = dogName;
        }

        @Override
        public String toString() {
            return "Dog{" +
                    "dogName='" + dogName + '\'' +
                    '}';
        }
    }

    @JSONType(typeName = "cat")
    public static class Cat extends Animal {
        private String catName;

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        @Override
        public String toString() {
            return "Cat{" +
                    "catName='" + catName + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.dogName = "dog1001";

        String text = JSON.toJSONString(dog, SerializerFeature.WriteClassName);
        System.out.println(text);

        Dog dog2 = (Dog) JSON.parseObject(text, Animal.class);

        System.out.println(dog2);
    }

}
