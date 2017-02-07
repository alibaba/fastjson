package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 15/01/2017.
 */
public class Issue993 extends TestCase {
    public void test_for_issue() throws Exception {
        Student student = new Student();
        student.name = "小刚";

        String json = JSON.toJSONString(student, SerializerFeature.WriteMapNullValue);
        assertEquals("{\"student_name\":\"小刚\",\"student_age\":0,\"student_grade\":null}", json);
    }

    public static class Student {
        @JSONField(name="student_name",ordinal = 0)
        public String name;

        @JSONField(name="student_age",ordinal = 1)
        public int age;

        @JSONField(name="student_grade",ordinal = 2)
        public String grade;
    }
}
