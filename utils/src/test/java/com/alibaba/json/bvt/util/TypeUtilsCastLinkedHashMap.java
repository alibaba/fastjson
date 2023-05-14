package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.LinkedHashMap;

public class TypeUtilsCastLinkedHashMap extends TestCase {
    public void test_for_cast() throws Exception {
        JSONObject obj = JSON.parseObject("{\"id\":1001,\"name\":\"xxx\"}", Feature.OrderedField);
        obj.toJavaObject(LinkedHashMap.class);
    }
}
