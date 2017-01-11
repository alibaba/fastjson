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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JavaBeanSerializer extends SerializeFilterable implements ObjectSerializer {
    // serializers
    protected final FieldSerializer[] getters;
    protected final FieldSerializer[] sortedGetters;
    
    protected SerializeBeanInfo       beanInfo;
    
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
        this(TypeUtils.buildBeanInfo(beanType, aliasMap, null));
    }
    
    public JavaBeanSerializer(SerializeBeanInfo beanInfo) {
        this.beanInfo = beanInfo;
        
        sortedGetters = new FieldSerializer[beanInfo.sortedFields.length];
        for (int i = 0; i < sortedGetters.length; ++i) {
            sortedGetters[i] = new FieldSerializer(beanInfo.beanType, beanInfo.sortedFields[i]);
        }
        
        if (beanInfo.fields == beanInfo.sortedFields) {
            getters = sortedGetters;
        } else {
            getters = new FieldSerializer[beanInfo.fields.length]; 
            for (int i = 0; i < getters.length; ++i) {
                getters[i] = getFieldSerializer(beanInfo.fields[i].name);
            }
        }
    }

    public void writeDirectNonContext(JSONSerializer serializer, //
                      Object object, //
                      Object fieldName, //
                      Type fieldType, //
                      int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
    }
    
    public void writeAsArray(JSONSerializer serializer, //
                                       Object object, //
                                       Object fieldName, //
                                       Type fieldType, //
                                       int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
    }
    
    public void writeAsArrayNonContext(JSONSerializer serializer, //
                                       Object object, //
                                       Object fieldName, //
                                       Type fieldType, //
                                       int features) throws IOException {
        write(serializer, object, fieldName, fieldType, features);
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
        serializer.setContext(parent, object, fieldName, this.beanInfo.features, features);

        final boolean writeAsArray = isWriteAsArray(serializer, features);

        try {
            final char startSeperator = writeAsArray ? '[' : '{';
            final char endSeperator = writeAsArray ? ']' : '}';
            out.append(startSeperator);

            if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
                serializer.incrementIndent();
                serializer.println();
            }

            boolean commaFlag = false;

            if ((this.beanInfo.features & SerializerFeature.WriteClassName.mask) != 0
                || serializer.isWriteClassName(fieldType, object)) {
                Class<?> objClass = object.getClass();
                if (objClass != fieldType) {
                    writeClassName(serializer, object);
                    commaFlag = true;
                }
            }

            char seperator = commaFlag ? ',' : '\0';

            final boolean directWritePrefix = out.quoteFieldNames && !out.useSingleQuotes;
            char newSeperator = this.writeBefore(serializer, object, seperator);
            commaFlag = newSeperator == ',';

            final boolean skipTransient = out.isEnabled(SerializerFeature.SkipTransientField);
            final boolean ignoreNonFieldGetter = out.isEnabled(SerializerFeature.IgnoreNonFieldGetter);

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

                if ((!this.applyName(serializer, object, fieldInfo.name)) //
                    || !this.applyLabel(serializer, fieldInfo.label)) {
                    continue;
                }


                Object propertyValue;
                
                try {
                    propertyValue = fieldSerializer.getPropertyValueDirect(object);
                } catch (InvocationTargetException ex) {
                    if (out.isEnabled(SerializerFeature.IgnoreErrorGetter)) {
                        propertyValue = null;
                    } else {
                        throw ex;
                    }
                }

                if (!this.apply(serializer, object, fieldInfoName, propertyValue)) {
                    continue;
                }

                String key = fieldInfoName;
                key = this.processKey(serializer, object, key, propertyValue);

                Object originalValue = propertyValue;
                propertyValue = this.processValue(serializer, fieldSerializer.fieldContext, object, fieldInfoName,
                                                        propertyValue);

                if (propertyValue == null && !writeAsArray) {
                    if ((!fieldSerializer.writeNull) && (!out.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES))) {
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
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
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

                    if (!writeAsArray) {
                        JSONField fieldAnnotation = fieldInfo.getAnnotation();
                        if (fieldClass == String.class && (fieldAnnotation == null || fieldAnnotation.serializeUsing() == Void.class)) {
                            if (propertyValue == null) {
                                if ((out.features & SerializerFeature.WriteNullStringAsEmpty.mask) != 0
                                    || (fieldSerializer.features
                                        & SerializerFeature.WriteNullStringAsEmpty.mask) != 0) {
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

                commaFlag = true;
            }

            this.writeAfter(serializer, object, commaFlag ? ',' : '\0');

            if (getters.length > 0 && out.isEnabled(SerializerFeature.PrettyFormat)) {
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

    protected void writeClassName(JSONSerializer serializer, Object object) {
        serializer.out.writeFieldName(serializer.config.typeKey, false);
        String typeName = this.beanInfo.typeName;
        if (typeName == null) {
            Class<?> clazz = object.getClass();

            if (TypeUtils.isProxy(clazz)) {
                clazz = clazz.getSuperclass();
            }

            typeName = clazz.getName();
        }
        serializer.write(typeName);
    }

    public boolean writeReference(JSONSerializer serializer, Object object, int fieldFeatures) {
        SerialContext context = serializer.context;
        int mask = SerializerFeature.DisableCircularReferenceDetect.mask;
        if (context == null || (context.features & mask) != 0 || (fieldFeatures & mask) != 0) {
            return false;
        }

        if (serializer.references != null && serializer.references.containsKey(object)) {
            serializer.writeReference(object);
            return true;
        } else {
            return false;
        }
    }
    
    protected boolean isWriteAsArray(JSONSerializer serializer) {
        return isWriteAsArray(serializer, 0);   
    }

    protected boolean isWriteAsArray(JSONSerializer serializer, int fieldFeatrues) {
        final int mask = SerializerFeature.BeanToArray.mask;
        return (beanInfo.features & mask) != 0 //
                || serializer.out.beanToArray //
                || (fieldFeatrues & mask) != 0;
    }
    
    public Object getFieldValue(Object object, String key) {
        FieldSerializer fieldDeser = getFieldSerializer(key);
        if (fieldDeser == null) {
            throw new JSONException("field not found. " + key);
        }
        
        try {
            return fieldDeser.getPropertyValue(object);
        } catch (InvocationTargetException ex) {
            throw new JSONException("getFieldValue error." + key, ex);
        } catch (IllegalAccessException ex) {
            throw new JSONException("getFieldValue error." + key, ex);
        }
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

        return null; // key not found.
    }

    public List<Object> getFieldValues(Object object) throws Exception {
        List<Object> fieldValues = new ArrayList<Object>(sortedGetters.length);
        for (FieldSerializer getter : sortedGetters) {
            fieldValues.add(getter.getPropertyValue(object));
        }

        return fieldValues;
    }
    
    public int getSize(Object object) throws Exception {
        int size = 0;
        for (FieldSerializer getter : sortedGetters) {
            Object value = getter.getPropertyValueDirect(object);
            if (value != null) {
                size ++;
            }
        }
        return size;
    }
    
    public Map<String, Object> getFieldValuesMap(Object object) throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>(sortedGetters.length);
        
        for (FieldSerializer getter : sortedGetters) {
            map.put(getter.fieldInfo.name, getter.getPropertyValue(object));
        }
        
        return map;
    }

    protected BeanContext getBeanContext(int orinal) {
        return sortedGetters[orinal].fieldContext;
    }
    
    protected Type getFieldType(int ordinal) {
        return sortedGetters[ordinal].fieldInfo.fieldType;
    }
    
    protected char writeBefore(JSONSerializer jsonBeanDeser, //
                            Object object, char seperator) {
        
        if (jsonBeanDeser.beforeFilters != null) {
            for (BeforeFilter beforeFilter : jsonBeanDeser.beforeFilters) {
                seperator = beforeFilter.writeBefore(jsonBeanDeser, object, seperator);
            }
        }
        
        if (this.beforeFilters != null) {
            for (BeforeFilter beforeFilter : this.beforeFilters) {
                seperator = beforeFilter.writeBefore(jsonBeanDeser, object, seperator);
            }
        }
        
        return seperator;
    }
    
    protected char writeAfter(JSONSerializer jsonBeanDeser, // 
                           Object object, char seperator) {
        if (jsonBeanDeser.afterFilters != null) {
            for (AfterFilter afterFilter : jsonBeanDeser.afterFilters) {
                seperator = afterFilter.writeAfter(jsonBeanDeser, object, seperator);
            }
        }
        
        if (this.afterFilters != null) {
            for (AfterFilter afterFilter : this.afterFilters) {
                seperator = afterFilter.writeAfter(jsonBeanDeser, object, seperator);
            }
        }
        
        return seperator;
    }
    
    protected boolean applyLabel(JSONSerializer jsonBeanDeser, String label) {
        if (jsonBeanDeser.labelFilters != null) {
            for (LabelFilter propertyFilter : jsonBeanDeser.labelFilters) {
                if (!propertyFilter.apply(label)) {
                    return false;
                }
            }
        }
        
        if (this.labelFilters != null) {
            for (LabelFilter propertyFilter : this.labelFilters) {
                if (!propertyFilter.apply(label)) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
