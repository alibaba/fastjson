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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.deserializer.DefaultObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.IntegerDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.StringDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
@SuppressWarnings("rawtypes")
public class DefaultExtJSONParser extends DefaultJSONParser {

    private DefaultObjectDeserializer  derializer           = new DefaultObjectDeserializer();
    private ParseContext               context;

    private ParseContext[]             contextArray         = new ParseContext[8];
    private int                        contextArrayIndex    = 0;

    private final List<ResolveTask>    resolveTaskList      = new ArrayList<ResolveTask>();

    public final static int            NONE                 = 0;
    public final static int            NeedToResolve        = 1;

    private int                        referenceResolveStat = NONE;

    private final static Set<Class<?>> primitiveClasses     = new HashSet<Class<?>>();
    static {
        primitiveClasses.add(boolean.class);
        primitiveClasses.add(byte.class);
        primitiveClasses.add(short.class);
        primitiveClasses.add(int.class);
        primitiveClasses.add(long.class);
        primitiveClasses.add(float.class);
        primitiveClasses.add(double.class);

        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);

        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }
    
    public Object getObject(String path) {
        for (int i = 0; i < contextArrayIndex; ++i) {
            if (path.equals(contextArray[i].getPath())) {
                return contextArray[i].getObject();
            }
        }
        
        return null;
    }

    public DefaultExtJSONParser(String input){
        this(input, ParserConfig.getGlobalInstance());
    }

    public DefaultExtJSONParser(String input, ParserConfig mapping){
        super(input, mapping);
    }

    public DefaultExtJSONParser(String input, ParserConfig mapping, int features){
        super(input, mapping, features);
    }

    public DefaultExtJSONParser(char[] input, int length, ParserConfig mapping, int features){
        super(input, length, mapping, features);
    }

    public int getReferenceResolveStat() {
        return referenceResolveStat;
    }

    public void setReferenceResolveStat(int referenceResolveStat) {
        this.referenceResolveStat = referenceResolveStat;
    }

    public ParseContext getContext() {
        return context;
    }

    public List<ResolveTask> getResolveTaskList() {
        return resolveTaskList;
    }

    public ResolveTask getLastResolveTask() {
        return resolveTaskList.get(resolveTaskList.size() - 1);
    }

    public void setContext(ParseContext context) {
        this.context = context;
    }

    public ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        this.context = new ParseContext(parent, object, fieldName);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext context) {
        int i = contextArrayIndex++;
        if (i >= contextArray.length) {
            int newLen = contextArray.length * 2 / 3;
            ParseContext[] newArray = new ParseContext[newLen];
            System.arraycopy(contextArray, 0, newArray, 0, contextArray.length);
            contextArray = newArray;
        }
        contextArray[i] = context;
    }

    public ParserConfig getConfig() {
        return config;
    }

    public void setConfig(ParserConfig config) {
        this.config = config;
    }

    // compatible
    @SuppressWarnings("unchecked")
    public <T> T parseObject(Class<T> clazz) {
        return (T) parseObject((Type) clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T parseObject(Type type) {
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        ObjectDeserializer derializer = config.getDeserializer(type);

        try {
            return (T) derializer.deserialze(this, type, null);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        List<T> array = new ArrayList<T>();
        parseArray(clazz, array);
        return array;
    }

    public void parseArray(Class<?> clazz, Collection array) {
        parseArray((Type) clazz, array);
    }

    @SuppressWarnings({ "unchecked" })
    public void parseArray(Type type, Collection array) {
        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("exepct '[', but " + JSONToken.name(lexer.token()));
        }

        ObjectDeserializer deserializer = null;
        if (int.class == type) {
            deserializer = IntegerDeserializer.instance;
            lexer.nextToken(JSONToken.LITERAL_INT);
        } else if (String.class == type) {
            deserializer = StringDeserializer.instance;
            lexer.nextToken(JSONToken.LITERAL_STRING);
        } else {
            deserializer = config.getDeserializer(type);
            lexer.nextToken(deserializer.getFastMatchToken());
        }

        for (;;) {
            if (isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }

            if (lexer.token() == JSONToken.RBRACKET) {
                break;
            }

            if (int.class == type) {
                Object val = IntegerDeserializer.deserialze(this);
                array.add(val);
            } else if (String.class == type) {
                String value;
                if (lexer.token() == JSONToken.LITERAL_STRING) {
                    value = lexer.stringVal();
                    lexer.nextToken(JSONToken.COMMA);
                } else {
                    Object obj = this.parse();
                    if (obj == null) {
                        value = null;
                    } else {
                        value = obj.toString();
                    }
                }

                array.add(value);
            } else {
                Object val = deserializer.deserialze(this, type, null);
                array.add(val);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(deserializer.getFastMatchToken());
                continue;
            }
        }

        lexer.nextToken(JSONToken.COMMA);
    }

    public Object[] parseArray(Type[] types) {

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("syntax error");
        }

        Object[] list = new Object[types.length];
        if (types.length == 0) {
            lexer.nextToken(JSONToken.RBRACKET);

            if (lexer.token() != JSONToken.RBRACKET) {
                throw new JSONException("syntax error");
            }

            lexer.nextToken(JSONToken.COMMA);
            return new Object[0];
        }

        lexer.nextToken(JSONToken.LITERAL_INT);

        for (int i = 0; i < types.length; ++i) {
            Object value;

            if (lexer.token() == JSONToken.NULL) {
                value = null;
                lexer.nextToken(JSONToken.COMMA);
            } else {
                Type type = types[i];
                if (type == int.class || type == Integer.class) {
                    if (lexer.token() == JSONToken.LITERAL_INT) {
                        value = Integer.valueOf(lexer.intValue());
                        lexer.nextToken(JSONToken.COMMA);
                    } else {
                        value = this.parse();
                        value = TypeUtils.cast(value, type, config);
                    }
                } else if (type == String.class) {
                    if (lexer.token() == JSONToken.LITERAL_STRING) {
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
                    if (isArray && lexer.token() != JSONToken.LBRACKET) {
                        List<Object> varList = new ArrayList<Object>();

                        ObjectDeserializer derializer = config.getDeserializer(componentType);
                        int fastMatch = derializer.getFastMatchToken();

                        if (lexer.token() != JSONToken.RBRACKET) {
                            for (;;) {
                                Object item = derializer.deserialze(this, type, null);
                                varList.add(item);

                                if (lexer.token() == JSONToken.COMMA) {
                                    lexer.nextToken(fastMatch);
                                } else if (lexer.token() == JSONToken.RBRACKET) {
                                    break;
                                } else {
                                    throw new JSONException("syntax error :" + JSONToken.name(lexer.token()));
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

            if (lexer.token() == JSONToken.RBRACKET) {
                break;
            }

            if (lexer.token() != JSONToken.COMMA) {
                throw new JSONException("syntax error :" + JSONToken.name(lexer.token()));
            }

            if (i == types.length - 1) {
                lexer.nextToken(JSONToken.RBRACKET);
            } else {
                lexer.nextToken(JSONToken.LITERAL_INT);
            }
        }

        if (lexer.token() != JSONToken.RBRACKET) {
            throw new JSONException("syntax error");
        }

        lexer.nextToken(JSONToken.COMMA);

        return list;
    }

    public void parseObject(Object object) {
        derializer.parseObject(this, object);
    }

    public Object parseArrayWithType(Type collectionType) {
        if (lexer.token() == JSONToken.NULL) {
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
            this.parseArray((Class) actualTypeArgument, array);
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
            this.parseArray((Class) upperBoundType, array);
            return array;

            // throw new JSONException("not support type : " +
            // collectionType);return parse();
        }

        if (actualTypeArgument instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) actualTypeArgument;
            Type[] bounds = typeVariable.getBounds();

            if (bounds.length != 1) {
                throw new JSONException("not support : " + typeVariable);
            }

            Type boundType = bounds[0];
            if (boundType instanceof Class) {
                List<Object> array = new ArrayList<Object>();
                this.parseArray((Class) boundType, array);
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

    public static class ResolveTask {

        private final ParseContext context;
        private final String       referenceValue;
        private FieldDeserializer  fieldDeserializer;
        private ParseContext       ownerContext;

        public ResolveTask(ParseContext context, String referenceValue){
            super();
            this.context = context;
            this.referenceValue = referenceValue;
        }

        public ParseContext getContext() {
            return context;
        }

        public String getReferenceValue() {
            return referenceValue;
        }

        public FieldDeserializer getFieldDeserializer() {
            return fieldDeserializer;
        }

        public void setFieldDeserializer(FieldDeserializer fieldDeserializer) {
            this.fieldDeserializer = fieldDeserializer;
        }

        public ParseContext getOwnerContext() {
            return ownerContext;
        }

        public void setOwnerContext(ParseContext ownerContext) {
            this.ownerContext = ownerContext;
        }

    }
}
