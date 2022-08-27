package com.alibaba.json.bvt.bug;

import java.util.ArrayList;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class Bug_for_issue_389 extends TestCase {

    public void test_for_issue() throws Exception {
        Def def = new Def();
        def.add(new User());
        String defStr = JSON.toJSONString(def);

        Assert.assertEquals("[{}]", defStr);
        
        Def _def = JSON.parseObject(defStr, Def.class);
        Assert.assertEquals(User.class, _def.get(0).getClass());
    }
    
    public void test_for_issue_1() throws Exception {
        Def def = new Def();
        def.add(new User());
        String defStr = JSON.toJSONString(def);

        Assert.assertEquals("[{}]", defStr);
        
        Def _def = JSON.parseObject(defStr, new TypeReference<Def>() {});
        Assert.assertEquals(User.class, _def.get(0).getClass());
    }

    public static class User {

        String name;
        String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class Def extends ArrayList<User> {

    }
}
