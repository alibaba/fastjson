package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;

public class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();
    
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type == JSONObject.class && parser.getFieldTypeResolver() == null) {
            return (T) parser.parseObject();
        }
        
        final JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        boolean unmodifiableMap = type instanceof Class
                && "java.util.Collections$UnmodifiableMap".equals(((Class) type).getName());

        Map<Object, Object> map = (lexer.getFeatures() & Feature.OrderedField.mask) != 0
                ? createMap(type, lexer.getFeatures())
                : createMap(type);

        ParseContext context = parser.getContext();

        try {
            parser.setContext(context, map, fieldName);
            T t = (T) deserialze(parser, type, fieldName, map);
            if (unmodifiableMap) {
                t = (T) Collections.unmodifiableMap((Map) t);
            }
            return t;
        } finally {
            parser.setContext(context);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type keyType = parameterizedType.getActualTypeArguments()[0];
            Type valueType = null;
            if(map.getClass().getName().equals("org.springframework.util.LinkedMultiValueMap")){
                valueType = List.class;
            }else{
                valueType = parameterizedType.getActualTypeArguments()[1];
            }
            if (String.class == keyType) {
                return parseMap(parser, (Map<String, Object>) map, valueType, fieldName);
            } else {
                return parseMap(parser, map, keyType, valueType, fieldName);
            }
        } else {
            return parser.parseObject(map, fieldName);
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static Map parseMap(DefaultJSONParser parser, Map<String, Object> map, Type valueType, Object fieldName) {
        JSONLexer lexer = parser.lexer;

        int token = lexer.token();
        if (token != JSONToken.LBRACE) {
            String msg = "syntax error, expect {, actual " + lexer.tokenName();
            if (fieldName instanceof String) {
                msg += ", fieldName ";
                msg += fieldName;
            }
            msg += ", ";
            msg += lexer.info();

            if (token != JSONToken.LITERAL_STRING) {
                JSONArray array = new JSONArray();
                parser.parseArray(array, fieldName);

                if (array.size() == 1) {
                    Object first = array.get(0);
                    if (first instanceof JSONObject) {
                        return (JSONObject) first;
                    }
                }
            }

            throw new JSONException(msg);
        }

        ParseContext context = parser.getContext();
        try {
            for (int i = 0;;++i) {
                lexer.skipWhitespace();
                char ch = lexer.getCurrent();
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (ch == ',') {
                        lexer.next();
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
                    lexer.next();
                    lexer.resetStringPosition();
                    lexer.nextToken(JSONToken.COMMA);
                    return map;
                } else if (ch == '\'') {
                    if (!lexer.isEnabled(Feature.AllowSingleQuotes)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbol(parser.getSymbolTable(), '\'');
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos());
                    }
                } else {
                    if (!lexer.isEnabled(Feature.AllowUnQuotedFieldNames)) {
                        throw new JSONException("syntax error");
                    }

                    key = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
                    lexer.skipWhitespace();
                    ch = lexer.getCurrent();
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos() + ", actual " + ch);
                    }
                }

                lexer.next();
                lexer.skipWhitespace();
                ch = lexer.getCurrent();

                lexer.resetStringPosition();

                if (key == JSON.DEFAULT_TYPE_KEY && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    String typeName = lexer.scanSymbol(parser.getSymbolTable(), '"');
                    final ParserConfig config = parser.getConfig();

                    Class<?> clazz = config.checkAutoType(typeName, null, lexer.getFeatures());

                    if (Map.class.isAssignableFrom(clazz) ) {
                        lexer.nextToken(JSONToken.COMMA);
                        if (lexer.token() == JSONToken.RBRACE) {
                            lexer.nextToken(JSONToken.COMMA);
                            return map;
                        }
                        continue;
                    }

                    ObjectDeserializer deserializer = config.getDeserializer(clazz);

                    lexer.nextToken(JSONToken.COMMA);

                    parser.setResolveStatus(DefaultJSONParser.TypeNameRedirect);

                    if (context != null && !(fieldName instanceof Integer)) {
                        parser.popContext();
                    }

                    return (Map) deserializer.deserialze(parser, clazz, fieldName);
                }

                Object value;
                lexer.nextToken();

                if (i != 0) {
                    parser.setContext(context);
                }
                
                if (lexer.token() == JSONToken.NULL) {
                    value = null;
                    lexer.nextToken();
                } else {
                    value = parser.parseObject(valueType, key);
                }

                map.put(key, value);
                parser.checkMapResolve(map, key);

                parser.setContext(context, value, key);
                parser.setContext(context);

                final int tok = lexer.token();
                if (tok == JSONToken.EOF || tok == JSONToken.RBRACKET) {
                    return map;
                }

                if (tok == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return map;
                }
            }
        } finally {
            parser.setContext(context);
        }

    }
    
    public static Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType,
                                  Object fieldName) {
        JSONLexer lexer = parser.lexer;

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

                if (lexer.token() == JSONToken.LITERAL_STRING //
                    && lexer.isRef() //
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect) //
                ) {
                    Object object = null;

                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        if ("..".equals(ref)) {
                            ParseContext parentContext = context.parent;
                            object = parentContext.object;
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.parent != null) {
                                rootContext = rootContext.parent;
                            }

                            object = rootContext.object;
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

                    // parser.setContext(context, map, fieldName);
                    // parser.setContext(context);

                    return object;
                }

                if (map.size() == 0 //
                    && lexer.token() == JSONToken.LITERAL_STRING //
                    && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal()) //
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    lexer.nextToken(JSONToken.COMMA);
                    if (lexer.token() == JSONToken.RBRACE) {
                        lexer.nextToken();
                        return map;
                    }
                    lexer.nextToken(keyDeserializer.getFastMatchToken());
                }

                Object key;
                if (lexer.token() == JSONToken.LITERAL_STRING
                        && keyDeserializer instanceof JavaBeanDeserializer
                ) {
                    String keyStrValue = lexer.stringVal();
                    lexer.nextToken();
                    DefaultJSONParser keyParser = new DefaultJSONParser(keyStrValue, parser.getConfig(), parser.getLexer().getFeatures());
                    keyParser.setDateFormat(parser.getDateFomartPattern());
                    key = keyDeserializer.deserialze(keyParser, keyType, null);
                } else {
                    key = keyDeserializer.deserialze(parser, keyType, null);
                }

                if (lexer.token() != JSONToken.COLON) {
                    throw new JSONException("syntax error, expect :, actual " + lexer.token());
                }

                lexer.nextToken(valueDeserializer.getFastMatchToken());

                Object value = valueDeserializer.deserialze(parser, valueType, key);
                parser.checkMapResolve(map, key);

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

    public Map<Object, Object> createMap(Type type) {
        return createMap(type, JSON.DEFAULT_GENERATE_FEATURE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<Object, Object> createMap(Type type, int featrues) {
        if (type == Properties.class) {
            return new Properties();
        }

        if (type == Hashtable.class) {
            return new Hashtable();
        }

        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }

        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }

        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        
        if (type == Map.class) {
            return (featrues & Feature.OrderedField.mask) != 0
                    ? new LinkedHashMap()
                    : new HashMap();
        }

        if (type == HashMap.class) {
            return new HashMap();
        }

        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            Type rawType = parameterizedType.getRawType();
            if (EnumMap.class.equals(rawType)) {
                Type[] actualArgs = parameterizedType.getActualTypeArguments();
                return new EnumMap((Class) actualArgs[0]);
            }

            return createMap(rawType, featrues);
        }

        Class<?> clazz = (Class<?>) type;
        if (clazz.isInterface()) {
            throw new JSONException("unsupport type " + type);
        }

        if ("java.util.Collections$UnmodifiableMap".equals(clazz.getName())) {
            return new HashMap();
        }
        
        try {
            return (Map<Object, Object>) clazz.newInstance();
        } catch (Exception e) {
            throw new JSONException("unsupport type " + type, e);
        }
    }
    

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
