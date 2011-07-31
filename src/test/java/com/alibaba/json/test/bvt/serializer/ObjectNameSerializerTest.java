package com.alibaba.json.test.bvt.serializer;

import javax.management.ObjectName;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class ObjectNameSerializerTest extends TestCase {
    public void test_objectName() throws Exception {
        ObjectName name = new ObjectName("com.alibaba:type=A");
        String text = JSON.toJSONString(name);
        Assert.assertEquals("\"com.alibaba:type=A\"", text);
    }
}
