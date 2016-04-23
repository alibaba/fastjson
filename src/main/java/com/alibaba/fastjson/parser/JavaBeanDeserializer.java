package com.alibaba.fastjson.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {

    private final FieldDeserializer[] fieldDeserializers;
    private final FieldDeserializer[] sortedFieldDeserializers;
    private final Class<?>            clazz;
    public final JavaBeanInfo         beanInfo;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        this(config, clazz, type, JavaBeanInfo.build(clazz, clazz.getModifiers(), type, false, true, true, true));
    }
    
    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type, JavaBeanInfo beanInfo){
        this.clazz = clazz;
        this.beanInfo = beanInfo;
        
        sortedFieldDeserializers = new FieldDeserializer[beanInfo.sortedFields.length];
        for (int i = 0, size = beanInfo.sortedFields.length; i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.sortedFields[i];
            FieldDeserializer fieldDeserializer = config.createFieldDeserializer(config, clazz, fieldInfo);

            sortedFieldDeserializers[i] = fieldDeserializer;
        }

        fieldDeserializers = new FieldDeserializer[beanInfo.fields.length];
        for (int i = 0, size = beanInfo.fields.length; i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.fields[i];
            FieldDeserializer fieldDeserializer = getFieldDeserializer(fieldInfo.name);
            fieldDeserializers[i] = fieldDeserializer;
        }
    }

    protected Object createInstance(DefaultJSONParser parser, Type type) {
        if (type instanceof Class) {
            if (clazz.isInterface()) {
                Class<?> clazz = (Class<?>) type;
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                boolean ordred = (parser.lexer.features & Feature.OrderedField.mask) != 0;
                JSONObject object = new JSONObject(ordred);
                Object proxy = Proxy.newProxyInstance(loader, new Class<?>[] { clazz }, object);
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
                object = constructor.newInstance(parser.contex.object);
            }
        
            if (parser != null // 
                    && (parser.lexer.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
                for (FieldInfo fieldInfo : beanInfo.fields) {
                    if (fieldInfo.fieldClass == String.class) {
                        fieldInfo.set(object, "");
                    }
                }
            }
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        return object;
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null);
    }

    @SuppressWarnings({ "unchecked" })
    private <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        final JSONLexer lexer = parser.lexer; // xxx
        object = createInstance(parser, type);

        int size = sortedFieldDeserializers.length;
        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';
            FieldDeserializer fieldDeser = sortedFieldDeserializers[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass == int.class) {
                Number number = lexer.scanNumberValue();
                fieldDeser.setValue(object, number);
                lexer.nextToken(JSONToken.COMMA);
            } else if (fieldClass == String.class) {
                String strVal;
                if (lexer.ch == '"') {
                    strVal = lexer.scanStringValue('"');
                } else {
                    lexer.nextToken();
                    if (lexer.token == JSONToken.NULL) {
                        strVal = null;
                    } else {
                        throw new JSONException("not match string. feild : " + fieldName);
                    }
                }
                fieldDeser.setValue(object, strVal);
                lexer.nextToken(JSONToken.COMMA);
            } else if (fieldClass == long.class) {
                Number number = lexer.scanNumberValue();
                if (!(number instanceof Long)) {
                    number = number.longValue();
                }
                fieldDeser.setValue(object, number);
                lexer.nextToken(JSONToken.COMMA);
            } else if (fieldClass.isEnum()) {
                String enumName = lexer.scanSymbol(parser.symbolTable);
                @SuppressWarnings("rawtypes")
                Object value = enumName == null //
                    ? null//
                    : Enum.valueOf((Class<? extends Enum>) fieldClass, enumName);
                fieldDeser.setValue(object, value);
                lexer.nextToken(JSONToken.COMMA);
            } else {
                lexer.nextToken(JSONToken.LBRACKET);
                Object value = parser.parseObject(fieldDeser.fieldInfo.fieldType);
                fieldDeser.setValue(object, value);

                if (seperator == ']') {
                    if (lexer.token != JSONToken.RBRACKET) {
                        throw new JSONException("syntax error");
                    }
                    lexer.nextToken(JSONToken.COMMA);
                } else if (seperator == ',') {
                    if (lexer.token != JSONToken.COMMA) {
                        throw new JSONException("syntax error");
                    }
                }
            }
        }
        lexer.nextToken(JSONToken.COMMA);

        return (T) object;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        if (type == JSON.class || type == JSONObject.class) {
            return (T) parser.parse();
        }

        final JSONLexer lexer = parser.lexer; // xxx

        int token = lexer.token;
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        ParseContext context = parser.contex;
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
                int beanFeatures = 0;
                if (beanInfo.jsonType != null) {
                    for (Feature feature: beanInfo.jsonType.parseFeatures()) {
                        beanFeatures |= feature.mask;
                    }
                }
                
                boolean isSupportArrayToBean = (beanFeatures & Feature.SupportArrayToBean.mask) != 0
                                               || (lexer.features & Feature.SupportArrayToBean.mask) != 0;
                if (isSupportArrayToBean) {
                    return deserialzeArrayMapping(parser, type, fieldName, object);
                }
            }

            if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
                if (lexer.isBlankInput()) {
                    return null;
                }
                
                if (token == JSONToken.LITERAL_STRING) {
                    String strVal = lexer.stringVal();
                    if (strVal.length() == 0) {
                        lexer.nextToken();
                        return null;
                    }
                }
                
                StringBuffer buf = (new StringBuffer()) //
                                                        .append("syntax error, expect {, actual ") //
                                                        .append(lexer.info()) //
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
                    } else if (fieldInfo.isEnum
                            && parser.config.getDeserializer(fieldClass) instanceof EnumDeserializer
                            ) {
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
                        token = lexer.token;
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
                        token = lexer.token;
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
                        if (lexer.token != JSONToken.RBRACE) {
                            throw new JSONException("illegal ref");
                        }
                        lexer.nextToken(JSONToken.COMMA);

                        parser.setContext(context, object, fieldName);

                        return (T) object;
                    }

                    if (JSON.DEFAULT_TYPE_KEY == key) {
                        lexer.nextTokenWithChar(':');
                        if (lexer.token == JSONToken.LITERAL_STRING) {
                            String typeName = lexer.stringVal();
                            lexer.nextToken(JSONToken.COMMA);

                            if (type instanceof Class && typeName.equals(((Class<?>) type).getName())) {
                                if (lexer.token == JSONToken.RBRACE) {
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
                            try {
                                if (fieldClass == int.class || fieldClass == Integer.class) {
                                    if (fieldInfo.fieldAccess && fieldClass == int.class) {
                                        fieldDeser.setValue(object, fieldValueInt);
                                    } else {
                                        fieldDeser.setValue(object, Integer.valueOf(fieldValueInt));
                                    }
                                } else if (fieldClass == long.class || fieldClass == Long.class) {
                                    if (fieldInfo.fieldAccess && fieldClass == long.class) {
                                        fieldDeser.setValue(object, fieldValueLong);
                                    } else {
                                        fieldDeser.setValue(object, Long.valueOf(fieldValueLong));
                                    }
                                } else if (fieldClass == float.class || fieldClass == Float.class) {
                                    if (fieldInfo.fieldAccess && fieldClass == float.class) {
                                        fieldDeser.setValue(object, fieldValueFloat);
                                    } else {
                                        fieldDeser.setValue(object, new Float(fieldValueFloat));
                                    }
                                } else if (fieldClass == double.class || fieldClass == Double.class) {
                                    if (fieldInfo.fieldAccess && fieldClass == double.class) {
                                        fieldDeser.setValue(object, fieldValueDouble);
                                    } else {
                                        fieldDeser.setValue(object, new Double(fieldValueDouble));
                                    }
                                } else {
                                    fieldDeser.setValue(object, fieldValue);
                                }
                            } catch (IllegalAccessException ex) {
                                throw new JSONException("set property error, " + fieldInfo.name, ex);
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
                        if (lexer.token == JSONToken.RBRACE) {
                            lexer.nextToken();
                            break;
                        }

                        continue;
                    }
                }

                if (lexer.token == JSONToken.COMMA) {
                    continue;
                }

                if (lexer.token == JSONToken.RBRACE) {
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                }

                if (lexer.token == JSONToken.IDENTIFIER || lexer.token == JSONToken.ERROR) {
                    throw new JSONException("syntax error, unexpect token " + JSONToken.name(lexer.token));
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
    

    protected FieldDeserializer getFieldDeserializer(String key) {
        if (key == null) {
            return null;
        }
        
        if (beanInfo.ordered) {
            for (int i = 0; i < sortedFieldDeserializers.length; ++i) {
                if (sortedFieldDeserializers[i].fieldInfo.name.equalsIgnoreCase(key)) {
                    return sortedFieldDeserializers[i];
                }
            }
            return null;
        }
        
        int low = 0;
        int high = sortedFieldDeserializers.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            
            String fieldName = sortedFieldDeserializers[mid].fieldInfo.name;
            
            int cmp = fieldName.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return sortedFieldDeserializers[mid]; // key found
            }
        }
        
        return null;  // key not found.
    }

    private boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType,
                              Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer; // xxx

        FieldDeserializer fieldDeserializer = getFieldDeserializer(key);

        if (fieldDeserializer == null) {
            boolean startsWithIs = key.startsWith("is");
            
            for (FieldDeserializer fieldDeser : sortedFieldDeserializers) {
                FieldInfo fieldInfo = fieldDeser.fieldInfo;
                Class<?> fieldClass = fieldInfo.fieldClass;
                String fieldName = fieldInfo.name;
                if (fieldName.equalsIgnoreCase(key)) {
                    fieldDeserializer = fieldDeser;
                    break;
                }
                
                if (startsWithIs //
                        && (fieldClass == boolean.class || fieldClass == Boolean.class) //
                        && fieldName.equalsIgnoreCase(key.substring(2))) {
                    fieldDeserializer = fieldDeser;
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
        Type type = null; 
        List<ExtraTypeProvider> extraTypeProviders = parser.extraTypeProviders;
        if (extraTypeProviders != null) {
            for (ExtraTypeProvider extraProvider : extraTypeProviders) {
                type = extraProvider.getExtraType(object, key);
            }
        }
        
        Object value = type == null //
            ? parser.parse() // skip
            : parser.parseObject(type);
            
        if (object instanceof ExtraProcessable) {
            ExtraProcessable extraProcessable = ((ExtraProcessable) object);
            extraProcessable.processExtra(key, value);
            return;
        }

        List<ExtraProcessor> extraProcessors = parser.extraProcessors;
        if (extraProcessors != null) {
            for (ExtraProcessor process : extraProcessors) {
                process.processExtra(object, key, value);
            }
        }
    }

    public Object createInstance(Map<String, Object> map, ParserConfig config) //
                                                                               throws IllegalAccessException,
                                                                               IllegalArgumentException,
                                                                               InvocationTargetException {
        Object object = null;
        
        if (beanInfo.creatorConstructor == null) {
            object = createInstance(null, clazz);
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                FieldDeserializer fieldDeser = getFieldDeserializer(entry.getKey());
                if (fieldDeser == null) {
                    continue;
                }

                Object value = entry.getValue();
                Method method = fieldDeser.fieldInfo.method;
                if (method != null) {
                    Type paramType = method.getGenericParameterTypes()[0];
                    value = TypeUtils.cast(value, paramType, config);
                    method.invoke(object, new Object[] { value });
                } else {
                    Field field = fieldDeser.fieldInfo.field;
                    Type paramType = fieldDeser.fieldInfo.fieldType;
                    value = TypeUtils.cast(value, paramType, config);
                    field.set(object, value);
                }
            }
            
            return object;
        }
        
        FieldInfo[] fieldInfoList = beanInfo.fields;
        int size = fieldInfoList.length;
        Object[] params = new Object[size];
        for (int i = 0; i < size; ++i) {
            FieldInfo fieldInfo = fieldInfoList[i];
            params[i] = map.get(fieldInfo.name);
        }
        
        if (beanInfo.creatorConstructor != null) {
            try {
                object = beanInfo.creatorConstructor.newInstance(params);
            } catch (Exception e) {
                throw new JSONException("create instance error, "
                                        + beanInfo.creatorConstructor.toGenericString(), e);
            }
        }
        
        return object;
    }
}
