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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JavaBeanSerializer implements ObjectSerializer {

    // serializers
    private final FieldSerializer[]                getters;
    private final FieldSerializer[]                sortedGetters;
    private transient Map<String, FieldSerializer> getterMap;

    protected int                                  features = 0;

    public FieldSerializer[] getGetters() {
        return getters;
    }

    public JavaBeanSerializer(Class<?> clazz){
        this(clazz, (Map<String, String>) null);
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
        this(clazz, aliasMap, TypeUtils.getSerializeFeatures(clazz));
    }

    public JavaBeanSerializer(Class<?> clazz, Map<String, String> aliasMap, int features){
        this.features = features;
        
        JSONType jsonType = clazz.getAnnotation(JSONType.class);
        
        if (jsonType != null) {
            features = SerializerFeature.of(jsonType.serialzeFeatures());
        }

        {
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, jsonType, aliasMap, false);

            for (FieldInfo fieldInfo : fieldInfoList) {
                getterList.add(createFieldSerializer(fieldInfo));
            }

            getters = getterList.toArray(new FieldSerializer[getterList.size()]);
        }
        
        String[] orders = null;

        if (jsonType != null) {
            orders = jsonType.orders();
        }
        
        if (orders != null && orders.length != 0) {
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, jsonType, aliasMap, true);
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

            for (FieldInfo fieldInfo : fieldInfoList) {
                getterList.add(createFieldSerializer(fieldInfo));
            }

            sortedGetters = getterList.toArray(new FieldSerializer[getterList.size()]);
        } else {
            sortedGetters = new FieldSerializer[getters.length];
            System.arraycopy(getters, 0, sortedGetters, 0, getters.length);
            Arrays.sort(sortedGetters);
        }
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
                                                                                                               throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if (writeReference(serializer, object, features)) {
            return;
        }

        final FieldSerializer[] getters;

        if (out.sortField) {
            getters = this.sortedGetters;
        } else {
            getters = this.getters;
        }

        SerialContext parent = serializer.context;
        serializer.setContext(parent, object, fieldName, this.features, features);

        final boolean writeAsArray = isWriteAsArray(serializer);

        try {
            final char startSeperator = writeAsArray ? '[' : '{';
            final char endSeperator = writeAsArray ? ']' : '}';
            out.append(startSeperator);

            if (getters.length > 0 && out.prettyFormat) {
                serializer.incrementIndent();
                serializer.println();
            }

            boolean commaFlag = false;

            if ((this.features & SerializerFeature.WriteClassName.mask) != 0 
                    || serializer.isWriteClassName(fieldType, object)) {
                Class<?> objClass = object.getClass();
                if (objClass != fieldType) {
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY, false);
                    serializer.write(object.getClass());
                    commaFlag = true;
                }
            }

            char seperator = commaFlag ? ',' : '\0';

            char newSeperator = FilterUtils.writeBefore(serializer, object, seperator);
            commaFlag = newSeperator == ',';

            final boolean skipTransient = out.skipTransientField;
            final boolean ignoreNonFieldGetter = out.ignoreNonFieldGetter;
            for (int i = 0; i < getters.length; ++i) {
                FieldSerializer fieldSerializer = getters[i];

                Field field = fieldSerializer.fieldInfo.field;
                FieldInfo fieldInfo = fieldSerializer.fieldInfo;
                if (skipTransient) {
                    if (field != null) {
                        if (fieldInfo.fieldTransient) {
                            continue;
                        }
                    }
                }
                
                if (ignoreNonFieldGetter) {
                    if (field == null) {
                        continue;
                    }
                }

                if (!FilterUtils.applyName(serializer, object, fieldInfo.name)) {
                    continue;
                }
                
                if (!FilterUtils.applyLabel(serializer, fieldInfo.label)) {
                    continue;
                }

                Object propertyValue = fieldSerializer.getPropertyValue(object);

                if (!FilterUtils.apply(serializer, object, fieldInfo.name, propertyValue)) {
                    continue;
                }

                String key = FilterUtils.processKey(serializer, object, fieldInfo.name, propertyValue);

                Object originalValue = propertyValue;
                propertyValue = FilterUtils.processValue(serializer, object, fieldInfo.name, propertyValue);

                if (propertyValue == null && !writeAsArray) {
                    if ((!fieldSerializer.writeNull)
                        && (!out.writeMapNullValue)) {
                        continue;
                    }
                }

                if (propertyValue != null && out.notWriteDefaultValue) {
                    Class<?> fieldCLass = fieldInfo.fieldClass;
                    if (fieldCLass == byte.class && propertyValue instanceof Byte
                        && ((Byte) propertyValue).byteValue() == 0) {
                        continue;
                    } else if (fieldCLass == short.class && propertyValue instanceof Short
                               && ((Short) propertyValue).shortValue() == 0) {
                        continue;
                    } else if (fieldCLass == int.class && propertyValue instanceof Integer
                               && ((Integer) propertyValue).intValue() == 0) {
                        continue;
                    } else if (fieldCLass == long.class && propertyValue instanceof Long
                               && ((Long) propertyValue).longValue() == 0L) {
                        continue;
                    } else if (fieldCLass == float.class && propertyValue instanceof Float
                               && ((Float) propertyValue).floatValue() == 0F) {
                        continue;
                    } else if (fieldCLass == double.class && propertyValue instanceof Double
                               && ((Double) propertyValue).doubleValue() == 0D) {
                        continue;
                    } else if (fieldCLass == boolean.class && propertyValue instanceof Boolean
                               && !((Boolean) propertyValue).booleanValue()) {
                        continue;
                    }
                }

                if (commaFlag) {
                    out.append(',');
                    if (out.prettyFormat) {
                        serializer.println();
                    }
                }

                if (key != fieldSerializer.fieldInfo.name) {
                    if (!writeAsArray) {
                        out.writeFieldName(key);
                    }
                    serializer.write(propertyValue);
                } else if (originalValue != propertyValue) {
                    if (!writeAsArray) {
                        fieldSerializer.writePrefix(serializer);
                    }
                    serializer.write(propertyValue);
                } else {
                    if (!writeAsArray) {
                        fieldSerializer.writeProperty(serializer, propertyValue);
                    } else {
                        fieldSerializer.writeValue(serializer, propertyValue);
                    }
                }

                commaFlag = true;
            }

            FilterUtils.writeAfter(serializer, object, commaFlag ? ',' : '\0');

            if (getters.length > 0 && out.prettyFormat) {
                serializer.decrementIdent();
                serializer.println();
            }

            out.append(endSeperator);
        } catch (Exception e) {
            throw new JSONException("write javaBean error", e);
        } finally {
            serializer.context = parent;
        }
    }

    public boolean writeReference(JSONSerializer serializer, Object object, int fieldFeatures) {
        {
            SerialContext context = serializer.context;
            int mask = SerializerFeature.DisableCircularReferenceDetect.mask;
            if (context != null
                && ((context.features & mask) != 0 || (fieldFeatures & mask) != 0)) {
                return false;
            }
        }

        if (!serializer.containsReference(object)) {
            return false;
        }

        serializer.writeReference(object);
        return true;
    }

    public FieldSerializer createFieldSerializer(FieldInfo fieldInfo) {
        Class<?> clazz = fieldInfo.fieldClass;

        if (clazz == Number.class) {
            return new NumberFieldSerializer(fieldInfo);
        }

        return new ObjectFieldSerializer(fieldInfo);
    }

    public boolean isWriteAsArray(JSONSerializer serializer) {
        return (features & SerializerFeature.BeanToArray.mask) != 0 || serializer.out.beanToArray;
    }
    
    public Map<String, FieldSerializer> getGetterMap() {
        if (getterMap == null) {
            HashMap<String, FieldSerializer> map = new HashMap<String, FieldSerializer>(getters.length);
            for (FieldSerializer getter : sortedGetters) {
                map.put(getter.fieldInfo.name, getter);
            }
            getterMap = map;
        }
        return getterMap;
    }
    
    public Object getFieldValue(Object object, String name) throws Exception {
        Map<String, FieldSerializer> map = getGetterMap();
        
        FieldSerializer getter = map.get(name);
        if (getter == null) {
            return null;
        }
        
        return getter.getPropertyValue(object);
    }
    
    public List<Object> getFieldValues(Object object) throws Exception {
        List<Object> fieldValues = new ArrayList<Object>(sortedGetters.length);
        for (FieldSerializer getter : sortedGetters) {
            fieldValues.add(getter.getPropertyValue(object));
        }
        
        return fieldValues;
    }
}
