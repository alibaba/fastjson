package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import com.alibaba.fastjson.annotation.JSONField;

public class FieldInfo implements Comparable<FieldInfo> {

    public final String     name;
    public final Method     method;
    public final Field      field;

    private int             ordinal = 0;
    public final Class<?>   fieldClass;
    public final Type       fieldType;
    public final Class<?>   declaringClass;
    public final boolean    getOnly;
    public final int        serialzeFeatures;
    public final int        parserFeatures;
    public final String     label;

    private final JSONField fieldAnnotation;
    private final JSONField methodAnnotation;

    public final boolean    fieldAccess;
    public final boolean    fieldTransient;

    public final char[]     name_chars;
    
    public final boolean    isEnum;
    public final boolean    jsonDirect;
    public final boolean    unwrapped;
    
    public final String     format;

    public final String[]  alternateNames;
    
    public FieldInfo(String name, // 
                     Class<?> declaringClass, // 
                     Class<?> fieldClass, // 
                     Type fieldType, // 
                     Field field, // 
                     int ordinal, // 
                     int serialzeFeatures, // 
                     int parserFeatures){
        if (ordinal < 0) {
            ordinal = 0;
        }

        this.name = name;
        this.declaringClass = declaringClass;
        this.fieldClass = fieldClass;
        this.fieldType = fieldType;
        this.method = null;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;
        this.parserFeatures = parserFeatures;
        
        isEnum = fieldClass.isEnum();
        
        if (field != null) {
            int modifiers = field.getModifiers();
            fieldAccess = (modifiers & Modifier.PUBLIC) != 0 || method == null;
            fieldTransient = Modifier.isTransient(modifiers);
        } else {
            fieldTransient = false;
            fieldAccess = false;
        }
        
        name_chars = genFieldNameChars();

        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        
        this.label = "";
        fieldAnnotation = null;
        methodAnnotation = null;
        this.getOnly = false;
        this.jsonDirect = false;
        this.unwrapped = false;
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
                     int parserFeatures, //
                     JSONField fieldAnnotation, // 
                     JSONField methodAnnotation, //
                     String label){
        if (field != null) {
            String fieldName = field.getName();
            if (fieldName.equals(name)) {
                name = fieldName;
            }
        }

        if (ordinal < 0) {
            ordinal = 0;
        }
        
        this.name = name;
        this.method = method;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;
        this.parserFeatures = parserFeatures;
        this.fieldAnnotation = fieldAnnotation;
        this.methodAnnotation = methodAnnotation;
        
        if (field != null) {
            int modifiers = field.getModifiers();
            fieldAccess = ((modifiers & Modifier.PUBLIC) != 0 || method == null);
            fieldTransient = Modifier.isTransient(modifiers)
                    || TypeUtils.isTransient(method);
        } else {
            fieldAccess = false;
            fieldTransient = false;
        }
        
        if (label != null && label.length() > 0) { 
            this.label = label;
        } else {
            this.label = "";
        }
        
        String format = null;
        JSONField annotation = getAnnotation();

        boolean jsonDirect = false;
        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }
            jsonDirect = annotation.jsonDirect();
            unwrapped = annotation.unwrapped();
            alternateNames = annotation.alternateNames();
        } else {
            jsonDirect = false;
            unwrapped = false;
            alternateNames = new String[0];
        }
        this.format = format;
        
        name_chars = genFieldNameChars();

        if (method != null) {
            TypeUtils.setAccessible(method);
        }

        if (field != null) {
            TypeUtils.setAccessible(field);
        }

        boolean getOnly = false;
        Type fieldType;
        Class<?> fieldClass;
        if (method != null) {
        	Class<?>[] types;
            if ((types = method.getParameterTypes()).length == 1) {
                fieldClass = types[0];
                fieldType = method.getGenericParameterTypes()[0];
            } else if (types.length == 2 && types[0] == String.class && types[1] == Object.class) {
                fieldType = fieldClass = types[0];
            } else {
                fieldClass = method.getReturnType();
                fieldType = method.getGenericReturnType();
                getOnly = true;
            }
            this.declaringClass = method.getDeclaringClass();
        } else {
            fieldClass = field.getType();
            fieldType = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
            getOnly = Modifier.isFinal(field.getModifiers());
        }
        this.getOnly = getOnly;
        this.jsonDirect = jsonDirect && fieldClass == String.class;

        if (clazz != null && fieldClass == Object.class && fieldType instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable<?>) fieldType;
            Type genericFieldType = getInheritGenericType(clazz, type, tv);
            if (genericFieldType != null) {
                this.fieldClass = TypeUtils.getClass(genericFieldType);
                this.fieldType = genericFieldType;
                
                isEnum = fieldClass.isEnum();
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
        
        isEnum = fieldClass.isEnum();
    }
    
    protected char[] genFieldNameChars() {
        int nameLen = this.name.length();
        char[] name_chars = new char[nameLen + 3];
        this.name.getChars(0, this.name.length(), name_chars, 1);
        name_chars[0] = '"';
        name_chars[nameLen + 1] = '"';
        name_chars[nameLen + 2] = ':';
        return name_chars;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getAnnation(Class<T> annotationClass) {
        if (annotationClass == JSONField.class) {
            return (T) getAnnotation();
        }
        
        T annotatition = null;
        if (method != null) {
            annotatition = method.getAnnotation(annotationClass);
        }
        
        if (annotatition == null && field != null) {
            annotatition = field.getAnnotation(annotationClass);
        }
        
        return annotatition;
    }

    public static Type getFieldType(final Class<?> clazz, final Type type, Type fieldType) {
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
            final TypeVariable<?> typeVar = (TypeVariable<?>) fieldType;
            
            TypeVariable<?>[] typeVariables = parameterizedClass.getTypeParameters();
            for (int i = 0; i < typeVariables.length; ++i) {
                if (typeVariables[i].getName().equals(typeVar.getName())) {
                    fieldType = paramType.getActualTypeArguments()[i];
                    return fieldType;
                }
            }
        }

        if (fieldType instanceof ParameterizedType) {
            ParameterizedType parameterizedFieldType = (ParameterizedType) fieldType;

            Type[] arguments = parameterizedFieldType.getActualTypeArguments();
            TypeVariable<?>[] typeVariables;
            ParameterizedType paramType;
            if (type instanceof ParameterizedType) {
                paramType = (ParameterizedType) type;
                typeVariables = clazz.getTypeParameters();
            } else if(clazz.getGenericSuperclass() instanceof ParameterizedType) {
                paramType = (ParameterizedType) clazz.getGenericSuperclass();
                typeVariables = clazz.getSuperclass().getTypeParameters();
            } else {
                paramType = parameterizedFieldType;
                typeVariables = type.getClass().getTypeParameters();
            }

            boolean changed = getArgument(arguments, typeVariables, paramType.getActualTypeArguments());
            if (changed) {
                fieldType = new ParameterizedTypeImpl(arguments, parameterizedFieldType.getOwnerType(),
                                                      parameterizedFieldType.getRawType());
                return fieldType;
            }
        }

        return fieldType;
    }

    private static boolean getArgument(Type[] typeArgs, TypeVariable[] typeVariables, Type[] arguments) {
        if (arguments == null || typeVariables.length == 0) {
            return false;
        }

        boolean changed = false;
        for (int i = 0; i < typeArgs.length; ++i) {
            Type typeArg = typeArgs[i];
            if (typeArg instanceof ParameterizedType) {
                ParameterizedType p_typeArg = (ParameterizedType) typeArg;
                Type[] p_typeArg_args = p_typeArg.getActualTypeArguments();
                boolean p_changed = getArgument(p_typeArg_args, typeVariables, arguments);
                if (p_changed) {
                    typeArgs[i] = new ParameterizedTypeImpl(p_typeArg_args, p_typeArg.getOwnerType(), p_typeArg.getRawType());
                    changed = true;
                }
            } else if (typeArg instanceof TypeVariable) {
                for (int j = 0; j < typeVariables.length; ++j) {
                    if (typeArg.equals(typeVariables[j])) {
                        typeArgs[i] = arguments[j];
                        changed = true;
                    }
                }
            }
        }

        return changed;
    }

    private static Type getInheritGenericType(Class<?> clazz, Type type, TypeVariable<?> tv) {
        GenericDeclaration gd = tv.getGenericDeclaration();

        Class<?> class_gd = null;
        if (gd instanceof Class) {
            class_gd = (Class<?>) tv.getGenericDeclaration();
        }

        Type[] arguments = null;
        if (class_gd == clazz) {
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                arguments = ptype.getActualTypeArguments();
            }
        } else {
            for (Class<?> c = clazz; c != null && c != Object.class && c != class_gd; c = c.getSuperclass()) {
                Type superType = c.getGenericSuperclass();

                if (superType instanceof ParameterizedType) {
                    ParameterizedType p_superType = (ParameterizedType) superType;
                    Type[] p_superType_args = p_superType.getActualTypeArguments();
                    getArgument(p_superType_args, c.getTypeParameters(), arguments);
                    arguments = p_superType_args;
                }
            }
        }

        if (arguments == null || class_gd == null) {
            return null;
        }

        Type actualType = null;
        TypeVariable<?>[] typeVariables = class_gd.getTypeParameters();
        for (int j = 0; j < typeVariables.length; ++j) {
            if (tv.equals(typeVariables[j])) {
                actualType = arguments[j];
                break;
            }
        }

        return actualType;
    }

    public String toString() {
        return this.name;
    }

    public Member getMember() {
        if (method != null) {
            return method;
        } else {
            return field;
        }
    }

    protected Class<?> getDeclaredClass() {
        if (this.method != null) {
            return this.method.getDeclaringClass();
        }
        
        if (this.field != null) {
            return this.field.getDeclaringClass();
        }
        
        return null;
    }

    public int compareTo(FieldInfo o) {
        if (this.ordinal < o.ordinal) {
            return -1;
        }

        if (this.ordinal > o.ordinal) {
            return 1;
        }

        int result = this.name.compareTo(o.name);
        
        if (result != 0) {
            return result;
        }
        
        Class<?> thisDeclaringClass = this.getDeclaredClass();
        Class<?> otherDeclaringClass = o.getDeclaredClass();
        
        if (thisDeclaringClass != null && otherDeclaringClass != null && thisDeclaringClass != otherDeclaringClass) {
            if (thisDeclaringClass.isAssignableFrom(otherDeclaringClass)) {
                return -1;
            }
            
            if (otherDeclaringClass.isAssignableFrom(thisDeclaringClass)) {
                return 1;
            }
        }
        
        boolean isSampeType = this.field != null && this.field.getType() == this.fieldClass;
        boolean oSameType = o.field != null && o.field.getType() == o.fieldClass;
        
        if (isSampeType && !oSameType) {
            return 1;
        }
        
        if (oSameType && !isSampeType) {
            return -1;
        }
        
        if (o.fieldClass.isPrimitive() && !this.fieldClass.isPrimitive()) {
            return 1;
        }
        
        if (this.fieldClass.isPrimitive() && !o.fieldClass.isPrimitive()) {
            return -1;
        }
        
        if (o.fieldClass.getName().startsWith("java.") && !this.fieldClass.getName().startsWith("java.")) {
            return 1;
        }
        
        if (this.fieldClass.getName().startsWith("java.") && !o.fieldClass.getName().startsWith("java.")) {
            return -1;
        }
        
        return this.fieldClass.getName().compareTo(o.fieldClass.getName());
    }
    
    public JSONField getAnnotation() {
        if (this.fieldAnnotation != null) {
            return this.fieldAnnotation;
        }
        
        return this.methodAnnotation;
    }

    public String getFormat() {
        return format;
    }

    public Object get(Object javaObject) throws IllegalAccessException, InvocationTargetException {
        return method != null
                ? method.invoke(javaObject)
                : field.get(javaObject);
    }

    public void set(Object javaObject, Object value) throws IllegalAccessException, InvocationTargetException {
        if (method != null) {
            method.invoke(javaObject, new Object[] { value });
            return;
        }

        field.set(javaObject, value);
    }

    public void setAccessible() throws SecurityException {
        if (method != null) {
            TypeUtils.setAccessible(method);
            return;
        }

        TypeUtils.setAccessible(field);
    }
}
