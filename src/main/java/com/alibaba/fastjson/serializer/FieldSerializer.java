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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public abstract class FieldSerializer {

    protected final FieldInfo fieldInfo;
    private final String      double_quoted_fieldPrefix;
    private final String      single_quoted_fieldPrefix;
    private final String      un_quoted_fieldPrefix;
    private boolean           writeNull = false;

    public FieldSerializer(FieldInfo fieldInfo){
        super();
        this.fieldInfo = fieldInfo;
        fieldInfo.setAccessible(true);

        this.double_quoted_fieldPrefix = '"' + fieldInfo.getName() + "\":";

        this.single_quoted_fieldPrefix = '\'' + fieldInfo.getName() + "\':";

        this.un_quoted_fieldPrefix = fieldInfo.getName() + ":";

        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteMapNullValue) {
                    writeNull = true;
                }
            }
        }
    }

    public boolean isWriteNull() {
        return writeNull;
    }

    public Field getField() {
        return fieldInfo.getField();
    }

    public String getName() {
        return fieldInfo.getName();
    }

    public Method getMethod() {
        return fieldInfo.getMethod();
    }
    
    public String getLabel() {
        return fieldInfo.getLabel();
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (serializer.isEnabled(SerializerFeature.QuoteFieldNames)) {
            if (serializer.isEnabled(SerializerFeature.UseSingleQuotes)) {
                out.write(single_quoted_fieldPrefix);
            } else {
                out.write(double_quoted_fieldPrefix);
            }
        } else {
            out.write(un_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValue(Object object) throws Exception {
        try {
            return fieldInfo.get(object);
        } catch (Exception ex) {
            throw new JSONException("get property error。 " + fieldInfo.gerQualifiedName(), ex);
        }
    }

    public abstract void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception;

    public abstract void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception;
}
