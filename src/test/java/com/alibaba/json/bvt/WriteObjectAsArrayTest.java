package com.alibaba.json.bvt;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteObjectAsArrayTest extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("abc");

        {
            String text = JSON.toJSONString(vo, SerializerFeature.WriteObjectAsArray);
            System.out.println(text);
        }
        {
            String text = JSON.toJSONString(vo);
            System.out.println(text);
        }
    }

    private static class VO {

        private int    id;
        private String name;

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
