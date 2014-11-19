package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.FilterUtils;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {

    private final Map<String, FieldDeserializer> feildDeserializerMap     = new IdentityHashMap<String, FieldDeserializer>();

    private final List<FieldDeserializer>        fieldDeserializers       = new ArrayList<FieldDeserializer>();
    private final List<FieldDeserializer>        sortedFieldDeserializers = new ArrayList<FieldDeserializer>();
    private final Class<?>                       clazz;

    private DeserializeBeanInfo                  beanInfo;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz){
        this(config, clazz, clazz);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        this.clazz = clazz;

        beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);

        for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
            addFieldDeserializer(config, clazz, fieldInfo);
        }

        for (FieldInfo fieldInfo : beanInfo.getSortedFieldList()) {
            FieldDeserializer fieldDeserializer = feildDeserializerMap.get(fieldInfo.getName().intern());
            sortedFieldDeserializers.add(fieldDeserializer);
        }
    }

    public Map<String, FieldDeserializer> getFieldDeserializerMap() {
        return feildDeserializerMap;
    }

    private void addFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        String interName = fieldInfo.getName().intern();
        FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);

        feildDeserializerMap.put(interName, fieldDeserializer);
        fieldDeserializers.add(fieldDeserializer);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public Object createInstance(DefaultJSONParser parser, Type type) {
        if (type instanceof Class) {
            if (clazz.isInterface()) {
                Class<?> clazz = (Class<?>) type;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                final JSONObject obj = new JSONObject();
                Object proxy = Proxy.newProxyInstance(loader, new Class<?>[] { clazz }, obj);
                return proxy;
            }
        }

        if (beanInfo.getDefaultConstructor() == null) {
            return null;
        }

        Object object;
        try {
            Constructor<?> constructor = beanInfo.getDefaultConstructor();
            if (constructor.getParameterTypes().length == 0) {
                object = constructor.newInstance();
            } else {
                object = constructor.newInstance(parser.getContext().getObject());
            }
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        if (parser.isEnabled(Feature.InitStringFieldAsEmpty)) {
            for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
                if (fieldInfo.getFieldClass() == String.class) {
                    try {
                        fieldInfo.set(object, "");
                    } catch (Exception e) {
                        throw new JSONException("create instance error, class " + clazz.getName(), e);
                    }
                }
            }
        }

        return object;
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null);
    }

    @SuppressWarnings({ "unchecked" })
    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        final JSONLexer lexer = parser.getLexer(); // xxx
        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("error");
        }

        object = createInstance(parser, type);

        int size = sortedFieldDeserializers.size();
        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';
            FieldDeserializer fieldDeser = sortedFieldDeserializers.get(i);
            Class<?> fieldClass = fieldDeser.getFieldClass();
            if (fieldClass == int.class) {
                int value = lexer.scanInt(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == String.class) {
                String value = lexer.scanString(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == long.class) {
                long value = lexer.scanLong(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass.isEnum()) {
                Enum<?> value = lexer.scanEnum(fieldClass, parser.getSymbolTable(), seperator);
                fieldDeser.setValue(object, value);
            } else {
                lexer.nextToken(JSONToken.LBRACKET);
                Object value = parser.parseObject(fieldDeser.getFieldType());
                fieldDeser.setValue(object, value);

                if (seperator == ']') {
                    if (lexer.token() != JSONToken.RBRACKET) {
                        throw new JSONException("syntax error");
                    }
                    lexer.nextToken(JSONToken.COMMA);
                } else if (seperator == ',') {
                    if (lexer.token() != JSONToken.COMMA) {
                        throw new JSONException("syntax error");
                    }
                }
            }
        }
        lexer.nextToken(JSONToken.COMMA);

        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        if (type == JSON.class || type == JSONObject.class) {
            return (T) parser.parse();
        }
        
        final JSONLexer lexer = parser.getLexer(); // xxx

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        ParseContext context = parser.getContext();
        if (object != null && context != null) {
            context = context.getParentContext();
        }
        ParseContext childContext = null;

        try {
            Map<String, Object> fieldValues = null;

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                if (object == null) {
                    object = createInstance(parser, type);
                }
                return (T) object;
            }

            if (lexer.token() == JSONToken.LBRACKET && isSupportArrayToBean(lexer)) {
                return deserialzeArrayMapping(parser, type, fieldName, object);
            }

            if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
                StringBuffer buf = (new StringBuffer()) //
                .append("syntax error, expect {, actual ") //
                .append(lexer.tokenName()) //
                .append(", pos ") //
                .append(lexer.pos()) //
                ;
                if (fieldName instanceof String) {
                    buf //
                    .append(", fieldName ") //
                    .append(fieldName);
                }
                throw new JSONException(buf.toString());
            }

            if (parser.getResolveStatus() == DefaultJSONParser.TypeNameRedirect) {
                parser.setResolveStatus(DefaultJSONParser.NONE);
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
                            ParseContext parentContext = context.getParentContext();
                            if (parentContext.getObject() != null) {
                                object = parentContext.getObject();
                            } else {
                                parser.addResolveTask(new ResolveTask(parentContext, ref));
                                parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
                            }
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.getParentContext() != null) {
                                rootContext = rootContext.getParentContext();
                            }

                            if (rootContext.getObject() != null) {
                                object = rootContext.getObject();
                            } else {
                                parser.addResolveTask(new ResolveTask(rootContext, ref));
                                parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
                            }
                        } else {
                            parser.addResolveTask(new ResolveTask(context, ref));
                            parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
                        }
                    } else {
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }

                    lexer.nextToken(JSONToken.RBRACE);
                    if (lexer.token() != JSONToken.RBRACE) {
                        throw new JSONException("illegal ref");
                    }
                    lexer.nextToken(JSONToken.COMMA);

                    parser.setContext(context, object, fieldName);

                    return (T) object;
                }

                if (JSON.DEFAULT_TYPE_KEY == key) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
                        String typeName = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);

                        if (type instanceof Class && typeName.equals(((Class<?>) type).getName())) {
                            if (lexer.token() == JSONToken.RBRACE) {
                                lexer.nextToken();
                                break;
                            }
                            continue;
                        }

                        Class<?> userType = TypeUtils.loadClass(typeName);
                        ObjectDeserializer deserizer = parser.getConfig().getDeserializer(userType);
                        return (T) deserizer.deserialze(parser, userType, fieldName);
                    } else {
                        throw new JSONException("syntax error");
                    }
                }

                if (object == null && fieldValues == null) {
                    object = createInstance(parser, type);
                    if (object == null) {
                        fieldValues = new HashMap<String, Object>(this.fieldDeserializers.size());
                    }
                    childContext = parser.setContext(context, object, fieldName);
                }

                boolean match = parseField(parser, key, object, type, fieldValues);
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
                    object = createInstance(parser, type);
                    if (childContext == null) {
                        childContext = parser.setContext(context, object, fieldName);
                    }
                    return (T) object;
                }

                List<FieldInfo> fieldInfoList = beanInfo.getFieldList();
                int size = fieldInfoList.size();
                Object[] params = new Object[size];
                for (int i = 0; i < size; ++i) {
                    FieldInfo fieldInfo = fieldInfoList.get(i);
                    params[i] = fieldValues.get(fieldInfo.getName());
                }

                if (beanInfo.getCreatorConstructor() != null) {
                    try {
                        object = beanInfo.getCreatorConstructor().newInstance(params);
                    } catch (Exception e) {
                        throw new JSONException("create instance error, "
                                                + beanInfo.getCreatorConstructor().toGenericString(), e);
                    }
                } else if (beanInfo.getFactoryMethod() != null) {
                    try {
                        object = beanInfo.getFactoryMethod().invoke(null, params);
                    } catch (Exception e) {
                        throw new JSONException("create factory method error, "
                                                + beanInfo.getFactoryMethod().toString(), e);
                    }
                }
            }

            return (T) object;
        } finally {
            if (childContext != null) {
                childContext.setObject(object);
            }
            parser.setContext(context);
        }
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType,
                              Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.getLexer(); // xxx

        FieldDeserializer fieldDeserializer = feildDeserializerMap.get(key);

        if (fieldDeserializer == null) {
            for (Map.Entry<String, FieldDeserializer> entry : feildDeserializerMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    fieldDeserializer = entry.getValue();
                    break;
                }
            }
        }

        if (fieldDeserializer == null) {
            parseExtra(parser, object, key);

            return false;
        }

        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());

        fieldDeserializer.parseField(parser, object, objectType, fieldValues);

        return true;
    }

    void parseExtra(DefaultJSONParser parser, Object object, String key) {
        final JSONLexer lexer = parser.getLexer(); // xxx
        if (!lexer.isEnabled(Feature.IgnoreNotMatch)) {
            throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
        }

        lexer.nextTokenWithColon();
        Type type = FilterUtils.getExtratype(parser, object, key);
        Object value;
        if (type == null) {
            value = parser.parse(); // skip
        } else {
            value = parser.parseObject(type);
        }

        FilterUtils.processExtra(parser, object, key, value);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

    public final boolean isSupportArrayToBean(JSONLexer lexer) {
        return Feature.isEnabled(beanInfo.getParserFeatures(), Feature.SupportArrayToBean)
               || lexer.isEnabled(Feature.SupportArrayToBean);
    }
}
