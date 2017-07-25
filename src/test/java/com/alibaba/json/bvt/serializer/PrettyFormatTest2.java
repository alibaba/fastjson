package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;
import org.junit.Assert;

public class PrettyFormatTest2 extends TestCase {

    public void test_0() throws Exception {
        Model model = new Model();
        model.id = 123;
        model.name = "wenshao";
        String text = JSON.toJSONString(model, SerializerFeature.PrettyFormat);
        assertEquals("{\n" +
                "\t\"id\":123,\n" +
                "\t\"name\":\"wenshao\"\n" +
                "}", text);
        
        Assert.assertEquals("[\n\t{},\n\t{}\n]", JSON.toJSONString(new Object[] { new Object(), new Object() }, SerializerFeature.PrettyFormat));
    }

    public  static class Model {
        public int id;
        public String name;
    }
}
