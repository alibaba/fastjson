package com.alibaba.fastjson.deserializer.issue3762;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.deserializer.issue3762.beans.Demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
/**
 * Test for issue 3762
 *
 */
public class TestIssue3762 {
    /**
     * test case 1
     * @throws JsonProcessingException throw when parse meet error
     */
    @Test
    public void testCase1() throws JsonProcessingException {
        final Demo.Info<Demo.SubInfo> subInfo = new Demo.SubInfo()
                .setName("a")
                .setId(1L);
        final String jsonString = JSON.toJSONString(subInfo);
        // SubInfo{id=1, name='a'}
        final Demo.SubInfo parseValue = JSON.parseObject(jsonString, Demo.SubInfo.class);
        // SubInfo{id=1, name='a'}
        final Demo.SubInfo readValue = new ObjectMapper().readValue(jsonString, Demo.SubInfo.class);
        Assert.assertEquals( parseValue, readValue );
    }

    /**
     * test case 2
     * @throws JsonProcessingException throw when parse meet error
     */
    @Test
    public void testCase2() throws JsonProcessingException {
        final Demo.A tester = new Demo.B().setId( "1" );
        final String jsonString2 = JSON.toJSONString( tester );
        // B{id='1'}
        final Demo.B parseValue2 = JSON.parseObject(jsonString2, Demo.B.class);
        // B{id='1'}
        final Demo.B readValue2 = new ObjectMapper().readValue(jsonString2, Demo.B.class);
        Assert.assertEquals( parseValue2, readValue2 );
    }
}