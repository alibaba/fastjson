package com.alibaba.fastjson.deserializer.issues569.parser;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.util.FieldInfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Author : BlackShadowWalker
 * Date   : 2016-10-10
 */
public class DefaultFieldDeserializerBug569 extends DefaultFieldDeserializer {
    public DefaultFieldDeserializerBug569(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(mapping, clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (fieldValueDeserilizer == null) {
            getFieldValueDeserilizer(parser.getConfig());
        }

        Type fieldType = fieldInfo.fieldType;
        if (objectType instanceof ParameterizedType) {
            ParseContext objContext = parser.getContext();
            objContext.type = objectType;
            fieldType = FieldInfo.getFieldType(this.clazz, objectType, fieldType);
        }

        // ContextObjectDeserializer
        Object value;
        if (fieldValueDeserilizer instanceof JavaBeanDeserializer) {
            JavaBeanDeserializer javaBeanDeser = (JavaBeanDeserializer) fieldValueDeserilizer;
            value = javaBeanDeser.deserialze(parser, fieldType, fieldInfo.name, fieldInfo.parserFeatures);
        } else {
            if (this.fieldInfo.format != null && fieldValueDeserilizer instanceof ContextObjectDeserializer) {
                value = ((ContextObjectDeserializer) fieldValueDeserilizer) //
                        .deserialze(parser, fieldType,
                                fieldInfo.name,
                                fieldInfo.format,
                                fieldInfo.parserFeatures);
            } else {
                value = fieldValueDeserilizer.deserialze(parser, fieldType, fieldInfo.name);
            }
        }
        if (parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
            DefaultJSONParser.ResolveTask task = parser.getLastResolveTask();
            task.fieldDeserializer = this;
            task.ownerContext = parser.getContext();
            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            if (object == null) {
                fieldValues.put(fieldInfo.name, value);
            } else {
                setValue(object, value);
            }
        }
    }

}
