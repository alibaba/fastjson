package com.alibaba.json.bvt.writeClassName;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 14/08/2017.
 */
public class WriteClassNameTest6 extends TestCase {
    public void test_for_writeClassName() throws Exception {
        String json = "{\"@type\":\"java.util.HashMap\",\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest6$Model\",\"id\":1001}";

        Model model = (Model) JSON.parse(json);
        assertNotNull(model);
    }

    public void test_for_writeClassName_1() throws Exception {
        String json = "{\"@type\":\"java.util.HashMap\",\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest6$Model\",\"id\":1001}";

        Model model = JSON.parseObject(json, Model.class);
        assertNotNull(model);
    }

    @JSONType
    public static class Model {
        public int id;
    }


}
