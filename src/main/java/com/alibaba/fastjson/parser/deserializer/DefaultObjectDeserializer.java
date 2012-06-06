package com.alibaba.fastjson.parser.deserializer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.TypeUtils;

public class DefaultObjectDeserializer implements ObjectDeserializer {

    public final static DefaultObjectDeserializer instance = new DefaultObjectDeserializer();

    public DefaultObjectDeserializer(){
    }

    public Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType,
                           Object fieldName) {
        JSONScanner lexer = (JSONScanner) parser.getLexer();

        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
        }

        ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
        ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
        lexer.nextToken(keyDeserializer.getFastMatchToken());

        ParseContext context = parser.getContext();
        try {
            for (;;) {
                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }

                if (lexer.token() == JSONToken.LITERAL_STRING && lexer.isRef()) {
                    Object object = null;

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

                    //parser.setContext(context, map, fieldName);
                    //parser.setContext(context);

                    return object;
                }

                if (map.size() == 0 && lexer.token() == JSONToken.LITERAL_STRING && "@type".equals(lexer.stringVal())) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    lexer.nextToken(JSONToken.COMMA);
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }

                Object key = keyDeserializer.deserialze(parser, keyType, null);

                if (lexer.token() != JSONToken.COLON) {
                    throw new JSONException("syntax error, expect :, actual " + lexer.token());
                }

                lexer.nextToken(valueDeserializer.getFastMatchToken());

                Object value = valueDeserializer.deserialze(parser, valueType, key);

                if (map.size() == 0 && context != null && context.getObject() != map) {
                    parser.setContext(context, map, fieldName);
                }

                map.put(key, value);

                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }
            }
        } finally {
            parser.setContext(context);
        }

        return map;
    }

    @SuppressWarnings("rawtypes")
    public Map parseMap(DefaultJSONParser parser, Map<String, Object> map, Type valueType, Object fieldName) {
        JSONScanner lexer = (JSONScanner) parser.getLexer();

        if (lexer.token() != JSONToken.LBRACE) {
            throw new JSONException("syntax error, expect {, actual " + lexer.token());
        }

        ParseContext context = parser.getContext();
        try {
            for (;;) {
                lexer.skipWhitespace();
                char ch = lexer.getCurrent();
                if (parser.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (ch == ',') {
                        lexer.incrementBufferPosition();
                        lexer.skipWhitespace();
                        ch = lexer.getCurrent();
                    }
                }

                String key;
                if (ch == '"') {
                    key = lexer.scanSymbol(parser.getSymbolTable(), '"');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos());
                    }
                } else if (ch == '}') {
                    lexer.incrementBufferPosition();
                    lexer.resetStringPosition();
                    lexer.nextToken(JSONToken.COMMA);
                    return map;
                } else if (ch == '\'') {
                    if (!parser.isEnabled(Feature.AllowSingleQuotes)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos());
                    }
                } else {
                    if (!parser.isEnabled(Feature.AllowUnQuotedFieldNames)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos() + ", actual " + ch);
                    }
                }

                lexer.incrementBufferPosition();
                lexer.skipWhitespace();
                ch = lexer.getCurrent();

                lexer.resetStringPosition();

                if (key == "@type") {
                    String typeName = lexer.scanSymbol(parser.getSymbolTable(), '"');
                    Class<?> clazz = TypeUtils.loadClass(typeName);

                    if (clazz == map.getClass()) {
                        lexer.nextToken(JSONToken.COMMA);
                        if (lexer.token() == JSONToken.RBRACE) {
                            lexer.nextToken(JSONToken.COMMA);
                            return map;
                        }
                        continue;
                    }

                    ObjectDeserializer deserializer = parser.getConfig().getDeserializer(clazz);

                    lexer.nextToken(JSONToken.COMMA);

                    parser.setResolveStatus(DefaultJSONParser.TypeNameRedirect);
                    
                    if (context != null && !(fieldName instanceof Integer)) {
                        parser.popContext();
                    }
                    
                    return (Map) deserializer.deserialze(parser, clazz, fieldName);
                }
                
                Object value;
                lexer.nextToken();

                if (lexer.token() == JSONToken.NULL) {
                    value = null;
                    lexer.nextToken();
                } else {
                    value = parser.parseObject(valueType);
                }

                map.put(key, value);
                parser.checkMapResolve(map, key);
                
                parser.setContext(context, value, key);

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return map;
                }
            }
        } finally {
            parser.setContext(context);
        }

    }

    public void parseObject(DefaultJSONParser parser, Object object) {
        Class<?> clazz = object.getClass();
        Map<String, FieldDeserializer> setters = parser.getConfig().getFieldDeserializers(clazz);

        JSONScanner lexer = (JSONScanner) parser.getLexer(); // xxx

        if (lexer.token() == JSONToken.RBRACE) {
            lexer.nextToken(JSONToken.COMMA);
            return;
        }

        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
        }

        final Object[] args = new Object[1];

        for (;;) {
            // lexer.scanSymbol
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

            FieldDeserializer fieldDeser = setters.get(key);
            if (fieldDeser == null) {
                if (!parser.isEnabled(Feature.IgnoreNotMatch)) {
                    throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
                }

                lexer.nextTokenWithColon();
                parser.parse(); // skip

                if (lexer.token() == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return;
                }

                continue;
            } else {
                Method method = fieldDeser.getMethod();
                Class<?> fieldClass = method.getParameterTypes()[0];
                Type fieldType = method.getGenericParameterTypes()[0];
                if (fieldClass == int.class) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
                    args[0] = IntegerDeserializer.deserialze(parser);
                } else if (fieldClass == String.class) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    args[0] = StringDeserializer.deserialze(parser);
                } else if (fieldClass == long.class) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
                    args[0] = LongDeserializer.deserialze(parser);
                } else if (fieldClass == List.class) {
                    lexer.nextTokenWithColon(JSONToken.LBRACE);
                    args[0] = CollectionDeserializer.instance.deserialze(parser, fieldType, null);
                } else {
                    ObjectDeserializer fieldValueDeserializer = parser.getConfig().getDeserializer(fieldClass,
                                                                                                   fieldType);

                    lexer.nextTokenWithColon(fieldValueDeserializer.getFastMatchToken());
                    args[0] = fieldValueDeserializer.deserialze(parser, fieldType, null);
                }

                try {
                    method.invoke(object, args);
                } catch (Exception e) {
                    throw new JSONException("set proprety error, " + method.getName(), e);
                }
            }

            if (lexer.token() == JSONToken.COMMA) {
                continue;
            }

            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof Class<?>) {
            return deserialze(parser, (Class<T>) type);
        }

        if (type instanceof ParameterizedType) {
            return (T) deserialze(parser, (ParameterizedType) type, fieldName);
        }

        if (type instanceof TypeVariable) {
            return (T) parser.parse(fieldName);
        }

        if (type instanceof WildcardType) {
            return (T) parser.parse(fieldName);
        }
        
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            List<Object> list = new ArrayList<Object>();
            parser.parseArray(componentType, list);
            if (componentType instanceof Class) {
                Class<?> componentClass = (Class<?>) componentType;
                Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
                
                list.toArray(array);
                
                return (T) array;
            }
        }

        throw new JSONException("not support type : " + type);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T deserialze(DefaultJSONParser parser, ParameterizedType type, Object fieldName) {
        try {
            JSONLexer lexer = parser.getLexer();
            if (lexer.token() == JSONToken.NULL) {
                lexer.nextToken();
                return null;
            }

            Type rawType = type.getRawType();
            if (rawType instanceof Class<?>) {
                Class<?> rawClass = (Class<?>) rawType;

                if (Map.class.isAssignableFrom(rawClass)) {
                    Map map;

                    if (Modifier.isAbstract(rawClass.getModifiers())) {
                        if (rawClass == Map.class) {
                            map = new HashMap();
                        } else if (rawClass == SortedMap.class) {
                            map = new TreeMap();
                        } else if (rawClass == ConcurrentMap.class) {
                            map = new ConcurrentHashMap();
                        } else {
                            throw new JSONException("can not create instance : " + rawClass);
                        }
                    } else {
                        if (rawClass == HashMap.class) {
                            map = new HashMap();
                        } else {
                            map = (Map) rawClass.newInstance();
                        }
                    }

                    Type keyType = type.getActualTypeArguments()[0];
                    Type valueType = type.getActualTypeArguments()[1];

                    if (keyType == String.class) {
                        return (T) parseMap(parser, map, valueType, fieldName);
                    } else {
                        return (T) parseMap(parser, map, keyType, valueType, fieldName);
                    }
                }

            }

            throw new JSONException("not support type : " + type);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T deserialze(DefaultJSONParser parser, Class<T> clazz) {
        Object value = null;
        
        if (parser.getLexer().token() == JSONToken.NULL) {
            parser.getLexer().nextToken(JSONToken.COMMA);
            return null;
        }
        if (clazz.isAssignableFrom(HashMap.class)) {
            value = new HashMap();
        } else if (clazz.isAssignableFrom(TreeMap.class)) {
            value = new TreeMap();
        } else if (clazz.isAssignableFrom(ConcurrentHashMap.class)) {
            value = new ConcurrentHashMap();
        } else if (clazz.isAssignableFrom(Properties.class)) {
            value = new Properties();
        } else if (clazz.isAssignableFrom(IdentityHashMap.class)) {
            value = new IdentityHashMap();
        }

        if (clazz == Class.class) {
            Object classValue = parser.parse();
            if (classValue == null) {
                return null;
            }

            if (classValue instanceof String) {
                return (T) ASMClassLoader.forName((String) classValue);
            }
        } else if (clazz == Serializable.class) {
            return (T) parser.parse();
        }

        if (value == null) {
            throw new JSONException("not support type : " + clazz);
        }

        try {
            parseObject(parser, value);
            return (T) value;
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
