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
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.DefaultExtJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.ThreadLocalCache;
import com.alibaba.fastjson.util.TypeUtils;
import com.alibaba.fastjson.util.UTF8Decoder;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public abstract class JSON implements JSONStreamAware, JSONAware {

    public static final int DEFAULT_PARSER_FEATURE;
    static {
        int features = 0;
        features |= Feature.AutoCloseSource.getMask();
        features |= Feature.InternFieldNames.getMask();
        features |= Feature.UseBigDecimal.getMask();
        features |= Feature.AllowUnQuotedFieldNames.getMask();
        features |= Feature.AllowSingleQuotes.getMask();
        features |= Feature.AllowArbitraryCommas.getMask();
        features |= Feature.SortFeidFastMatch.getMask();
        features |= Feature.IgnoreNotMatch.getMask();
        DEFAULT_PARSER_FEATURE = features;
    }

    public static final int DEFAULT_GENERATE_FEATURE;
    static {
        int features = 0;
        features |= com.alibaba.fastjson.serializer.SerializerFeature.QuoteFieldNames.getMask();
        features |= com.alibaba.fastjson.serializer.SerializerFeature.SkipTransientField.getMask();
        features |= com.alibaba.fastjson.serializer.SerializerFeature.SortField.getMask();
        DEFAULT_GENERATE_FEATURE = features;
    }

    public static final Object parse(String text) {
        return parse(text, DEFAULT_PARSER_FEATURE);
    }

    public static final Object parse(String text, int features) {
        if (text == null) {
            return null;
        }

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance(), features);
        Object value = parser.parse();

        parser.close();

        return value;
    }

    public static final Object parse(byte[] input, Feature... features) {
        return parse(input, 0, input.length, UTF8_CharsetEncoder, features);
    }

    public static final Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, Feature... features) {
        if (input == null || input.length == 0) {
            return null;
        }

        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        return parse(input, off, len, charsetDecoder, featureValues);
    }

    public static final Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, int features) {
        charsetDecoder.reset();

        int scaleLength = (int) (len * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = ThreadLocalCache.getChars(scaleLength);

        ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
        CharBuffer charBuf = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, byteBuf, charBuf);

        int position = charBuf.position();

        DefaultJSONParser parser = new DefaultJSONParser(chars, position, ParserConfig.getGlobalInstance(), features);
        Object value = parser.parse();

        parser.close();

        return value;
    }

    public static final Object parse(String text, Feature... features) {
        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        return parse(text, featureValues);
    }

    public static final JSONObject parseObject(String text, Feature... features) {
        return (JSONObject) parse(text, features);
    }

    public static final JSONObject parseObject(String text) {
        return (JSONObject) parse(text);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
        return (T) parseObject(text, type.getType(), ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String text, Class<T> clazz, Feature... features) {
        return (T) parseObject(text, (Type) clazz, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, Feature... features) {
        return (T) parseObject(input, clazz, ParserConfig.getGlobalInstance(), DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
        T value = (T) parser.parseObject(clazz);
        
        handleResovleTask(parser, value);

        parser.close();

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(String input, Type clazz, ParserConfig config, int featureValues,
                                          Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, config, featureValues);
        T value = (T) parser.parseObject(clazz);

        handleResovleTask(parser, value);

        parser.close();

        return (T) value;
    }

    private static <T> void handleResovleTask(DefaultExtJSONParser parser, T value) {
        int size = parser.getResolveTaskList().size();
        for (int i = 0; i < size; ++i) {
            ResolveTask task = parser.getResolveTaskList().get(i);
            FieldDeserializer fieldDeser = task.getFieldDeserializer();
            fieldDeser.setValue(task.getOwnerContext().getObject(), value);
        }
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(byte[] input, Type clazz, Feature... features) {
        return (T) parseObject(input, 0, input.length, UTF8_CharsetEncoder, clazz, features);
    }

    public static CharsetDecoder UTF8_CharsetEncoder = new UTF8Decoder(); // =
                                                                          // Charset.forName("UTF-8").newDecoder();

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(byte[] input, int off, int len, CharsetDecoder charsetDecoder, Type clazz,
                                          Feature... features) {
        charsetDecoder.reset();

        int scaleLength = (int) (len * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = ThreadLocalCache.getChars(scaleLength);

        ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
        CharBuffer charByte = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, byteBuf, charByte);

        int position = charByte.position();

        return (T) parseObject(chars, position, clazz, features);
    }

    @SuppressWarnings("unchecked")
    public static final <T> T parseObject(char[] input, int length, Type clazz, Feature... features) {
        if (input == null || input.length == 0) {
            return null;
        }

        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature featrue : features) {
            featureValues = Feature.config(featureValues, featrue, true);
        }

        DefaultExtJSONParser parser = new DefaultExtJSONParser(input, length, ParserConfig.getGlobalInstance(),
                                                               featureValues);
        T value = (T) parser.parseObject(clazz);
        
        handleResovleTask(parser, value);

        parser.close();

        return (T) value;
    }

    public static final <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text, clazz, new Feature[0]);
    }

    public static final JSONArray parseArray(String text) {
        return (JSONArray) parse(text);
    }

    public static final <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null) {
            return null;
        }

        List<T> list = new ArrayList<T>();

        DefaultExtJSONParser parser = new DefaultExtJSONParser(text, ParserConfig.getGlobalInstance());
        parser.parseArray(clazz, list);

        parser.close();

        return list;
    }

    public static final List<Object> parseArray(String text, Type[] types) {
        if (text == null) {
            return null;
        }

        List<Object> list;

        DefaultExtJSONParser parser = new DefaultExtJSONParser(text, ParserConfig.getGlobalInstance());
        list = Arrays.asList(parser.parseArray(types));

        parser.close();

        return list;
    }

    // ======================

    public static final String toJSONString(Object object) {
        return toJSONString(object, new SerializerFeature[0]);
    }

    public static final String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

    public static final String toJSONString(Object object, SerializeConfig config, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out, config);
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

    public static final String toJSONStringZ(Object object, SerializeConfig mapping, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, mapping);

            serializer.write(object);

            return out.toString();
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

    // ======================================

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        SerializeWriter out = new SerializeWriter();
        try {
            new JSONSerializer(out).write(this);
            return out.toString();
        } finally {
            out.close();
        }
    }

    public void writeJSONString(Appendable appendable) {
        SerializeWriter out = new SerializeWriter();
        try {
            new JSONSerializer(out).write(this);
            appendable.append(out.toString());
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    // ///////

    public static final Object toJSON(Object javaObject) {
        return toJSON(javaObject, ParserConfig.getGlobalInstance());
    }

    @SuppressWarnings("unchecked")
    public static final Object toJSON(Object javaObject, ParserConfig mapping) {
        if (javaObject == null) {
            return null;
        }

        if (javaObject instanceof JSON) {
            return (JSON) javaObject;
        }

        if (javaObject instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) javaObject;

            JSONObject json = new JSONObject(map.size());

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object jsonValue = toJSON(entry.getValue());
                json.put(entry.getKey(), jsonValue);
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

        if (mapping.isPrimitive(clazz)) {
            return javaObject;
        }

        try {
            List<FieldInfo> getters = JavaBeanSerializer.computeGetters(clazz, null);

            JSONObject json = new JSONObject(getters.size());

            for (FieldInfo field : getters) {
                Method method = field.getMethod();
                Object value = method.invoke(javaObject, new Object[0]);
                Object jsonValue = toJSON(value);

                json.put(field.getName(), jsonValue);
            }

            return json;
        } catch (Exception e) {
            throw new JSONException("toJSON error", e);
        }
    }

    public static final <T> T toJavaObject(JSON json, Class<T> clazz) {
        return TypeUtils.cast(json, clazz, ParserConfig.getGlobalInstance());
    }
}
