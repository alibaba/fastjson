package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 2016/10/14.
 */
public class ChineseSpaceTest extends TestCase {
    public void test_for_chinese_space() throws Exception {
        Map<String, String> map = Collections.singletonMap("v", " ");
        String json = JSON.toJSONString(map);
        assertEquals("{\"v\":\" \"}", json);

        JSONObject jsonObject = JSON.parseObject(json);
        assertEquals(map.get("v"), jsonObject.get("v"));
    }
}
