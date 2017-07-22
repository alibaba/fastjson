package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.FieldInfo;

public abstract class FieldDeserializer {

    public final FieldInfo fieldInfo;

    public final Class<?>  clazz;

    protected Enum[] enums;
    protected long[] enumNameHashCodes;

    public char[] smartMatchKey;
    
    public FieldDeserializer(Class<?> clazz, FieldInfo fieldInfo, int fastMatchToken){
        this.clazz = clazz;
        this.fieldInfo = fieldInfo;

        if (fieldInfo == null) {
            return;
        }

        Class fieldClass = fieldInfo.fieldClass;
        if (fieldClass.isEnum()) {
            Enum[] enums = (Enum[]) fieldClass.getEnumConstants();

            long[] enumNameHashCodes = new long[enums.length];
            this.enumNameHashCodes = new long[enums.length];
            for (int i = 0; i < enums.length; ++i) {
                String name = enums[i].name();
                long hash = 0x811c9dc5;
                for (int j = 0; j < name.length(); ++j) {
                    char ch = name.charAt(j);
                    hash ^= ch;
                    hash *= 0x1000193;
                }
                enumNameHashCodes[i] = hash;
                this.enumNameHashCodes[i] = hash;
            }

            Arrays.sort(this.enumNameHashCodes);

            this.enums = new Enum[enums.length];
            for (int i = 0; i < this.enumNameHashCodes.length; ++i) {
                for (int j = 0; j < enumNameHashCodes.length; ++j) {
                    if (this.enumNameHashCodes[i] == enumNameHashCodes[j]) {
                        this.enums[i] = enums[j];
                        break;
                    }
                }
            }
        }
    }

    public Enum getEnumByHashCode(long hashCode) {
        if (enums == null) {
            return null;
        }

        int enumIndex = Arrays.binarySearch(this.enumNameHashCodes, hashCode);

        if (enumIndex == -1) {
            return null;
        }

        return enums[enumIndex];
    }

    public abstract void parseField(DefaultJSONParser parser, Object object, Type objectType,
                                    Map<String, Object> fieldValues);
    
    public void setValue(Object object, int value) throws IllegalAccessException {
        fieldInfo.field.setInt(object, value);
    }
    
    public void setValue(Object object, long value) throws IllegalAccessException {
        fieldInfo.field.setLong(object, value);
    }
    
    public void setValue(Object object, float value) throws IllegalAccessException {
        fieldInfo.field.setFloat(object, value);
    }
    
    public void setValue(Object object, double value) throws IllegalAccessException {
        fieldInfo.field.setDouble(object, value);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setValue(Object object, Object value) {
        if (value == null) {
            Class<?> fieldClass = fieldInfo.fieldClass;
            if (fieldClass.isPrimitive()) {
                return;
            }
        }
        
        final Field field = fieldInfo.field;
        final Method method = fieldInfo.method;
        try {
            if (fieldInfo.fieldAccess) {
                if (fieldInfo.getOnly) {
                    if (Map.class.isAssignableFrom(fieldInfo.fieldClass)) {
                        Map map = (Map) field.get(object);
                        if (map != null) {
                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) field.get(object);
                        if (collection != null) {
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    field.set(object, value);
                }
            } else {
                if (fieldInfo.getOnly) {
                    if (Map.class.isAssignableFrom(fieldInfo.fieldClass)) {
                        Map map = (Map) method.invoke(object);
                        if (map != null) {
                            map.putAll((Map) value);
                        }
                    } else {
                        Collection collection = (Collection) method.invoke(object);
                        if (collection != null) {
                            collection.addAll((Collection) value);
                        }
                    }
                } else {
                    method.invoke(object, value);
                }
            }
        } catch (Exception e) {
            throw new JSONException("set property error, " + fieldInfo.name, e);
        }
    }
}
