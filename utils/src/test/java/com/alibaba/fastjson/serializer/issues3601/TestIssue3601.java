package com.alibaba.fastjson.serializer.issues3601;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

public class TestIssue3601 {

    @Test
    public void enumTest() {
        TestEntity testEntity = new TestEntity();
        testEntity.setTestName("ganyu");
        testEntity.setTestEnum(TestEnum.test1);
        String json = JSON.toJSONString(testEntity);
        System.out.println(json);
        Assert.assertEquals("{\"testEnum\":\"test1\",\"testName\":\"ganyu\"}", json);
    }

}