package com.alibaba.json.bvt.serializer;

import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONSerializerFeatureTest extends TestCase {

    public void test_0() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.UseSingleQuotes));
    }

    public void test_0_g() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.UseSingleQuotes));
    }

    public void test_1() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write("abc");

        Assert.assertEquals("'abc'", serializer.getWriter().toString());
    }

    public void test_1_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write("abc");

        Assert.assertEquals("'abc'", serializer.getWriter().toString());
    }

    public void test_2() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("age", 33));

        Assert.assertEquals("{'age':33}", serializer.getWriter().toString());
    }

    public void test_2_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("age", 33));

        Assert.assertEquals("{'age':33}", serializer.getWriter().toString());
    }

    public void test_3() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(Collections.singletonMap("age", 33));

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public void test_3_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(Collections.singletonMap("age", 33));

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public void test_4() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(Collections.singletonMap("a\nge", 33));

        Assert.assertEquals("{\"a\\nge\":33}", serializer.getWriter().toString());
    }

    public void test_4_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(Collections.singletonMap("a\nge", 33));

        Assert.assertEquals("{\"a\\nge\":33}", serializer.getWriter().toString());
    }

    public void test_5() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("a\nge", 33));

        Assert.assertEquals("{'a\\nge':33}", serializer.getWriter().toString());
    }

    public void test_5_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("a\nge", 33));

        Assert.assertEquals("{'a\\nge':33}", serializer.getWriter().toString());
    }

    public void test_6() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("a'ge", 33));

        Assert.assertEquals("{'a\\'ge':33}", serializer.getWriter().toString());
    }

    public void test_6_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer(new SerializeWriter());

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(Collections.singletonMap("a'ge", 33));

        Assert.assertEquals("{'a\\'ge':33}", serializer.getWriter().toString());
    }

    public void test_7() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(new User(33));

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public void test_7_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));

        serializer.write(new User(33));

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public void test_8() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(new User(33));

        Assert.assertEquals("{'age':33}", serializer.getWriter().toString());
    }

    public void test_8_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.UseSingleQuotes, true);
        Assert.assertEquals(true, serializer.isEnabled(SerializerFeature.UseSingleQuotes));

        serializer.write(new User(33));

        Assert.assertEquals("{'age':33}", serializer.getWriter().toString());
    }

    public void test_9() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.WriteMapNullValue, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.WriteMapNullValue));

        StringWriter out = new StringWriter();

        Map map = new LinkedHashMap();
        map.put("a", null);
        map.put("age", 33);
        map.put("c", null);

        serializer.write(map);

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public void test_9_s() throws Exception {
        JSONSerializer serializer = new JSONSerializer();

        serializer.config(SerializerFeature.QuoteFieldNames, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.QuoteFieldNames));
        serializer.config(SerializerFeature.WriteMapNullValue, false);
        Assert.assertEquals(false, serializer.isEnabled(SerializerFeature.WriteMapNullValue));

        SerializeWriter out = new SerializeWriter();

        Map map = new LinkedHashMap();
        map.put("a", null);
        map.put("age", 33);
        map.put("c", null);

        serializer.write(map);

        Assert.assertEquals("{age:33}", serializer.getWriter().toString());
    }

    public static class User {

        private int age;

        public User(){
        }

        public User(int age){
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

}
