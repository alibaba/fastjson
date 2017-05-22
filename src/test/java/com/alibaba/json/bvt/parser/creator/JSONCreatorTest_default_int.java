package com.alibaba.json.bvt.parser.creator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import org.junit.Assert;

public class JSONCreatorTest_default_int extends TestCase {

    public void test_create() throws Exception {
        Model model = JSON.parseObject("{\"name\":\"wenshao\"}", Model.class);
        Assert.assertEquals(0, model.id);
        Assert.assertEquals("wenshao", model.name);
    }


    public static class Model {

        private final int id;
        private final String name;

        @JSONCreator
        public Model(@JSONField(name="id") int id, @JSONField(name="name") String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static class Value {

    }
}
