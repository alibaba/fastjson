package com.alibaba.fastjson.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DeserializeBeanInfo {

    public final Constructor<?>        defaultConstructor;
    public final int             defaultConstructorParameterSize;
    public final Constructor<?>        creatorConstructor;
    public final Method                factoryMethod;

    public final List<FieldInfo> fields          = new ArrayList<FieldInfo>();
    public final List<FieldInfo> sortedFieldList = new ArrayList<FieldInfo>();

    public final int             parserFeatures;
    
    public final JSONType        jsonType;

    public DeserializeBeanInfo(Class<?> clazz, //
                               Constructor<?> defaultConstructor, //
                               Constructor<?> creatorConstructor, //
                               Method factoryMethod){
        this.defaultConstructor = defaultConstructor;
        this.creatorConstructor = creatorConstructor;
        this.factoryMethod = factoryMethod;
        
        this.jsonType = clazz.getAnnotation(JSONType.class);
        
        if (jsonType != null) {
            Feature[] features = jsonType.parseFeatures();
            if (features == null) {
                parserFeatures = 0;
            } else {
                int value = 0;
                
                for (Feature feature: features) {
                    value |= feature.mask;
                }
                
                parserFeatures = value;
            }
        } else {
            parserFeatures = 0;
        }
        
        defaultConstructorParameterSize = defaultConstructor != null ? defaultConstructor.getParameterTypes().length : 0;
    }

    public boolean add(FieldInfo field) {
        for (FieldInfo item : this.fields) {
            if (item.name.equals(field.name)) {
                if (item.getOnly && !field.getOnly) {
                    continue;
                }
                
                return false;
            }
        }
        fields.add(field);
        sortedFieldList.add(field);
        Collections.sort(sortedFieldList);

        return true;
    }

    public static DeserializeBeanInfo computeSetters(Class<?> clazz, Type type) {
        DeserializeBeanInfo beanInfo = null;
        Constructor<?> defaultConstructor = getDefaultConstructor(clazz);
        Method[] methods = clazz.getMethods();
        Field[] declaredFields = clazz.getDeclaredFields();
        final int modifiers = clazz.getModifiers();
        
        if (defaultConstructor != null) {
            TypeUtils.setAccessible(clazz, defaultConstructor, modifiers);
            beanInfo = new DeserializeBeanInfo(clazz, defaultConstructor, null, null);
        } else if (defaultConstructor == null && !(clazz.isInterface() || (modifiers & Modifier.ABSTRACT) != 0)) {
            Constructor<?> creatorConstructor = getCreatorConstructor(clazz);
            if (creatorConstructor != null) {
                TypeUtils.setAccessible(clazz, creatorConstructor, modifiers);
                beanInfo = new DeserializeBeanInfo(clazz, null, creatorConstructor, null);

                Class<?>[] parameterTypes = creatorConstructor.getParameterTypes();
                Type[] getGenericParameterTypes = creatorConstructor.getGenericParameterTypes();
                for (int i = 0; i < parameterTypes.length; ++i) {
                    Annotation[] paramAnnotations = creatorConstructor.getParameterAnnotations()[i];
                    JSONField fieldAnnotation = null;
                    for (Annotation paramAnnotation : paramAnnotations) {
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation;
                            break;
                        }
                    }
                    if (fieldAnnotation == null) {
                        throw new JSONException("illegal json creator");
                    }

                    Class<?> fieldClass = parameterTypes[i];
                    Type fieldType = getGenericParameterTypes[i];
                    Field field = TypeUtils.getField(clazz, fieldAnnotation.name(), declaredFields);
                    
                    if (field != null) {
                        TypeUtils.setAccessible(clazz, field, modifiers);
                    }
                    
                    final int ordinal = fieldAnnotation.ordinal();
                    final int serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                    FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, field,
                                                        ordinal, serialzeFeatures);
                    beanInfo.add(fieldInfo);
                }
                return beanInfo;
            } 
            
            Method factoryMethod = getFactoryMethod(clazz, methods);
            if (factoryMethod != null) {
                TypeUtils.setAccessible(clazz, factoryMethod, modifiers);
                beanInfo = new DeserializeBeanInfo(clazz, null, null, factoryMethod);

                Class<?>[] parameterTypes = factoryMethod.getParameterTypes();
                Type[] genericParameterTypes = factoryMethod.getGenericParameterTypes();
                for (int i = 0; i < parameterTypes.length; ++i) {
                    Annotation[] paramAnnotations = factoryMethod.getParameterAnnotations()[i];
                    JSONField fieldAnnotation = null;
                    for (Annotation paramAnnotation : paramAnnotations) {
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation;
                            break;
                        }
                    }
                    if (fieldAnnotation == null) {
                        throw new JSONException("illegal json creator");
                    }

                    Class<?> fieldClass = parameterTypes[i];
                    Type fieldType = genericParameterTypes[i];
                    Field field = TypeUtils.getField(clazz, fieldAnnotation.name(), declaredFields);
                    final int ordinal = fieldAnnotation.ordinal();
                    final int serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                    FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name() //
                                                        , clazz //
                                                        , fieldClass //
                                                        , fieldType //
                                                        , field //
                                                        , ordinal //
                                                        , serialzeFeatures);
                    beanInfo.add(fieldInfo);
                }
                return beanInfo;
            }

            throw new JSONException("default constructor not found. " + clazz);
        }
        
        if (beanInfo == null) {
            beanInfo = new DeserializeBeanInfo(clazz, defaultConstructor, null, null);
        }
        
        for (Method method : methods) {
            int ordinal = 0, serialzeFeatures = 0;
            String methodName = method.getName();
            if (methodName.length() < 4) {
                continue;
            }

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            // support builder set
            
            Class<?> returnType = method.getReturnType();
            if (!(returnType == Void.TYPE || returnType == clazz)) {
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                continue;
            }
            
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation == null) {
                annotation = TypeUtils.getSupperMethodAnnotation(clazz, method);
            }

            if (annotation != null) {
                if (!annotation.deserialize()) {
                    continue;
                }

                ordinal = annotation.ordinal();
                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());

                if (annotation.name().length() != 0) {
                    String propertyName = annotation.name();
                    beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures, annotation, null));
                    TypeUtils.setAccessible(clazz, method, modifiers);
                    continue;
                }
            }

            if (!methodName.startsWith("set")) {
                continue;
            }

            char c3 = methodName.charAt(3);

            String propertyName;
            if (Character.isUpperCase(c3)) {
                if (TypeUtils.compatibleWithJavaBean) {
                    propertyName = TypeUtils.decapitalize(methodName.substring(3));
                } else {
                    propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                }
            } else if (c3 == '_') {
                propertyName = methodName.substring(4);
            } else if (c3 == 'f') {
                propertyName = methodName.substring(3);
            } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                propertyName = TypeUtils.decapitalize(methodName.substring(3));
            } else {
                continue;
            }

            Field field = TypeUtils.getField(clazz, propertyName, declaredFields);
            if (field == null && method.getParameterTypes()[0] == boolean.class) {
                String isFieldName = "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
                field = TypeUtils.getField(clazz, isFieldName, declaredFields);
            }

            if (field != null) {
                JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

                if (fieldAnnotation != null) {
                    ordinal = fieldAnnotation.ordinal();
                    serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                    if (fieldAnnotation.name().length() != 0) {
                        propertyName = fieldAnnotation.name();
                        beanInfo.add(new FieldInfo(propertyName, method, field, clazz, type, ordinal, serialzeFeatures, annotation, fieldAnnotation));
                        continue;
                    }
                }

            }
            beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures, annotation, null));
            TypeUtils.setAccessible(clazz, method, modifiers);
        }

        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            final String fieldName = field.getName();
            boolean contains = false;
            for (FieldInfo item : beanInfo.fields) {
                if (item.name.equals(fieldName)) {
                    contains = true;
                    continue;
                }
            }

            if (contains) {
                continue;
            }

            int ordinal = 0, serialzeFeatures = 0;
            String propertyName = fieldName;

            JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

            if (fieldAnnotation != null) {
                ordinal = fieldAnnotation.ordinal();
                serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                if (fieldAnnotation.name().length() != 0) {
                    propertyName = fieldAnnotation.name();
                }
            }
            TypeUtils.setAccessible(clazz, field, modifiers);
            beanInfo.add(new FieldInfo(propertyName, null, field, clazz, type, ordinal, serialzeFeatures, null, fieldAnnotation));
        }

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.length() < 4) {
                continue;
            }

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (methodName.startsWith("get") && Character.isUpperCase(methodName.charAt(3))) {
                if (method.getParameterTypes().length != 0) {
                    continue;
                }

                Class<?> methodReturnType = method.getReturnType();
                if (Collection.class.isAssignableFrom(methodReturnType) //
                    || Map.class.isAssignableFrom(methodReturnType) //
                ) {
                    String propertyName;
                    
                    JSONField annotation = method.getAnnotation(JSONField.class);
                    String annotationName;
                    if (annotation != null && (annotationName = annotation.name()).length() > 0) {
                        propertyName = annotationName;
                    } else {
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    }

                    beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, 0, 0, annotation, null));
                    TypeUtils.setAccessible(clazz, method, modifiers);
                }
            }
        }

        return beanInfo;
    }

    private static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        final int classModifiers = clazz.getModifiers();
        if ((classModifiers & Modifier.ABSTRACT) != 0) {
            return null;
        }

        Constructor<?> defaultConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
                break;
            }
        }

        
        if (defaultConstructor == null) {
            if (clazz.isMemberClass() && (classModifiers & Modifier.STATIC) == 0) {  // for inner none static class
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 1
                        && parameterTypes[0].equals(clazz.getDeclaringClass())) {
                        defaultConstructor = constructor;
                        break;
                    }
                }
            }
        }

        return defaultConstructor;
    }

    public static Constructor<?> getCreatorConstructor(Class<?> clazz) {
        Constructor<?> creatorConstructor = null;

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            JSONCreator annotation = constructor.getAnnotation(JSONCreator.class);
            if (annotation != null) {
                if (creatorConstructor != null) {
                    throw new JSONException("multi-json creator");
                }

                creatorConstructor = constructor;
                break;
            }
        }
        return creatorConstructor;
    }

    private static Method getFactoryMethod(Class<?> clazz, Method[] methods) {
        Method factoryMethod = null;

        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (!clazz.isAssignableFrom(method.getReturnType())) {
                continue;
            }

            JSONCreator annotation = method.getAnnotation(JSONCreator.class);
            if (annotation != null) {
                if (factoryMethod != null) {
                    throw new JSONException("multi-json creator");
                }

                factoryMethod = method;
                break;
            }
        }
        return factoryMethod;
    }
}
