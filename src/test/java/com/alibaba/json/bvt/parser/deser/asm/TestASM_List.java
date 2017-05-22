package com.alibaba.json.bvt.parser.deser.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestASM_List extends TestCase {

    public void test_decimal_3() throws Exception {
        V0 v = new V0();
        v.getList().add(new V1());
        v.getList().add(new V1());
        String text = JSON.toJSONString(v, SerializerFeature.UseSingleQuotes, SerializerFeature.WriteMapNullValue);
        System.out.println(text);

        // Assert.assertEquals("{'list':[{},{}]}", text);
    }

    public static class V0 {

        private List<V1> list = new ArrayList<V1>();

        public List<V1> getList() {
            return list;
        }

        public void setList(List<V1> list) {
            this.list = list;
        }

    }

    public static class V1 {

        private int      id;
        private TimeUnit unit = TimeUnit.SECONDS;
        private String   name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
