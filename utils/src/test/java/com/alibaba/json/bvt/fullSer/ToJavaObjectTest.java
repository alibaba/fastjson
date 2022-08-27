package com.alibaba.json.bvt.fullSer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 04/02/2017.
 */
public class ToJavaObjectTest extends TestCase {
    public void test_for_toJavaObject() throws Exception {
        JSONObject obj = JSON.parseObject("{\"id\":123}");
        Model model = obj.toJavaObject(Model.class);
        assertEquals(123, model.id);
    }

    public static class Model {
        public int id;
    }
}
