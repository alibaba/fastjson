package com.alibaba.json.bvt.serializer.fieldbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class FieldBaseTest0 extends TestCase {

    public void test_0() throws Exception {
        SerializeConfig config = new SerializeConfig(true);

        Model model = new Model();
        model.id = 123;
        assertEquals("{\"id\":123}", JSON.toJSONString(model, config));
    }

    public static class Model {
        private int id;
    }
}
