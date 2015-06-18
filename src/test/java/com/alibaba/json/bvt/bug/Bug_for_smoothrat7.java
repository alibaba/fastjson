package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat7 extends TestCase {

    @SuppressWarnings("unchecked")
	public void test_self() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("self", map);

        String text = JSON.toJSONString(map, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"java.util.HashMap\",\"self\":{\"$ref\":\"@\"}}",
                            text);

        Map<String, Object> entity2 = (Map<String, Object>) JSON.parse(text);
        Assert.assertEquals(map.getClass(), entity2.getClass());
        Assert.assertSame(entity2, entity2.get("self"));
    }
    

}
