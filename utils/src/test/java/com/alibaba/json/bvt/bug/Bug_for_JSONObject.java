package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class Bug_for_JSONObject extends TestCase {
    public void test_0 () throws Exception {
        JSONSerializer ser = new JSONSerializer();
        ser.config(SerializerFeature.WriteClassName, true);
        ser.write(new JSONObject());
    }
}
