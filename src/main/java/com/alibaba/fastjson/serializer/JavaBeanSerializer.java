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
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JavaBeanSerializer implements ObjectSerializer {
    private static final char[]     true_chars = new char[] {'t', 'r', 'u', 'e'};
    private static final char[]     false_chars = new char[] {'f', 'a', 'l', 's', 'e'};
    
    // serializers
    private final FieldSerializer[] getters;
    private final FieldSerializer[] sortedGetters;
    
    protected int                   features = 0;
    
    protected final String          typeName;
    protected final String          typeKey;
    
    public JavaBeanSerializer(Class<?> clazz) {
        this(clazz, (PropertyNamingStrategy) null);
    }
    
    public JavaBeanSerializer(Class<?> clazz, PropertyNamingStrategy propertyNamingStrategy){
        this(clazz, clazz.getModifiers(), (Map<String, String>) null, false, true, true, true, propertyNamingStrategy);
    }

    public JavaBeanSerializer(Class<?> clazz, String... aliasList){
        this(clazz, clazz.getModifiers(), map(aliasList), false, true, true, true, null);
    }

    private static Map<String, String> map(String... aliasList) {
        Map<String, String> aliasMap = new HashMap<String, String>();
        for (String alias : aliasList) {
            aliasMap.put(alias, alias);
        }

        return aliasMap;
    }

    /**
     * @since 1.1.49.android
     * @param clazz
     * @param classModifiers
     * @param aliasMap
     * @param fieldOnly
     * @param jsonTypeSupport
     * @param jsonFieldSupport
     * @param fieldGenericSupport
     */
    public JavaBeanSerializer(Class<?> clazz, // 
                              int classModifiers, //
                              Map<String, String> aliasMap, // 
                              boolean fieldOnly, //
                              boolean jsonTypeSupport, // 
                              boolean jsonFieldSupport, //
                              boolean fieldGenericSupport, //
                              PropertyNamingStrategy propertyNamingStrategy
                              ){
        JSONType jsonType = jsonTypeSupport //
            ? clazz.getAnnotation(JSONType.class) //
            : null;

        String typeName = null, typeKey = null;
        if (jsonType != null) {
            features = SerializerFeature.of(jsonType.serialzeFeatures());
            
            typeName = jsonType.typeName();
            if (typeName.length() == 0) {
                typeName = null;
            } else {
                for (Class<?> supperClass = clazz.getSuperclass()
                     ; supperClass != null && supperClass != Object.class
                     ; supperClass = supperClass.getSuperclass()) {
                    JSONType superJsonType = supperClass.getAnnotation(JSONType.class);
                    if (superJsonType == null) {
                        break;
                    }

                    typeKey = superJsonType.typeKey();
                    if (typeKey.length() != 0) {
                        break;
                    }
                }

                for (Class<?> interfaceClass : clazz.getInterfaces()) {
                    JSONType superJsonType = interfaceClass.getAnnotation(JSONType.class);
                    if (superJsonType != null) {
                        typeKey = superJsonType.typeKey();
                        if (typeKey.length() != 0) {
                            break;
                        }
                    }
                }
                if (typeKey != null && typeKey.length() == 0) {
                    typeKey = null;
                }
            }
        }
        this.typeName = typeName;
        this.typeKey = typeKey;
        
        {
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, // 
                                                                     classModifiers, //
                                                                     fieldOnly, //
                                                                     jsonType, // 
                                                                     aliasMap, // 
                                                                     false, // sorted = false
                                                                     jsonFieldSupport, // 
                                                                     fieldGenericSupport, //
                                                                     propertyNamingStrategy);
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

            for (FieldInfo fieldInfo : fieldInfoList) {
                FieldSerializer fieldDeser = new FieldSerializer(fieldInfo);
                
                getterList.add(fieldDeser);
            }

            getters = getterList.toArray(new FieldSerializer[getterList.size()]);
        }
        
        String[] orders = null;

        if (jsonType != null) {
            orders = jsonType.orders();
        }
        
        if (orders != null && orders.length != 0) {
            List<FieldInfo> fieldInfoList = TypeUtils.computeGetters(clazz, //
                                                                     classModifiers, //
                                                                     fieldOnly, //
                                                                     jsonType, //
                                                                     aliasMap, //
                                                                     true, // sorted = true
                                                                     jsonFieldSupport, //
                                                                     fieldGenericSupport, //
                                                                     propertyNamingStrategy);
            List<FieldSerializer> getterList = new ArrayList<FieldSerializer>();

            for (FieldInfo fieldInfo : fieldInfoList) {
                FieldSerializer fieldDeser = new FieldSerializer(fieldInfo);
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

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if ((serializer.context == null //
             || (serializer.context.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) //
            && serializer.references != null //
            && serializer.references.containsKey(object)) {
            serializer.writeReference(object);
            return;
        }

        final FieldSerializer[] getters;

        if ((out.features & SerializerFeature.SortField.mask) != 0) {
            getters = this.sortedGetters;
        } else {
            getters = this.getters;
        }

        SerialContext parent = serializer.context;
//        serializer.setContext(parent, object, fieldName, features);
        if ((out.features & SerializerFeature.DisableCircularReferenceDetect.mask) == 0) {
            serializer.context = new SerialContext(parent, object, fieldName, features);
            if (serializer.references == null) {
                serializer.references = new IdentityHashMap<Object, SerialContext>();
            }
            serializer.references.put(object, serializer.context);
        }

        boolean writeAsArray;
        
        writeAsArray = (features & SerializerFeature.BeanToArray.mask) != 0 //
                       || (out.features & SerializerFeature.BeanToArray.mask) != 0;

        try {
            final char startSeperator = writeAsArray ? '[' : '{';
            final char endSeperator = writeAsArray ? ']' : '}';
            // out.write(startSeperator);
            {
                int newcount = out.count + 1;
                if (newcount > out.buf.length) {
                    if (out.writer == null) {
                        out.expandCapacity(newcount);
                    } else {
                        out.flush();
                        newcount = 1;
                    }
                }
                out.buf[out.count] = startSeperator;
                out.count = newcount;
            }

            if (getters.length > 0 // 
                    && (out.features & SerializerFeature.PrettyFormat.mask) != 0) {
                serializer.incrementIndent();
                serializer.println();
            }

            boolean commaFlag = false;
            
            boolean isWriteClassName; 
            isWriteClassName = (features & SerializerFeature.WriteClassName.mask) != 0 //
                    || ((out.features & SerializerFeature.WriteClassName.mask) != 0 // 
                            && (fieldType != null || (out.features & SerializerFeature.NotWriteRootClassName.mask) == 0
                            || (serializer.context != null && serializer.context.parent != null)))
                    ;

            if (isWriteClassName) {
                Class<?> objClass = object.getClass();
                if (objClass != fieldType) {
                    out.writeFieldName(typeKey != null ? typeKey : serializer.config.typeKey, false);
                    String typeName = this.typeName;
                    if (typeName == null) {
                        typeName = object.getClass().getName();
                    }
                    serializer.write(typeName);
                    commaFlag = true;
                }
            }

            char seperator = commaFlag ? ',' : '\0';

            char newSeperator = seperator;
            if (serializer.beforeFilters != null) {
                for (BeforeFilter beforeFilter : serializer.beforeFilters) {
                    newSeperator = beforeFilter.writeBefore(serializer, object, newSeperator);
                }
            }
            commaFlag = newSeperator == ',';
            
            final boolean directWritePrefix = (out.features & SerializerFeature.QuoteFieldNames.mask) != 0
                    && (out.features & SerializerFeature.UseSingleQuotes.mask) == 0;
            final boolean useSingleQuote = (out.features & SerializerFeature.UseSingleQuotes.mask) != 0;
            final boolean notWriteDefaultValue = (out.features & SerializerFeature.NotWriteDefaultValue.mask) != 0;

            final List<PropertyFilter> propertyFilters = serializer.propertyFilters;
            final List<NameFilter> nameFilters = serializer.nameFilters;
            final List<ValueFilter> valueFilters = serializer.valueFilters;
            final List<PropertyPreFilter> filters = serializer.propertyPreFilters;
            
            for (int i = 0; i < getters.length; ++i) {
                FieldSerializer fieldSerializer = getters[i];
                FieldInfo fieldInfo = fieldSerializer.fieldInfo;
                Class<?> fieldClass = fieldInfo.fieldClass;
                String fieldInfoName = fieldInfo.name;
                if ((out.features & SerializerFeature.SkipTransientField.mask) != 0) {
                    Field field = fieldInfo.field;
                    if (field != null) {
                        if (fieldInfo.fieldTransient) {
                            continue;
                        }
                    }
                }

                if (typeKey != null && typeKey.equals(fieldInfoName)) {
                    continue;
                }

                boolean applyName = true;
                {
                    if (filters != null) {
                        for (PropertyPreFilter filter : filters) {
                            if (!filter.apply(serializer, object, fieldInfoName)) {
                                applyName = false;
                                break;
                            }
                        }
                    }
                }
                if (!applyName) {
                    continue;
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

                if (propertyValueGot && propertyValue == null && !writeAsArray) {
                    if ((!fieldSerializer.writeNull)
                        && (out.features & SerializerFeature.WriteMapNullValue.mask) == 0) {
                        continue;
                    }
                }
                
                if (propertyValueGot && propertyValue != null && notWriteDefaultValue) {
                    if ((fieldClass == byte.class // 
                            || fieldClass == short.class //
                            || fieldClass == int.class //
                            || fieldClass == long.class //
                            || fieldClass == float.class //
                            || fieldClass == double.class //
                            ) //  
                            && propertyValue instanceof Number 
                            && ((Number)propertyValue).byteValue() == 0) {
                        continue;
                    } else if (fieldClass == boolean.class // 
                            && propertyValue instanceof Boolean // 
                            && !((Boolean)propertyValue).booleanValue()) {
                        continue;
                    }
                }

                if (commaFlag) {
                    //out.write(',');
                    {
                        int newcount = out.count + 1;
                        if (newcount > out.buf.length) {
                            if (out.writer == null) {
                                out.expandCapacity(newcount);
                            } else {
                                out.flush();
                                newcount = 1;
                            }
                        }
                        out.buf[out.count] = ',';
                        out.count = newcount;
                    }
                    if ((out.features & SerializerFeature.PrettyFormat.mask) != 0) {
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
                            // out.write(fieldInfo.name_chars, 0, fieldInfo.name_chars.length);
                            {
                                final char[] c = fieldSerializer.name_chars;
                                int off = 0;
                                int len = c.length;
                                
                                int newcount = out.count + len;
                                if (newcount > out.buf.length) {
                                    if (out.writer == null) {
                                        out.expandCapacity(newcount);
                                    } else {
                                        do {
                                            int rest = out.buf.length - out.count;
                                            System.arraycopy(c, off, out.buf, out.count, rest);
                                            out.count = out.buf.length;
                                            out.flush();
                                            len -= rest;
                                            off += rest;
                                        } while (len > out.buf.length);
                                        newcount = len;
                                    }
                                }
                                System.arraycopy(c, off, out.buf, out.count, len);
                                out.count = newcount;
                            }
                        } else {
                            fieldSerializer.writePrefix(serializer);
                        }
                    }

                    if (valueGot && !propertyValueGot) {
                        if (fieldClass == int.class) {
                            // serializer.out.writeInt(propertyValueInt);
                            {
                                if (propertyValueInt == 0x80000000 /*Integer.MIN_VALUE*/) {
                                    out.write("-2147483648");
                                } else {
                                    int size;
                                    final int x = propertyValueInt < 0 ? -propertyValueInt : propertyValueInt;
                                    for (int j = 0;; j++) {
                                        if (x <= SerializeWriter.sizeTable[j]) {
                                            size = j + 1;
                                            break;
                                        }
                                    }

                                    if (propertyValueInt < 0) {
                                        size++;
                                    }
    
                                    boolean flushed = false;
                                    int newcount = out.count + size;
                                    if (newcount > out.buf.length) {
                                        if (out.writer == null) {
                                            out.expandCapacity(newcount);
                                        } else {
                                            char[] chars = new char[size];
                                            SerializeWriter.getChars(propertyValueInt, size, chars);
                                            out.write(chars, 0, chars.length);
                                            flushed = true;
                                        }
                                    }
    
                                    if (!flushed) {
                                        SerializeWriter.getChars(propertyValueInt, newcount, out.buf);
                                        out.count = newcount;
                                    }
                                }
                            }
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
                                    
                                    if (useSingleQuote) {
                                        out.writeStringWithSingleQuote(propertyValueString);
                                    } else {
                                        out.writeStringWithDoubleQuote(propertyValueString, (char) 0, true);
                                    }
                                }
                            } else {
                                if (fieldInfo.isEnum) {
                                    if (propertyValue != null) {
                                        if ((out.features & SerializerFeature.WriteEnumUsingToString.mask) != 0) {
                                            Enum<?> e = (Enum<?>) propertyValue;
                                            
                                            String name = e.toString();
                                            boolean userSingleQuote = (out.features & SerializerFeature.UseSingleQuotes.mask) != 0;
                                            
                                            if (userSingleQuote) {
                                                out.writeStringWithSingleQuote(name);
                                            } else {
                                                out.writeStringWithDoubleQuote(name, (char) 0, false);    
                                            }
                                        } else {
                                            Enum<?> e = (Enum<?>) propertyValue;
                                            out.writeInt(e.ordinal());
                                        }
                                    } else {
                                        out.writeNull();
                                    }
                                } else {
                                    fieldSerializer.writeValue(serializer, propertyValue);
                                }
                            }
                        } else {
                            fieldSerializer.writeValue(serializer, propertyValue);
                        }
                    }
                }

                commaFlag = true;
            }
            
            // JSONSerializer.writeAfter(serializer, object, commaFlag ? ',' : '\0');
            if (serializer.afterFilters != null) {
                char afterOperator = commaFlag ? ',' : '\0';
                for (AfterFilter afterFilter : serializer.afterFilters) {
                    afterOperator = afterFilter.writeAfter(serializer, object, afterOperator);
                }
            }

            if (getters.length > 0 && (out.features & SerializerFeature.PrettyFormat.mask) != 0) {
                serializer.decrementIdent();
                serializer.println();
            }

            // out.write(endSeperator);
            {
                int newcount = out.count + 1;
                if (newcount > out.buf.length) {
                    if (out.writer == null) {
                        out.expandCapacity(newcount);
                    } else {
                        out.flush();
                        newcount = 1;
                    }
                }
                out.buf[out.count] = endSeperator;
                out.count = newcount;
            }
        } catch (Exception e) {
            String errorMessage = "write javaBean error";
            if (fieldName != null) {
                errorMessage += ", fieldName : " + fieldName;
            }
            throw new JSONException(errorMessage, e);
        } finally {
            serializer.context = parent;
        }
    }

    public Map<String, Object> getFieldValuesMap(Object object) throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>(sortedGetters.length);
        
        for (FieldSerializer getter : sortedGetters) {
            map.put(getter.fieldInfo.name, getter.getPropertyValue(object));
        }
        
        return map;
    }
}
