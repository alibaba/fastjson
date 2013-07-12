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

import java.util.Collection;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class ObjectFieldSerializer extends FieldSerializer {

    private ObjectSerializer fieldSerializer;

    private Class<?>         runtimeFieldClass;
    private String           format;
    private boolean          writeNumberAsZero       = false;
    boolean                  writeNullStringAsEmpty  = false;
    boolean                  writeNullBooleanAsFalse = false;
    boolean                  writeNullListAsEmpty    = false;
    boolean                  writeEnumUsingToString  = false;

    public ObjectFieldSerializer(FieldInfo fieldInfo){
        super(fieldInfo);

        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);

        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }

            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteNullNumberAsZero) {
                    writeNumberAsZero = true;
                } else if (feature == SerializerFeature.WriteNullStringAsEmpty) {
                    writeNullStringAsEmpty = true;
                } else if (feature == SerializerFeature.WriteNullBooleanAsFalse) {
                    writeNullBooleanAsFalse = true;
                } else if (feature == SerializerFeature.WriteNullListAsEmpty) {
                    writeNullListAsEmpty = true;
                } else if (feature == SerializerFeature.WriteEnumUsingToString) {
                    writeEnumUsingToString = true;
                }
            }
        }
    }

    @Override
    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        writePrefix(serializer);

        if (format != null) {
            serializer.writeWithFormat(propertyValue, format);
            return;
        }

        if (fieldSerializer == null) {
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.getFieldClass();
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }

            fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
        }

        if (propertyValue == null) {
            if (writeNumberAsZero && Number.class.isAssignableFrom(runtimeFieldClass)) {
                serializer.getWriter().write('0');
                return;
            } else if (writeNullStringAsEmpty && String.class == runtimeFieldClass) {
                serializer.getWriter().write("\"\"");
                return;
            } else if (writeNullBooleanAsFalse && Boolean.class == runtimeFieldClass) {
                serializer.getWriter().write("false");
                return;
            } else if (writeNullListAsEmpty && Collection.class.isAssignableFrom(runtimeFieldClass)) {
                serializer.getWriter().write("[]");
                return;
            }

            fieldSerializer.write(serializer, null, fieldInfo.getName(), null);
            return;
        }

        if (writeEnumUsingToString == true && runtimeFieldClass.isEnum()) {
            serializer.getWriter().writeString(((Enum<?>) propertyValue).name());
            return;
        }

        Class<?> valueClass = propertyValue.getClass();
        if (valueClass == runtimeFieldClass) {
            fieldSerializer.write(serializer, propertyValue, fieldInfo.getName(), fieldInfo.getFieldType());
            return;
        }

        ObjectSerializer valueSerializer = serializer.getObjectWriter(valueClass);
        valueSerializer.write(serializer, propertyValue, fieldInfo.getName(), fieldInfo.getFieldType());
    }

}
