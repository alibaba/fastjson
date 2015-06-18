package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

public class CodecCheck extends UDF {

    public String evaluate() throws Exception {
        A a = new A();
        a.setId(123);
        a.setName("xxx");
        
        String text = JSON.toJSONString(a);
        JSON.parseObject(text, A.class);
        
        return "ok";
    }

    public static class A {

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
