package com.alibaba.fastjson.serializer.issue3821;

import com.alibaba.fastjson.JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JSONParseMapperTest {

    @Test(timeout = 4000)
    public void testIfKeyIsString() throws JsonProcessingException {
        Map<Integer, String> data = new HashMap<Integer, String>() {{ put(1, "2"); }};

        Assert.assertEquals(JSON.toJSONString(data),"{\"1\":\"2\"}");

        Gson gson = new GsonBuilder().create();
        Assert.assertEquals(gson.toJson(data, Map.class),"{\"1\":\"2\"}");

        ObjectMapper jackson = new ObjectMapper();
        Assert.assertEquals(jackson.writeValueAsString(data),"{\"1\":\"2\"}");
    }
}
