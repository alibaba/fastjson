package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import junit.framework.TestCase;


public class NotWriteRootClassNameTest extends TestCase {
    public void test_NotWriteRootClassName() throws Exception {
        SerializerFeature[] features = new SerializerFeature[] {SerializerFeature.WriteClassName, SerializerFeature.NotWriteRootClassName};
        Assert.assertEquals("{}", JSON.toJSONString(new VO(), features));
        Assert.assertEquals("{}", JSON.toJSONString(new V1(), features));
    }
    
    public static class VO {
        
    }
    
    private static class V1 {
        
    }
}
