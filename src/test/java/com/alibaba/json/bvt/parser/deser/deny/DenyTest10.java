package com.alibaba.json.bvt.parser.deser.deny;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DenyTest10 extends TestCase {
    ParserConfig config = new ParserConfig();

    protected void setUp() throws Exception {
        assertFalse(config.isAutoTypeSupport());

        Properties properties = new Properties();
        properties.put(ParserConfig.AUTOTYPE_SUPPORT_PROPERTY, "false");
        // -ea -Dfastjson.parser.autoTypeAccept=com.alibaba.json.bvt.parser.deser.DenyTest9
        config.configFromPropety(properties);
    }

    public void test_hashMap() throws Exception {
        Object obj = JSON.parseObject("{\"@type\":\"java.util.HashMap\"}", Object.class, config);
        assertEquals(HashMap.class, obj.getClass());
    }

    public void test_hashMap_weekHashMap() throws Exception {
        Object obj = JSON.parseObject("{\"@type\":\"java.util.WeakHashMap\"}", Object.class, config);
        assertEquals(WeakHashMap.class, obj.getClass());
    }

    public void test_hashMap_concurrentHashMap() throws Exception {
        Object obj = JSON.parseObject("{\"@type\":\"java.util.concurrent.ConcurrentHashMap\"}", Object.class, config);
        assertEquals(ConcurrentHashMap.class, obj.getClass());
    }

    public void test_uuid() throws Exception {
        System.out.println(UUID.randomUUID());
        Object obj = JSON.parseObject("{\"@type\":\"java.util.UUID\",\"val\":\"290c580d-efa3-432b-8475-2655e336232a\"}", Object.class, config);
        assertEquals(UUID.class, obj.getClass());
    }
}
