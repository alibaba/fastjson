package com.alibaba.json.bvt.ref;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wenshao on 16/8/23.
 */
public class RefTest24
        extends TestCase {
    public void test_ref() throws Exception {
        ByteCodeDO codeDO = new ByteCodeDO();
        codeDO.id = 1001;

        Map<String, Object> data = new LinkedHashMap();
        Map<String, Object> m1 = new LinkedHashMap();
        m1.put("23299685@47", codeDO);
        Map<String, Object> m2 = new LinkedHashMap();
        m2.put("23299685@47", codeDO);
        data.put("com.alibaba.extAppConfigs", m1);
        data.put("com.alibaba.appConfigs", m2);
        String str = JSON.toJSONString(data);

        Object o = JSON.parseObject(str, Feature.OrderedField);

        assertEquals(str, JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
    }

    public static class ByteCodeDO {
        public int id;
    }

}
