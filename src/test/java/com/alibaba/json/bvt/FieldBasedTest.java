package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;

/**
 * @author TimAndy
 */
public class FieldBasedTest extends TestCase {
    public void test_filed_based_parse() {
        Student student = new Student("123", "你好世界", 60);
        String json = JSON.toJSONString(student, new SerializeConfig(true));
        Student result = JSON.parseObject(json, Student.class, new ParserConfig(true));

        assertNotNull(result);
        assertEquals("123", result.id);
        assertEquals("你好世界", result.name);
        assertEquals(60, result.score);
    }

    static final class Student {
        private String id;
        private String name;
        private int score;

        Student() {
        }

        Student(String id, String name, int score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }
    }
}
