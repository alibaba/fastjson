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

import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class ObjectFieldSerializer extends FieldSerializer {
    private RuntimeSerializerInfo runtimeInfo;

    public ObjectFieldSerializer(FieldInfo fieldInfo){
        super(fieldInfo);
    }

    @Override
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
            } else if ((features & SerializerFeature.WriteNullStringAsEmpty.mask) != 0 // 
                    && String.class == runtimeInfo.runtimeFieldClass) {
                serializer.out.write("\"\"");
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

        if ((features & SerializerFeature.WriteEnumUsingToString.mask) != 0 && runtimeInfo.runtimeFieldClass.isEnum()) {
            serializer.out.writeString(((Enum<?>) propertyValue).name());
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
}
