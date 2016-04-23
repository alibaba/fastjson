package com.alibaba.fastjson.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.alibaba.fastjson.annotation.JSONField;

public class FieldInfo implements Comparable<FieldInfo> {

    public final String     name;
    public final Method     method;
    public final Field      field;

    public final boolean    fieldAccess;
    public final boolean    fieldTransient;

    private int             ordinal = 0;
    public final Class<?>   fieldClass;
    public final Type       fieldType;
    public final Class<?>   declaringClass;
    public final boolean    getOnly;

    private final JSONField fieldAnnotation;
    private final JSONField methodAnnotation;

    public final char[]     name_chars;

    public final boolean    isEnum;

    public FieldInfo(String name, // 
                     Class<?> declaringClass, // 
                     Class<?> fieldClass, // 
                     Type fieldType, // 
                     Field field, //
                     int ordinal, // 
                     int serialzeFeatures){
        this.name = name;
        this.declaringClass = declaringClass;
        this.fieldClass = fieldClass;
        this.fieldType = fieldType;
        this.method = null;
        this.field = field;
        this.ordinal = ordinal;
        
        isEnum = fieldClass.isEnum();
        
        fieldAnnotation = null;
        methodAnnotation = null;
        
        if (field != null) {
            int modifiers = field.getModifiers();
            fieldAccess = (modifiers & Modifier.PUBLIC) != 0 || method == null;
            fieldTransient = Modifier.isTransient(modifiers);
        } else {
            fieldAccess = false;
            fieldTransient = false;
        }
        getOnly = false;
        
        int nameLen = this.name.length();
        name_chars = new char[nameLen + 3];
        this.name.getChars(0, this.name.length(), name_chars, 1);
        name_chars[0] = '"';
        name_chars[nameLen + 1] = '"';
        name_chars[nameLen + 2] = ':';
    }

    public FieldInfo(String name, // 
                     Method method, // 
                     Field field, // 
                     Class<?> clazz, // 
                     Type type, // 
                     int ordinal, //
                     int serialzeFeatures, // 
                     JSONField methodAnnotation, // 
                     JSONField fieldAnnotation, //
                     boolean fieldGenericSupport){
        this.name = name;
        this.method = method;
        this.field = field;
        this.ordinal = ordinal;
        this.methodAnnotation = methodAnnotation;
        this.fieldAnnotation = fieldAnnotation;

        if (field != null) {
            int modifiers = field.getModifiers();
            fieldAccess = method == null || (modifiers & Modifier.PUBLIC) != 0;
            fieldTransient = (modifiers & Modifier.TRANSIENT) != 0;
        } else {
            fieldAccess = false;
            fieldTransient = false;
        }
        
        int nameLen = this.name.length();
        name_chars = new char[nameLen + 3];
        this.name.getChars(0, this.name.length(), name_chars, 1);
        name_chars[0] = '"';
        name_chars[nameLen + 1] = '"';
        name_chars[nameLen + 2] = ':';
        
        Type fieldType;
        Class<?> fieldClass;
        if (method != null) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                fieldClass = parameterTypes[0];
                if (fieldClass == Class.class // 
                        || fieldClass == String.class //
                        || fieldClass.isPrimitive()) {
                    fieldType = fieldClass;
                } else {
                    fieldType = fieldGenericSupport ? method.getGenericParameterTypes()[0] : fieldClass;
                }
                getOnly = false;
            } else {
                fieldClass = method.getReturnType();
                if (fieldClass == Class.class) {
                    fieldType = fieldClass;
                } else {
                    fieldType = fieldGenericSupport ? method.getGenericReturnType() : fieldClass;
                }
                getOnly = true;
            }
            this.declaringClass = method.getDeclaringClass();
        } else {
            fieldClass = field.getType();
            if (fieldClass.isPrimitive() // 
                    || fieldClass == String.class // 
                    || fieldClass.isEnum()) {
                fieldType = fieldClass;
            } else {
                fieldType = fieldGenericSupport ? field.getGenericType() : fieldClass;    
            }
            
            this.declaringClass = field.getDeclaringClass();
            getOnly = false;
        }

        if (clazz != null // 
                && fieldClass == Object.class //
                && fieldType instanceof TypeVariable) {
            
            TypeVariable<?> tv = (TypeVariable<?>) fieldType;
            Type genericFieldType = getInheritGenericType(clazz, tv);
            if (genericFieldType != null) {
                this.fieldClass = TypeUtils.getClass(genericFieldType);
                this.fieldType = genericFieldType;
                
                isEnum = fieldClass.isEnum();
                return;
            }
        }

        Type genericFieldType = fieldType;
        
        if (!(fieldType instanceof Class)) {
            genericFieldType = getFieldType(clazz, type, fieldType);
            if (genericFieldType != fieldType) {
                if (genericFieldType instanceof ParameterizedType) {
                    fieldClass = TypeUtils.getClass(genericFieldType);
                } else if (genericFieldType instanceof Class) {
                    fieldClass = TypeUtils.getClass(genericFieldType);
                }
            }
        }

        this.fieldType = genericFieldType;
        this.fieldClass = fieldClass;
        
        isEnum = (!fieldClass.isArray()) && fieldClass.isEnum();
    }

    public static Type getFieldType(Class<?> clazz, Type type, Type fieldType) {
        if (clazz == null || type == null) {
            return fieldType;
        }
        
        if (fieldType instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) fieldType;
            Type componentType = genericArrayType.getGenericComponentType();
            Type componentTypeX = getFieldType(clazz, type, componentType);
            if (componentType != componentTypeX) {
                Type fieldTypeX = Array.newInstance(TypeUtils.getClass(componentTypeX), 0).getClass();
                return fieldTypeX;
            }

            return fieldType;
        }
        
        if (!TypeUtils.isGenericParamType(type)) {
            return fieldType;
        }

        if (fieldType instanceof TypeVariable) {
            ParameterizedType paramType = (ParameterizedType) TypeUtils.getGenericParamType(type);
            Class<?> parameterizedClass = TypeUtils.getClass(paramType);
            TypeVariable<?> typeVar = (TypeVariable<?>) fieldType;

            for (int i = 0; i < parameterizedClass.getTypeParameters().length; ++i) {
                if (parameterizedClass.getTypeParameters()[i].getName().equals(typeVar.getName())) {
                    fieldType = paramType.getActualTypeArguments()[i];
                    return fieldType;
                }
            }
        }

        if (fieldType instanceof ParameterizedType) {
            ParameterizedType parameterizedFieldType = (ParameterizedType) fieldType;

            Type[] arguments = parameterizedFieldType.getActualTypeArguments();
            boolean changed = false;
            for (int i = 0; i < arguments.length; ++i) {
                Type feildTypeArguement = arguments[i];
                if (feildTypeArguement instanceof TypeVariable) {
                    TypeVariable<?> typeVar = (TypeVariable<?>) feildTypeArguement;

                    if (type instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) type;
                        for (int j = 0; j < clazz.getTypeParameters().length; ++j) {
                            if (clazz.getTypeParameters()[j].getName().equals(typeVar.getName())) {
                                arguments[i] = parameterizedType.getActualTypeArguments()[j];
                                changed = true;
                            }
                        }
                    }
                }
            }
            if (changed) {
                fieldType = new ParameterizedTypeImpl(arguments, parameterizedFieldType.getOwnerType(),
                                                      parameterizedFieldType.getRawType());
                return fieldType;
            }
        }

        return fieldType;
    }

    public static Type getInheritGenericType(Class<?> clazz, TypeVariable<?> tv) {
        Type type = null;
        GenericDeclaration gd = tv.getGenericDeclaration();
        do {
            type = clazz.getGenericSuperclass();
            if (type == null) {
                return null;
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                if (ptype.getRawType() == gd) {
                    TypeVariable<?>[] tvs = gd.getTypeParameters();
                    Type[] types = ptype.getActualTypeArguments();
                    for (int i = 0; i < tvs.length; i++) {
                        if (tvs[i] == tv) return types[i];
                    }
                    return null;
                }
            }
            clazz = TypeUtils.getClass(type);
        } while (type != null);
        return null;
    }

    public String toString() {
        return this.name;
    }

    public int compareTo(FieldInfo o) {
        if (this.ordinal < o.ordinal) {
            return -1;
        }

        if (this.ordinal > o.ordinal) {
            return 1;
        }

        return this.name.compareTo(o.name);
    }
    
    public boolean equals(FieldInfo o) {
        if (o == this) {
            return true;
        }
        return this.compareTo(o) == 0;
    }
    
    public JSONField getAnnotation() {
        if (this.fieldAnnotation != null) {
            return this.fieldAnnotation;
        }
        
        return this.methodAnnotation;
    }

    public Object get(Object javaObject) throws IllegalAccessException, InvocationTargetException {
        if (fieldAccess) {
            return field.get(javaObject);
        }
        
        Object value = method.invoke(javaObject, new Object[0]);
        return value;
    }

    public void set(Object javaObject, Object value) throws IllegalAccessException, InvocationTargetException {
        if (method != null) {
            method.invoke(javaObject, new Object[] { value });
            return;
        }

        field.set(javaObject, value);
    }
}
