package com.alibaba.json.bvt.serializer.enum_;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class EnumTest extends TestCase {

    public static enum Type {
        Big, Medium, Small
    }

    public void test_enum() throws Exception {
        Assert.assertEquals("0", JSON.toJSONStringZ(Type.Big, SerializeConfig.getGlobalInstance())); // 0
        Assert.assertEquals("1", JSON.toJSONStringZ(Type.Medium, SerializeConfig.getGlobalInstance())); // 1
        Assert.assertEquals("2", JSON.toJSONStringZ(Type.Small, SerializeConfig.getGlobalInstance())); // 2

        Assert.assertEquals("\"Big\"", JSON.toJSONString(Type.Big, SerializerFeature.WriteEnumUsingToString)); // "Big"
        Assert.assertEquals("\"Medium\"", JSON.toJSONString(Type.Medium, SerializerFeature.WriteEnumUsingToString)); // "Medium"
        Assert.assertEquals("\"Small\"", JSON.toJSONString(Type.Small, SerializerFeature.WriteEnumUsingToString)); // "Small"
        Assert.assertEquals("'Small'", JSON.toJSONString(Type.Small, SerializerFeature.UseSingleQuotes)); // "Small"
    }

    public void test_empty() throws Exception {
        Model model = JSON.parseObject("{\"type\":\"\"}", Model.class);
        assertNull(model.type);
    }

    public static class Model {
        public Type type;
    }

}
