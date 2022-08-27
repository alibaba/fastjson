package com.alibaba.json.bvt.parser;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ReadOnlyAtomicBooleanTest_field extends TestCase {

    public void test_readOnly() throws Exception {
        Model model = new Model();
        model.value.set(true);

        String text = JSON.toJSONString(model);
        Assert.assertEquals("{\"value\":true}", text);
        Model model2 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.value.get(), model2.value.get());
    }

    public static class Model {
        public final AtomicBoolean value = new AtomicBoolean();

    }
}
