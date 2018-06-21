package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_smoothrat4 extends TestCase {

    public void test_long() throws Exception {

        Entity entity = new Entity();

        entity.setValue(3L);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3L}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(Long.valueOf(3), entity2.getValue());
    }

    public void test_int() throws Exception {

        Entity entity = new Entity();

        entity.setValue(3);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3}", text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(Integer.valueOf(3), entity2.getValue());
    }

    public void test_short() throws Exception {

        Entity entity = new Entity();

        entity.setValue((short) 3);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3S}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(Short.valueOf((short) 3), entity2.getValue());
    }

    public void test_byte() throws Exception {

        Entity entity = new Entity();

        entity.setValue((byte) 3);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3B}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(Byte.valueOf((byte) 3), entity2.getValue());
    }

    public void test_float() throws Exception {

        Entity entity = new Entity();

        entity.setValue(3F);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3.0F}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(3F, entity2.getValue());
    }

    public void test_double() throws Exception {

        Entity entity = new Entity();

        entity.setValue(3D);

        String text = JSON.toJSONString(entity, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.bug.Bug_for_smoothrat4$Entity\",\"value\":3.0D}",
                            text);

        Entity entity2 = JSON.parseObject(text, Entity.class);
        Assert.assertEquals(3D, entity2.getValue());
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
