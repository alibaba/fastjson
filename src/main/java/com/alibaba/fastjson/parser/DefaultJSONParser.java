/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.parser;

import static com.alibaba.fastjson.parser.JSONLexer.EOI;
import static com.alibaba.fastjson.parser.JSONToken.EOF;
import static com.alibaba.fastjson.parser.JSONToken.ERROR;
import static com.alibaba.fastjson.parser.JSONToken.FALSE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_FLOAT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_INT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;
import static com.alibaba.fastjson.parser.JSONToken.NEW;
import static com.alibaba.fastjson.parser.JSONToken.NULL;
import static com.alibaba.fastjson.parser.JSONToken.RBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.SET;
import static com.alibaba.fastjson.parser.JSONToken.TREE_SET;
import static com.alibaba.fastjson.parser.JSONToken.TRUE;
import static com.alibaba.fastjson.parser.JSONToken.UNDEFINED;

import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class DefaultJSONParser implements Closeable {

    public final SymbolTable          symbolTable;
    public ParserConfig               config;

    private String                    dateFormatPattern  = JSON.DEFFAULT_DATE_FORMAT;
    private DateFormat                dateFormat;

    public final JSONLexer            lexer;

    protected ParseContext            contex;

    private ParseContext[]            contextArray;
    private int                       contextArrayIndex  = 0;

    private List<ResolveTask>         resolveTaskList;

    public final static int           NONE               = 0;
    public final static int           NeedToResolve      = 1;
    public final static int           TypeNameRedirect   = 2;

    public int                        resolveStatus      = NONE;

    protected List<ExtraTypeProvider> extraTypeProviders = null;
    protected List<ExtraProcessor>    extraProcessors    = null;
    public FieldTypeResolver          fieldTypeResolver  = null;

    public String getDateFomartPattern() {
        return dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat(dateFormatPattern, lexer.locale);
            dateFormat.setTimeZone(lexer.timeZone);
        }
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String input){
        this(input, ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(final String input, final ParserConfig config){
        this(new JSONLexer(input, JSON.DEFAULT_PARSER_FEATURE), config);
    }

    public DefaultJSONParser(final String input, final ParserConfig config, int features){
        this(new JSONLexer(input, features), config);
    }

    public DefaultJSONParser(final char[] input, int length, final ParserConfig config, int features){
        this(new JSONLexer(input, length, features), config);
    }

    public DefaultJSONParser(final JSONLexer lexer){
        this(lexer, ParserConfig.global);
    }

    public DefaultJSONParser(final JSONLexer lexer, final ParserConfig config){
        this.lexer = lexer;
        this.config = config;
        this.symbolTable = config.symbolTable;

        if (lexer.ch == '{') {
            int index = ++lexer.bp;
            lexer.ch = (index >= lexer.len ? //
                JSONLexer.EOI //
                : lexer.text.charAt(index));
            lexer.token = JSONToken.LBRACE;
        } else  if (lexer.ch == '[') {
            int index = ++lexer.bp;
            lexer.ch = (index >= lexer.len ? //
                JSONLexer.EOI //
                : lexer.text.charAt(index));
            lexer.token = JSONToken.LBRACKET;
        } else {
            lexer.nextToken();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final Object parseObject(final Map object, Object fieldName) {
        final JSONLexer lexer = this.lexer;

        int token = lexer.token;
        if (token == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
            throw new JSONException("syntax error, expect {, actual " + JSONToken.name(token) + ", " + lexer.info());
        }
        
        final Map innerMap;
        final boolean isJSONObject;
        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            innerMap = jsonObject.getInnerMap();
            isJSONObject = true;
        } else {
            isJSONObject = false;
            innerMap = null;
        }
        
        //getInnerMap
        final boolean allowISO8601DateFormat = (lexer.features & Feature.AllowISO8601DateFormat.mask) != 0;
        final boolean disableCircularReferenceDetect = lexer.disableCircularReferenceDetect;

        ParseContext context = this.contex;
        try {
            boolean setContextFlag = false;
            for (;;) {
                char ch = lexer.ch;
                if (ch != '"' //
                    && ch != '}') {
                    lexer.skipWhitespace();
                    ch = lexer.ch;
                }

                while (ch == ',') {
                    lexer.next();
                    lexer.skipWhitespace();
                    ch = lexer.ch;
                }

                boolean isObjectKey = false;
                Object key;
                if (ch == '"') {
                    key = lexer.scanSymbol(symbolTable, '"');

                    ch = lexer.ch;
                    if (ch != ':') {
                        lexer.skipWhitespace();
                        ch = lexer.ch;
                        
                        if (ch != ':') {
                            throw new JSONException("expect ':' at " + lexer.pos + ", name " + key);
                        }
                    }
                } else if (ch == '}') {
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }
                    lexer.sp = 0; // resetStringPosition
                    lexer.nextToken(JSONToken.COMMA);
                    return object;
                } else if (ch == '\'') {
                    key = lexer.scanSymbol(symbolTable, '\'');
                    if (lexer.ch != ':') {
                        lexer.skipWhitespace();
                    }
                    ch = lexer.ch;
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.pos);
                    }
                } else if (ch == EOI) {
                    throw new JSONException("syntax error, " + lexer.info());
                } else if (ch == ',') {
                    throw new JSONException("syntax error, " + lexer.info());
                } else if ((ch >= '0' && ch <= '9') || ch == '-') {
                    lexer.sp = 0; // resetStringPosition
                    lexer.scanNumber();
                    try {
                        if (lexer.token == JSONToken.LITERAL_INT) {
                            key = lexer.integerValue();
                        } else {
                            key = lexer.decimalValue(true);
                        }
                        
                        if (isJSONObject) {
                            key = key.toString();
                        }
                    } catch (NumberFormatException ex) {
                        throw new JSONException("parse number key error, " + lexer.info());
                    }
                    ch = lexer.ch;
                    if (ch != ':') {
                        throw new JSONException("parse number key error, " + lexer.info());
                    }
                } else if (ch == '{' || ch == '[') {
                    lexer.nextToken();
                    key = parse();
                    isObjectKey = true;
                    
                    if (isJSONObject) {
                        key = key.toString();
                    }
                } else {
                    key = lexer.scanSymbolUnQuoted(symbolTable);
                    lexer.skipWhitespace();
                    ch = lexer.ch;
                    if (ch != ':') {
                        throw new JSONException("expect ':' at " + lexer.bp + ", actual " + ch);
                    }
                    
                    if (isJSONObject) {
                        key = key.toString();
                    }
                }

                if (!isObjectKey) {
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        ch = lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }

                    // lexer.skipWhitespace();
                    for (;;) {
                        if (ch <= ' ' //
                            && (ch == ' ' // 32
                                || ch == '\n' // 10
                                || ch == '\r' // 13
                                || ch == '\t' // 9
                                || ch == '\f' // 12
                                || ch == '\b') // 8
                        ) {
                            lexer.next();
                            ch = lexer.ch;
                        } else {
                            break;
                        }
                    }
                } else {
                    ch = lexer.ch;                    
                }

                lexer.sp = 0; // lexer.resetStringPosition();

                if (key == JSON.DEFAULT_TYPE_KEY //
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    String typeName = lexer.scanSymbol(symbolTable, '"');
                    Class<?> clazz = TypeUtils.loadClass(typeName, this.config.defaultClassLoader);

                    if (clazz == null) {
                        object.put(JSON.DEFAULT_TYPE_KEY, typeName);
                        continue;
                    }

                    lexer.nextToken(JSONToken.COMMA);
                    if (lexer.token == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);
                        try {
                            Object instance = null;
                            ObjectDeserializer deserializer = this.config.getDeserializer(clazz);
                            if (deserializer instanceof JavaBeanDeserializer) {
                                instance = ((JavaBeanDeserializer) deserializer).createInstance(this, clazz);
                            }

                            if (instance == null) {
                                if (clazz == Cloneable.class) {
                                    instance = new HashMap();
                                } else if ("java.util.Collections$EmptyMap".equals(typeName)) {
                                    instance = Collections.emptyMap();
                                } else {
                                    instance = clazz.newInstance();
                                }
                            }

                            return instance;
                        } catch (Exception e) {
                            throw new JSONException("create instance error", e);
                        }
                    }

                    this.resolveStatus = TypeNameRedirect;

                    if (this.contex != null && !(fieldName instanceof Integer)) {
                        this.popContext();
                    }

                    if (object.size() > 0) {
                        Object newObj = TypeUtils.cast(object, clazz, this.config);
                        this.parseObject(newObj);
                        return newObj;
                    }

                    ObjectDeserializer deserializer = config.getDeserializer(clazz);
                    return deserializer.deserialze(this, clazz, fieldName);
                }

                if (key == "$ref" //
                    && context != null
                    && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                    
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                    if (lexer.token == JSONToken.LITERAL_STRING) {
                        String ref = lexer.stringVal();
                        lexer.nextToken(JSONToken.RBRACE);

                        Object refValue = null;
                        if ("@".equals(ref)) {
                            ParseContext thisContext = this.contex;
                            Object thisObj = thisContext.object;
                            if (thisObj instanceof Object[] || thisObj instanceof Collection<?>) {
                                refValue = thisObj;
                            } else if (thisContext.parent != null) {
                                refValue = thisContext.parent.object;
                            }
                        } else if ("..".equals(ref)) {
                            if (context.object != null) {
                                refValue = context.object;
                            } else {
                                addResolveTask(new ResolveTask(context, ref));
                                resolveStatus = DefaultJSONParser.NeedToResolve;
                            }
                        } else if ("$".equals(ref)) {
                            ParseContext rootContext = context;
                            while (rootContext.parent != null) {
                                rootContext = rootContext.parent;
                            }

                            if (rootContext.object != null) {
                                refValue = rootContext.object;
                            } else {
                                addResolveTask(new ResolveTask(rootContext, ref));
                                resolveStatus = DefaultJSONParser.NeedToResolve;
                            }
                        } else {
                            addResolveTask(new ResolveTask(context, ref));
                            resolveStatus = DefaultJSONParser.NeedToResolve;
                        }

                        if (lexer.token != JSONToken.RBRACE) {
                            throw new JSONException("syntax error, " + lexer.info());
                        }
                        lexer.nextToken(JSONToken.COMMA);

                        return refValue;
                    } else {
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token));
                    }
                }

                if ((!disableCircularReferenceDetect) // 
                        && !setContextFlag) {
                    ParseContext contextR = setContext(this.contex, object, fieldName);
                    if (context == null) {
                        context = contextR;
                    }
                    setContextFlag = true;
                }

                Object value;
                if (ch == '"') {
                    String strValue = lexer.scanStringValue('"');
                    value = strValue;

                    if (allowISO8601DateFormat) {
                        JSONLexer iso8601Lexer = new JSONLexer(strValue);
                        if (iso8601Lexer.scanISO8601DateIfMatch(true)) {
                            value = iso8601Lexer.calendar.getTime();
                        }
                        iso8601Lexer.close();
                    }

                    if (innerMap != null) {
                        innerMap.put(key, value);
                    } else {
                        object.put(key, value);
                    }
                } else if (ch >= '0' && ch <= '9' || ch == '-') {
                    value = lexer.scanNumberValue();
                    object.put(key, value);
                } else if (ch == '[') { // 减少嵌套，兼容android
                    lexer.token = JSONToken.LBRACKET;
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }
                    ArrayList list = new ArrayList();

                    final boolean parentIsArray = fieldName != null && fieldName.getClass() == Integer.class;
                    if (!parentIsArray) {
                        this.setContext(context);
                    }

                    this.parseArray(list, key);
                    value = new JSONArray(list);
                    if (innerMap != null) {
                        innerMap.put(key, value);
                    } else {
                        object.put(key, value);
                    }

                    token = lexer.token;
                    if (token == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);
                        return object;
                    } else if (token == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error, " + lexer.info());
                    }
                } else if (ch == '{') { // 减少嵌套，兼容android
                    {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                        lexer.token = JSONToken.LBRACE;
                    }

                    final boolean parentIsArray = fieldName instanceof Integer;

                    JSONObject input = (lexer.features & Feature.OrderedField.mask) != 0 //
                        ? new JSONObject(new LinkedHashMap<String, Object>()) //
                        : new JSONObject();
                    
                    ParseContext ctxLocal = null;

                    if ((!disableCircularReferenceDetect) //
                        && !parentIsArray) {
                        ctxLocal = setContext(context, input, key);
                    }

                    Object obj = null;
                    boolean objParsed = false;
                    if (fieldTypeResolver != null) {
                        String resolveFieldName = key != null ? key.toString() : null;
                        Type fieldType = fieldTypeResolver.resolve(object, resolveFieldName);
                        if (fieldType != null) {
                            ObjectDeserializer fieldDeser = config.getDeserializer(fieldType);
                            obj = fieldDeser.deserialze(this, fieldType, key);
                            objParsed = true;
                        }
                    }
                    if (!objParsed) {
                        obj = this.parseObject(input, key);
                    }
                    if (ctxLocal != null && input != obj) {
                        ctxLocal.object = object;
                    }

                    if (resolveStatus == NeedToResolve) {
                        checkMapResolve(object, key.toString());
                    }
                    
                    if (innerMap != null) {
                        innerMap.put(key, obj);
                    } else {
                        object.put(key, obj);
                    }

                    if (parentIsArray) {
                        setContext(context, obj, key);
                    }

                    token = lexer.token;
                    if (token == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);

                        if (!disableCircularReferenceDetect) {
                            this.contex = context;
                        }
                        return object;
                    } else if (token == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error, " + lexer.info());
                    }
                } else if (ch == 't') {
                    if (lexer.text.startsWith("true", lexer.bp)) {
                        lexer.bp += 3;
                        lexer.next();
                        object.put(key, Boolean.TRUE);
                    }
                } else if (ch == 'f') {
                    if (lexer.text.startsWith("false", lexer.bp)) {
                        lexer.bp += 4;
                        lexer.next();
                        object.put(key, Boolean.FALSE);
                    }
                } else {
                    lexer.nextToken();
                    value = parse();

                    object.put(object.getClass() == JSONObject.class //
                        ? key.toString() //
                        : key, //
                               value);

                    if (lexer.token == JSONToken.RBRACE) {
                        lexer.nextToken(JSONToken.COMMA);
                        return object;
                    } else if (lexer.token == JSONToken.COMMA) {
                        continue;
                    } else {
                        throw new JSONException("syntax error, " + lexer.info());
                    }
                }

                ch = lexer.ch;
                if (ch != ',' && ch != '}') {
                    lexer.skipWhitespace();
                    ch = lexer.ch;
                }

                if (ch == ',') {
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }
                    continue;
                } else if (ch == '}') {
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        ch = lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }
                    lexer.sp = 0; // lexer.resetStringPosition();
                    
                    if (ch == ',') {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                            ? EOI //
                            : lexer.text.charAt(index));
                        lexer.token = JSONToken.COMMA;
                    } else if (ch == '}') {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                            ? EOI //
                            : lexer.text.charAt(index));
                        lexer.token = JSONToken.RBRACE;
                    } else if (ch == ']') {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                            ? EOI //
                            : lexer.text.charAt(index));
                        lexer.token = JSONToken.RBRACKET;
                    } else {
                        lexer.nextToken();
                    }

                    if (!disableCircularReferenceDetect) {
                        this.setContext(this.contex, object, fieldName);
                    }

                    return object;
                } else {
                    throw new JSONException("syntax error, " + lexer.info());
                }
            }
        } finally {
            if (!disableCircularReferenceDetect) {
                this.contex = context;
            }
        }
    }

    // compatible
    @SuppressWarnings("unchecked")
    public <T> T parseObject(Class<T> clazz) {
        return (T) parseObject(clazz, null);
    }

    public <T> T parseObject(Type type) {
        return parseObject(type, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T parseObject(Type type, Object fieldName) {
        if (lexer.token == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (lexer.token == JSONToken.LITERAL_STRING) {
            if (type == byte[].class) {
                byte[] bytes = lexer.bytesValue();
                lexer.nextToken();
                return (T) bytes;
            }

            if (type == char[].class) {
                String strVal = lexer.stringVal();
                lexer.nextToken();
                return (T) strVal.toCharArray();
            }
        }

        ObjectDeserializer derializer = config.getDeserializer(type);

        try {
            return (T) derializer.deserialze(this, type, fieldName);
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        List<T> array = new ArrayList<T>();
        parseArray(clazz, array);
        return array;
    }

    public void parseArray(Class<?> clazz, @SuppressWarnings("rawtypes") Collection array) {
        parseArray((Type) clazz, array);
    }

    @SuppressWarnings("rawtypes")
    public void parseArray(Type type, Collection array) {
        parseArray(type, array, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void parseArray(Type type, Collection array, Object fieldName) {
        if (lexer.token == JSONToken.SET || lexer.token == JSONToken.TREE_SET) {
            lexer.nextToken();
        }

        if (lexer.token != JSONToken.LBRACKET) {
            throw new JSONException("exepct '[', but " + JSONToken.name(lexer.token) + ", " + lexer.info());
        }

        ObjectDeserializer deserializer = null;
        if (int.class == type) {
            deserializer = IntegerCodec.instance;
            lexer.nextToken(JSONToken.LITERAL_INT);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            lexer.nextToken(JSONToken.LITERAL_STRING);
        } else {
            deserializer = config.getDeserializer(type);
            lexer.nextToken(JSONToken.LBRACE);
        }

        ParseContext context = this.contex;
        if (!lexer.disableCircularReferenceDetect) {
            setContext(this.contex, array, fieldName);
        }
        try {
            for (int i = 0;; ++i) {
                while (lexer.token == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                if (lexer.token == JSONToken.RBRACKET) {
                    break;
                }

                if (int.class == type) {
                    Object val = IntegerCodec.instance.deserialze(this, null, null);
                    array.add(val);
                } else if (String.class == type) {
                    String value;
                    if (lexer.token == JSONToken.LITERAL_STRING) {
                        value = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);
                    } else {
                        Object obj = this.parse();
                        value = obj == null //
                            ? null //
                            : obj.toString();
                    }

                    array.add(value);
                } else {
                    Object val;
                    if (lexer.token == JSONToken.NULL) {
                        lexer.nextToken();
                        val = null;
                    } else {
                        val = deserializer.deserialze(this, type, i);
                    }
                    array.add(val);
                    if (resolveStatus == NeedToResolve) {
                        checkListResolve(array);
                    }
                }

                if (lexer.token == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }
        } finally {
            this.contex = context;
        }

        lexer.nextToken(JSONToken.COMMA);
    }

    public Object[] parseArray(Type[] types) {
        if (lexer.token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        if (lexer.token != JSONToken.LBRACKET) {
            throw new JSONException("syntax error, " + lexer.info());
        }

        Object[] list = new Object[types.length];
        if (types.length == 0) {
            lexer.nextToken(JSONToken.RBRACKET);

            if (lexer.token != JSONToken.RBRACKET) {
                throw new JSONException("syntax error, " + lexer.info());
            }

            lexer.nextToken(JSONToken.COMMA);
            return new Object[0];
        }

        lexer.nextToken(JSONToken.LITERAL_INT);

        for (int i = 0; i < types.length; ++i) {
            Object value;

            if (lexer.token == JSONToken.NULL) {
                value = null;
                lexer.nextToken(JSONToken.COMMA);
            } else {
                Type type = types[i];
                if (type == int.class || type == Integer.class) {
                    if (lexer.token == JSONToken.LITERAL_INT) {
                        value = Integer.valueOf(lexer.intValue());
                        lexer.nextToken(JSONToken.COMMA);
                    } else {
                        value = this.parse();
                        value = TypeUtils.cast(value, type, config);
                    }
                } else if (type == String.class) {
                    if (lexer.token == JSONToken.LITERAL_STRING) {
                        value = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);
                    } else {
                        value = this.parse();
                        value = TypeUtils.cast(value, type, config);
                    }
                } else {
                    boolean isArray = false;
                    Class<?> componentType = null;
                    if (i == types.length - 1) {
                        if (type instanceof Class) {
                            Class<?> clazz = (Class<?>) type;
                            isArray = clazz.isArray();
                            componentType = clazz.getComponentType();
                        }
                    }

                    // support varArgs
                    if (isArray && lexer.token != JSONToken.LBRACKET) {
                        List<Object> varList = new ArrayList<Object>();

                        ObjectDeserializer derializer = config.getDeserializer(componentType);

                        if (lexer.token != JSONToken.RBRACKET) {
                            for (;;) {
                                varList.add( //
                                             derializer.deserialze(this, type, null));

                                if (lexer.token == JSONToken.COMMA) {
                                    lexer.nextToken(JSONToken.LBRACE);
                                } else if (lexer.token == JSONToken.RBRACKET) {
                                    break;
                                } else {
                                    throw new JSONException("syntax error, " + lexer.info());
                                }
                            }
                        }

                        value = TypeUtils.cast(varList, type, config);
                    } else {
                        ObjectDeserializer derializer = config.getDeserializer(type);
                        value = derializer.deserialze(this, type, null);
                    }
                }
            }
            list[i] = value;

            if (lexer.token == JSONToken.RBRACKET) {
                break;
            }

            if (lexer.token != JSONToken.COMMA) {
                throw new JSONException("syntax error, " + lexer.info());
            }

            if (i == types.length - 1) {
                lexer.nextToken(JSONToken.RBRACKET);
            } else {
                lexer.nextToken(JSONToken.LITERAL_INT);
            }
        }

        if (lexer.token != JSONToken.RBRACKET) {
            throw new JSONException("syntax error, " + lexer.info());
        }

        lexer.nextToken(JSONToken.COMMA);

        return list;
    }

    public void parseObject(Object object) {
        Class<?> clazz = object.getClass();
        JavaBeanDeserializer beanDeser = null;
        ObjectDeserializer deserizer = config.getDeserializer(clazz);
        if (deserizer instanceof JavaBeanDeserializer) {
            beanDeser = (JavaBeanDeserializer) deserizer;
        }

        // Map<String, FieldDeserializer> setters = config.getFieldDeserializers(clazz);

        int token = lexer.token;
        if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
            throw new JSONException("syntax error, expect {, actual " + JSONToken.name(token));
        }

        for (;;) {
            // lexer.scanSymbol
            String key = lexer.scanSymbol(symbolTable);

            if (key == null) {
                if (lexer.token == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }
                if (lexer.token == JSONToken.COMMA) {
                    continue;
                }
            }

            FieldDeserializer fieldDeser = null;
            if (beanDeser != null) {
                fieldDeser = beanDeser.getFieldDeserializer(key);
            }

            if (fieldDeser == null) {
                if ((lexer.features & Feature.IgnoreNotMatch.mask) == 0) {
                    throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
                }

                lexer.nextTokenWithChar(':');
                parse(); // skip

                if (lexer.token == JSONToken.RBRACE) {
                    lexer.nextToken();
                    return;
                }

                continue;
            } else {
                Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
                Type fieldType = fieldDeser.fieldInfo.fieldType;
                Object fieldValue;
                if (fieldClass == int.class) {
                    lexer.nextTokenWithChar(':');
                    fieldValue = IntegerCodec.instance.deserialze(this, fieldType, null);
                } else if (fieldClass == String.class) {
                    lexer.nextTokenWithChar(':');
                    fieldValue = parseString();
                } else if (fieldClass == long.class) {
                    lexer.nextTokenWithChar(':');
                    fieldValue = IntegerCodec.instance.deserialze(this, fieldType, null);
                } else {
                    ObjectDeserializer fieldValueDeserializer = config.getDeserializer(fieldClass, fieldType);

                    lexer.nextTokenWithChar(':');
                    fieldValue = fieldValueDeserializer.deserialze(this, fieldType, null);
                }

                fieldDeser.setValue(object, fieldValue);
            }

            if (lexer.token == JSONToken.COMMA) {
                continue;
            }

            if (lexer.token == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                return;
            }
        }
    }

    public Object parseArrayWithType(Type collectionType) {
        if (lexer.token == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        Type[] actualTypes = ((ParameterizedType) collectionType).getActualTypeArguments();

        if (actualTypes.length != 1) {
            throw new JSONException("not support type " + collectionType);
        }

        Type actualTypeArgument = actualTypes[0];

        if (actualTypeArgument instanceof Class) {
            List<Object> array = new ArrayList<Object>();
            this.parseArray((Class<?>) actualTypeArgument, array);
            return array;
        }

        if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;

            // assert wildcardType.getUpperBounds().length == 1;
            Type upperBoundType = wildcardType.getUpperBounds()[0];

            // assert upperBoundType instanceof Class;
            if (Object.class.equals(upperBoundType)) {
                if (wildcardType.getLowerBounds().length == 0) {
                    // Collection<?>
                    return parse();
                } else {
                    throw new JSONException("not support type : " + collectionType);
                }
            }

            List<Object> array = new ArrayList<Object>();
            this.parseArray((Class<?>) upperBoundType, array);
            return array;

            // throw new JSONException("not support type : " +
            // collectionType);return parse();
        }

        if (actualTypeArgument instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
            Type[] bounds = typeVariable.getBounds();

            if (bounds.length != 1) {
                throw new JSONException("not support : " + typeVariable);
            }

            Type boundType = bounds[0];
            if (boundType instanceof Class) {
                List<Object> array = new ArrayList<Object>();
                this.parseArray((Class<?>) boundType, array);
                return array;
            }
        }

        if (actualTypeArgument instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) actualTypeArgument;

            List<Object> array = new ArrayList<Object>();
            this.parseArray(parameterizedType, array);
            return array;
        }

        throw new JSONException("TODO : " + collectionType);
    }

    @SuppressWarnings("rawtypes")
    protected void checkListResolve(Collection array) {
        if (array instanceof List) {
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = new ResolveFieldDeserializer(this, (List) array, array.size() - 1);
            task.ownerContext = contex;
            resolveStatus = DefaultJSONParser.NONE;
        } else {
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = new ResolveFieldDeserializer(array);
            task.ownerContext = contex;
            resolveStatus = DefaultJSONParser.NONE;
        }
    }

    @SuppressWarnings("rawtypes")
    protected void checkMapResolve(Map object, Object fieldName) {
        ResolveFieldDeserializer fieldResolver = new ResolveFieldDeserializer(object, fieldName);
        ResolveTask task = getLastResolveTask();
        task.fieldDeserializer = fieldResolver;
        task.ownerContext = contex;
        resolveStatus = DefaultJSONParser.NONE;
    }

    @SuppressWarnings("rawtypes")
    public Object parseObject(final Map object) {
        return parseObject(object, null);
    }

    public JSONObject parseObject() {
        JSONObject object = (lexer.features & Feature.OrderedField.mask) != 0 //
            ? new JSONObject(new LinkedHashMap<String, Object>()) //
            : new JSONObject();
        return (JSONObject) parseObject(object, null);
    }

    @SuppressWarnings("rawtypes")
    public final void parseArray(final Collection array) {
        parseArray(array, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(final Collection array, Object fieldName) {
        int token = lexer.token;
        if (token == JSONToken.SET || token == JSONToken.TREE_SET) {
            lexer.nextToken();
            token = lexer.token;
        }

        if (token != JSONToken.LBRACKET) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(token) + ", pos " + lexer.pos);
        }
        
        final boolean disableCircularReferenceDetect = lexer.disableCircularReferenceDetect;

        ParseContext context = this.contex;
        if (!disableCircularReferenceDetect) {
            setContext(this.contex, array, fieldName);
        }
        
        try {
            boolean first_quote;
            char ch = lexer.ch;
            if (ch != '"') {
                if (ch == ']') {
                    lexer.next();
                    lexer.nextToken(JSONToken.COMMA);
                    return;
                }
                if (ch == '{') {
                    // lexer.next();
                    {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len //
                                ? EOI //
                                : lexer.text.charAt(index));
                    }
                    lexer.token = JSONToken.LBRACE;
                } else {
                    lexer.nextToken(JSONToken.LBRACE);
                }
                first_quote = false;
            } else {
                if ((lexer.features & Feature.AllowISO8601DateFormat.mask) == 0) {
                    first_quote = true;
                } else {
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                    first_quote = false;
                }
            }

            for (int i = 0;; ++i) {
                Object value;
                if (first_quote && lexer.ch == '"') {
                    value = lexer.scanStringValue('"');

                    ch = lexer.ch;
                    if (ch == ',') {
                        // lexer.next();
                        {
                            int index = ++lexer.bp;
                            ch = lexer.ch = (index >= lexer.len //
                                    ? EOI //
                                    : lexer.text.charAt(index));
                        }
                        array.add(value);
                        if (resolveStatus == NeedToResolve) {
                            checkListResolve(array);
                        }

                        if (ch == '"') {
                            continue;
                        } else {
                            first_quote = false;
                        }

                        lexer.nextToken();
                    } else if (ch == ']') {
                        // lexer.next();
                        {
                            int index = ++lexer.bp;
                            lexer.ch = (index >= lexer.len //
                                    ? EOI //
                                    : lexer.text.charAt(index));
                        }
                        array.add(value);
                        if (resolveStatus == NeedToResolve) {
                            checkListResolve(array);
                        }
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    } else {
                        lexer.nextToken();
                    }
                }

                token = lexer.token;
                while (token == JSONToken.COMMA) {
                    lexer.nextToken();
                    token = lexer.token;
                    continue;
                }

                switch (token) {
                    case LITERAL_INT:
                        value = lexer.integerValue();
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LITERAL_FLOAT:
                        if ((lexer.features & Feature.UseBigDecimal.mask) != 0) {
                            value = lexer.decimalValue(true);
                        } else {
                            value = lexer.decimalValue(false);
                        }
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LITERAL_STRING:
                        String stringLiteral = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);

                        if ((lexer.features & Feature.AllowISO8601DateFormat.mask) != 0) {
                            JSONLexer iso8601Lexer = new JSONLexer(stringLiteral);
                            if (iso8601Lexer.scanISO8601DateIfMatch(true)) {
                                value = iso8601Lexer.calendar.getTime();
                            } else {
                                value = stringLiteral;
                            }
                            iso8601Lexer.close();
                        } else {
                            value = stringLiteral;
                        }

                        break;
                    case TRUE:
                        value = Boolean.TRUE;
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case FALSE:
                        value = Boolean.FALSE;
                        lexer.nextToken(JSONToken.COMMA);
                        break;
                    case LBRACE:
                        JSONObject object;
                        if ((lexer.features & Feature.OrderedField.mask) != 0) {
                            object = new JSONObject(new LinkedHashMap<String, Object>());
                        } else {
                            object = new JSONObject();
                        }
                        value = parseObject(object, i);
                        break;
                    case LBRACKET:
                        Collection items = new JSONArray();
                        parseArray(items, i);
                        value = items;
                        break;
                    case NULL:
                        value = null;
                        lexer.nextToken(JSONToken.LITERAL_STRING);
                        break;
                    case UNDEFINED:
                        value = null;
                        lexer.nextToken(JSONToken.LITERAL_STRING);
                        break;
                    case RBRACKET:
                        lexer.nextToken(JSONToken.COMMA);
                        return;
                    case EOF:
                        throw new JSONException("unclosed jsonArray");
                    default:
                        value = parse();
                        break;
                }

                array.add(value);
                if (resolveStatus == NeedToResolve) {
                    checkListResolve(array);
                }
                if (lexer.token == JSONToken.COMMA) {
                    // lexer.nextToken(JSONToken.LITERAL_STRING);
                    ch = lexer.ch;
                    if (ch == '"') {
                        lexer.pos = lexer.bp;
                        lexer.scanString();
                    } else if (ch >= '0' && ch <= '9') {
                        lexer.pos = lexer.bp;
                        lexer.scanNumber();
                    } else if (ch == '{') {
                        lexer.token = JSONToken.LBRACE;
                        // next();
                        {
                            int index = ++lexer.bp;
                            lexer.ch = (index >= lexer.len ? //
                                EOI //
                                : lexer.text.charAt(index));
                        }
                    } else {
                        lexer.nextToken();
                    }
                    continue;
                }
            }
        } finally {
            if (!disableCircularReferenceDetect) {
                this.contex = context;
            }
        }
    }

    protected void addResolveTask(ResolveTask task) {
        if (resolveTaskList == null) {
            resolveTaskList = new ArrayList<ResolveTask>(2);
        }
        resolveTaskList.add(task);
    }

    protected ResolveTask getLastResolveTask() {
        return resolveTaskList.get(resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (extraProcessors == null) {
            extraProcessors = new ArrayList<ExtraProcessor>(2);
        }
        return extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (extraTypeProviders == null) {
            extraTypeProviders = new ArrayList<ExtraTypeProvider>(2);
        }
        return extraTypeProviders;
    }

    public void setContext(ParseContext context) {
        if (lexer.disableCircularReferenceDetect) {
            return;
        }
        this.contex = context;
    }

    protected void popContext() {
        this.contex = this.contex.parent;
        contextArray[contextArrayIndex - 1] = null;
        contextArrayIndex--;
    }

    protected ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        if (lexer.disableCircularReferenceDetect) {
            return null;
        }

        this.contex = new ParseContext(parent, object, fieldName);

        int i = contextArrayIndex++;
        if (contextArray == null) {
            contextArray = new ParseContext[8];
        } else if (i >= contextArray.length) {
            int newLen = (contextArray.length * 3) / 2;
            ParseContext[] newArray = new ParseContext[newLen];
            System.arraycopy(contextArray, 0, newArray, 0, contextArray.length);
            contextArray = newArray;
        }
        contextArray[i] = contex;

        return this.contex;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parse(Object fieldName) {
        switch (lexer.token) {
            case SET:
                lexer.nextToken();
                HashSet<Object> set = new HashSet<Object>();
                parseArray(set, fieldName);
                return set;
            case TREE_SET:
                lexer.nextToken();
                TreeSet<Object> treeSet = new TreeSet<Object>();
                parseArray(treeSet, fieldName);
                return treeSet;
            case LBRACKET:
                JSONArray array = new JSONArray();
                parseArray(array, fieldName);
                return array;
            case LBRACE:
                JSONObject object = (lexer.features & Feature.OrderedField.mask) != 0 //
                    ? new JSONObject(new LinkedHashMap<String, Object>()) //
                    : new JSONObject();
                return parseObject(object, fieldName);
            case LITERAL_INT:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue;
            case LITERAL_FLOAT:
                boolean useBigDecimal = (lexer.features & Feature.UseBigDecimal.mask) != 0;
                Object value = lexer.decimalValue(useBigDecimal);
                lexer.nextToken();
                return value;
            case LITERAL_STRING:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if ((lexer.features & Feature.AllowISO8601DateFormat.mask) != 0) {
                    JSONLexer iso8601Lexer = new JSONLexer(stringLiteral);
                    try {
                        if (iso8601Lexer.scanISO8601DateIfMatch(true)) {
                            return iso8601Lexer.calendar.getTime();
                        }
                    } finally {
                        iso8601Lexer.close();
                    }
                }

                return stringLiteral;
            case NULL:
            case UNDEFINED:
                lexer.nextToken();
                return null;
            case TRUE:
                lexer.nextToken(JSONToken.COMMA);
                return Boolean.TRUE;
            case FALSE:
                lexer.nextToken(JSONToken.COMMA);
                return Boolean.FALSE;
            case NEW:
                lexer.nextToken(JSONToken.IDENTIFIER);

                if (lexer.token != JSONToken.IDENTIFIER) {
                    throw new JSONException("syntax error, " + lexer.info());
                }
                lexer.nextToken(JSONToken.LPAREN);

                accept(JSONToken.LPAREN);
                long time = ((Number) lexer.integerValue()).longValue();
                accept(JSONToken.LITERAL_INT);

                accept(JSONToken.RPAREN);

                return new Date(time);
            case EOF:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("syntax error, " + lexer.info());
            case ERROR:
            default:
                throw new JSONException("syntax error, " + lexer.info());
        }
    }

    public void config(Feature feature, boolean state) {
        lexer.config(feature, state);
    }

    public final void accept(final int token) {
        if (lexer.token == token) {
            lexer.nextToken();
        } else {
            throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual "
                                    + JSONToken.name(lexer.token));
        }
    }

    public void close() {
        try {
            if (lexer.token != JSONToken.EOF) {
                throw new JSONException("not close json text, token : " + JSONToken.name(lexer.token));
            }
        } finally {
            lexer.close();
        }
    }

    public void handleResovleTask(Object value) {
        if (resolveTaskList == null) {
            return;
        }

        for (int i = 0, size = resolveTaskList.size(); i < size; ++i) {
            ResolveTask task = resolveTaskList.get(i);
            FieldDeserializer fieldDeser = task.fieldDeserializer;

            if (fieldDeser == null) {
                continue;
            }

            Object object = null;
            if (task.ownerContext != null) {
                object = task.ownerContext.object;
            }

            String ref = task.referenceValue;
            Object refValue = null;
            if (ref.startsWith("$")) {
                for (int j = 0; j < contextArrayIndex; ++j) {
                    if (ref.equals(contextArray[j].toString())) {
                        refValue = contextArray[j].object;
                    }
                }
            } else {
                refValue = task.context.object;
            }
            fieldDeser.setValue(object, refValue);
        }
    }
    
    public String parseString() {
        int token = lexer.token;
        if (token == JSONToken.LITERAL_STRING) {
            String val = lexer.stringVal();
            if (lexer.ch == ',') {
                int index = ++lexer.bp;
                lexer.ch = (index >= lexer.len ? //
                    JSONLexer.EOI //
                    : lexer.text.charAt(index));
                lexer.token = JSONToken.COMMA;
            } else if (lexer.ch == ']') {
                int index = ++lexer.bp;
                lexer.ch = (index >= lexer.len ? //
                    JSONLexer.EOI //
                    : lexer.text.charAt(index));
                lexer.token = JSONToken.RBRACKET;
            } else if (lexer.ch == '}') {
                int index = ++lexer.bp;
                lexer.ch = (index >= lexer.len ? //
                    JSONLexer.EOI //
                    : lexer.text.charAt(index));
                lexer.token = JSONToken.RBRACE;
            } else {
                lexer.nextToken();
            }
            return val;
        }
        
        if (token == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return val;
        }

        Object value = parse();

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public static class ResolveTask {

        private final ParseContext context;
        private final String       referenceValue;
        public FieldDeserializer   fieldDeserializer;
        public ParseContext        ownerContext;

        public ResolveTask(ParseContext context, String referenceValue){
            this.context = context;
            this.referenceValue = referenceValue;
        }
    }
}
