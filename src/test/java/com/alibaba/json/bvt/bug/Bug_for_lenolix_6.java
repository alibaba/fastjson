package com.alibaba.json.bvt.bug;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_lenolix_6 extends TestCase {

    public void test_for_objectKey() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("name", "leno.lix");
        map.put("birthday", new Date());
        map.put("gmtCreate", new java.util.Date(new Date().getTime()));
        map.put("gmtModified", new java.util.Date(new Date().getTime()));

        String userJSON = JSON.toJSONString(map, SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue);
        
        System.out.println(userJSON);

        Object object = JSON.parse(userJSON);
        
    }
}
