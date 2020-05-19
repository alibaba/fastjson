package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_10 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_lenolix_10.");
    }

    public void test_for_objectKey() throws Exception {
        Map<Integer, User> map2 = new HashMap<Integer, User>();
        User user = new User();
        user.setId(1);
        user.setIsBoy(true);
        user.setName("leno.lix");
        // user.setBirthDay(simpleDateFormat.parse("2012-03-07 22:38:21 CST"));
        // user.setGmtCreate(new java.sql.Date(simpleDateFormat.parse("2012-02-03 22:38:21 CST")
        // .getTime()));
        map2.put(1, user);
        String mapJson2 = JSON.toJSONString(map2, SerializerFeature.WriteClassName, SerializerFeature.WriteMapNullValue);
        System.out.println(mapJson2);
        Object object2 = JSON.parse(mapJson2);

    }

    public static class User {

        private int     id;
        private Boolean isBoy;
        private String  name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Boolean getIsBoy() {
            return isBoy;
        }

        public void setIsBoy(Boolean isBoy) {
            this.isBoy = isBoy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
