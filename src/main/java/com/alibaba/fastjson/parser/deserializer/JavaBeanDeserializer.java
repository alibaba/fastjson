package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.IdentityHashMap;
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
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {

    public final Map<String, FieldDeserializer> feildDeserializerMap     = new IdentityHashMap<String, FieldDeserializer>();

    private final FieldDeserializer[]           fieldDeserializers;
    private final FieldDeserializer[]           sortedFieldDeserializers;
    private final Class<?>                       clazz;

    private DeserializeBeanInfo                  beanInfo;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz){
        this(config, clazz, clazz);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        this.clazz = clazz;

        beanInfo = DeserializeBeanInfo.computeSetters(clazz, type);

        fieldDeserializers = new FieldDeserializer[beanInfo.fields.size()];
        for (int i = 0, size = beanInfo.fields.size(); i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.fields.get(i);
            String interName = fieldInfo.name.intern();
            FieldDeserializer fieldDeserializer = config.createFieldDeserializer(config, clazz, fieldInfo);

            feildDeserializerMap.put(interName, fieldDeserializer);
            fieldDeserializers[i] = fieldDeserializer;
        }

        sortedFieldDeserializers = new FieldDeserializer[beanInfo.sortedFieldList.size()];
        for (int i = 0, size = beanInfo.sortedFieldList.size(); i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.sortedFieldList.get(i);
            FieldDeserializer fieldDeserializer = feildDeserializerMap.get(fieldInfo.name.intern());
            sortedFieldDeserializers[i] = fieldDeserializer;
        }
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

        if (beanInfo.defaultConstructor == null) {
            return null;
        }

        Object object;
        try {
            Constructor<?> constructor = beanInfo.defaultConstructor;
            if (beanInfo.defaultConstructorParameterSize == 0) {
                object = constructor.newInstance();
            } else {
                object = constructor.newInstance(parser.getContext().object);
            }
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        if ((parser.lexer.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            for (FieldInfo fieldInfo : beanInfo.fields) {
                if (fieldInfo.fieldClass == String.class) {
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
        final JSONLexer lexer = parser.lexer; // xxx
        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("error");
        }

        object = createInstance(parser, type);

        int size = sortedFieldDeserializers.length;
        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';
            FieldDeserializer fieldDeser = sortedFieldDeserializers[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass == int.class) {
                int value = lexer.scanInt(seperator);
                fieldDeser.setValue(object, Integer.valueOf(value));
            } else if (fieldClass == String.class) {
                String value = lexer.scanString(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == long.class) {
                long value = lexer.scanLong(seperator);
                fieldDeser.setValue(object, Long.valueOf(value));
            } else if (fieldClass.isEnum()) {
                Enum<?> value = lexer.scanEnum(fieldClass, parser.symbolTable, seperator);
                fieldDeser.setValue(object, value);
            } else {
                lexer.nextToken(JSONToken.LBRACKET);
                Object value = parser.parseObject(fieldDeser.fieldInfo.fieldType);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        if (type == JSON.class || type == JSONObject.class) {
            return (T) parser.parse();
        }

        final JSONLexer lexer = parser.lexer; // xxx

        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        ParseContext context = parser.getContext();
        if (object != null && context != null) {
            context = context.parent;
        }
        ParseContext childContext = null;

        try {
            Map<String, Object> fieldValues = null;

            if (token == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                if (object == null) {
                    object = createInstance(parser, type);
                }
                return (T) object;
            }

            if (token == JSONToken.LBRACKET) {
                boolean isSupportArrayToBean = (beanInfo.parserFeatures & Feature.SupportArrayToBean.mask) != 0
                                               || (lexer.features & Feature.SupportArrayToBean.mask) != 0;
                if (isSupportArrayToBean) {
                    return deserialzeArrayMapping(parser, type, fieldName, object);
                }
            }

            if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
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

            if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
                parser.resolveStatus = DefaultJSONParser.NONE;
            }

            for (int fieldIndex = 0, size = sortedFieldDeserializers.length;; fieldIndex++) {
                String key = null;
                FieldDeserializer fieldDeser = null;
                FieldInfo fieldInfo = null;
                Class<?> fieldClass = null;
                if (fieldIndex < size) {
                    fieldDeser = sortedFieldDeserializers[fieldIndex];
                    fieldInfo = fieldDeser.fieldInfo;
                    fieldClass = fieldInfo.fieldClass;
                }

                boolean matchField = false;
                boolean valueParsed = false;
                
                Object fieldValue = null;
                int fieldValueInt = 0;
                long fieldValueLong = 0;
                float fieldValueFloat = 0;
                double fieldValueDouble = 0;
                if (fieldDeser != null) {
                    char[] name_chars = fieldInfo.name_chars;
                    if (fieldClass == int.class || fieldClass == Integer.class) {
                        fieldValueInt = lexer.scanFieldInt(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == long.class || fieldClass == Long.class) {
                        fieldValueLong = lexer.scanFieldLong(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == String.class) {
                        fieldValue = lexer.scanFieldString(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == boolean.class || fieldClass == Boolean.class) {
                        fieldValue = lexer.scanFieldBoolean(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == float.class || fieldClass == Float.class) {
                        fieldValueFloat = lexer.scanFieldFloat(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == double.class || fieldClass == Double.class) {
                        fieldValueDouble = lexer.scanFieldDouble(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldInfo.isEnum) {
                        String enumName = lexer.scanFieldSymbol(name_chars, parser.symbolTable);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                            
                            fieldValue = Enum.valueOf((Class<Enum>)fieldClass, enumName);
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (lexer.matchField(name_chars)) {
                        matchField = true;
                    } else {
                        continue;
                    }
                }
                
                if (!matchField) {
                    key = lexer.scanSymbol(parser.symbolTable);

                    if (key == null) {
                        token = lexer.token();
                        if (token == JSONToken.RBRACE) {
                            lexer.nextToken(JSONToken.COMMA);
                            break;
                        }
                        if (token == JSONToken.COMMA) {
                            if ((lexer.features & Feature.AllowArbitraryCommas.mask) != 0) {
                                continue;
                            }
                        }
                    }

                    if ("$ref" == key) {
                        lexer.nextTokenWithChar(':');
                        token = lexer.token();
                        if (token == JSONToken.LITERAL_STRING) {
                            String ref = lexer.stringVal();
                            if ("@".equals(ref)) {
                                object = context.object;
                            } else if ("..".equals(ref)) {
                                ParseContext parentContext = context.parent;
                                if (parentContext.object != null) {
                                    object = parentContext.object;
                                } else {
                                    parser.addResolveTask(new ResolveTask(parentContext, ref));
                                    parser.resolveStatus = DefaultJSONParser.NeedToResolve;
                                }
                            } else if ("$".equals(ref)) {
                                ParseContext rootContext = context;
                                while (rootContext.parent != null) {
                                    rootContext = rootContext.parent;
                                }

                                if (rootContext.object != null) {
                                    object = rootContext.object;
                                } else {
                                    parser.addResolveTask(new ResolveTask(rootContext, ref));
                                    parser.resolveStatus = DefaultJSONParser.NeedToResolve;
                                }
                            } else {
                                parser.addResolveTask(new ResolveTask(context, ref));
                                parser.resolveStatus = DefaultJSONParser.NeedToResolve;
                            }
                        } else {
                            throw new JSONException("illegal ref, " + JSONToken.name(token));
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
                        lexer.nextTokenWithChar(':');
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

                            Class<?> userType = TypeUtils.loadClass(typeName, parser.config.defaultClassLoader);
                            ObjectDeserializer deserizer = parser.config.getDeserializer(userType);
                            return (T) deserizer.deserialze(parser, userType, fieldName);
                        } else {
                            throw new JSONException("syntax error");
                        }
                    }
                }

                if (object == null && fieldValues == null) {
                    object = createInstance(parser, type);
                    if (object == null) {
                        fieldValues = new HashMap<String, Object>(this.fieldDeserializers.length);
                    }
                    childContext = parser.setContext(context, object, fieldName);
                }

                if (matchField) {
                    if (!valueParsed) {
                        fieldDeser.parseField(parser, object, type, fieldValues);
                    } else {
                        if (object == null) {
                            if (fieldClass == int.class || fieldClass == Integer.class) {
                                fieldValue = Integer.valueOf(fieldValueInt);
                            } else if (fieldClass == long.class || fieldClass == Long.class) {
                                fieldValue = Long.valueOf(fieldValueLong);
                            } else if (fieldClass == float.class || fieldClass == Float.class) {
                                fieldValue = new Float(fieldValueFloat);
                            } else if (fieldClass == double.class || fieldClass == Double.class) {
                                fieldValue = new Double(fieldValueDouble);
                            }
                            fieldValues.put(fieldInfo.name, fieldValue);
                        } else if (fieldValue == null) {
                            if (fieldClass == int.class || fieldClass == Integer.class) {
                                if (valueParsed) {
                                    if (fieldInfo.fieldAccess) {
                                        fieldDeser.setValue(object, fieldValueInt);
                                    } else {
                                        fieldDeser.setValue(object, Integer.valueOf(fieldValueInt));
                                    }
                                } else {
                                    if (fieldClass == Integer.class) {
                                        fieldDeser.setValue(object, null);
                                    }
                                }
                            } else if (fieldClass == long.class || fieldClass == Long.class) {
                                if (valueParsed) {
                                    if (fieldInfo.fieldAccess) {
                                        fieldDeser.setValue(object, fieldValueLong);
                                    } else {
                                        fieldDeser.setValue(object, Long.valueOf(fieldValueLong));
                                    }
                                } else {
                                    if (fieldClass == Long.class) {
                                        fieldDeser.setValue(object, null);
                                    }
                                }
                            } else if (fieldClass == float.class || fieldClass == Float.class) {
                                if (valueParsed) {
                                    if (fieldInfo.fieldAccess) {
                                        fieldDeser.setValue(object, fieldValueFloat);
                                    } else {
                                        fieldDeser.setValue(object, new Float(fieldValueFloat));
                                    }
                                } else {
                                    if (fieldClass == Float.class) {
                                        fieldDeser.setValue(object, null);
                                    }
                                }
                            } else if (fieldClass == double.class || fieldClass == Double.class) {
                                if (valueParsed) {
                                    if (fieldInfo.fieldAccess) {
                                        fieldDeser.setValue(object, fieldValueDouble);
                                    } else {
                                        fieldDeser.setValue(object, new Double(fieldValueDouble));
                                    }
                                } else {
                                    if (fieldClass == Double.class) {
                                        fieldDeser.setValue(object, null);
                                    }
                                }
                            } else if (fieldClass != float.class //
                                    && fieldClass != double.class //
                                    && fieldClass != boolean.class //
                                    ) {
                                fieldDeser.setValue(object, fieldValue);
                            }
                        } else {
                            fieldDeser.setValue(object, fieldValue);
                        }
                        if (lexer.matchStat == JSONLexer.END) {
                            break;
                        }
                    }
                } else {
                    boolean match = parseField(parser, key, object, type, fieldValues);
                    if (!match) {
                        if (lexer.token() == JSONToken.RBRACE) {
                            lexer.nextToken();
                            break;
                        }

                        continue;
                    }
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

                int size = fieldDeserializers.length;
                Object[] params = new Object[size];
                for (int i = 0; i < size; ++i) {
                    FieldInfo fieldInfo = fieldDeserializers[i].fieldInfo;
                    params[i] = fieldValues.get(fieldInfo.name);
                }

                if (beanInfo.creatorConstructor != null) {
                    try {
                        object = beanInfo.creatorConstructor.newInstance(params);
                    } catch (Exception e) {
                        throw new JSONException("create instance error, "
                                                + beanInfo.creatorConstructor.toGenericString(), e);
                    }
                } else if (beanInfo.factoryMethod != null) {
                    try {
                        object = beanInfo.factoryMethod.invoke(null, params);
                    } catch (Exception e) {
                        throw new JSONException("create factory method error, " + beanInfo.factoryMethod.toString(), e);
                    }
                }
            }

            return (T) object;
        } finally {
            if (childContext != null) {
                childContext.object = object;
            }
            parser.setContext(context);
        }
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType,
                              Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer; // xxx

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

        lexer.nextTokenWithChar(':');

        fieldDeserializer.parseField(parser, object, objectType, fieldValues);

        return true;
    }

    void parseExtra(DefaultJSONParser parser, Object object, String key) {
        final JSONLexer lexer = parser.lexer; // xxx
        if ((parser.lexer.features & Feature.IgnoreNotMatch.mask) == 0) {
            throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
        }

        lexer.nextTokenWithChar(':');
        Type type = DefaultJSONParser.getExtratype(parser, object, key);
        Object value;
        if (type == null) {
            value = parser.parse(); // skip
        } else {
            value = parser.parseObject(type);
        }

        DefaultJSONParser.processExtra(parser, object, key, value);
    }

}
