package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_SpitFire extends TestCase {

    public void test_for_spitFire() throws Exception {
        GenericDTO<MyDTO> object = new GenericDTO<MyDTO>();
        object.setFiled(new MyDTO());

        String text = JSON.toJSONString(object, SerializerFeature.WriteClassName);

        GenericDTO<MyDTO> object2 = (GenericDTO<MyDTO>) JSON.parseObject(text, GenericDTO.class);
        
        Assert.assertEquals(object.getName(), object2.getName());
        Assert.assertEquals(object.getFiled().getId(), object2.getFiled().getId());
    }

    public static class GenericDTO<T extends AbstractDTO> extends AbstractDTO {

        private String name;
        private T      filed;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public T getFiled() {
            return filed;
        }

        public void setFiled(T filed) {
            this.filed = filed;
        }
    }

    public abstract static class AbstractDTO {

    }

    public static class MyDTO extends AbstractDTO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
