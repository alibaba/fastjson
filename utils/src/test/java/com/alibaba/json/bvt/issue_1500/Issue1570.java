package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.List;

public class Issue1570 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        assertEquals("{}", JSON.toJSONString(model, SerializerFeature.WriteNullBooleanAsFalse));
        assertEquals("{\"value\":\"\"}", JSON.toJSONString(model, SerializerFeature.WriteNullStringAsEmpty));
    }

    public void test_for_issue_int() throws Exception {
        ModelInt model = new ModelInt();
        assertEquals("{}", JSON.toJSONString(model, SerializerFeature.WriteNullBooleanAsFalse));
        assertEquals("{\"value\":0}", JSON.toJSONString(model, SerializerFeature.WriteNullNumberAsZero));
    }

    public void test_for_issue_long() throws Exception {
        ModelLong model = new ModelLong();
        assertEquals("{}", JSON.toJSONString(model, SerializerFeature.WriteNullBooleanAsFalse));
        assertEquals("{\"value\":0}", JSON.toJSONString(model, SerializerFeature.WriteNullNumberAsZero));
    }

    public void test_for_issue_bool() throws Exception {
        ModelBool model = new ModelBool();
        assertEquals("{}", JSON.toJSONString(model, SerializerFeature.WriteNullNumberAsZero));
        assertEquals("{\"value\":false}", JSON.toJSONString(model, SerializerFeature.WriteNullBooleanAsFalse));
    }

    public void test_for_issue_list() throws Exception {
        ModelList model = new ModelList();
        assertEquals("{}", JSON.toJSONString(model, SerializerFeature.WriteNullNumberAsZero));
        assertEquals("{\"value\":[]}", JSON.toJSONString(model, SerializerFeature.WriteNullListAsEmpty));
    }

    public static class Model {
        public String value;
    }

    public static class ModelInt {
        public Integer value;
    }

    public static class ModelLong {
        public Long value;
    }

    public static class ModelBool {
        public Boolean value;
    }

    public static class ModelList {
        public List value;
    }
}
