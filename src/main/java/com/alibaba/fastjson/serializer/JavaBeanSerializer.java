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
    private static final char[]       true_chars  = new char[] { 't', 'r', 'u', 'e' };
    private static final char[]       false_chars = new char[] { 'f', 'a', 'l', 's', 'e' };

    // serializers
    private final FieldSerializer[]   getters;
    protected final FieldSerializer[] sortedGetters;

    protected int                     features    = 0;
    
    protected final Class<?>          beanType;

    public JavaBeanSerializer(Class<?> beanType){
        this(beanType, (Map<String, String>) null);
    }

    public JavaBeanSerializer(Class<?> beanType, String... aliasList){
        this(beanType, createAliasMap(aliasList));
    }

    static Map<String, String> createAliasMap(String... aliasList) {
        Map<String, String> aliasMap = new HashMap<String, String>();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }

        return aliasMap;
    }
    
    public JavaBeanSerializer(Class<?> beanType, Map<String, String> aliasMap){
        this(beanType, aliasMap, TypeUtils.getSerializeFeatures(beanType));
    }

    public JavaBeanSerializer(Class<?> beanType, Map<String, String> aliasMap, int features){
        this.features = features;
        this.beanType = beanType;
        
        JSONType jsonType = beanType.getAnnotation(JSONType.class);
        
        if (jsonType != null) {
            features = SerializerFeature.of(jsonType.serialzeFeatures());
        }

        {
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(beanType, jsonType, aliasMap, false);

            for (FieldInfo fieldInfo : fieldInfoList) {
                getterList.add(new FieldSerializer(beanType, fieldInfo));
            }

            getters = getterList.toArray(new FieldSerializer[getterList.size()]);
        }
        
        String[] orders = null;

        if (jsonType != null) {
            orders = jsonType.orders();
        }
        
        if (orders != null && orders.length != 0) {
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(beanType, jsonType, aliasMap, true);
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

            for (FieldInfo fieldInfo : fieldInfoList) {
                FieldSerializer fieldDeser = new FieldSerializer(beanType, fieldInfo);
                getterList.add(fieldDeser);
            }

            sortedGetters = getterList.toArray(new FieldSerializer[getterList.size()]);
        } else {
            FieldSerializer[] sortedGetters = new FieldSerializer[getters.length];
            System.arraycopy(getters, 0, sortedGetters, 0, getters.length);
            Arrays.sort(sortedGetters);
            
            if (Arrays.equals(sortedGetters, getters)) {
                this.sortedGetters = getters; 
            } else {
                this.sortedGetters = sortedGetters;
            }
        }
    }

    public void write(JSONSerializer serializer, //
                      Object object, //
                      Object fieldName, //
                      Type fieldType, //
                      int features) throws IOException {
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

            
            final boolean directWritePrefix = out.quoteFieldNames && !out.useSingleQuotes;
            char newSeperator = serializer.writeBefore(object, seperator);
            commaFlag = newSeperator == ',';

            final boolean skipTransient = out.skipTransientField;
            final boolean ignoreNonFieldGetter = out.ignoreNonFieldGetter;
            
            final List<LabelFilter> labelFilters = serializer.labelFilters;
            final List<PropertyFilter> propertyFilters = serializer.propertyFilters;
            final List<NameFilter> nameFilters = serializer.nameFilters;
            final List<ValueFilter> valueFilters = serializer.valueFilters;
            final List<ContextValueFilter> contextValueFilters = serializer.contextValueFilters;
            final List<PropertyPreFilter> filters = serializer.propertyPreFilters;
            
            for (int i = 0; i < getters.length; ++i) {
                FieldSerializer fieldSerializer = getters[i];

                Field field = fieldSerializer.fieldInfo.field;
                FieldInfo fieldInfo = fieldSerializer.fieldInfo;
                String fieldInfoName = fieldInfo.name;
                Class<?> fieldClass = fieldInfo.fieldClass;
                
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

                {
                    boolean apply = true;

                    if (filters != null) {
                        for (PropertyPreFilter filter : filters) {
                            if (!filter.apply(serializer, object, fieldInfo.name)) {
                                apply = false;
                                break;
                            }
                        }
                    }
                    
                    if (!apply) {
                        continue;
                    }
                }
                
                {
                    boolean apply = true;
                    if (labelFilters != null) {
                        for (LabelFilter propertyFilter : labelFilters) {
                            if (!propertyFilter.apply(fieldInfo.label)) {
                                apply = false;
                                break;
                            }
                        }
                    }
                    
                    if (!apply) {
                        continue;
                    }
                }

                Object propertyValue = null;
                int propertyValueInt = 0;
                long propertyValueLong = 0L;
                boolean propertyValueBoolean = false;
                
                boolean propertyValueGot = false;
                boolean valueGot = false;
                if (fieldInfo.fieldAccess) {
                    if (fieldClass == int.class) {
                        propertyValueInt = fieldInfo.field.getInt(object);
                        valueGot = true;
                    } else if (fieldClass == long.class) {
                        propertyValueLong = fieldInfo.field.getLong(object);
                        valueGot = true;
                    } else if (fieldClass == boolean.class) {
                        propertyValueBoolean = fieldInfo.field.getBoolean(object);
                        valueGot = true;
                    } else {
                        propertyValue = fieldInfo.field.get(object);
                        propertyValueGot = true;
                    }
                } else {
                    propertyValue = fieldSerializer.getPropertyValue(object);
                    propertyValueGot = true;
                }

                boolean apply = true;
                {
                    if (propertyFilters != null) {
                        if (valueGot) {
                            if (fieldClass == int.class) {
                                propertyValue = Integer.valueOf(propertyValueInt);
                                propertyValueGot = true;
                            } else if (fieldClass == long.class) {
                                propertyValue = Long.valueOf(propertyValueLong);
                                propertyValueGot = true;
                            } else if (fieldClass == boolean.class) {
                                propertyValue = propertyValueBoolean;
                                propertyValueGot = true;
                            }
                        }
                        
                        for (PropertyFilter propertyFilter : propertyFilters) {
                            if (!propertyFilter.apply(object, fieldInfoName, propertyValue)) {
                                apply = false;
                                break;
                            }
                        }
                    }
                }
                
                if (!apply) {
                    continue;
                }

                String key = fieldInfoName;
                {
                    if (nameFilters != null) {
                        if (valueGot && !propertyValueGot) {
                            if (fieldClass == int.class) {
                                propertyValue = Integer.valueOf(propertyValueInt);
                                propertyValueGot = true;
                            } else if (fieldClass == long.class) {
                                propertyValue = Long.valueOf(propertyValueLong);
                                propertyValueGot = true;
                            } else if (fieldClass == boolean.class) {
                                propertyValue = propertyValueBoolean;
                                propertyValueGot = true;
                            }
                        }
                        
                        for (NameFilter nameFilter : nameFilters) {
                            key = nameFilter.process(object, key, propertyValue);
                        }
                    }
                }
                
                if (out.writeNonStringValueAsString) {
                    if (fieldClass == int.class) {
                        propertyValue = Integer.toString(propertyValueInt);
                        propertyValueGot = true;
                    } else if (fieldClass == long.class) {
                        propertyValue = Long.toString(propertyValueLong);
                        propertyValueGot = true;
                    } else if (fieldClass == boolean.class) {
                        propertyValue = Boolean.toString(propertyValueBoolean);
                        propertyValueGot = true;
                    } else if (fieldClass == String.class) {
                        // skip
                    } else if (propertyValue instanceof Number || propertyValue instanceof Boolean) {
                        propertyValue = propertyValue.toString();
                    }
                }

                Object originalValue = propertyValue;
                {
                    if (valueFilters != null) {
                        if (valueGot && !propertyValueGot) {
                            if (fieldClass == int.class) {
                                propertyValue = Integer.valueOf(propertyValueInt);
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            } else if (fieldClass == long.class) {
                                propertyValue = Long.valueOf(propertyValueLong);
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            } else if (fieldClass == boolean.class) {
                                propertyValue = propertyValueBoolean;
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            }
                        }
                        
                        for (ValueFilter valueFilter : valueFilters) {
                            propertyValue = valueFilter.process(object, fieldInfoName, propertyValue);
                        }
                    }
                }
                {
                    if (contextValueFilters != null) {
                        if (valueGot && !propertyValueGot) {
                            if (fieldClass == int.class) {
                                propertyValue = Integer.valueOf(propertyValueInt);
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            } else if (fieldClass == long.class) {
                                propertyValue = Long.valueOf(propertyValueLong);
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            } else if (fieldClass == boolean.class) {
                                propertyValue = propertyValueBoolean;
                                originalValue = propertyValue;
                                propertyValueGot = true;
                            }
                        }
                        
                        for (ContextValueFilter valueFilter : contextValueFilters) {
                            propertyValue = valueFilter.process(fieldSerializer.fieldContext, object, fieldInfoName, propertyValue);
                        }
                    }
                }

                if (propertyValueGot && propertyValue == null && !writeAsArray) {
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
                    out.write(',');
                    if (out.prettyFormat) {
                        serializer.println();
                    }
                }

                if (key != fieldInfoName) {
                    if (!writeAsArray) {
                        out.writeFieldName(key, true);
                    }
                    
                    serializer.write(propertyValue);
                } else if (originalValue != propertyValue) {
                    if (!writeAsArray) {
                        fieldSerializer.writePrefix(serializer);
                    }
                    serializer.write(propertyValue);
                } else {
                    if (!writeAsArray) {
                        if (directWritePrefix) {
                            out.write(fieldInfo.name_chars, 0, fieldInfo.name_chars.length);
                        } else {
                            fieldSerializer.writePrefix(serializer);
                        }
                    }

                    if (valueGot && !propertyValueGot) {
                        if (fieldClass == int.class) {
                            serializer.out.writeInt(propertyValueInt);
                        } else if (fieldClass == long.class) {
                            serializer.out.writeLong(propertyValueLong);
                        } else if (fieldClass == boolean.class) {
                            if (propertyValueBoolean) {
                                serializer.out.write(true_chars, 0, true_chars.length);    
                            } else {
                                serializer.out.write(false_chars, 0, false_chars.length);
                            }
                        }
                    } else {
                        if (!writeAsArray) {
                            if (fieldClass == String.class) {
                                if (propertyValue == null) {
                                    if ((out.features & SerializerFeature.WriteNullStringAsEmpty.mask) != 0
                                            || (fieldSerializer.features & SerializerFeature.WriteNullStringAsEmpty.mask) != 0
                                            ) {
                                        out.writeString("");
                                    } else {
                                        out.writeNull();
                                    }
                                } else {
                                    String propertyValueString = (String) propertyValue;
                                    
                                    if (out.useSingleQuotes) {
                                        out.writeStringWithSingleQuote(propertyValueString);
                                    } else {
                                        out.writeStringWithDoubleQuote(propertyValueString, (char) 0);
                                    }
                                }
                            } else {
                                fieldSerializer.writeValue(serializer, propertyValue);
                            }
                        } else {
                            fieldSerializer.writeValue(serializer, propertyValue);
                        }
                    }
                }

                commaFlag = true;
            }

            serializer.writeAfter(object, commaFlag ? ',' : '\0');

            if (getters.length > 0 && out.prettyFormat) {
                serializer.decrementIdent();
                serializer.println();
            }

            out.append(endSeperator);
        } catch (Exception e) {
            String errorMessage = "write javaBean error";
            if (object != null) {
                errorMessage += ", class " + object.getClass().getName();
            }
            if (fieldName != null) {
                errorMessage += ", fieldName : " + fieldName;
            }
            if (e.getMessage() != null) {
                errorMessage += (", " + e.getMessage());
            }
            
            throw new JSONException(errorMessage, e);
        } finally {
            serializer.context = parent;
        }
    }

    public boolean writeReference(JSONSerializer serializer, Object object, int fieldFeatures) {
        SerialContext context = serializer.context;
        int mask = SerializerFeature.DisableCircularReferenceDetect.mask;
        if (context != null && ((context.features & mask) != 0 || (fieldFeatures & mask) != 0)) {
            return false;
        }

        if (serializer.references != null && serializer.references.containsKey(object)) {
            serializer.writeReference(object);
            return true;
        } else {
            return false;            
        }
    }

    public boolean isWriteAsArray(JSONSerializer serializer) {
        return (features & SerializerFeature.BeanToArray.mask) != 0 || serializer.out.beanToArray;
    }
    
    public FieldSerializer getFieldSerializer(String key) {
        if (key == null) {
            return null;
        }
        
        int low = 0;
        int high = sortedGetters.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            
            String fieldName = sortedGetters[mid].fieldInfo.name;
            
            int cmp = fieldName.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return sortedGetters[mid]; // key found
            }
        }
        
        return null;  // key not found.
    }
    
    public List<Object> getFieldValues(Object object) throws Exception {
        List<Object> fieldValues = new ArrayList<Object>(sortedGetters.length);
        for (FieldSerializer getter : sortedGetters) {
            fieldValues.add(getter.getPropertyValue(object));
        }
        
        return fieldValues;
    }
}
