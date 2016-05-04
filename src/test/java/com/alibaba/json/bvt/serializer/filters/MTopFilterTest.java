package com.alibaba.json.bvt.serializer.filters;

import java.util.HashMap;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

import junit.framework.TestCase;

public class MTopFilterTest extends TestCase {

    public void test_0() throws Exception {
        Model model = new Model();
        model.id = 1001;
        model.name = "yongbo";
        model.user = new Person();
        model.user.personId = 2002;

        final HashMap<String, Object> values = new HashMap<String, Object>();
        ValueFilter valueFilter = new ValueFilter() {

            @Override
            public Object process(Object object, String name, Object value) {
                values.put(name, value);
                return value;
            }
        };
        
        String jsonString = JSON.toJSONString(model, valueFilter);
        Assert.assertEquals("{\"id\":1001,\"name\":\"yongbo\",\"user\":{\"personId\":2002}}", jsonString);
        
        Assert.assertEquals(4, values.size());
        Assert.assertEquals(model.id, values.get("id"));
        Assert.assertSame(model.name, values.get("name"));
        Assert.assertEquals(model.user, values.get("user"));
        Assert.assertEquals(model.user.personId, values.get("personId"));
    }

    public static class Model {

        private int    id;
        private String name;
        private Person user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Person getUser() {
            return user;
        }

        public void setUser(Person user) {
            this.user = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Person {

        private int personId;

        public int getPersonId() {
            return personId;
        }

        public void setPersonId(int personId) {
            this.personId = personId;
        }

    }
}
