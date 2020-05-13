package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class ByteArrayFieldTest_5_base64 extends TestCase {

    public void test_0() throws Exception {
        Model model = new Model();

        model.value = "ABCDEG".getBytes();

        String json = JSON.toJSONString(model);

        assertEquals("{\"value\":\"QUJDREVH\"}", json);

        Model model1 = JSON.parseObject(json, Model.class);
        Assert.assertArrayEquals(model.value, model1.value);
    }

    private static class Model {

        @JSONField(format = "base64")
        public byte[] value;


    }
}
