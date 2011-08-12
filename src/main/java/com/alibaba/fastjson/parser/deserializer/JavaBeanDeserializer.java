package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.DefaultExtJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.util.FieldInfo;

public class JavaBeanDeserializer implements ObjectDeserializer, ReferenceResolver {

    private final Map<String, FieldDeserializer> setters            = new IdentityHashMap<String, FieldDeserializer>();

    private final List<FieldDeserializer>        fieldDeserializers = new ArrayList<FieldDeserializer>();

    private final Class<?>                       clazz;

    private Constructor<?>                       constructor;

    private FieldInfo                            keyField;

    public JavaBeanDeserializer(ParserConfig mapping, Class<?> clazz){
        this.clazz = clazz;

        if (!Modifier.isAbstract(clazz.getModifiers())) {
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new JSONException("class not has default constructor : " + clazz.getName());
            }
        }

        List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();

        computeSetters(clazz, fieldInfoList);

        for (FieldInfo fieldInfo : fieldInfoList) {
            addFieldDeserializer(mapping, clazz, fieldInfo);
        }

        String key = "";
        JSONType annotation = clazz.getAnnotation(JSONType.class);
        if (annotation != null) {
            key = annotation.key();
        }

        List<FieldInfo> getters = JavaBeanSerializer.computeGetters(clazz, null);
        if (key.length() == 0) {
            for (FieldInfo field : getters) {
                if ("id".equals(field.getName())) {
                    keyField = field;
                    key = "id";
                    break;
                }
            }
        } else {
            for (FieldInfo field : getters) {
                if (key.equals(field.getName())) {
                    keyField = field;
                    break;
                }
            }
        }
        if (keyField != null) {
            keyField.getMethod().setAccessible(true);
        }
    }

    public Map<String, FieldDeserializer> getFieldDeserializerMap() {
        return setters;
    }

    public static void computeSetters(Class<?> clazz, List<FieldInfo> fieldInfoList) {
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

    private void addFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);

        setters.put(fieldInfo.getName().intern(), fieldDeserializer);
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

    public Object createInstance(DefaultExtJSONParser parser, Type type) {

        Object object;
        try {
            object = constructor.newInstance();
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        return object;
    }

    public boolean resolve(Object object, Object reference) {
        if (keyField == null) {
            return false;
        }

        Object key;
        try {
            Method getter = keyField.getMethod();
            key = getter.invoke(object);
        } catch (Exception e) {
            return false;
        }

        if (key.equals(reference)) {
            return true;
        }

        return false;
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

                if (object == null) {
                    object = createInstance(parser, type);
                    parser.addReference(object);
                    parser.setContext(context, object);
                }

                boolean match = parseField(parser, key, object);
                if (!match) {
                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();
                        return (T) object;
                    }

                    continue;
                }

                if (lexer.token() == JSONToken.COMMA) {
                    continue;
                }

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    return (T) object;
                }

                if (lexer.token() == JSONToken.IDENTIFIER || lexer.token() == JSONToken.ERROR) {
                    throw new JSONException("syntax error, unexpect token " + JSONToken.name(lexer.token()));
                }
            }
            
            if (object == null) {
                object = createInstance(parser, type);
            }

            return (T) object;
        } finally {
            parser.setContext(context);
        }
    }

    public boolean parseField(DefaultExtJSONParser parser, String key, Object object) {
        JSONScanner lexer = (JSONScanner) parser.getLexer(); // xxx

        FieldDeserializer fieldDeserializer = setters.get(key);
        if (fieldDeserializer == null) {
            if (!parser.isEnabled(Feature.IgnoreNotMatch)) {
                throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
            }

            lexer.nextTokenWithColon();
            parser.parse(); // skip

            return false;
        }

        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
        fieldDeserializer.parseField(parser, object);
        return true;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
