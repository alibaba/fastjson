package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import junit.framework.TestCase;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

public class Issue1449 extends TestCase {
    public void test_for_issue() throws Exception {
        Student student = new Student();
        student.name = "name";
        student.id = 1L;
        student.sex = Sex.MAN;
        System.out.println(JSON.toJSON(student).toString());
        System.out.println(JSON.toJSONString(student));
        String str1 = "{\"id\":1,\"name\":\"name\",\"sex\":\"MAN\"}";
        Student stu1 = JSON.parseObject(str1, Student.class);
        System.out.println(JSON.toJSONString(stu1));
        String str2 = "{\"id\":1,\"name\":\"name\",\"sex\":{\"code\":\"1\",\"des\":\"男\"}}";
        JSON.parseObject(str2, Student.class);

    }

    @JSONType(deserializer = SexDeserializer.class)
    public static enum Sex implements JSONSerializable {

        NONE("0","NONE"),MAN("1","男"),WOMAN("2","女");
        private final String code;
        private final String des;
        private Sex(String code, String des) {
            this.code = code;
            this.des = des;
        }

        public String getCode() {
            return code;
        }

        public String getDes() {
            return des;
        }

        public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {

            JSONObject object = new JSONObject();
            object.put("code", code);
            object.put("des", des);
            serializer.write(object);

        }
    }

    public static class SexDeserializer implements ObjectDeserializer {

        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String code;
            Object object = parser.parse();
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                code = jsonObject.getString("code");
            } else {
                code = (String) object;
            }
            if ("0".equals(code)) {
                return (T) Sex.NONE;
            } else if ("1".equals(code)) {
                return (T) Sex.MAN;
            } else if ("2".equals(code)) {
                return (T) Sex.WOMAN;
            }
            return (T) Sex.NONE;
        }

        public int getFastMatchToken() {
            return 0;
        }
    }
    public static class Student implements Serializable {

        public Long id;

        public String name;

        public Sex sex;
    }
}
