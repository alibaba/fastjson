/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.Collection;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class FieldSerializer implements Comparable<FieldSerializer> {
    public final FieldInfo  fieldInfo;
    protected final boolean writeNull;
    protected final int     features;
    protected final String  format;
    protected char[] name_chars;
    
    private RuntimeSerializerInfo runtimeInfo;
    
    public FieldSerializer(FieldInfo fieldInfo){
        this.fieldInfo = fieldInfo;

        boolean writeNull = false;
        JSONField annotation = fieldInfo.getAnnotation();
        String format = null;
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteMapNullValue) {
                    writeNull = true;
                }
            }
            
            format = annotation.format();

            format = format.trim();
            if (format.length() == 0) {
                format = null;
            }
            
            features = SerializerFeature.of(annotation.serialzeFeatures());
        } else {
            features = 0;
        }
        this.writeNull = writeNull;
        this.format = format;

        String name = fieldInfo.name;
        int nameLen = name.length();
        name_chars = new char[nameLen + 3];
        name.getChars(0, name.length(), name_chars, 1);
        name_chars[0] = '"';
        name_chars[nameLen + 1] = '"';
        name_chars[nameLen + 2] = ':';
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.out;
        
        final int featurs = out.features;
        
        if ((featurs & SerializerFeature.QuoteFieldNames.mask) != 0) {
            if ((featurs & SerializerFeature.UseSingleQuotes.mask) != 0) {
                out.writeFieldName(fieldInfo.name, true);
            } else {
                out.write(name_chars, 0, name_chars.length);
            }
        } else {
            out.writeFieldName(fieldInfo.name, true);
        }
    }

    public Object getPropertyValue(Object object) throws Exception {
        try {
            return fieldInfo.get(object);
        } catch (Exception ex) {
            Member member = fieldInfo.method != null ? //
                fieldInfo.method //
                : fieldInfo.field;
            
            String qualifiedName = member.getDeclaringClass().getName() + "." + member.getName();
            
            throw new JSONException("get property error。 " + qualifiedName, ex);
        }
    }

    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (format != null) {
            serializer.writeWithFormat(propertyValue, format);
            return;
        }

        if (runtimeInfo == null) {

            Class<?> runtimeFieldClass;
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.fieldClass;
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }

            ObjectSerializer fieldSerializer = serializer.config.get(runtimeFieldClass);
            runtimeInfo = new RuntimeSerializerInfo(fieldSerializer, runtimeFieldClass);
        }
        
        final RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;

        if (propertyValue == null) {
            if ((features & SerializerFeature.WriteNullNumberAsZero.mask) != 0 // 
                    && Number.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.out.write('0');
                return;
            } else if ((features & SerializerFeature.WriteNullBooleanAsFalse.mask) != 0 // 
                    && Boolean.class == runtimeInfo.runtimeFieldClass) {
                serializer.out.write("false");
                return;
            } else if ((features & SerializerFeature.WriteNullListAsEmpty.mask) != 0 // 
                    && Collection.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.out.write("[]");
                return;
            }

            runtimeInfo.fieldSerializer.write(serializer, null, fieldInfo.name, runtimeInfo.runtimeFieldClass);
            return;
        }

        Class<?> valueClass = propertyValue.getClass();
        if (valueClass == runtimeInfo.runtimeFieldClass) {
            runtimeInfo.fieldSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType);
            return;
        }

        ObjectSerializer valueSerializer = serializer.config.get(valueClass);
        valueSerializer.write(serializer, propertyValue, fieldInfo.name, fieldInfo.fieldType);
    }
    
    static class RuntimeSerializerInfo {

        ObjectSerializer fieldSerializer;
        Class<?>         runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass){
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }
    
    public int compareTo(FieldSerializer o) {
        return this.fieldInfo.compareTo(o.fieldInfo);
    }
}
