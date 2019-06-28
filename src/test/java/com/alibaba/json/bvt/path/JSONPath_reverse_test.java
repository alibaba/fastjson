package com.alibaba.json.bvt.path;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.TestCase;

public class JSONPath_reverse_test extends TestCase
{
    public void test_reserve() throws Exception {
        JSONObject object = JSON.parseObject("{\"id\":1001,\"name\":\"ljw\",\"age\":50}");

        assertEquals("[1001,\"ljw\"]", JSONPath.reserveToArray(object, "id", "name").toString());
        assertEquals("[\"ljw\",1001]", JSONPath.reserveToArray(object, "name", "id").toString());
        assertEquals("[\"ljw\",[\"ljw\",1001,50]]", JSONPath.reserveToArray(object, "name", "*").toString());
    }

    public void test_reserve2() throws Exception {
        JSONObject object = JSON.parseObject("{\"id\":1001,\"name\":\"ljw\",\"age\":50}");

        assertEquals("{\"id\":1001,\"name\":\"ljw\"}", JSONPath.reserveToObject(object, "id", "name").toString());
        assertEquals("{\"name\":\"ljw\",\"id\":1001}", JSONPath.reserveToObject(object, "name", "id").toString());
    }


    public void test_reserve3() throws Exception {
        JSONObject object = JSON.parseObject("{\"player\":{\"id\":1001,\"name\":\"ljw\",\"age\":50}}");

        assertEquals("{\"player\":{\"name\":\"ljw\",\"id\":1001}}", JSONPath.reserveToObject(object, "player.id", "player.name").toString());
        assertEquals("{\"player\":{\"name\":\"ljw\",\"id\":1001}}", JSONPath.reserveToObject(object, "player.name", "player.id", "ab.c").toString());
    }


}
