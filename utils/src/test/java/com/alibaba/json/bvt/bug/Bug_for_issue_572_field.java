package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_572_field extends TestCase {

    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "wenshao";
        
        String text = JSON.toJSONString(model, SerializerFeature.WriteNonStringValueAsString);
        Assert.assertEquals("{\"id\":\"1001\",\"name\":\"wenshao\"}", text);
    }

    public static class Model {

        public int    id;
        public String name;

    }
}
