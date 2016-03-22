package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.alibaba.fastjson.annotation.JSONField;

public class FieldInfo implements Comparable<FieldInfo> {

    private final String   name;
    private final Method   method;
    private final Field    field;

    private int            ordinal = 0;
    private final Class<?> fieldClass;
    private final Type     fieldType;
    private final Class<?> declaringClass;
    private boolean        getOnly = false;
    private int            serialzeFeatures;
    private String         label = "";
    
    public FieldInfo(String name, Class<?> declaringClass, Class<?> fieldClass, Type fieldType, Field field){
        this(name, declaringClass, fieldClass, fieldType, field, 0, 0);
    }

    public FieldInfo(String name, Class<?> declaringClass, Class<?> fieldClass, Type fieldType, Field field, int ordinal, int serialzeFeatures){
        this.name = name;
        this.declaringClass = declaringClass;
        this.fieldClass = fieldClass;
        this.fieldType = fieldType;
        this.method = null;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;

        if (field != null) {
            TypeUtils.setAccessible(field);
        }
    }
    
    public FieldInfo(String name, Method method, Field field){
        this(name, method, field, null, null);
    }
    
    public FieldInfo(String name, Method method, Field field, int ordinal, int serialzeFeatures){
        this(name, method, field, ordinal, serialzeFeatures, null);
    }

    public FieldInfo(String name, Method method, Field field, int ordinal, int serialzeFeatures, String label){
        this(name, method, field, null, null, ordinal, serialzeFeatures);
        if (label != null && label.length() > 0) {
            this.label = label;
        }
    }

    public FieldInfo(String name, Method method, Field field, Class<?> clazz, Type type){
        this(name, method, field, clazz, type, 0, 0);
    }

    public FieldInfo(String name, Method method, Field field, Class<?> clazz, Type type, int ordinal, int serialzeFeatures){
        this.name = name;
        this.method = method;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;

        if (method != null) {
            TypeUtils.setAccessible(method);
        }

        if (field != null) {
            TypeUtils.setAccessible(field);
        }

        Type fieldType;
        Class<?> fieldClass;
        if (method != null) {
        	Class<?>[] types;
            if ((types = method.getParameterTypes()).length == 1) {
                fieldClass = types[0];
                fieldType = method.getGenericParameterTypes()[0];
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
        }

        if (clazz != null && fieldClass == Object.class && fieldType instanceof TypeVariable) {
            TypeVariable<?> tv = (TypeVariable<?>) fieldType;
            Type genericFieldType = getInheritGenericType(clazz, tv);
            if (genericFieldType != null) {
                this.fieldClass = TypeUtils.getClass(genericFieldType);
                this.fieldType = genericFieldType;
                return;
            }
        }

        Type genericFieldType = getFieldType(clazz, type, fieldType);

        if (genericFieldType != fieldType) {
            if (genericFieldType instanceof ParameterizedType) {
                fieldClass = TypeUtils.getClass(genericFieldType);
            } else if (genericFieldType instanceof Class) {
                fieldClass = TypeUtils.getClass(genericFieldType);
            }
        }

        this.fieldType = genericFieldType;
        this.fieldClass = fieldClass;
    }
    
    
    public String getLabel() {
        return label;
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
            boolean changed = false;
            TypeVariable<?>[] typeVariables = null;
            Type[] actualTypes = null;
            for (int i = 0; i < arguments.length; ++i) {
                Type feildTypeArguement = arguments[i];
                if (feildTypeArguement instanceof TypeVariable) {
                    TypeVariable<?> typeVar = (TypeVariable<?>) feildTypeArguement;

                    if (type instanceof ParameterizedType) {
                        if (typeVariables == null) {
                        	typeVariables = clazz.getTypeParameters();							
						}
                        for (int j = 0; j < typeVariables.length; ++j) {
                            if (typeVariables[j].getName().equals(typeVar.getName())) {
                            	if (actualTypes == null) {
									actualTypes = ((ParameterizedType) type).getActualTypeArguments();
								}
                                arguments[i] = actualTypes[j];
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

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public Type getFieldType() {
        return fieldType;
    }

    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        Member member = getMember();
        return member.getDeclaringClass().getName() + "." + member.getName();
    }

    public Member getMember() {
        if (method != null) {
            return method;
        } else {
            return field;
        }
    }

    public Method getMethod() {
        return method;
    }

    public Field getField() {
        return field;
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
        
        if (this.isSameType() && !o.isSameType()) {
            return 1;
        }
        
        if (o.isSameType() && !this.isSameType()) {
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
    
    private boolean isSameType() {
        if (this.field != null) {
            return this.field.getType() == fieldClass;
        }
        
        return false;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T annotation = null;
        if (method != null) {
            annotation = method.getAnnotation(annotationClass);
        }

        if (annotation == null) {
            if (field != null) {
                annotation = field.getAnnotation(annotationClass);
            }
        }

        return annotation;
    }

    public String getFormat() {
        String format = null;
        JSONField annotation = getAnnotation(JSONField.class);

        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }
        }
        return format;
    }

    public Object get(Object javaObject) throws IllegalAccessException, InvocationTargetException {
        if (method != null) {
            Object value = method.invoke(javaObject, new Object[0]);
            return value;
        }

        return field.get(javaObject);
    }

    public void set(Object javaObject, Object value) throws IllegalAccessException, InvocationTargetException {
        if (method != null) {
            method.invoke(javaObject, new Object[] { value });
            return;
        }

        field.set(javaObject, value);
    }

    public void setAccessible(boolean flag) throws SecurityException {
        if (method != null) {
            TypeUtils.setAccessible(method);
            return;
        }

        TypeUtils.setAccessible(field);
    }

    public boolean isGetOnly() {
        return getOnly;
    }

    
    public int getSerialzeFeatures() {
        return serialzeFeatures;
    }

}
