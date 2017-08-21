package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * Created by wenshao on 15/03/2017.
 */
public class ObjectWriteTest extends TestCase {
    public void test_objectWriteTest() throws Exception {
        ObjectSerializer serializer = SerializeConfig.getGlobalInstance().getObjectWriter(Model.class);

        JSONSerializer jsonSerializer = new JSONSerializer();
        serializer.write(jsonSerializer, null, "a", Model.class, 0);

        String text = jsonSerializer.out.toString();
        assertEquals("null", text);
    }

    public static class Model {
        public int id;
    }
}
