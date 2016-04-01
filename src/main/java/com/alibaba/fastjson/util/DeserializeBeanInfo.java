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
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DeserializeBeanInfo {

    private Constructor<?>        defaultConstructor;
    private Constructor<?>        creatorConstructor;
    private Method                factoryMethod;

    public final List<FieldInfo> fields          = new ArrayList<FieldInfo>();
    public final List<FieldInfo> sortedFieldList = new ArrayList<FieldInfo>();

    public final int             parserFeatures;

    public DeserializeBeanInfo(Class<?> clazz){
        this.parserFeatures = TypeUtils.getParserFeatures(clazz);
    }

    public Constructor<?> getDefaultConstructor() {
        return defaultConstructor;
    }

    public Constructor<?> getCreatorConstructor() {
        return creatorConstructor;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public boolean add(FieldInfo field) {
        for (FieldInfo item : this.fields) {
            if (item.getName().equals(field.getName())) {
                if (item.isGetOnly() && !field.isGetOnly()) {
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
        DeserializeBeanInfo beanInfo = new DeserializeBeanInfo(clazz);

        Constructor<?> defaultConstructor = getDefaultConstructor(clazz);
        if (defaultConstructor != null) {
            TypeUtils.setAccessible(defaultConstructor);
            beanInfo.defaultConstructor = defaultConstructor;
        } else if (defaultConstructor == null && !(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))) {
            Constructor<?> creatorConstructor = getCreatorConstructor(clazz);
            if (creatorConstructor != null) {
                TypeUtils.setAccessible(creatorConstructor);
                beanInfo.creatorConstructor = creatorConstructor;

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
                    Field field = TypeUtils.getField(clazz, fieldAnnotation.name());
                    final int ordinal = fieldAnnotation.ordinal();
                    final int serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                    FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, field,
                                                        ordinal, serialzeFeatures);
                    beanInfo.add(fieldInfo);
                }
                return beanInfo;
            }

            Method factoryMethod = getFactoryMethod(clazz);
            if (factoryMethod != null) {
                TypeUtils.setAccessible(factoryMethod);
                beanInfo.factoryMethod = factoryMethod;

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
                    Field field = TypeUtils.getField(clazz, fieldAnnotation.name());
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

        for (Method method : clazz.getMethods()) {
            int ordinal = 0, serialzeFeatures = 0;
            String methodName = method.getName();
            if (methodName.length() < 4) {
                continue;
            }

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            // support builder set
            if (!(method.getReturnType().equals(Void.TYPE) || method.getReturnType().equals(clazz))) {
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
                    TypeUtils.setAccessible(method);
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

            Field field = TypeUtils.getField(clazz, propertyName);
            if (field == null && method.getParameterTypes()[0] == boolean.class) {
                String isFieldName = "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
                field = TypeUtils.getField(clazz, isFieldName);
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
            TypeUtils.setAccessible(method);
        }

        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            boolean contains = false;
            for (FieldInfo item : beanInfo.fields) {
                if (item.getName().equals(field.getName())) {
                    contains = true;
                    continue;
                }
            }

            if (contains) {
                continue;
            }

            int ordinal = 0, serialzeFeatures = 0;
            String propertyName = field.getName();

            JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

            if (fieldAnnotation != null) {
                ordinal = fieldAnnotation.ordinal();
                serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());

                if (fieldAnnotation.name().length() != 0) {
                    propertyName = fieldAnnotation.name();
                }
            }
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

                if (Collection.class.isAssignableFrom(method.getReturnType()) //
                    || Map.class.isAssignableFrom(method.getReturnType()) //
                ) {
                    String propertyName;
                    
                    JSONField annotation = method.getAnnotation(JSONField.class);
                    if (annotation != null && annotation.name().length() > 0) {
                        propertyName = annotation.name();
                    } else {
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    }

                    beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, annotation));
                    TypeUtils.setAccessible(method);
                }
            }
        }

        return beanInfo;
    }

    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
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
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.getParameterTypes().length == 1
                        && constructor.getParameterTypes()[0].equals(clazz.getDeclaringClass())) {
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

    public static Method getFactoryMethod(Class<?> clazz) {
        Method factoryMethod = null;

        for (Method method : clazz.getDeclaredMethods()) {
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
