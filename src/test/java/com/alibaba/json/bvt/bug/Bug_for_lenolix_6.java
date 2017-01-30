package com.alibaba.json.bvt.bug;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_6 extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.getGlobalInstance().addAccept("com.alibaba.json.bvt.bug.Bug_for_lenolix_6.");
    }

    public void test_for_objectKey() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("name", "leno.lix");
        map.put("birthday", new Date());
        map.put("gmtCreate", new java.sql.Date(new Date().getTime()));
        map.put("gmtModified", new java.sql.Timestamp(new Date().getTime()));

        String userJSON = JSON.toJSONString(map, SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue);
        
        System.out.println(userJSON);

        Object object = JSON.parse(userJSON);
        
    }
}
