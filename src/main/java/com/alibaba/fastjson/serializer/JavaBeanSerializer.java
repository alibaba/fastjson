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
import java.lang.reflect.Modifier;
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
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;
    
    private int features = 0;
    
    protected boolean writeClassName = false;
    
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
        JSONType jsonType = clazz.getAnnotation(JSONType.class);
        
        if (jsonType != null) {
            features = SerializerFeature.of(jsonType.serialzeFeatures());
        }
        
        {
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, jsonType, aliasMap, false);
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

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

    protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName) {
        return writeClassName || serializer.isWriteClassName(fieldType, obj);
    }
    
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if (writeReference(serializer, object)) {
            return;
        }

        final FieldSerializer[] getters;

        if (out.isEnabled(SerializerFeature.SortField)) {
            getters = this.sortedGetters;
        } else {
            getters = this.getters;
        }

        SerialContext parent = serializer.context;
        serializer.setContext(parent, object, fieldName, features);

        final boolean writeAsArray = isWriteAsArray(serializer);

        try {
            final char startSeperator = writeAsArray ? '[' : '{';
            final char endSeperator = writeAsArray ? ']' : '}';
            out.append(startSeperator);

            if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.incrementIndent();
                serializer.println();
            }

            boolean commaFlag = false;

            if (isWriteClassName(serializer, object, fieldType, fieldName)) {
                Class<?> objClass = object.getClass();
                if (objClass != fieldType) {
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass());
                    commaFlag = true;
                }
            }

            char seperator = commaFlag ? ',' : '\0';

            char newSeperator = JSONSerializer.writeBefore(serializer, object, seperator);
            commaFlag = newSeperator == ',';

            for (int i = 0; i < getters.length; ++i) {
                FieldSerializer fieldSerializer = getters[i];

                if (out.isEnabled(SerializerFeature.SkipTransientField)) {
                    Field field = fieldSerializer.fieldInfo.field;
                    if (field != null) {
                        if (Modifier.isTransient(field.getModifiers())) {
                            continue;
                        }
                    }
                }

                if (!JSONSerializer.applyName(serializer, object, fieldSerializer.fieldInfo.name)) {
                    continue;
                }

                Object propertyValue = fieldSerializer.getPropertyValue(object);

                if (!JSONSerializer.apply(serializer, object, fieldSerializer.fieldInfo.name, propertyValue)) {
                    continue;
                }

                String key = JSONSerializer.processKey(serializer, object, fieldSerializer.fieldInfo.name, propertyValue);

                Object originalValue = propertyValue;
                propertyValue = JSONSerializer.processValue(serializer, object, fieldSerializer.fieldInfo.name, propertyValue);

                if (propertyValue == null && !writeAsArray) {
                    if ((!fieldSerializer.writeNull)
                        && (!out.isEnabled(SerializerFeature.WriteMapNullValue))) {
                        continue;
                    }
                }
                
                if (propertyValue != null && out.isEnabled(SerializerFeature.NotWriteDefaultValue)) {
                    Class<?> fieldCLass = fieldSerializer.fieldInfo.fieldClass;
                    if (fieldCLass == byte.class && propertyValue instanceof Byte && ((Byte)propertyValue).byteValue() == 0) {
                        continue;
                    } else if (fieldCLass == short.class && propertyValue instanceof Short && ((Short)propertyValue).shortValue() == 0) {
                        continue;
                    } else if (fieldCLass == int.class && propertyValue instanceof Integer && ((Integer)propertyValue).intValue() == 0) {
                        continue;
                    } else if (fieldCLass == long.class && propertyValue instanceof Long && ((Long)propertyValue).longValue() == 0L) {
                        continue;
                    } else if (fieldCLass == float.class && propertyValue instanceof Float && ((Float)propertyValue).floatValue() == 0F) {
                        continue;
                    } else if (fieldCLass == double.class && propertyValue instanceof Double && ((Double)propertyValue).doubleValue() == 0D) {
                        continue;
                    } else if (fieldCLass == boolean.class && propertyValue instanceof Boolean && !((Boolean)propertyValue).booleanValue()) {
                        continue;
                    }
                }

                if (commaFlag) {
                    out.append(',');
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
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
            
            JSONSerializer.writeAfter(serializer, object, commaFlag ? ',' : '\0');

            if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
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
    
    public boolean writeReference(JSONSerializer serializer, Object object) {
        {
            SerialContext context = serializer.context;
            if (context != null && (context.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0) {
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
        if ((features & SerializerFeature.BeanToArray.mask) != 0) {
            return true;
        }
        
        boolean writeAsArray;
        if (serializer.out.isEnabled(SerializerFeature.BeanToArray)) {
            writeAsArray = true;
        } else {
            writeAsArray = false;
        }

        return writeAsArray;
    }
    
   
}
