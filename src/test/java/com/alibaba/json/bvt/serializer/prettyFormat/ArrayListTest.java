package com.alibaba.json.bvt.serializer.prettyFormat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ArrayListTest extends TestCase {

    public void test_array() throws Exception {
        List<Entity> list = new ArrayList<Entity>();
        list.add(new Entity(123, "aaa"));
        list.add(new Entity(234, "bbb"));
        list.add(new Entity(3, "ccc"));
        String text = JSON.toJSONString(list, SerializerFeature.PrettyFormat, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("[\n\t{\n\t\t'id':123,\n\t\t'name':'aaa'\n\t},\n\t{\n\t\t'id':234,\n\t\t'name':'bbb'\n\t},\n\t{\n\t\t'id':3,\n\t\t'name':'ccc'\n\t}\n]", text);
    }

    public static class Entity {

        private int    id;
        private String name;

        public Entity(){

        }

        public Entity(int id, String name){
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
