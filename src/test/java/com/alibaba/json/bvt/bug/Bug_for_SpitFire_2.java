package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_SpitFire_2 extends TestCase {

    public void test_for_SpringFire() {
        Generic<String> q = new Generic<String>();
        String text = JSON.toJSONString(q, SerializerFeature.WriteClassName);
        System.out.println(text);
        JSON.parseObject(text, Generic.class);
    }

    public static class Generic<T> {

        String header;
        T      payload;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public T getPayload() {
            return payload;
        }

        public void setPayload(T payload) {
            this.payload = payload;
        }
    }

}
