package com.alibaba.fastjson.parser.deserializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.DefaultExtJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

public class JavaBeanDeserializer implements ObjectDeserializer {

    private final Map<String, FieldDeserializer> feildDeserializerMap = new IdentityHashMap<String, FieldDeserializer>();

    private final List<FieldDeserializer>        fieldDeserializers   = new ArrayList<FieldDeserializer>();

    private final Class<?>                       clazz;
    private List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();

    private Constructor<?>                       defaultConstructor;
    private Constructor<?>                       creatorConstructor;
    private Method                               factoryMethod;

    public JavaBeanDeserializer(ParserConfig mapping, Class<?> clazz){
        this.clazz = clazz;

        if (!Modifier.isAbstract(clazz.getModifiers())) {
            defaultConstructor = JavaBeanDeserializer.getDefaultConstructor(clazz);
            if (defaultConstructor == null) {
                creatorConstructor = JavaBeanDeserializer.getCreatorConstructor(clazz);
                if (creatorConstructor != null) {
                    creatorConstructor.setAccessible(true);
                } else {
                    factoryMethod = JavaBeanDeserializer.getFactoryMethod(clazz);
                    if (factoryMethod != null) {
                        factoryMethod.setAccessible(true);
                    }
                }
            } else {
                defaultConstructor.setAccessible(true);
            }
        }

        computeSetters(clazz, fieldInfoList);

        for (FieldInfo fieldInfo : fieldInfoList) {
            addFieldDeserializer(mapping, clazz, fieldInfo);
        }
    }

    public Map<String, FieldDeserializer> getFieldDeserializerMap() {
        return feildDeserializerMap;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static void computeSetters(Class<?> clazz, List<FieldInfo> fieldInfoList) {
        Constructor<?> defaultConstructor = getDefaultConstructor(clazz);

        if (defaultConstructor == null && !(clazz.isInterface())) {
            Constructor<?> creatorConstructor = getCreatorConstructor(clazz);

            if (creatorConstructor != null) {
                for (int i = 0; i < creatorConstructor.getParameterTypes().length; ++i) {
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

                    Class<?> fieldClass = creatorConstructor.getParameterTypes()[i];
                    Type fieldType = creatorConstructor.getGenericParameterTypes()[i];
                    fieldInfoList.add(new FieldInfo(fieldAnnotation, clazz, fieldClass, fieldType));
                }
                return;
            }

            Method factoryMethod = getFactoryMethod(clazz);
            if (factoryMethod != null) {
                for (int i = 0; i < factoryMethod.getParameterTypes().length; ++i) {
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

                    Class<?> fieldClass = factoryMethod.getParameterTypes()[i];
                    Type fieldType = factoryMethod.getGenericParameterTypes()[i];
                    fieldInfoList.add(new FieldInfo(fieldAnnotation, clazz, fieldClass, fieldType));
                }
                return;
            }
            
            throw new JSONException("default constructor not found.");
        }

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.length() < 4) {
                continue;
            }

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (!method.getReturnType().equals(Void.TYPE)) {
                continue;
            }

            if (method.getParameterTypes().length != 1) {
                continue;
            }

            JSONField annotation = method.getAnnotation(JSONField.class);

            if (annotation != null) {
                if (!annotation.deserialize()) {
                    continue;
                }

                if (annotation.name().length() != 0) {
                    String propertyName = annotation.name();
                    fieldInfoList.add(new FieldInfo(propertyName, method, null));
                    method.setAccessible(true);
                    continue;
                }
            }

            if (methodName.startsWith("set") && Character.isUpperCase(methodName.charAt(3))) {
                String propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);

                Field field = getField(clazz, propertyName);
                if (field != null) {

                    JSONField fieldAnnotation = field.getAnnotation(JSONField.class);

                    if (fieldAnnotation != null && fieldAnnotation.name().length() != 0) {
                        propertyName = fieldAnnotation.name();

                        fieldInfoList.add(new FieldInfo(propertyName, method, field));
                        continue;
                    }
                }

                fieldInfoList.add(new FieldInfo(propertyName, method, null));
                method.setAccessible(true);
            }
        }
    }

    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        Constructor<?> defaultConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
                break;
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

    private void addFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);

        feildDeserializerMap.put(fieldInfo.getName().intern(), fieldDeserializer);
        fieldDeserializers.add(fieldDeserializer);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            return null;
        }
    }

    public Object createInstance(Type type) {
        if (type instanceof Class) {
            if (clazz.isInterface()) {
                Class<?> clazz = (Class<?>) type;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                final JSONObject obj = new JSONObject();
                Object proxy = Proxy.newProxyInstance(loader, new Class<?>[] { clazz }, obj);
                return proxy;
            }
        }

        if (defaultConstructor == null) {
            return null;
        }

        Object object;
        try {
            object = defaultConstructor.newInstance();
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type type) {
        JSONScanner lexer = (JSONScanner) parser.getLexer(); // xxx

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        ParseContext context = parser.getContext();

        try {
            Object object = null;
            Map<String, Object> fieldValues = null;

            if (lexer.token() != JSONToken.LBRACE) {
                throw new JSONException("syntax error, expect {, actual " + JSONToken.name(lexer.token()));
            }

            for (;;) {

                String key = lexer.scanSymbol(parser.getSymbolTable());

                if (key == null) {
                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    }
                    if (lexer.token() == JSONToken.COMMA) {
                        if (parser.isEnabled(Feature.AllowArbitraryCommas)) {
                            continue;
                        }
                    }
                }

                if ("$ref" == key) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        if ("@".equals(ref)) {
                            object = context.getObject();
                        } else if ("..".equals(ref)) {
                            object = context.getParentContext().getObject();
                        } else if ("$".equals(ref)) {
                            ParseContext root = context;
                            while (root.getParentContext() != null) {
                                root = root.getParentContext();
                            }
                            object = root.getObject();
                        } else {
                            parser.getResolveTaskList().add(new ResolveTask(context, ref));
                            parser.setReferenceResolveStat(DefaultExtJSONParser.NeedToResolve);
                        }
                    } else if (lexer.token() == JSONToken.LITERAL_INT) {
                        parser.getResolveTaskList().add(new ResolveTask(context, lexer.integerValue()));
                        parser.setReferenceResolveStat(DefaultExtJSONParser.NeedToResolve);
                    } else {
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }

                    lexer.nextToken(JSONToken.RBRACE);
                    if (lexer.token() != JSONToken.RBRACE) {
                        throw new JSONException("illegal ref");
                    }
                    lexer.nextToken(JSONToken.COMMA);

                    return (T) object;
                }

                if (object == null && fieldValues == null) {
                    object = createInstance(type);
                    if (object == null) {
                        fieldValues = new HashMap<String, Object>(this.fieldDeserializers.size());
                    }
                    parser.addReference(object);
                    parser.setContext(context, object);
                }

                boolean match = parseField(parser, key, object, fieldValues);
                if (!match) {
                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();
                        break;
                    }

                    continue;
                }

                if (lexer.token() == JSONToken.COMMA) {
                    continue;
                }

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }

                if (lexer.token() == JSONToken.IDENTIFIER || lexer.token() == JSONToken.ERROR) {
                    throw new JSONException("syntax error, unexpect token " + JSONToken.name(lexer.token()));
                }
            }

            if (object == null) {
                if (fieldValues == null) {
                    object = createInstance(type);
                    return (T) object;        
                }
                
                int size = fieldInfoList.size();
                Object[] params = new Object[size];
                for (int i = 0; i < size; ++i) {
                    FieldInfo fieldInfo = fieldInfoList.get(i);
                    params[i] = fieldValues.get(fieldInfo.getName());
                }
                
                if (creatorConstructor != null) {
                    try {
                        object = creatorConstructor.newInstance(params);
                    } catch (Exception e) {
                        throw new JSONException("create instance error, " +  creatorConstructor.toGenericString(), e);
                    }
                } else if (factoryMethod != null) {
                    try {
                        object = factoryMethod.invoke(null, params);
                    } catch (Exception e) {
                        throw new JSONException("create factory method error, " +  factoryMethod.toString(), e);
                    }
                }
            }

            return (T) object;
        } finally {
            parser.setContext(context);
        }
    }

    public boolean parseField(DefaultExtJSONParser parser, String key, Object object, Map<String, Object> fieldValues) {
        JSONScanner lexer = (JSONScanner) parser.getLexer(); // xxx

        FieldDeserializer fieldDeserializer = feildDeserializerMap.get(key);
        if (fieldDeserializer == null) {
            if (!parser.isEnabled(Feature.IgnoreNotMatch)) {
                throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
            }

            lexer.nextTokenWithColon();
            parser.parse(); // skip

            return false;
        }

        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
        fieldDeserializer.parseField(parser, object, fieldValues);
        return true;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
