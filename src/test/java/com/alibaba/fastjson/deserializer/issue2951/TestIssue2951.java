package com.alibaba.fastjson.deserializer.issue2951;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestIssue2951 {
    @Test
    public void test() {
        String data = "{\"field1\": null, \"field2\": null, \"field3\": \"1\", \"field4\": null}";

        Model model;

        model = JSON.parseObject(data, Model.class);

        Assert.assertEquals(model.field1, 0);
        Assert.assertEquals(model.field2, 0F, 0);
        Assert.assertEquals(model.field3, "1");
        Assert.assertNull(model.field4);

        String data1 = "{\"field1\": null, \"field2\": null, \"field3\": \"1\", \"field4\": \"null\"}";
        model = JSON.parseObject(data1, Model.class);
        Assert.assertEquals(model.field1, 0);
        Assert.assertEquals(model.field2, 0F, 0);
        Assert.assertEquals(model.field3, "1");

        List<String> actualField4 = new ArrayList<String>();
        actualField4.add("null");
        Assert.assertEquals(model.field4, actualField4);
    }

    public static class Model {
        public final int field1;
        public final float field2;
        public final String field3;
        public final List<String> field4;

        public Model(int field1, float field2, String field3, List<String> field4) {
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
        }
    }
}
