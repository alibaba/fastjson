package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 14/02/2017.
 */
public class MapSortFieldTest extends TestCase {
    public void test_mapSortField() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 123);
        map.put("name", "wenshao");

        String json = JSON.toJSONString(map, SerializerFeature.MapSortField);
        assertEquals("{\"id\":123,\"name\":\"wenshao\"}", json);
    }
}
