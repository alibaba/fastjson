package com.alibaba.json.bvt.support.spring.messaging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.messaging.MappingFastJsonMessageConverter;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


public class MappingFastJsonMessageConverterTest extends TestCase {

    public void test_1() throws Exception {

        MappingFastJsonMessageConverter converter = new MappingFastJsonMessageConverter();

        Assert.assertNotNull(converter.getFastJsonConfig());
        converter.setFastJsonConfig(new FastJsonConfig());

        VO p = new VO();
        p.setId(1);

        String pstr = JSON.toJSONString(p);

        System.out.println(pstr);

        TestMessage message = new TestMessage(pstr);

        // test fromMessage/convertFromInternal
        VO vo = (VO) converter.fromMessage(message, VO.class);
        Assert.assertEquals(1, vo.getId());

        // test toMessage/convertToInternal
        Message message1 = converter.toMessage(vo, null);
        System.out.println(message1.getPayload());
        Assert.assertEquals("{\"id\":1}", new String((byte[]) message1.getPayload()));

//		// test toMessage/convertToInternal
        Message message2 = converter.toMessage("{\"id\":1}", null);
        System.out.println(message2.getPayload());
        Assert.assertEquals("{\"id\":1}", new String((byte[]) message2.getPayload()));

        converter.setSerializedPayloadClass(String.class);

        // test toMessage/convertToInternal
        Message message3 = converter.toMessage(vo, null);
        System.out.println(message3.getPayload());
        Assert.assertEquals("{\"id\":1}", message3.getPayload());

//		// test toMessage/convertToInternal
        Message message4 = converter.toMessage("{\"id\":1}", null);
        System.out.println(message4.getPayload());
        Assert.assertEquals("{\"id\":1}", message4.getPayload());
    }

    public static class TestMessage<T> implements Message<T> {

        private T payload;

        public TestMessage(T payload) {
            this.payload = payload;
        }

        @Override
        public T getPayload() {
            return (T) payload;
        }

        @Override
        public MessageHeaders getHeaders() {
            return null;
        }
    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}