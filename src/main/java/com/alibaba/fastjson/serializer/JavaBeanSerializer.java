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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class JavaBeanSerializer implements ObjectSerializer {

    // serializers
    private final FieldSerializer[] getters;
    private boolean                 managedReference = true;
    private FieldSerializer         keyField;

    public FieldSerializer[] getGetters() {
        return getters;
    }

    public JavaBeanSerializer(Class<?> clazz){
        this(clazz, (Map<String, String>) null);

        String key = "";
        JSONType annotation = clazz.getAnnotation(JSONType.class);
        if (annotation != null) {
            key = annotation.key();
        }

        if (key.length() == 0) {
            for (FieldSerializer field : getters) {
                if ("id".equals(field.getName())) {
                    keyField = field;
                    key = "id";
                    break;
                }
            }
        } else {
            for (FieldSerializer field : getters) {
                if (key.equals(field.getName())) {
                    keyField = field;
                    break;
                }
            }
        }
    }

    public JavaBeanSerializer(Class<?> clazz, String... aliasList){
        this(clazz, createAliasMap(aliasList));
    }

    static Map<String, String> createAliasMap(String... aliasList) {
        Map<String, String> aliasMap = new HashMap<String, String>();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }

        return aliasMap;
    }

    public JavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap){
        List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

        List<FieldInfo> fieldInfoList = computeGetters(clazz, aliasMap);

        for (FieldInfo fieldInfo : fieldInfoList) {
            getterList.add(createFieldSerializer(fieldInfo));
        }

        //
        getters = getterList.toArray(new FieldSerializer[getterList.size()]);
    }

    protected boolean isWriteClassName(JSONSerializer serializer) {
        return serializer.isEnabled(SerializerFeature.WriteClassName);
    }

    public void write(JSONSerializer serializer, Object object) throws IOException {
        Object parent = serializer.getParent();

        serializer.setParent(object);
        writeInternal(serializer, parent, object);

        serializer.setParent(parent);
    }

    private void writeInternal(JSONSerializer serializer, Object parent, Object object) {
        SerializeWriter out = serializer.getWriter();

        if (object == null) {
            out.writeNull();
            return;
        }

        if (managedReference) {
            if (serializer.containsReference(object)) {
                writeReference(serializer, parent, object);
                return;
            }

            serializer.addReference(object);
        }

        FieldSerializer[] getters = this.getters;

        if (out.isEnabled(SerializerFeature.SortField)) {
            Arrays.sort(getters);
        }

        try {
            out.append('{');

            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.incrementIndent();
                serializer.println();
            }

            boolean commaFlag = false;

            if (isWriteClassName(serializer)) {
                out.writeFieldName("class");
                serializer.write(object.getClass());
                commaFlag = true;
            }

            for (int i = 0; i < getters.length; ++i) {
                FieldSerializer fieldSerializer = getters[i];

                if (serializer.isEnabled(SerializerFeature.SkipTransientField)) {
                    Field field = fieldSerializer.getField();
                    if (field != null) {
                        if (Modifier.isTransient(field.getModifiers())) {
                            continue;
                        }
                    }
                }

                Object propertyValue = fieldSerializer.getPropertyValue(object);

                if (!FilterUtils.apply(serializer, object, fieldSerializer.getName(), propertyValue)) {
                    continue;
                }

                String key = FilterUtils.processKey(serializer, object, fieldSerializer.getName(), propertyValue);

                Object originalValue = propertyValue;
                propertyValue = FilterUtils.processValue(serializer, object, fieldSerializer.getName(), propertyValue);

                if (propertyValue == null) {
                    if ((!fieldSerializer.isWriteNull())
                        && (!serializer.isEnabled(SerializerFeature.WriteMapNullValue))) {
                        continue;
                    }
                }

                if (commaFlag) {
                    out.append(',');
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.println();
                    }
                }

                if (key != fieldSerializer.getName()) {
                    out.writeFieldName(key);
                    serializer.write(propertyValue);
                } else if (originalValue != propertyValue) {
                    fieldSerializer.writePrefix(serializer);
                    serializer.write(propertyValue);
                } else {
                    fieldSerializer.writeProperty(serializer, propertyValue);
                }

                commaFlag = true;
            }

            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.decrementIdent();
                serializer.println();
            }

            out.append('}');
        } catch (Exception e) {
            throw new JSONException("write javaBean error", e);
        }
    }

    public void writeReference(JSONSerializer serializer, Object parent, Object object) {
        SerializeWriter out = serializer.getWriter();

        if (object == parent) {
            out.write("{\"$ref\":\"#\"}");
        } else {
            out.write("{\"$ref\":");
            try {
                Object key = keyField.getPropertyValue(object);
                serializer.write(key);
            } catch (Exception e) {
                throw new JSONException("get keyField error", e);
            }
            out.write("}");
        }

    }

    public FieldSerializer createFieldSerializer(FieldInfo fieldInfo) {
        Class<?> clazz = fieldInfo.getMethod().getReturnType();

        if (clazz == Number.class) {
            return new NumberFieldSerializer(fieldInfo);
        }

        return new ObjectFieldSerializer(fieldInfo);
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, Map<String, String> aliasMap) {
        List<FieldInfo> getters = new ArrayList<FieldInfo>();

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }

            if (method.getParameterTypes().length != 0) {
                continue;
            }

            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation != null) {
                if (!annotation.serialize()) {
                    continue;
                }

                if (annotation.name().length() != 0) {
                    String propertyName = annotation.name();

                    if (aliasMap != null) {
                        propertyName = aliasMap.get(propertyName);
                        if (propertyName == null) {
                            continue;
                        }
                    }

                    getters.add(new FieldInfo(propertyName, method, null));
                    continue;
                }
            }

            if (methodName.startsWith("get")) {
                if (methodName.length() < 4) {
                    continue;
                }

                if (methodName.equals("getClass")) {
                    continue;
                }

                if (!Character.isUpperCase(methodName.charAt(3))) {
                    continue;
                }

                String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);

                Field field = ParserConfig.getField(clazz, propertyName);
                if (field != null) {
                    JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

                    if (fieldAnnotation != null && fieldAnnotation.name().length() != 0) {
                        propertyName = fieldAnnotation.name();

                        if (aliasMap != null) {
                            propertyName = aliasMap.get(propertyName);
                            if (propertyName == null) {
                                continue;
                            }
                        }
                    }
                }

                if (aliasMap != null) {
                    propertyName = aliasMap.get(propertyName);
                    if (propertyName == null) {
                        continue;
                    }
                }

                getters.add(new FieldInfo(propertyName, method, field));
            }

            if (methodName.startsWith("is")) {
                if (methodName.length() < 3) {
                    continue;
                }

                if (!Character.isUpperCase(methodName.charAt(2))) {
                    continue;
                }

                String propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);

                Field field = ParserConfig.getField(clazz, propertyName);
                if (field != null) {
                    JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

                    if (fieldAnnotation != null && fieldAnnotation.name().length() != 0) {
                        propertyName = fieldAnnotation.name();

                        if (aliasMap != null) {
                            propertyName = aliasMap.get(propertyName);
                            if (propertyName == null) {
                                continue;
                            }
                        }
                    }
                }

                if (aliasMap != null) {
                    propertyName = aliasMap.get(propertyName);
                    if (propertyName == null) {
                        continue;
                    }
                }

                getters.add(new FieldInfo(propertyName, method, field));
            }
        }

        return getters;
    }
}
