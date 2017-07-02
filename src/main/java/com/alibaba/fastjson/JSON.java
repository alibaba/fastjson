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
package com.alibaba.fastjson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public abstract class JSON implements JSONStreamAware, JSONAware {

    public static TimeZone     defaultTimeZone  = TimeZone.getDefault();
    public static Locale       defaultLocale    = Locale.getDefault();
    
    public final static String DEFAULT_TYPE_KEY = "@type";

    public static int          DEFAULT_PARSER_FEATURE;
    
    static {
        int features = 0;
        // features |= Feature.AutoCloseSource.mask; always allow
        features |= Feature.UseBigDecimal.mask;
        // features |= Feature.AllowArbitraryCommas.mask; always allow
        features |= Feature.SortFeidFastMatch.mask;
        features |= Feature.IgnoreNotMatch.mask;
        DEFAULT_PARSER_FEATURE = features;
    }

    public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static int    DEFAULT_GENERATE_FEATURE;

    static {
        int features = 0;
        features |= SerializerFeature.QuoteFieldNames.mask;
        features |= SerializerFeature.SkipTransientField.mask;
        features |= SerializerFeature.WriteEnumUsingToString.mask;
        features |= SerializerFeature.SortField.mask;
        // features |=
        // com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial.getMask();
        DEFAULT_GENERATE_FEATURE = features;
    }

    public static final Object parse(String text) {
        return parse(text, DEFAULT_PARSER_FEATURE);
    }

    public static final Object parse(String text, int features) {
        if (text == null) {
            return null;
        }

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.global, features);
        Object value = parser.parse(null);

        parser.handleResovleTask(value);

        parser.close();

        return value;
    }

    public static final Object parse(byte[] input, Feature... features) {
        try {
            return parseObject(new String(input, "UTF-8"), features);
        } catch (UnsupportedEncodingException e) {
            throw new JSONException("UTF-8 not support", e);
        }
    }

    public static final Object parse(String text, Feature... features) {
        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues |= feature.mask;
        }

        return parse(text, featureValues);
    }

    public static final JSONObject parseObject(String text, Feature... features) {
        return (JSONObject) parse(text, features);
    }

    public static final JSONObject parseObject(String text) {
        Object obj = parse(text);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }

        return (JSONObject) JSON.toJSON(obj);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
        return (T) parseObject(text, type.type, ParserConfig.global, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String text, Class<T> clazz, Feature... features) {
        return (T) parseObject(text, (Type) clazz, ParserConfig.global, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String text, Class<T> clazz, ParseProcess processor, Feature... features) {
        return (T) parseObject(text, (Type) clazz, ParserConfig.global, processor, DEFAULT_PARSER_FEATURE,
                               features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, Feature... features) {
        return (T) parseObject(input, clazz, ParserConfig.global, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, ParseProcess processor, Feature... features) {
        return (T) parseObject(input, clazz, ParserConfig.global, processor, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature feature : features) {
            featureValues |= feature.mask;
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.global, featureValues);
        T value = (T) parser.parseObject(clazz);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }

    public static final <T> T parseObject(String input, Type clazz, ParserConfig config, int featureValues,
                                          Feature... features) {
        return parseObject(input, clazz, config, null, featureValues, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, ParserConfig config, ParseProcess processor,
                                          int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature feature : features) {
            featureValues |= feature.mask;
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, config, featureValues);

        if (processor instanceof ExtraTypeProvider) {
            parser.getExtraTypeProviders().add((ExtraTypeProvider) processor);
        }

        if (processor instanceof ExtraProcessor) {
            parser.getExtraProcessors().add((ExtraProcessor) processor);
        }
        
        if (processor instanceof FieldTypeResolver) {
            parser.fieldTypeResolver = (FieldTypeResolver) processor;
        }

        T value = (T) parser.parseObject(clazz);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(byte[] input, Type clazz, Feature... features) {
        try {
            return (T) parseObject(new String(input, "UTF-8"), clazz, features);
        } catch (UnsupportedEncodingException e) {
            throw new JSONException("UTF-8 not support");
        }
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(char[] input, int length, Type clazz, Feature... features) {
        if (input == null || input.length == 0) {
            return null;
        }

        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues |= feature.mask;
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, length, ParserConfig.global, featureValues);
        T value = (T) parser.parseObject(clazz);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }

    public static final <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text, clazz, new Feature[0]);
    }

    public static final JSONArray parseArray(String text) {
        if (text == null) {
            return null;
        }

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.global);

        JSONArray array;

        JSONLexer lexer = parser.lexer;
        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken();
            array = null;
        } else if (token == JSONToken.EOF) {
            array = null;
        } else {
            array = new JSONArray();
            parser.parseArray(array);

            parser.handleResovleTask(array);
        }

        parser.close();

        return array;
    }

    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null) {
            return null;
        }

        List<T> list;

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.global);
        JSONLexer lexer = parser.lexer;
        int token = lexer.token();
        if (token == JSONToken.NULL) {
            lexer.nextToken();
            list = null;
        } else if (token == JSONToken.EOF && lexer.isBlankInput()) {
            list = null;
        } else {
            list = new ArrayList<T>();
            parser.parseArray(clazz, list);

            parser.handleResovleTask(list);
        }

        parser.close();

        return list;
    }

    public static final List<Object> parseArray(String text, Type[] types) {
        if (text == null) {
            return null;
        }

        List<Object> list;

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.global);
        Object[] objectArray = parser.parseArray(types);
        if (objectArray == null) {
            list = null;
        } else {
            list = Arrays.asList(objectArray);
        }

        parser.handleResovleTask(list);

        parser.close();

        return list;
    }

    // ======================
    public static final String toJSONString(Object object) {
        return toJSONString(object, SerializeConfig.globalInstance, null, null, DEFAULT_GENERATE_FEATURE);
    }

    /**
     * @since 1.2.11
     */
    public static final String toJSONString(Object object, SerializerFeature... features) {
        return toJSONString(object, DEFAULT_GENERATE_FEATURE, features);
    }
    
    public static final String toJSONString(Object object, int defaultFeatures, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, null, null, defaultFeatures, features);
    }

    /**
     * @since 1.1.14
     */
    public static final String toJSONStringWithDateFormat(Object object, String dateFormat,
                                                          SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, null, dateFormat, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final String toJSONString(Object object, SerializeFilter filter, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, new SerializeFilter[] {filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final String toJSONString(Object object, SerializeFilter[] filters, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final byte[] toJSONBytes(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, SerializeConfig.globalInstance);

            serializer.write(object);

            return out.toBytes("UTF-8");
        } finally {
            out.close();
        }
    }

    public static final String toJSONString(Object object, SerializeConfig config, SerializerFeature... features) {
        return toJSONString(object, config, null, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final String toJSONString(Object object, SerializeConfig config, SerializeFilter filter,
                                            SerializerFeature... features) {
        return toJSONString(object, config, new SerializeFilter[] {filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final String toJSONString(Object object, SerializeConfig config, SerializeFilter[] filters,
                                            SerializerFeature... features) {
        return toJSONString(object, config, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static final String toJSONStringZ(Object object, SerializeConfig mapping, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, null, null, 0, features);
    }

    public static final byte[] toJSONBytes(Object object, SerializeConfig config, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter((Writer)null, JSON.DEFAULT_GENERATE_FEATURE, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, config);
            serializer.write(object);

            return out.toBytes("UTF-8");
        } finally {
            out.close();
        }
    }

    public static final String toJSONString(Object object, boolean prettyFormat) {
        if (!prettyFormat) {
            return toJSONString(object);
        }

        return toJSONString(object, SerializerFeature.PrettyFormat);
    }

    public static final void writeJSONStringTo(Object object, Writer writer, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(writer, JSON.DEFAULT_GENERATE_FEATURE, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, SerializeConfig.globalInstance);

            serializer.write(object);
        } finally {
            out.close();
        }
    }

    // ======================================
    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        SerializeWriter out = new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
        try {
            new JSONSerializer(out, SerializeConfig.globalInstance).write(this);
            return out.toString();
        } finally {
            out.close();
        }
    }

    public void writeJSONString(Appendable appendable) {
        SerializeWriter out = new SerializeWriter((Writer) null, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
        try {
            new JSONSerializer(out, SerializeConfig.globalInstance).write(this);
            appendable.append(out.toString());
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    // ///////
    public static final Object toJSON(Object javaObject) {
        return toJSON(javaObject, SerializeConfig.globalInstance);
    }

    @Deprecated
    public static final Object toJSON(Object javaObject, ParserConfig mapping) {
        return toJSON(javaObject, SerializeConfig.globalInstance);
    }

    @SuppressWarnings("unchecked")
    public static Object toJSON(Object javaObject, SerializeConfig config) {
        if (javaObject == null) {
            return null;
        }

        if (javaObject instanceof JSON) {
            return (JSON) javaObject;
        }

        if (javaObject instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>) javaObject;

            JSONObject json = new JSONObject(map.size());

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object key = entry.getKey();
                String jsonKey = TypeUtils.castToString(key);
                Object jsonValue = toJSON(entry.getValue());
                json.put(jsonKey, jsonValue);
            }

            return json;
        }

        if (javaObject instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) javaObject;

            JSONArray array = new JSONArray(collection.size());

            for (Object item : collection) {
                Object jsonValue = toJSON(item);
                array.add(jsonValue);
            }

            return array;
        }

        Class<?> clazz = javaObject.getClass();

        if (clazz.isEnum()) {
            return ((Enum<?>) javaObject).name();
        }

        if (clazz.isArray()) {
            int len = Array.getLength(javaObject);

            JSONArray array = new JSONArray(len);

            for (int i = 0; i < len; ++i) {
                Object item = Array.get(javaObject, i);
                Object jsonValue = toJSON(item);
                array.add(jsonValue);
            }

            return array;
        }

        if (ParserConfig.isPrimitive(clazz)) {
            return javaObject;
        }

        ObjectSerializer serializer = config.get(clazz);
        if (serializer instanceof JavaBeanSerializer) {
            JavaBeanSerializer javaBeanSerializer = (JavaBeanSerializer) serializer;
            
            JSONObject json = new JSONObject();
            try {
                Map<String, Object> values = javaBeanSerializer.getFieldValuesMap(javaObject);
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    json.put(entry.getKey(), toJSON(entry.getValue()));
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
            return json;
        }
        
        return null;
    }

    public static final <T> T toJavaObject(JSON json, Class<T> clazz) {
        return TypeUtils.cast(json, clazz, ParserConfig.global);
    }
    
    /**
     * @since 1.2.9 back port 1.1.52.android
     */
    public <T> T toJavaObject(Class<T> clazz) {
        return TypeUtils.cast(this, clazz, ParserConfig.getGlobalInstance());
    }
    
    /**
     * @since 1.2.9, back port to 1.1.49.android
     * @return
     */
    public static String toJSONString(Object object, // 
                                      SerializeConfig config, // 
                                      SerializeFilter[] filters, // 
                                      String dateFormat, //
                                      int defaultFeatures, // 
                                      SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(null, defaultFeatures, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, config);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }
            
            if (dateFormat != null && dateFormat.length() != 0) {
                serializer.setDateFormat(dateFormat);
                serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            }

            if (filters != null) {
                for (SerializeFilter filter : filters) {
                    if (filter == null) {
                        continue;
                    }
                    
                    if (filter instanceof PropertyPreFilter) {
                        serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
                    }

                    if (filter instanceof NameFilter) {
                        serializer.getNameFilters().add((NameFilter) filter);
                    }

                    if (filter instanceof ValueFilter) {
                        serializer.getValueFilters().add((ValueFilter) filter);
                    }

                    if (filter instanceof PropertyFilter) {
                        serializer.getPropertyFilters().add((PropertyFilter) filter);
                    }

                    if (filter instanceof BeforeFilter) {
                        serializer.getBeforeFilters().add((BeforeFilter) filter);
                    }

                    if (filter instanceof AfterFilter) {
                        serializer.getAfterFilters().add((AfterFilter) filter);
                    }
                }
            }

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

    public final static String VERSION = "1.1.60";
}
