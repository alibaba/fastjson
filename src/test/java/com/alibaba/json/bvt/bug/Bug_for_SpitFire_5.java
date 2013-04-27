package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_SpitFire_5 extends TestCase {

    public void test_for_SpitFire() {
        Generic<Payload> q = new Generic<Payload>();
        q.setHeader(new Header());
        q.setPayload(new Payload());
        String text = JSON.toJSONString(q, SerializerFeature.WriteClassName);
        System.out.println(text);
        Generic<Payload> o = (Generic<Payload>) JSON.parseObject(text, q.getClass());
        Assert.assertNotNull(o.getPayload());
    }

    public static abstract class AbstractDTO {
    }

    public static class Header {

    }

    public static class Payload extends AbstractDTO {

        private String field;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }

    public static class Generic<T extends AbstractDTO> extends AbstractDTO {

        Header header;
        T      payload;

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
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
