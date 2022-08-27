package com.alibaba.json.bvt.fullSer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;

/**
 * Created by wenshao on 04/02/2017.
 */
public class ToJavaObjectTest2 extends TestCase {
    public void test_for_toJavaObject() throws Exception {
        JSONObject obj = JSON.parseObject("{\"model\":{\"id\":123}}");
        Map<String, Model> models = obj.toJavaObject(new TypeReference<Map<String, Model>>(){}.getType());
        assertEquals(123, models.get("model").id);
    }

    public static class Model {
        public int id;
    }
}
