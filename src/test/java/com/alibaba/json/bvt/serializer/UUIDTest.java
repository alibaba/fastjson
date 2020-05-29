package com.alibaba.json.bvt.serializer;

import java.util.UUID;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class UUIDTest extends TestCase {

    public void test_timezone() throws Exception {
        UUID id = UUID.randomUUID();
        
        String text = JSON.toJSONString(id);
        
        System.out.println(text);

        Assert.assertEquals(JSON.toJSONString(id.toString()), text);
     
        UUID id2 = JSON.parseObject(text,  UUID.class);
        Assert.assertEquals(id, id2);
    }
}
