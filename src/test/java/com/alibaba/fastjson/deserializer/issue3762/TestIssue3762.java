package com.alibaba.fastjson.deserializer.issue3762;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.deserializer.issue3762.beans.Demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TestIssue3762 {
    @Test
    public void testCase1() throws JsonProcessingException {
        Demo.Info<Demo.SubInfo> subInfo = new Demo.SubInfo()
                .setName("a")
                .setId(1L);
        String jsonString = JSON.toJSONString(subInfo);
        // SubInfo{id=1, name='a'}
        Demo.SubInfo parseValue = JSON.parseObject(jsonString, Demo.SubInfo.class);
        // SubInfo{id=1, name='a'}
        Demo.SubInfo readValue = new ObjectMapper().readValue(jsonString, Demo.SubInfo.class);
        Assert.assertEquals( parseValue, readValue );
    }

    @Test
    public void testCase2() throws JsonProcessingException {
        Demo.A b = new Demo.B().setId( "1" );
        String jsonString2 = JSON.toJSONString( b );
        // B{id='1'}
        Demo.B parseValue2 = JSON.parseObject(jsonString2, Demo.B.class);
        // B{id='1'}
        Demo.B readValue2 = new ObjectMapper().readValue(jsonString2, Demo.B.class);
        Assert.assertEquals( parseValue2, readValue2 );
    }
}
