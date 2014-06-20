package com.alibaba.json.bvt;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONObjectSubclassTest extends JSONObject {

    private static final long serialVersionUID = 1L;

    @Test
    public void test() throws Exception {
        String content = "{'map':{'key':'value'},'list':[{'key':'value'},{'map':{'key':'value'}}]}";
        validateMap(JSON.parseObject(content, JSONObjectSubclassTest.class));
    }

    public static void validateList(JSONArray value) {
        Iterator<Object> it = value.iterator();
        while (it.hasNext()) {
            validateObject(it.next());
        }
    }

    public static void validateMap(JSONObject value) {
        Assert.assertEquals(JSONObjectSubclassTest.class, value.getClass());
        for (Object v : value.values()) {
            validateObject(v);
        }
    }

    public static void validateObject(Object value) {
        if (value == null) {
        } else if (JSONObject.class.isAssignableFrom(value.getClass())) {
            validateMap((JSONObject) value);
        } else if (JSONArray.class.isAssignableFrom(value.getClass())) {
            validateList((JSONArray) value);
        }
    }

}
