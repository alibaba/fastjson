package com.alibaba.json.bvt.bug;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_lenolix_11 extends TestCase {

    public void test_for_objectKey() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");

        String simpleDateFormatJson = JSON.toJSONString(simpleDateFormat, SerializerFeature.WriteClassName,
                                                        SerializerFeature.WriteMapNullValue);

        System.out.println(simpleDateFormatJson);

        java.text.SimpleDateFormat format = (java.text.SimpleDateFormat) JSON.parse(simpleDateFormatJson);
        Assert.assertEquals("MM-dd-yyyy", format.toPattern());

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
