package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteClassNameTest extends TestCase {

    public void test_0() throws Exception {
        Entity entity = new Entity(3, "jobs");
        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);

        Entity entity2 = (Entity) JSON.parse(text);

        assertEquals(entity.id, entity2.id);
        assertEquals(entity.name, entity2.name);
    }

    public static class Entity {
        public int    id;
        public String name;

        public Entity(){
        }

        public Entity(int id, String name){
            this.id = id;
            this.name = name;
        }
    }
}
