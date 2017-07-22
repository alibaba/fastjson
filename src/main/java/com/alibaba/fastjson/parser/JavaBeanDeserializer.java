package com.alibaba.fastjson.parser;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    private final Map<String, FieldDeserializer> alterNameFieldDeserializers;

    private final Class<?>            clazz;
    public final JavaBeanInfo         beanInfo;
    private ConcurrentMap<String, Object> extraFieldDeserializers;

    private transient long[] smartMatchHashArray;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        this(config, clazz, type, JavaBeanInfo.build(clazz, clazz.getModifiers(), type, false, true, true, true, config.propertyNamingStrategy));
    }
    
    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type, JavaBeanInfo beanInfo){
        this.clazz = clazz;
        this.beanInfo = beanInfo;

        Map<String, FieldDeserializer> alterNameFieldDeserializers = null;
        sortedFieldDeserializers = new FieldDeserializer[beanInfo.sortedFields.length];
        for (int i = 0, size = beanInfo.sortedFields.length; i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.sortedFields[i];
            FieldDeserializer fieldDeserializer = config.createFieldDeserializer(config, clazz, fieldInfo);

            sortedFieldDeserializers[i] = fieldDeserializer;

            for (String name : fieldInfo.alternateNames) {
                if (alterNameFieldDeserializers == null) {
                    alterNameFieldDeserializers = new HashMap<String, FieldDeserializer>();
                }
                alterNameFieldDeserializers.put(name, fieldDeserializer);
            }
        }
        this.alterNameFieldDeserializers = alterNameFieldDeserializers;

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
                boolean ordered = (parser.lexer.features & Feature.OrderedField.mask) != 0;
                JSONObject object = new JSONObject(ordered);
                Object proxy = Proxy.newProxyInstance(loader, new Class<?>[] { clazz }, object);
                return proxy;
            }
        }

        if (beanInfo.defaultConstructor == null && beanInfo.factoryMethod == null) {
            return null;
        }

        if (beanInfo.factoryMethod != null && beanInfo.defaultConstructorParameterSize > 0) {
            return null;
        }

        Object object;
        try {
            Constructor<?> constructor = beanInfo.defaultConstructor;
            if (beanInfo.defaultConstructorParameterSize == 0) {
                if (constructor != null) {
                    object = constructor.newInstance();
                } else {
                    object = beanInfo.factoryMethod.invoke(null);
                }
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        final JSONLexer lexer = parser.lexer; // xxx
        object = createInstance(parser, type);

        int size = sortedFieldDeserializers.length;
        for (int i = 0; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';
            FieldDeserializer fieldDeser = sortedFieldDeserializers[i];
            FieldInfo fieldInfo = fieldDeser.fieldInfo;
            Class<?> fieldClass = fieldInfo.fieldClass;
            try {
                if (fieldClass == int.class) {
                    int intValue = (int) lexer.scanLongValue();
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.setInt(object, intValue);
                    } else {
                        fieldDeser.setValue(object, new Integer(intValue));
                    }
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else if (fieldClass == String.class) {
                    String strVal;
                    if (lexer.ch == '"') {
                        strVal = lexer.scanStringValue('"');
                    } else if (lexer.ch == 'n' //
                            && lexer.text.startsWith("null", lexer.bp)) {
                        lexer.bp += 4;
                        {
                            int index = lexer.bp;
                            lexer.ch = lexer.bp >= lexer.len ? //
                                JSONLexer.EOI //
                                : lexer.text.charAt(index);
                        }
                        strVal = null;
                    } else {
                        throw new JSONException("not match string. feild : " + fieldName);
                    }
                    
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.set(object, strVal);
                    } else {
                        fieldDeser.setValue(object, strVal);
                    }
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else if (fieldClass == long.class) {
                    long longValue = lexer.scanLongValue();
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.setLong(object, longValue);
                    } else {
                        fieldDeser.setValue(object, new Long(longValue));
                    }
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else if (fieldClass == boolean.class) {
                    boolean booleanValue = lexer.scanBoolean();
                    if (fieldInfo.fieldAccess) {
                        fieldInfo.field.setBoolean(object, booleanValue);
                    } else {
                        fieldDeser.setValue(object, booleanValue);
                    }
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else if (fieldClass.isEnum()) {
                    char ch = lexer.ch;
                    Object value;
                    if (ch == '\"') {
                        String enumName = lexer.scanSymbol(parser.symbolTable);
                        value = (enumName == null) //
                            ? null//
                            : Enum.valueOf((Class<? extends Enum>) fieldClass, enumName);
                    } else if (ch >= '0' && ch <= '9') {
                        int ordinal = (int) lexer.scanLongValue();
                        
                        EnumDeserializer enumDeser = (EnumDeserializer) ((DefaultFieldDeserializer) fieldDeser).getFieldValueDeserilizer(parser.config);
                        value = enumDeser.values[ordinal];
                    } else {
                        throw new JSONException("illegal enum." + lexer.info());
                    }
                    
                    fieldDeser.setValue(object, value);
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else if (fieldClass == java.util.Date.class && lexer.ch == '1') {
                    long longValue = lexer.scanLongValue();
                    fieldDeser.setValue(object, new java.util.Date(longValue));
                    
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
                    } else {
                        lexer.nextToken();
                    }
                } else {
                    if (lexer.ch == '[') {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len ? //
                            JSONLexer.EOI //
                            : lexer.text.charAt(index));
                        lexer.token = JSONToken.LBRACKET;
                    } else if (lexer.ch == '{') {
                        int index = ++lexer.bp;
                        lexer.ch = (index >= lexer.len ? //
                            JSONLexer.EOI //
                            : lexer.text.charAt(index));
                        lexer.token = JSONToken.LBRACE;
                    } else {
                        lexer.nextToken();
                    }
                    
                    fieldDeser.parseField(parser, object, fieldInfo.fieldType, null);
    
                    if (seperator == ']') {
                        if (lexer.token != JSONToken.RBRACKET) {
                            throw new JSONException("syntax error");
                        }
                    } else if (seperator == ',') {
                        if (lexer.token != JSONToken.COMMA) {
                            throw new JSONException("syntax error");
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new JSONException("set " + fieldInfo.name + "error", e);
            }
        }
        
        if (lexer.ch == ',') {
            int index = ++lexer.bp;
            lexer.ch = (index >= lexer.len ? //
                JSONLexer.EOI //
                : lexer.text.charAt(index));
            lexer.token = JSONToken.COMMA;
        } else {
            lexer.nextToken();
        }

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
        
        final boolean disableCircularReferenceDetect = lexer.disableCircularReferenceDetect;

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
                boolean isSupportArrayToBean = beanInfo.supportBeanToArray //
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

            String typeKey = beanInfo.typeKey;
            long matchFieldHash = 0L;
            FieldDeserializer fieldDeser;
            for (int fieldIndex = 0, size = sortedFieldDeserializers.length;; ) {
                String key = null;
                fieldDeser = null;
                FieldInfo fieldInfo = null;
                Class<?> fieldClass = null;

                if (matchFieldHash != 0L) {
                    fieldDeser = getFieldDeserializerByHash(matchFieldHash);
                    if (fieldDeser != null) {
                        fieldInfo = fieldDeser.fieldInfo;
                        fieldClass = fieldInfo.fieldClass;
                    }
                    matchFieldHash = 0L;
                }

                if (fieldDeser == null) {
                    if (fieldIndex < size) {
                        fieldDeser = sortedFieldDeserializers[fieldIndex];
                        fieldInfo = fieldDeser.fieldInfo;
                        fieldClass = fieldInfo.fieldClass;
                        fieldIndex++;
                    } else {
                        fieldIndex++;
                    }
                }

                boolean matchField = false;
                boolean valueParsed = false;
                
                Object fieldValue = null;
                int fieldValueInt = 0;
                long fieldValueLong = 0;
                float fieldValueFloat = 0;
                double fieldValueDouble = 0;
                if (fieldDeser != null) {
                    long fieldHashCode = fieldInfo.nameHashCode;
                    if (fieldClass == int.class || fieldClass == Integer.class) {
                        // fieldValueInt = lexer.scanFieldInt(name_chars);
                        fieldValueInt = lexer.scanFieldInt(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;  
                        }
                    } else if (fieldClass == long.class || fieldClass == Long.class) {
                        // fieldValueLong = lexer.scanFieldLong(name_chars);
                        fieldValueLong = lexer.scanFieldLong(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;  
                        }
                    } else if (fieldClass == String.class) {
                        fieldValue = lexer.scanFieldString(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == boolean.class || fieldClass == Boolean.class) {
                        // fieldValue = lexer.scanFieldBoolean(name_chars);
                        fieldValue = lexer.scanFieldBoolean(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;  
                        }
                    } else if (fieldClass == float.class || fieldClass == Float.class) {
                        fieldValueFloat = lexer.scanFieldFloat(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;  
                        }
                    } else if (fieldClass == double.class || fieldClass == Double.class) {
                        fieldValueDouble = lexer.scanFieldDouble(fieldHashCode);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;  
                        }
                    } else if (fieldInfo.isEnum
                            && parser.config.getDeserializer(fieldClass) instanceof EnumDeserializer
                            ) {
                        long enumNameHashCode = lexer.scanFieldSymbol(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;

                            fieldValue = fieldDeser.getEnumByHashCode(enumNameHashCode);
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == int[].class) {
                        fieldValue = lexer.scanFieldIntArray(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == float[].class) {
                        fieldValue = lexer.scanFieldFloatArray(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == double[].class) {
                        fieldValue = lexer.scanFieldDoubleArray(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == float[][].class) {
                        fieldValue = lexer.scanFieldFloatArray2(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (fieldClass == double[][].class) {
                        fieldValue = lexer.scanFieldDoubleArray2(fieldHashCode);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            matchFieldHash = lexer.fieldHash;
                            continue;
                        }
                    } else if (lexer.matchField(fieldInfo.nameHashCode)) {
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
                            continue;
                        }
                    }

                    if ("$ref" == key && context != null) {
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


                    if ((typeKey != null && typeKey.equals(key))
                            || JSON.DEFAULT_TYPE_KEY == key) {
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

                            ObjectDeserializer deserizer = getSeeAlso(parser.config, this.beanInfo, typeName);
                            Class<?> userType = null;
                            if (deserizer == null) {
                                userType = TypeUtils.loadClass(typeName, parser.config.defaultClassLoader);
                                
                                Class<?> expectClass = TypeUtils.getClass(type);
                                if (expectClass == null || 
                                    (userType != null && expectClass.isAssignableFrom(userType))) {
                                    deserizer = parser.config.getDeserializer(userType);                                        
                                } else {
                                    throw new JSONException("type not match");
                                }
                            }

                            Object typedObject;
                            if (deserizer instanceof JavaBeanDeserializer) {
                                JavaBeanDeserializer javaBeanDeserializer = (JavaBeanDeserializer) deserizer;
                                typedObject = javaBeanDeserializer.deserialze(parser, userType, fieldName, null);
                                if (typeKey != null) {
                                    FieldDeserializer typeKeyFieldDeser = javaBeanDeserializer.getFieldDeserializer(typeKey);
                                    typeKeyFieldDeser.setValue(typedObject, typeName);
                                }
                            } else {
                                typedObject = deserizer.deserialze(parser, userType, fieldName);
                            }
                            return (T) typedObject;
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
                    if (!disableCircularReferenceDetect) {
                        childContext = parser.setContext(context, object, fieldName);
                    }
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
                    } else if (lexer.token == JSONToken.COLON) {
                        throw new JSONException("syntax error, unexpect token ':'");
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
//
//                if (lexer.token == JSONToken.EOF) {
//                    break;
//                }
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
                    Object param = fieldValues.get(fieldInfo.name);
                    if (param == null) {
                        param = TypeUtils.defaultValue(fieldInfo.fieldClass);
                    }
                    params[i] = param;
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

    protected FieldDeserializer getFieldDeserializerByHash(long fieldHash) {
        for (int i = 0; i < sortedFieldDeserializers.length; ++i) {
            FieldDeserializer fieldDeserializer = sortedFieldDeserializers[i];
            if (fieldDeserializer.fieldInfo.nameHashCode == fieldHash) {
                return fieldDeserializer;
            }
        }

        return null;
    }
    

    protected FieldDeserializer getFieldDeserializer(String key) {
        if (key == null) {
            return null;
        }
        
        if (beanInfo.ordered) {
            for (int i = 0; i < sortedFieldDeserializers.length; ++i) {
                FieldDeserializer fieldDeserializer = sortedFieldDeserializers[i];
                if (fieldDeserializer.fieldInfo.name.equalsIgnoreCase(key)) {
                    return fieldDeserializer;
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

        if (alterNameFieldDeserializers != null) {
            return alterNameFieldDeserializers.get(key);
        }
        
        return null;  // key not found.
    }



    private boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType,
                              Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer; // xxx

        FieldDeserializer fieldDeserializer = getFieldDeserializer(key);

        if (fieldDeserializer == null) {
            long smartKeyHash = TypeUtils.fnv_64_lower(key);
            if (this.smartMatchHashArray == null) {
                long[] hashArray = new long[sortedFieldDeserializers.length];
                for (int i = 0; i < sortedFieldDeserializers.length; i++) {
                    hashArray[i] = TypeUtils.fnv_64_lower(sortedFieldDeserializers[i].fieldInfo.name);
                }
                this.smartMatchHashArray = hashArray;
            }

            for (int i = 0; i < this.smartMatchHashArray.length; i++) {
                if (this.smartMatchHashArray[i] == smartKeyHash) {
                    fieldDeserializer = sortedFieldDeserializers[i];
                    break;
                }
            }

            if (fieldDeserializer == null && key.startsWith("is")) {
                smartKeyHash = TypeUtils.fnv_64_lower(key.substring(2));
                for (int i = 0; i < this.smartMatchHashArray.length; i++) {
                    if (this.smartMatchHashArray[i] == smartKeyHash) {
                        fieldDeserializer = sortedFieldDeserializers[i];
                        break;
                    }
                }
            }
        }

        final int mask = Feature.SupportNonPublicField.mask;
        if (fieldDeserializer == null
                && ((parser.lexer.features & mask) != 0
                    || (this.beanInfo.parserFeatures & mask) != 0)) {
            if (this.extraFieldDeserializers == null) {
                ConcurrentHashMap extraFieldDeserializers = new ConcurrentHashMap<String, Object>(1, 0.75f, 1);
                Field[] fields = this.clazz.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (this.getFieldDeserializer(fieldName) != null) {
                        continue;
                    }
                    int fieldModifiers = field.getModifiers();
                    if ((fieldModifiers & Modifier.FINAL) != 0 || (fieldModifiers & Modifier.STATIC) != 0) {
                        continue;
                    }
                    extraFieldDeserializers.put(fieldName, field);
                }
                this.extraFieldDeserializers = extraFieldDeserializers;
            }

            Object deserOrField = extraFieldDeserializers.get(key);
            if (deserOrField != null) {
                if (deserOrField instanceof FieldDeserializer) {
                    fieldDeserializer = ((FieldDeserializer) deserOrField);
                } else {
                    Field field = (Field) deserOrField;
                    field.setAccessible(true);
                    FieldInfo fieldInfo = new FieldInfo(key, field.getDeclaringClass(), field.getType(), field.getGenericType(), field, 0, 0);
                    fieldDeserializer = new DefaultFieldDeserializer(parser.config, clazz, fieldInfo);
                    extraFieldDeserializers.put(key, fieldDeserializer);
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
            Object param = map.get(fieldInfo.name);
            if (param == null) {
                param = TypeUtils.defaultValue(fieldInfo.fieldClass);
            }
            params[i] = param;
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
    
    protected JavaBeanDeserializer getSeeAlso(ParserConfig config, JavaBeanInfo beanInfo, String typeName) {
        if (beanInfo.jsonType == null) {
            return null;
        }
        
        for (Class<?> seeAlsoClass : beanInfo.jsonType.seeAlso()) {
            ObjectDeserializer seeAlsoDeser = config.getDeserializer(seeAlsoClass);
            if (seeAlsoDeser instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer seeAlsoJavaBeanDeser = (JavaBeanDeserializer) seeAlsoDeser;

                JavaBeanInfo subBeanInfo = seeAlsoJavaBeanDeser.beanInfo;
                if (subBeanInfo.typeName.equals(typeName)) {
                    return seeAlsoJavaBeanDeser;
                }
                
                JavaBeanDeserializer subSeeAlso = getSeeAlso(config, subBeanInfo, typeName);
                if (subSeeAlso != null) {
                    return subSeeAlso;
                }
            }
        }

        return null;
    }


}
