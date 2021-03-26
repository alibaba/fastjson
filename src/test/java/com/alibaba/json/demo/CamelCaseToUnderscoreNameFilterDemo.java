package com.alibaba.json.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.CamelCaseToUnderscoreNameFilter;
import junit.framework.TestCase;
import org.junit.Assert;

public class CamelCaseToUnderscoreNameFilterDemo extends TestCase {

    public static class User {
        private String name;

        @JSONField(name = "testUserName")
        public void setName(String name) { this.name = name; }

        @JSONField(name = "testUserName")
        public String getName() { return name; }
    }

    public void test_secure() throws Exception {
        User user = new User();
        user.setName("毛头");
        Object object = JSONObject.toJSON(user);
        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getNameFilters().add(new CamelCaseToUnderscoreNameFilter());

        serializer.write(object);

        String text = out.toString();
        Assert.assertEquals("{\"test_user_name\":\"毛头\"}", text);
        System.out.println(text);
    }
}
