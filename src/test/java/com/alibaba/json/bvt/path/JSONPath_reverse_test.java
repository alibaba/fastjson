package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_reverse_test extends TestCase
{
    public void test_reserve() throws Exception {
        JSONObject object = JSON.parseObject("{\"id\":1001,\"name\":\"ljw\",\"age\":50}");

        assertEquals("{\"id\":1001,\"name\":\"ljw\"}", JSONPath.reserve(object, "id", "name").toString());
        assertEquals("{\"name\":\"ljw\",\"id\":1001}", JSONPath.reserve(object, "name", "id").toString());
    }
}
