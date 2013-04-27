package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_5 extends TestCase {

    public void test_for_objectKey() throws Exception {
        Map obj = new HashMap();
        Object obja = new Object();
        Object objb = new Object();
        obj.put(obja, objb);

        String newJsonString = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                                                 SerializerFeature.WriteClassName);
        System.out.println(newJsonString);

        Object newObject = JSON.parse(newJsonString);

        System.out.println(newObject);
    }
}
