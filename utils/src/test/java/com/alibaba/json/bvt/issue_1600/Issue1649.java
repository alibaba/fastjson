package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue1649 extends TestCase {
    public void test_for_issue() throws Exception {
        Apple apple = new Apple();
        String json = JSON.toJSONString(apple);
        assertEquals("{\"color\":\"\",\"productCity\":\"\",\"size\":0}", json);
    }

    @JSONType(serialzeFeatures = {SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteMapNullValue})
    public static class Apple {

        // @JSONField(serialzeFeatures = {SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue})
        private String color;
        private String productCity;
        private int size;

        public String getColor() {
            return color;
        }

        public Apple setColor(String color) {
            this.color = color;
            return this;
        }

        public int getSize() {
            return size;
        }

        public Apple setSize(int size) {
            this.size = size;
            return this;
        }

        public String getProductCity() {
            return productCity;
        }

        public Apple setProductCity(String productCity) {
            this.productCity = productCity;
            return this;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }

        public static void main(String[] args) {
            System.out.println(JSON.toJSONString(new Apple()));
        }
    }
}
