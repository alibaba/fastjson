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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DeserializeBeanInfo {

    private final Class<?>        clazz;
    private final Class<?>        builderClass;
    private Constructor<?>        defaultConstructor;
    private Constructor<?>        creatorConstructor;
    private Method                factoryMethod;
    private Method                buildMethod;

    private final List<FieldInfo> fieldList       = new ArrayList<FieldInfo>();
    private final List<FieldInfo> sortedFieldList = new ArrayList<FieldInfo>();

    private int                   parserFeatures  = 0;

    public DeserializeBeanInfo(Class<?> clazz){
        this(clazz, getBuilderClass(clazz));
    }

    public DeserializeBeanInfo(Class<?> clazz, Class<?> builderClass){
        super();
        this.clazz = clazz;
        this.builderClass = builderClass;
        this.parserFeatures = TypeUtils.getParserFeatures(clazz);
    }

    public Constructor<?> getDefaultConstructor() {
        return defaultConstructor;
    }

    public void setDefaultConstructor(Constructor<?> defaultConstructor) {
        this.defaultConstructor = defaultConstructor;
    }

    public Constructor<?> getCreatorConstructor() {
        return creatorConstructor;
    }

    public void setCreatorConstructor(Constructor<?> createConstructor) {
        this.creatorConstructor = createConstructor;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public Method getBuildMethod() {
        return buildMethod;
    }

    public void setBuildMethod(Method buildMethod) {
        this.buildMethod = buildMethod;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Class<?> getBuilderClass() {
        return builderClass;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public List<FieldInfo> getSortedFieldList() {
        return sortedFieldList;
    }

    public FieldInfo getField(String propertyName) {
        for (FieldInfo item : this.fieldList) {
            if (item.getName().equals(propertyName)) {
                return item;
            }
        }

        return null;
    }

    public boolean add(FieldInfo field) {
        for (int i = this.fieldList.size() - 1; i >= 0; --i) {
            FieldInfo item = this.fieldList.get(i);
            
            if (item.getName().equals(field.getName())) {
                if (item.isGetOnly() && !field.isGetOnly()) {
                    continue;
                }

                if (item.getFieldClass().isAssignableFrom(field.getFieldClass())) {
                    fieldList.remove(i);
                    break;
                }
                
                int result = item.compareTo(field);
                
                if (result < 0) {
                    fieldList.remove(i);
                    break;
                } else {
                    return false;
                }
            }
        }
        fieldList.add(field);
        sortedFieldList.add(field);
        Collections.sort(sortedFieldList);

        return true;
    }

    public static DeserializeBeanInfo computeSetters(Class<?> clazz, Type type) {
        DeserializeBeanInfo beanInfo = new DeserializeBeanInfo(clazz);

        Class<?> builderClass = beanInfo.getBuilderClass();

        Constructor<?> defaultConstructor = getDefaultConstructor(builderClass == null ? clazz : builderClass);
        if (defaultConstructor != null) { // 如果存在默认构造方法
            TypeUtils.setAccessible(defaultConstructor);
            beanInfo.setDefaultConstructor(defaultConstructor);
        } else if (defaultConstructor == null && !(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))) {
            Constructor<?> creatorConstructor = getCreatorConstructor(clazz);
            if (creatorConstructor != null) { // 基于标记 JSONCreator 注解的构造方法
                TypeUtils.setAccessible(creatorConstructor);
                beanInfo.setCreatorConstructor(creatorConstructor);

                Class<?>[] types = creatorConstructor.getParameterTypes();
                if (types.length > 0) {
                	Annotation[][] paramAnnotationArrays = creatorConstructor.getParameterAnnotations();
                	for (int i = 0; i < types.length; ++i) {
                		Annotation[] paramAnnotations = paramAnnotationArrays[i];
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
                		Class<?> fieldClass = types[i];
                		Type fieldType = creatorConstructor.getGenericParameterTypes()[i];
                		Field field = TypeUtils.getField(clazz, fieldAnnotation.name());
                		final int ordinal = fieldAnnotation.ordinal();
                		final int serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                		FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, field,
                				ordinal, serialzeFeatures);
                		beanInfo.add(fieldInfo);
                	}
				}
                return beanInfo;
            }

            Method factoryMethod = getFactoryMethod(clazz); // 基于标记 JSONCreator 注解的工厂方法
            if (factoryMethod != null) {
                TypeUtils.setAccessible(factoryMethod);
                beanInfo.setFactoryMethod(factoryMethod);
                Class<?>[] types = factoryMethod.getParameterTypes();
                if (types.length > 0) {
                	Annotation[][] paramAnnotationArrays = factoryMethod.getParameterAnnotations();
                	for (int i = 0; i < types.length; ++i) {
                        Annotation[] paramAnnotations = paramAnnotationArrays[i];
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

                        Class<?> fieldClass = types[i];
                        Type fieldType = factoryMethod.getGenericParameterTypes()[i];
                        Field field = TypeUtils.getField(clazz, fieldAnnotation.name());
                        final int ordinal = fieldAnnotation.ordinal();
                        final int serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                        FieldInfo fieldInfo = new FieldInfo(fieldAnnotation.name(), clazz, fieldClass, fieldType, field, ordinal, serialzeFeatures);
                        beanInfo.add(fieldInfo);
                    }
				}
                return beanInfo;
            }

            throw new JSONException("default constructor not found. " + clazz);
        }

        if (builderClass != null) {
            String withPrefix = null;

            JSONPOJOBuilder builderAnno = builderClass.getAnnotation(JSONPOJOBuilder.class);
            if (builderAnno != null) {
                withPrefix = builderAnno.withPrefix();
            }

            if (withPrefix == null || withPrefix.length() == 0) {
                withPrefix = "with";
            }

            for (Method method : builderClass.getMethods()) {
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }

                if (!(method.getReturnType().equals(builderClass))) {
                    continue;
                }

                int ordinal = 0, serialzeFeatures = 0;

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
                        beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures));                        
                        continue;
                    }
                }

                String methodName = method.getName();
                if (!methodName.startsWith(withPrefix)) {
                    continue;
                }

                if (methodName.length() <= withPrefix.length()) {
                    continue;
                }

                char c0 = methodName.charAt(withPrefix.length());

                if (!Character.isUpperCase(c0)) {
                    continue;
                }

                StringBuilder properNameBuilder = new StringBuilder(methodName.substring(withPrefix.length()));
                properNameBuilder.setCharAt(0, Character.toLowerCase(c0));

                String propertyName = properNameBuilder.toString();

                beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures));                
            }

            if (builderClass != null) {
                JSONPOJOBuilder builderAnnotation = builderClass.getAnnotation(JSONPOJOBuilder.class);

                String buildMethodName = null;
                if (builderAnnotation != null) {
                    buildMethodName = builderAnnotation.buildMethod();
                }

                if (buildMethodName == null || buildMethodName.length() == 0) {
                    buildMethodName = "build";
                }

                Method buildMethod = null;
                try {
                    buildMethod = builderClass.getMethod(buildMethodName);
                } catch (NoSuchMethodException e) {
                    // skip
                } catch (SecurityException e) {
                    // skip
                }

                if (buildMethod == null) {
                    try {
                        buildMethod = builderClass.getMethod("create");
                    } catch (NoSuchMethodException e) {
                        // skip
                    } catch (SecurityException e) {
                        // skip
                    }
                }

                if (buildMethod == null) {
                    throw new JSONException("buildMethod not found.");
                }

                beanInfo.setBuildMethod(buildMethod);
                TypeUtils.setAccessible(buildMethod);
            }
        }

        for (Method method : clazz.getMethods()) { // 
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
            Class<?>[] types = method.getParameterTypes();
            if (types.length != 1) {
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
                    beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type, ordinal, serialzeFeatures));
                    continue;
                }
            }

            if (!methodName.startsWith("set")) { // TODO "set"的判断放在 JSONField 注解后面，意思是允许非 setter 方法标记 JSONField 注解？
                continue;
            }

            char c3 = methodName.charAt(3);

            String propertyName;
            if (Character.isUpperCase(c3) // 
                    || c3 > 512 // for unicode method name
                    ) {
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
            if (field == null && types[0] == boolean.class) {
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
                        beanInfo.add(new FieldInfo(propertyName, method, field, clazz, type, ordinal, serialzeFeatures));
                        continue;
                    }
                }

            }

            beanInfo.add(new FieldInfo(propertyName, method, field, clazz, type, ordinal, serialzeFeatures));
        }

        for (Field field : clazz.getFields()) { // public static fields
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            boolean contains = false;
            for (FieldInfo item : beanInfo.getFieldList()) {
                if (item.getName().equals(field.getName())) {
                    contains = true;
                    break; // 已经是 contains = true，无需继续遍历
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
            beanInfo.add(new FieldInfo(propertyName, null, field, clazz, type, ordinal, serialzeFeatures));
        }

        for (Method method : clazz.getMethods()) { // getter methods
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
                    || AtomicBoolean.class == method.getReturnType() //
                    || AtomicInteger.class == method.getReturnType() //
                    || AtomicLong.class == method.getReturnType() //
                ) {
                    String propertyName;

                    JSONField annotation = method.getAnnotation(JSONField.class);
                    if (annotation != null && annotation.name().length() > 0) {
                        propertyName = annotation.name();
                    } else {
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    }

                    FieldInfo fieldInfo = beanInfo.getField(propertyName);
                    if (fieldInfo != null) {
                        continue;
                    }

                    beanInfo.add(new FieldInfo(propertyName, method, null, clazz, type));
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
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
                break;
            }
        }

        if (defaultConstructor == null) {
            if (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            	Class<?>[] types;
                for (Constructor<?> constructor : constructors) {
                    if ( (types = constructor.getParameterTypes()).length == 1 && types[0].equals(clazz.getDeclaringClass())) {
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
                    throw new JSONException("multi-JSONCreator");
                }

                creatorConstructor = constructor;
                // 不应该break，否则多个构造方法上存在 JSONCreator 注解时，并不会触发上述异常抛出
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
                    throw new JSONException("multi-JSONCreator");
                }

                factoryMethod = method;
                // 不应该break，否则多个静态工厂方法上存在 JSONCreator 注解时，并不会触发上述异常抛出
            }
        }
        return factoryMethod;
    }

    public int getParserFeatures() {
        return parserFeatures;
    }

    public static Class<?> getBuilderClass(Class<?> clazz) {
        JSONType type = clazz.getAnnotation(JSONType.class);
        if (type == null) {
            return null;
        }

        Class<?> builderClass = type.builder();

        if (builderClass == Void.class) {
            return null;
        }

        return builderClass;
    }
}
