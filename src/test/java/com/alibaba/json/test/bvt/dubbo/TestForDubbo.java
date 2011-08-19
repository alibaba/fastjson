package com.alibaba.json.test.bvt.dubbo;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestForDubbo extends TestCase {
    private HelloServiceImpl helloService = new HelloServiceImpl();

    public void testParamType4() {
        Tiger tiger = new Tiger();
        tiger.setTigerName("东北虎");
        tiger.setTigerSex(true);
        Tigers tigers = helloService.eatTiger(tiger);
        
        String text = JSON.toJSONString(tigers, SerializerFeature.WriteClassName);
        System.out.println(text);
        
        Tigers tigers2 = JSON.parseObject(text, Tigers.class);
        
        Assert.assertEquals(text, JSON.toJSONString(tigers2, SerializerFeature.WriteClassName));
    }

}
