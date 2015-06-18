package com.alibaba.json.bvt.serializer.prettyFormat;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ArrayListFieldTest extends TestCase {

    public void test_prettyFormat() throws Exception {
        VO vo = new VO();
        vo.getEntries().add(new Entity(123, "aaa"));
        vo.getEntries().add(new Entity(234, "bbb"));
        vo.getEntries().add(new Entity(3, "ccc"));
        
        
        String text = JSON.toJSONString(vo, SerializerFeature.PrettyFormat, SerializerFeature.UseSingleQuotes);
        System.out.println(text);
    }

    public static class VO {

        private final List<Entity> entries = new ArrayList<Entity>();

        public List<Entity> getEntries() {
            return entries;
        }

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
