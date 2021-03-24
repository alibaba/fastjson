package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import junit.framework.TestCase;
import org.junit.Before;

import java.io.StringWriter;

/**
 * @Author ：Nanqi
 * @Date ：Created in 14:21 2020/7/5
 */
public class Issue3323 extends TestCase {
    private FastJsonRedisSerializer<Simple> serializer;

    @Before
    public void setUp() {
        this.serializer = new FastJsonRedisSerializer<Simple>(Simple.class);
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteNullOfNullObject);
        this.serializer.setFastJsonConfig(config);
    }

    public void test_for_issue() throws Exception {
        // String
        String nullString = JSON.toJSONString(null, SerializerFeature.WriteNullOfNullObject);
        assertNull(nullString);

        nullString = JSON.toJSONString(null);
        assertEquals("null", nullString);

        // bytes
        byte[] bytes = JSON.toJSONBytes(null, SerializerFeature.WriteNullOfNullObject);
        assertNull(bytes);

        String bytesString = new String(JSON.toJSONBytes(null));
        assertEquals("null", bytesString);

        // writer
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            JSON.writeJSONString(writer, null, SerializerFeature.WriteNullOfNullObject);
            String text = writer.toString();
            assertEquals("", text);
        } finally {
            writer.close();
        }

        try {
            writer = new StringWriter();
            JSON.writeJSONString(writer, null);
            String text = writer.toString();
            assertEquals("null", text);
        } finally {
            writer.close();
        }

        // FastJson
        byte[] serializeSimple = serializer.serialize(null);
        assertNull(serializeSimple);

        serializer.getFastJsonConfig().setSerializerFeatures();
        serializeSimple = serializer.serialize(null);
        assertTrue(serializeSimple.length == 0);
    }

    static class Simple {
        private int age;

        private Integer money;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Integer getMoney() {
            return money;
        }

        public void setMoney(Integer money) {
            this.money = money;
        }
    }
}
