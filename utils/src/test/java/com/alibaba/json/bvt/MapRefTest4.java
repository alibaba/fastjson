package com.alibaba.json.bvt;

import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class MapRefTest4 extends TestCase {

    public void test_0() throws Exception {
        String text = "{\"u1\":{\"id\":123,\"name\":\"wenshao\"},\"u2\":{\"$ref\":\"..\"}}";
        Map<String, Object> map = JSON.parseObject(text, new TypeReference<Map<String, Object>>() {});
        //Assert.assertEquals(map, map.get("this"));
        Assert.assertSame(map, map.get("u2"));
    }


}
