package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class JSONObjectTest6 extends TestCase {

    public void test() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", 123);
        
        Model model = jsonObject.toJavaObject(Model.class);
        assertEquals(123, model.value);
    }
    
    public static class Model {
        public int value;
    }
}
