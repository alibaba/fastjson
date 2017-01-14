package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {

    private final FieldDeserializer[]   fieldDeserializers;
    protected final FieldDeserializer[] sortedFieldDeserializers;
    protected final Class<?>            clazz;
    public final JavaBeanInfo           beanInfo;
    private ConcurrentMap<String, Object> extraFieldDeserializers;
    
    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz) {
        this(config, clazz, clazz);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type){
        this(config, JavaBeanInfo.build(clazz, type, config.propertyNamingStrategy));
    }
    
    public JavaBeanDeserializer(ParserConfig config, JavaBeanInfo beanInfo){
        this.clazz = beanInfo.clazz;
        this.beanInfo = beanInfo;
        
        sortedFieldDeserializers = new FieldDeserializer[beanInfo.sortedFields.length];
        for (int i = 0, size = beanInfo.sortedFields.length; i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.sortedFields[i];
            FieldDeserializer fieldDeserializer = config.createFieldDeserializer(config, beanInfo, fieldInfo);

            sortedFieldDeserializers[i] = fieldDeserializer;
        }

        fieldDeserializers = new FieldDeserializer[beanInfo.fields.length];
        for (int i = 0, size = beanInfo.fields.length; i < size; ++i) {
            FieldInfo fieldInfo = beanInfo.fields[i];
            FieldDeserializer fieldDeserializer = getFieldDeserializer(fieldInfo.name);
            fieldDeserializers[i] = fieldDeserializer;
        }
    }

    public FieldDeserializer getFieldDeserializer(String key) {
        if (key == null) {
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
                ParseContext context = parser.getContext();
                String parentName = context.object.getClass().getName();
                String typeName = "";
                
                if (type instanceof Class) {
                    typeName = ((Class<?>) type).getName();
                }
              
                if(parentName.length() != typeName.lastIndexOf('$') - 1){
                	char[] typeChars = typeName.toCharArray();
                	StringBuilder clsNameBuilder = new StringBuilder();
                	clsNameBuilder.append(parentName).append("$");
                	Map<String, Object> outterCached = new HashMap<String, Object>();
                	outterCached.put(parentName, context.object);//outtest
    				for(int i = parentName.length() + 1;  i <= typeName.lastIndexOf('$'); i ++){
    					char thisChar = typeChars[i];
    					if(thisChar == '$'){
    						String clsName = clsNameBuilder.toString();
    						Object outter = outterCached.get(parentName);
    						Class<?> clazz;
							try {
								clazz = Class.forName(parentName);
							
        						if(outter != null){
        							Class<?> innerCls = Class.forName(clsName);
        							Constructor<?> innerClsConstructor = innerCls.getDeclaredConstructor(clazz);
        							if(!innerClsConstructor.isAccessible()){
        								innerClsConstructor.setAccessible(true);
        							}
        							Object inner = innerClsConstructor.newInstance(outter);
        							outterCached.put(clsName, inner);
        							parentName = clsName;
        						}
							}catch(ClassNotFoundException e){
								throw new JSONException("unable to find class " + parentName);
							}catch(NoSuchMethodException e){
								throw new RuntimeException(e);// no default contrutor
							}catch(InvocationTargetException e){
								throw new RuntimeException("can not instantiate " + clsName);
							}catch(IllegalAccessException e){
								throw new RuntimeException(e);
							}catch(InstantiationException e){
								throw new RuntimeException(e);
							}
    					}
    					clsNameBuilder.append(thisChar);
    				}
    				object = constructor.newInstance(outterCached.get(parentName));
                }else{
                	object = constructor.newInstance(context.object);
                }
                
            }
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        if (parser != null // 
                && parser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
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
        return deserialze(parser, type, fieldName, 0);
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, int features) {
        return deserialze(parser, type, fieldName, null, features);
    }

    @SuppressWarnings({ "unchecked" })
    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        final JSONLexer lexer = parser.lexer; // xxx
        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("error");
        }

        object = createInstance(parser, type);

        for (int i = 0, size = sortedFieldDeserializers.length; i < size; ++i) {
            final char seperator = (i == size - 1) ? ']' : ',';
            FieldDeserializer fieldDeser = sortedFieldDeserializers[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
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
                char ch = lexer.getCurrent();
                
                Object value;
                if (ch == '\"' || ch == 'n') {
                    value = lexer.scanEnum(fieldClass, parser.getSymbolTable(), seperator);
                } else if (ch >= '0' && ch <= '9') {
                    int ordinal = lexer.scanInt(seperator);
                    
                    EnumDeserializer enumDeser = (EnumDeserializer) ((DefaultFieldDeserializer) fieldDeser).getFieldValueDeserilizer(parser.getConfig());
                    value = enumDeser.valueOf(ordinal);
                } else {
                    value = scanEnum(lexer, seperator);
                }
                
                fieldDeser.setValue(object, value);
            } else if (fieldClass == boolean.class) {
                boolean value = lexer.scanBoolean(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == float.class) {
                float value = lexer.scanFloat(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == double.class) {
                double value = lexer.scanDouble(seperator);
                fieldDeser.setValue(object, value);
            } else if (fieldClass == java.util.Date.class && lexer.getCurrent() == '1') {
                long longValue = lexer.scanLong(seperator);
                fieldDeser.setValue(object, new java.util.Date(longValue));
            } else {
                lexer.nextToken(JSONToken.LBRACKET);
                Object value = parser.parseObject(fieldDeser.fieldInfo.fieldType);
                fieldDeser.setValue(object, value);

                check(lexer, seperator == ']' ? JSONToken.RBRACKET : JSONToken.COMMA);
                // parser.accept(seperator == ']' ? JSONToken.RBRACKET : JSONToken.COMMA);
            }
        }
        lexer.nextToken(JSONToken.COMMA);

        return (T) object;
    }

    protected void check(final JSONLexer lexer, int token) {
        if (lexer.token() != token) {
            throw new JSONException("syntax error");
        }
    }
    
    protected Enum<?> scanEnum(JSONLexer lexer, char seperator) {
        throw new JSONException("illegal enum. " + lexer.info());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> T deserialze(DefaultJSONParser parser, // 
                               Type type, // 
                               Object fieldName, // 
                               Object object, //
                               int features) {
        if (type == JSON.class || type == JSONObject.class) {
            return (T) parser.parse();
        }

        final JSONLexerBase lexer = (JSONLexerBase) parser.lexer; // xxx

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
                final int mask = Feature.SupportArrayToBean.mask;
                boolean isSupportArrayToBean = (beanInfo.parserFeatures & mask) != 0 //
                                               || lexer.isEnabled(Feature.SupportArrayToBean) //
                                               || (features & mask) != 0
                                               ;
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

                if (token == JSONToken.LBRACKET && lexer.getCurrent() == ']') {
                    lexer.next();
                    lexer.nextToken();
                    return null;
                }
                
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

            for (int fieldIndex = 0;; fieldIndex++) {
                String key = null;
                FieldDeserializer fieldDeser = null;
                FieldInfo fieldInfo = null;
                Class<?> fieldClass = null;
                JSONField feildAnnotation = null;
                if (fieldIndex < sortedFieldDeserializers.length) {
                    fieldDeser = sortedFieldDeserializers[fieldIndex];
                    fieldInfo = fieldDeser.fieldInfo;
                    fieldClass = fieldInfo.fieldClass;
                    feildAnnotation = fieldInfo.getAnnotation();
                }

                boolean matchField = false;
                boolean valueParsed = false;
                
                Object fieldValue = null;
                if (fieldDeser != null) {
                    char[] name_chars = fieldInfo.name_chars;
                    if (fieldClass == int.class || fieldClass == Integer.class) {
                        fieldValue = lexer.scanFieldInt(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == long.class || fieldClass == Long.class) {
                        fieldValue = lexer.scanFieldLong(name_chars);
                        
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
                        fieldValue = lexer.scanFieldFloat(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass == double.class || fieldClass == Double.class) {
                        fieldValue = lexer.scanFieldDouble(name_chars);
                        
                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;  
                        }
                    } else if (fieldClass.isEnum() // 
                            && parser.getConfig().getDeserializer(fieldClass) instanceof EnumDeserializer
                            && (feildAnnotation == null || feildAnnotation.deserializeUsing() == Void.class)
                            ) {
                        if (fieldDeser instanceof DefaultFieldDeserializer) {
                            ObjectDeserializer fieldValueDeserilizer = ((DefaultFieldDeserializer) fieldDeser).fieldValueDeserilizer;
                            fieldValue = this.scanEnum(lexer, name_chars, fieldValueDeserilizer);

                            if (lexer.matchStat > 0) {
                                matchField = true;
                                valueParsed = true;
                            } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                                continue;
                            }
                        }
                    } else if (fieldClass == int[].class) {
                        fieldValue = lexer.scanFieldIntArray(name_chars);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;
                        }
                    } else if (fieldClass == float[].class) {
                        fieldValue = lexer.scanFieldFloatArray(name_chars);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
                        } else if (lexer.matchStat == JSONLexer.NOT_MATCH_NAME) {
                            continue;
                        }
                    } else if (fieldClass == float[][].class) {
                        fieldValue = lexer.scanFieldFloatArray2(name_chars);

                        if (lexer.matchStat > 0) {
                            matchField = true;
                            valueParsed = true;
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
                            if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                                continue;
                            }
                        }
                    }

                    if ("$ref" == key) {
                        lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
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
                        lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                        if (lexer.token() == JSONToken.LITERAL_STRING) {
                            String typeName = lexer.stringVal();
                            lexer.nextToken(JSONToken.COMMA);

                            if (typeName.equals(beanInfo.typeName)) {
                                if (lexer.token() == JSONToken.RBRACE) {
                                    lexer.nextToken();
                                    break;
                                }
                                continue;
                            }
                            
                            ParserConfig config = parser.getConfig();
                            ObjectDeserializer deserizer = getSeeAlso(config, this.beanInfo, typeName);
                            Class<?> userType = null;
                            if (deserizer == null) {
                                userType = TypeUtils.loadClass(typeName, config.getDefaultClassLoader());
                                
                                Class<?> expectClass = TypeUtils.getClass(type);
                                if (expectClass == null || 
                                    (userType != null && expectClass.isAssignableFrom(userType))) {
                                    deserizer = parser.getConfig().getDeserializer(userType);                                        
                                } else {
                                    throw new JSONException("type not match");
                                }
                            }
                            
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
                            fieldValues.put(fieldInfo.name, fieldValue);
                        } else if (fieldValue == null) {
                            if (fieldClass != int.class //
                                    && fieldClass != long.class //
                                    && fieldClass != float.class //
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
                    } else if (lexer.token() == JSONToken.COLON) {
                        throw new JSONException("syntax error, unexpect token ':'");
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

                FieldInfo[] fieldInfoList = beanInfo.fields;
                int size = fieldInfoList.length;
                Object[] params = new Object[size];
                for (int i = 0; i < size; ++i) {
                    FieldInfo fieldInfo = fieldInfoList[i];
                    Object param = fieldValues.get(fieldInfo.name);
                    if (param == null) {
                        Type fieldType = fieldInfo.fieldType;
                        if (fieldType == byte.class) {
                            param = (byte) 0;
                        } else if (fieldType == short.class) {
                            param = (short) 0;
                        } else if (fieldType == int.class) {
                            param = 0;
                        } else if (fieldType == long.class) {
                            param = 0L;
                        } else if (fieldType == float.class) {
                            param = 0F;
                        } else if (fieldType == double.class) {
                            param = 0D;
                        } else if (fieldType == boolean.class) {
                            param = Boolean.FALSE;
                        }
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
            
            Method buildMethod = beanInfo.buildMethod;
            if (buildMethod == null) {
                return (T) object;
            }
            
            
            Object builtObj;
            try {
                builtObj = buildMethod.invoke(object);
            } catch (Exception e) {
                throw new JSONException("build object error", e);
            }
            
            return (T) builtObj;
        } finally {
            if (childContext != null) {
                childContext.object = object;
            }
            parser.setContext(context);
        }
    }

    protected Enum scanEnum(JSONLexerBase lexer, char[] name_chars, ObjectDeserializer fieldValueDeserilizer) {
        EnumDeserializer enumDeserializer = null;
        if (fieldValueDeserilizer instanceof EnumDeserializer) {
            enumDeserializer = (EnumDeserializer) fieldValueDeserilizer;
        }

        if (enumDeserializer == null) {
            lexer.matchStat = JSONLexer.NOT_MATCH;
            return null;
        }

        long enumNameHashCode = lexer.scanFieldSymbol(name_chars);
        if (lexer.matchStat > 0) {
            return enumDeserializer.getEnumByHashCode(enumNameHashCode);
        } else {
            return null;
        }
    }
    
    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType,
                              Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer; // xxx

        FieldDeserializer fieldDeserializer = smartMatch(key);

        final int mask = Feature.SupportNonPublicField.mask;
        if (fieldDeserializer == null
                && (parser.lexer.isEnabled(mask)
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
                    FieldInfo fieldInfo = new FieldInfo(key, field.getDeclaringClass(), field.getType(), field.getGenericType(), field, 0, 0, 0);
                    fieldDeserializer = new DefaultFieldDeserializer(parser.getConfig(), clazz, fieldInfo);
                    extraFieldDeserializers.put(key, fieldDeserializer);
                }
            }
        }

        if (fieldDeserializer == null) {
            if (!lexer.isEnabled(Feature.IgnoreNotMatch)) {
                throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
            }
            
            parser.parseExtra(object, key);

            return false;
        }

        lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());

        fieldDeserializer.parseField(parser, object, objectType, fieldValues);

        return true;
    }

    public FieldDeserializer smartMatch(String key) {
        if (key == null) {
            return null;
        }
        
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
            boolean snakeOrkebab = false;
            String key2 = null;
            for (int i = 0; i < key.length(); ++i) {
                char ch = key.charAt(i);
                if (ch == '_') {
                    snakeOrkebab = true;
                    key2 = key.replaceAll("_", "");
                    break;
                } else if (ch == '-') {
                    snakeOrkebab = true;
                    key2 = key.replaceAll("-", "");
                    break;
                }
            }
            if (snakeOrkebab) {
                fieldDeserializer = getFieldDeserializer(key2);
                if (fieldDeserializer == null) {
                    for (FieldDeserializer fieldDeser : sortedFieldDeserializers) {
                        if (fieldDeser.fieldInfo.name.equalsIgnoreCase(key2)) {
                            fieldDeserializer = fieldDeser;
                            break;
                        }
                    }
                }
            }
        }

        if (fieldDeserializer == null) {
            for (FieldDeserializer fieldDeser : sortedFieldDeserializers) {
                if (fieldDeser.fieldInfo.alternateName(key)) {
                    fieldDeserializer = fieldDeser;
                    break;
                }
            }
        }

        return fieldDeserializer;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
    
    public Object createInstance(Map<String, Object> map, ParserConfig config) //
                                                                               throws IllegalArgumentException,
                                                                               IllegalAccessException,
                                                                               InvocationTargetException {
        Object object = null;
        
        if (beanInfo.creatorConstructor == null && beanInfo.factoryMethod == null) {
            object = createInstance(null, clazz);
            
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                FieldDeserializer fieldDeser = smartMatch(key);
                if (fieldDeser == null) {
                    continue;
                }

                final FieldInfo fieldInfo = fieldDeser.fieldInfo;
                Type paramType = fieldInfo.fieldType;
                value = TypeUtils.cast(value, paramType, config);

                fieldDeser.setValue(object, value);
            }

            if (beanInfo.buildMethod != null) {
                Object builtObj;
                try {
                    builtObj = beanInfo.buildMethod.invoke(object);
                } catch (Exception e) {
                    throw new JSONException("build object error", e);
                }

                return builtObj;
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
        } else if (beanInfo.factoryMethod != null) {
            try {
                object = beanInfo.factoryMethod.invoke(null, params);
            } catch (Exception e) {
                throw new JSONException("create factory method error, " + beanInfo.factoryMethod.toString(), e);
            }
        }
        
        return object;
    }
    
    public Type getFieldType(int ordinal) {
        return sortedFieldDeserializers[ordinal].fieldInfo.fieldType;
    }
    
    protected Object parseRest(DefaultJSONParser parser, Type type, Object fieldName, Object instance, int features) {
        Object value = deserialze(parser, type, fieldName, instance, features);

        return value;
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static void parseArray(Collection collection, //
                              ObjectDeserializer deser, //
                              DefaultJSONParser parser, //
                              Type type, //
                              Object fieldName) {

        final JSONLexerBase lexer = (JSONLexerBase) parser.lexer;
        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            token = lexer.token();
            return;
        }

        if (token != JSONToken.LBRACKET) {
            parser.throwException(token);
        }
        char ch = lexer.getCurrent();
        if (ch == '[') {
            lexer.next();
            lexer.setToken(JSONToken.LBRACKET);
        } else {
            lexer.nextToken(JSONToken.LBRACKET);
        }
        
        if (lexer.token() == JSONToken.RBRACKET) {
            lexer.nextToken();
            return;
        }

        int index = 0;
        for (;;) {
            Object item = deser.deserialze(parser, type, index);
            collection.add(item);
            index++;
            if (lexer.token() == JSONToken.COMMA) {
                ch = lexer.getCurrent();
                if (ch == '[') {
                    lexer.next();
                    lexer.setToken(JSONToken.LBRACKET);
                } else {
                    lexer.nextToken(JSONToken.LBRACKET);
                }
            } else {
                break;
            }
        }
        
        token = lexer.token();
        if (token != JSONToken.RBRACKET) {
            parser.throwException(token);
        }
        
        ch = lexer.getCurrent();
        if (ch == ',') {
            lexer.next();
            lexer.setToken(JSONToken.COMMA);
        } else {
            lexer.nextToken(JSONToken.COMMA);
        }
//        parser.accept(JSONToken.RBRACKET, JSONToken.COMMA);
    }
    
}
