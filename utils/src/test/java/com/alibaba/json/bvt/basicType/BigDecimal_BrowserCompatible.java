package com.alibaba.json.bvt.basicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

public class BigDecimal_BrowserCompatible extends TestCase {
    public void test_for_issue() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id1", new BigDecimal("-9223370018640066466"));
        map.put("id2", new BigDecimal("9223370018640066466"));
        map.put("id3", new BigDecimal("100"));
        assertEquals("{\"id1\":\"-9223370018640066466\",\"id2\":\"9223370018640066466\",\"id3\":100}",
                JSON.toJSONString(map, SerializerFeature.BrowserCompatible)
        );
    }
}
