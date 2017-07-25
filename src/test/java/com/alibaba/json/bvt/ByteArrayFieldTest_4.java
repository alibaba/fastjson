package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.json.test.TestUtils;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.UnsupportedEncodingException;

public class ByteArrayFieldTest_4 extends TestCase {

    public void test_0() throws Exception {
        Model model = new Model();

        model.value = "ABCDEG".getBytes();

        String json = JSON.toJSONString(model);

        assertEquals("{\"value\":x'414243444547'}", json);

        Model model1 = JSON.parseObject(json, Model.class);
        Assert.assertArrayEquals(model.value, model1.value);

    }

    private static class Model {

        @JSONField(format = "hex")
        public byte[] value;


    }
}
