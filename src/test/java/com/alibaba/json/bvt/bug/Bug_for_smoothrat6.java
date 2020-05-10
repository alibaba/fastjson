package com.alibaba.json.bvt.bug;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat6 extends TestCase {

    public void test_set() throws Exception {
        Set<Object> set = new LinkedHashSet<Object>();
        set.add(3L);
        set.add(4L);

        Entity entity = new Entity();

        entity.setValue(set);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat6$Entity\",\"value\":Set[3L,4L]}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(set, entity2.getValue());
        //Assert.assertEquals(set.getClass(), entity2.getValue().getClass());
    }
    

    public static class Entity {

        private Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
