package com.alibaba.json.bvt.serializer.fieldbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class FieldBaseTest1 extends TestCase {
    private static SerializeConfig config = new SerializeConfig(true);

    public void test_0() throws Exception {


        Model model = new Model();
        ((AbstractModel)model).parentId = 234;
        model.id = 123;
        assertEquals("{\"id\":123,\"parentId\":234}", JSON.toJSONString(model, config));
    }

    public static class AbstractModel {
        private int parentId;
    }

    public static class Model extends AbstractModel {
        private int id;
    }
}
