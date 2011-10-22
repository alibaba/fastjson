package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_SpitFire_3 extends TestCase {

    public void test_for_SpitFire() {
        Generic<Payload> q = new Generic<Payload>();
        q.setHeader("Sdfdf");
        q.setPayload(new Payload());
        String text = JSON.toJSONString(q, SerializerFeature.WriteClassName);
        System.out.println(text);
        JSON.parseObject(text, Generic.class);
    }

    public static abstract class AbstractDTO {

        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    public static class Payload extends AbstractDTO {

    }

    public static class Generic<T extends AbstractDTO> extends AbstractDTO {

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
