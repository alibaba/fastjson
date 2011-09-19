package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.DeserializeBeanInfo;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class JavaBeanDeserializer implements ObjectDeserializer {

    private final Map<String, FieldDeserializer> feildDeserializerMap = new IdentityHashMap<String, FieldDeserializer>();

    private final List<FieldDeserializer>        fieldDeserializers   = new ArrayList<FieldDeserializer>();

    private final Class<?>                       clazz;
    
    private DeserializeBeanInfo beanInfo;
    
    public JavaBeanDeserializer(DeserializeBeanInfo beanInfo) {
        this.beanInfo = beanInfo;
        this.clazz = beanInfo.getClass();
    }
    
    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz){
        this.clazz = clazz;

        beanInfo = DeserializeBeanInfo.computeSetters(clazz);
        
        for (FieldInfo fieldInfo : beanInfo.getFieldList()) {
            addFieldDeserializer(config, clazz, fieldInfo);
        }
    }

    public Map<String, FieldDeserializer> getFieldDeserializerMap() {
        return feildDeserializerMap;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    

   
    private void addFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        FieldDeserializer fieldDeserializer = createFieldDeserializer(mapping, clazz, fieldInfo);

        feildDeserializerMap.put(fieldInfo.getName().intern(), fieldDeserializer);
        fieldDeserializers.add(fieldDeserializer);
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        return mapping.createFieldDeserializer(mapping, clazz, fieldInfo);
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

        if (beanInfo.getDefaultConstructor() == null) {
            return null;
        }

        Object object;
        try {
            object = beanInfo.getDefaultConstructor().newInstance();
        } catch (Exception e) {
            throw new JSONException("create instance error, class " + clazz.getName(), e);
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONScanner lexer = (JSONScanner) parser.getLexer(); // xxx

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        ParseContext context = parser.getContext();
        ParseContext childContext = null;
        Object object = null;
        
        try {
            Map<String, Object> fieldValues = null;
            
            if (lexer.token() == JSONToken.RBRACE) {
               lexer.nextToken(JSONToken.COMMA);
               return (T) createInstance(type);
            }

            if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
                throw new JSONException("syntax error, expect {, actual " + JSONToken.name(lexer.token()));
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
                                parser.getResolveTaskList().add(new ResolveTask(parentContext, ref));        
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
                                parser.getResolveTaskList().add(new ResolveTask(rootContext, ref));
                                parser.setResolveStatus(DefaultJSONParser.NeedToResolve);
                            }
                        } else {
                            parser.getResolveTaskList().add(new ResolveTask(context, ref));
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

                    return (T) object;
                }
                
                if ("@type" == key) {
                    lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
                        String typeName = lexer.stringVal();
                        lexer.nextToken(JSONToken.COMMA);
                        Class<?> userType = TypeUtils.loadClass(typeName);
                        ObjectDeserializer deserizer = parser.getConfig().getDeserializer(userType);
                        return (T) deserizer.deserialze(parser, userType, fieldName);
                    } else {
                        throw new JSONException("syntax error");
                    }
                }

                if (object == null && fieldValues == null) {
                    object = createInstance(type);
                    if (object == null) {
                        fieldValues = new HashMap<String, Object>(this.fieldDeserializers.size());
                    }
                    childContext = parser.setContext(context, object, fieldName);
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
                        throw new JSONException("create instance error, " + beanInfo.getCreatorConstructor().toGenericString(), e);
                    }
                } else if (beanInfo.getFactoryMethod() != null) {
                    try {
                        object = beanInfo.getFactoryMethod().invoke(null, params);
                    } catch (Exception e) {
                        throw new JSONException("create factory method error, " + beanInfo.getFactoryMethod().toString(), e);
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

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Map<String, Object> fieldValues) {
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
