package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;

public abstract class ASMJavaBeanDeserializer implements ObjectDeserializer {

    private JavaBeanDeserializer serializer;

    public ASMJavaBeanDeserializer(ParserConfig mapping, Class<?> clazz){
        serializer = new JavaBeanDeserializer(mapping, clazz);
    }

    public abstract Object createInstance(DefaultJSONParser parser, Type type);

    public JavaBeanDeserializer getInnterSerializer() {
        return serializer;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return (T) serializer.deserialze(parser, type, fieldName);
    }

    public int getFastMatchToken() {
        return serializer.getFastMatchToken();
    }

    public Object createInstance(DefaultJSONParser parser) {
        return serializer.createInstance(parser, serializer.clazz);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, beanInfo, fieldInfo);
    }

    public FieldDeserializer getFieldDeserializer(String name) {
        return serializer.getFieldDeserializer(name);
    }

    public Type getFieldType(int ordinal) {
        return serializer.sortedFieldDeserializers[ordinal].fieldInfo.fieldType;
    }

    public boolean isSupportArrayToBean(JSONLexer lexer) {
        return serializer.isSupportArrayToBean(lexer);
    }

    public Object parseRest(DefaultJSONParser parser, Type type, Object fieldName, Object instance) {
        // serializer.parseField(parser, key, object, objectType, fieldValues)
        Object value = serializer.deserialze(parser, type, fieldName, instance);

        return value;
    }
    
    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        return serializer.deserialzeArrayMapping(parser, type, fieldName, object);
    }
}
