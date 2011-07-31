package com.alibaba.json.test.bvt.serializer;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EnumTest extends TestCase {

    public static enum Type {
        Big, Medium, Small
    }

    public void test_enum() throws Exception {
        Assert.assertEquals("0", JSON.toJSONString(Type.Big)); // 0
        Assert.assertEquals("1", JSON.toJSONString(Type.Medium)); // 1
        Assert.assertEquals("2", JSON.toJSONString(Type.Small)); // 2

        Assert.assertEquals("\"Big\"", JSON.toJSONString(Type.Big, SerializerFeature.WriteEnumUsingToString)); // "Big"
        Assert.assertEquals("\"Medium\"", JSON.toJSONString(Type.Medium, SerializerFeature.WriteEnumUsingToString)); // "Medium"
        Assert.assertEquals("\"Small\"", JSON.toJSONString(Type.Small, SerializerFeature.WriteEnumUsingToString)); // "Small"
    }

}
