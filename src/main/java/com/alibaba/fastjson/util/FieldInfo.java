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
import java.util.Arrays;

import com.alibaba.fastjson.JSONAware;
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

    public final boolean    isEnum;
    
    public final String     format;

    public final long       nameHashCode;

    public final String[]  alternateNames;

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
        
        isEnum = fieldClass.isEnum() && !JSONAware.class.isAssignableFrom(fieldClass);
        
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

        long hashCode = 0x811c9dc5;
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            hashCode ^= c;
            hashCode *= 0x1000193;
        }
        this.nameHashCode = hashCode;
        
        this.format = null;
        this.alternateNames = new String[0];
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
        
        JSONField annotation = getAnnotation();
        String format = null;
        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }

            alternateNames = annotation.alternateNames();
        } else {
            alternateNames = new String[0];
        }
        this.format = format;

        if (field != null) {
            int modifiers = field.getModifiers();
            fieldAccess = method == null || ((modifiers & Modifier.PUBLIC) != 0 && method.getReturnType() == field.getType());
            fieldTransient = (modifiers & Modifier.TRANSIENT) != 0;
        } else {
            fieldAccess = false;
            fieldTransient = false;
        }

        long hashCode = 0x811c9dc5;
        for (int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            hashCode ^= c;
            hashCode *= 0x1000193;
        }
        this.nameHashCode = hashCode;
        
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
            getOnly = Modifier.isFinal(field.getModifiers());
        }

        if (clazz != null // 
                && fieldClass == Object.class //
                && fieldType instanceof TypeVariable) {
            
            TypeVariable<?> tv = (TypeVariable<?>) fieldType;
            Type genericFieldType = null;
            {
                Type[] arguments = null;
                if (type instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) type;
                    arguments = ptype.getActualTypeArguments();
                }

                for (Class<?> c = clazz; c != null && c != Object.class && c != declaringClass; c = c.getSuperclass()) {
                    Type superType = c.getGenericSuperclass();

                    if (superType instanceof ParameterizedType) {
                        ParameterizedType p_superType = (ParameterizedType) superType;
                        Type[] p_superType_args = p_superType.getActualTypeArguments();
                        TypeUtils.getArgument(p_superType_args, c.getTypeParameters(), arguments);
                        arguments = p_superType_args;
                    }
                }

                if (arguments != null) {
                    TypeVariable<?>[] typeVariables = declaringClass.getTypeParameters();
                    for (int j = 0; j < typeVariables.length; ++j) {
                        if (tv.equals(typeVariables[j])) {
                            genericFieldType = arguments[j];
                            break;
                        }
                    }
                }
            }

            if (genericFieldType != null) {
                this.fieldClass = TypeUtils.getClass(genericFieldType);
                this.fieldType = genericFieldType;
                
                isEnum = fieldClass.isEnum() && !JSONAware.class.isAssignableFrom(fieldClass);
                return;
            }
        }

        Type genericFieldType = fieldType;
        
        if (!(fieldType instanceof Class)) {
            genericFieldType = getFieldType(clazz, type != null ? type : clazz, fieldType);
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

        isEnum = (!fieldClass.isArray())
                && fieldClass.isEnum()
                && !JSONAware.class.isAssignableFrom(fieldClass);
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
            TypeVariable<?>[] typeVariables = null;
            Type[] actualTypes = null;
            
            ParameterizedType paramType = null;
            if (type instanceof ParameterizedType) {
                paramType = (ParameterizedType) type;
                typeVariables = clazz.getTypeParameters();
            } else if(clazz.getGenericSuperclass() instanceof ParameterizedType) {
                paramType = (ParameterizedType) clazz.getGenericSuperclass();
                typeVariables = clazz.getSuperclass().getTypeParameters();
            }
            
            for (int i = 0; i < arguments.length && paramType != null; ++i) {
                Type feildTypeArguement = arguments[i];
                if (feildTypeArguement instanceof TypeVariable) {
                    TypeVariable<?> typeVar = (TypeVariable<?>) feildTypeArguement;

                    for (int j = 0; j < typeVariables.length; ++j) {
                        if (typeVariables[j].getName().equals(typeVar.getName())) {
                            if (actualTypes == null) {
                                actualTypes = paramType.getActualTypeArguments();
                            }
                            arguments[i] = actualTypes[j];
                            changed = true;
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
