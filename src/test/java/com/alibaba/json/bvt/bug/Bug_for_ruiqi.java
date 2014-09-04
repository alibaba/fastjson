package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_ruiqi extends TestCase {

    public void test_0() throws Exception {
        Map<String, Enum> map = new HashMap<String, Enum>();
        map.put("a", Enum.ENUM1);
        map.put("b", Enum.ENUM1);

        System.out.println(JSON.toJSONString(map, SerializerFeature.WriteEnumUsingToString));

        System.out.println(JSON.toJSONString(map));

    }

    public static enum Enum {

        ENUM1("name1"), ENUM2("name2");

        private String name;

        Enum(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "name: " + name;
        }
    }

}
