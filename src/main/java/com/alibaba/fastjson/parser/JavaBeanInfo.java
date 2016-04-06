package com.alibaba.fastjson.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

class JavaBeanInfo {
    final Constructor<?> defaultConstructor;
    final int            defaultConstructorParameterSize;
    final Constructor<?> creatorConstructor;
    final Method         factoryMethod;

    final FieldInfo[]    fields;
    final FieldInfo[]    sortedFields;
    final JSONType       jsonType;

    JavaBeanInfo(Class<?> clazz, //
                               Constructor<?> defaultConstructor, //
                               Constructor<?> creatorConstructor, //
                               Method factoryMethod, //
                               FieldInfo[] fields, //
                               FieldInfo[] sortedFields
                               ){
        
        this.defaultConstructor = defaultConstructor;
        this.creatorConstructor = creatorConstructor;
        this.factoryMethod = factoryMethod;
        this.fields = fields;
        
        this.sortedFields = (Arrays.equals(fields, sortedFields)) ? fields : sortedFields;
        
        this.jsonType = clazz.getAnnotation(JSONType.class);
        
        defaultConstructorParameterSize = defaultConstructor != null ? defaultConstructor.getParameterTypes().length : 0;
    }

    static boolean addField(List<FieldInfo> fields, FieldInfo field) {
        for (FieldInfo item : fields) {
            if (item.name.equals(field.name)) {
                if (item.getOnly && !field.getOnly) {
                    continue;
                }
                
                return false;
            }
        }
        
        fields.add(field);

        return true;
    }

    public static JavaBeanInfo build(Class<?> clazz, Type type) {
        List<FieldInfo> fieldList = new ArrayList<FieldInfo>();
        
        //DeserializeBeanInfo beanInfo = null;
        Constructor<?> defaultConstructor = getDefaultConstructor(clazz);
        Constructor<?> creatorConstructor = null;
        Method[] methods = clazz.getMethods();
        Field[] declaredFields = clazz.getDeclaredFields();
        final int modifiers = clazz.getModifiers();
        
        if (defaultConstructor == null && !(clazz.isInterface() || (modifiers & Modifier.ABSTRACT) != 0)) {
            creatorConstructor = getCreatorConstructor(clazz);
            if (creatorConstructor != null) {
                TypeUtils.setAccessible(clazz, creatorConstructor, modifiers);

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
                    addField(fieldList, fieldInfo);
                }
                
                FieldInfo[] fields = new FieldInfo[fieldList.size()];
                fieldList.toArray(fields);
                
                FieldInfo[] sortedFields = new FieldInfo[fields.length];
                System.arraycopy(fields, 0, sortedFields, 0, fields.length);
                Arrays.sort(sortedFields);

                return new JavaBeanInfo(clazz, null, creatorConstructor, null, fields, sortedFields);
            } 
            
            Method factoryMethod = getFactoryMethod(clazz, methods);
            if (factoryMethod != null) {
                TypeUtils.setAccessible(clazz, factoryMethod, modifiers);

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
                    addField(fieldList, fieldInfo);
                }
                
                FieldInfo[] fields = new FieldInfo[fieldList.size()];
                fieldList.toArray(fields);
                
                FieldInfo[] sortedFields = new FieldInfo[fields.length];
                System.arraycopy(fields, 0, sortedFields, 0, fields.length);
                Arrays.sort(sortedFields);
                
                if (Arrays.equals(fields, sortedFields)) {
                    sortedFields = fields;
                }
                
                JavaBeanInfo beanInfo = new JavaBeanInfo(clazz, null, null, factoryMethod, fields, sortedFields);
                return beanInfo;
            }

            throw new JSONException("default constructor not found. " + clazz);
        }
        
        if (defaultConstructor != null) {
            TypeUtils.setAccessible(clazz, defaultConstructor, modifiers);
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
                    addField(fieldList, new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures, annotation, null));
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
                        addField(fieldList, new FieldInfo(propertyName, method, field, clazz, type, ordinal, serialzeFeatures, annotation, fieldAnnotation));
                        continue;
                    }
                }

            }
            addField(fieldList, new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures, annotation, null));
            TypeUtils.setAccessible(clazz, method, modifiers);
        }

        for (Field field : clazz.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            final String fieldName = field.getName();
            boolean contains = false;
            for (FieldInfo item : fieldList) {
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
            addField(fieldList, new FieldInfo(propertyName, null, field, clazz, type, ordinal, serialzeFeatures, null, fieldAnnotation));
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

                    addField(fieldList, new FieldInfo(propertyName, method, null, clazz, type, 0, 0, annotation, null));
                    TypeUtils.setAccessible(clazz, method, modifiers);
                }
            }
        }

        FieldInfo[] fields = new FieldInfo[fieldList.size()];
        fieldList.toArray(fields);
        
        FieldInfo[] sortedFields = new FieldInfo[fields.length];
        System.arraycopy(fields, 0, sortedFields, 0, fields.length);
        Arrays.sort(sortedFields);
        
        return new JavaBeanInfo(clazz, defaultConstructor, null, null, fields, sortedFields);
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
