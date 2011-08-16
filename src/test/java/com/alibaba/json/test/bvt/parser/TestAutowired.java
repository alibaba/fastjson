package com.alibaba.json.test.bvt.parser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.serializer.AutowiredObjectSerializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.util.ServiceLoader;

public class TestAutowired extends TestCase {

    public void test_0() throws Exception {
        new ServiceLoader();

        ServiceLoader.close(null);

        String text = JSON.toJSONString(new Entity("xxx"));
        Assert.assertEquals("{\"v\":\"xxx\"}", text);
        Entity entity = JSON.parseObject(text, Entity.class);
        Assert.assertEquals("xxx", entity.getValue());
    }
    
    public void test_1() throws Exception {
        ServiceLoader.load(AutowiredObjectSerializer.class, new MyClassLoader());
    }

    public static class MyClassLoader extends ClassLoader {

        public Enumeration<URL> getResources(String name) throws IOException {
            throw new IOException();
        }
    }

    public static class Entity {

        private String value;

        public Entity(String value){
            super();
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class EntitySerializer implements AutowiredObjectSerializer {

        public void write(JSONSerializer serializer, Object object, Object fieldName) throws IOException {
            SerializeWriter out = serializer.getWriter();
            out.writeFieldValue('{', "v", ((Entity) object).getValue());
            out.write('}');
        }

        public Set<Type> getAutowiredFor() {
            return Collections.<Type> singleton(Entity.class);
        }

    }

    public static class EntityDeserializer implements AutowiredObjectDeserializer {

        public <T> T deserialze(DefaultExtJSONParser parser, Type type, Object fieldName) {
            parser.accept(JSONToken.LBRACE);
            JSONLexer lexer = parser.getLexer();
            Assert.assertEquals("v", lexer.stringVal());
            parser.accept(JSONToken.LITERAL_STRING);
            parser.accept(JSONToken.COLON);

            Entity entity = new Entity(lexer.stringVal());
            parser.accept(JSONToken.LITERAL_STRING);
            parser.accept(JSONToken.RBRACE);
            return (T) entity;
        }

        public int getFastMatchToken() {
            return JSONToken.LBRACE;
        }

        public Set<Type> getAutowiredFor() {
            return Collections.<Type> singleton(Entity.class);
        }

    }

}
