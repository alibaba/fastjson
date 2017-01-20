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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
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
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * This is the main class for using Fastjson. You usually call these two methods {@link #toJSONString(Object)} and {@link #parseObject(String, Class)}.
 * 
 * <p>Here is an example of how fastjson is used for a simple Class:
 *
 * <pre>
 * Model model = new Model();
 * String json = JSON.toJSONString(model); // serializes model to Json
 * Model model2 = JSON.parseObject(json, Model.class); // deserializes json into model2
 * </pre>
 * 
* <p>If the object that your are serializing/deserializing is a {@code ParameterizedType}
 * (i.e. contains at least one type parameter and may be an array) then you must use the
 * {@link #toJSONString(Object)} or {@link #parseObject(String, Type, Feature[])} method.  Here is an
 * example for serializing and deserialing a {@code ParameterizedType}:
 * 
 * <pre>
 * String json = "[{},...]";
 * Type listType = new TypeReference&lt;List&lt;Model&gt;&gt;() {}.getType();
 * List&lt;Model&gt; modelList = JSON.parseObject(json, listType);
 * </pre>
 * 
 * @see com.alibaba.fastjson.TypeReference
 * 
 * @author wenshao[szujobs@hotmail.com]
 */
public abstract class JSON implements JSONStreamAware, JSONAware {
    public static TimeZone         defaultTimeZone      = TimeZone.getDefault();
    public static Locale           defaultLocale        = Locale.getDefault();

    public static String           DEFAULT_TYPE_KEY     = "@type";

    static final SerializeFilter[] emptyFilters         = new SerializeFilter[0];

    public static String           DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static int              DEFAULT_PARSER_FEATURE;
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

    public static int DEFAULT_GENERATE_FEATURE;
    static {
        int features = 0;
        features |= SerializerFeature.QuoteFieldNames.getMask();
        features |= SerializerFeature.SkipTransientField.getMask();
        features |= SerializerFeature.WriteEnumUsingName.getMask();
        features |= SerializerFeature.SortField.getMask();
        DEFAULT_GENERATE_FEATURE = features;
    }
    
    /**
     * config default type key
     * @since 1.2.14
     */
    public static void setDefaultTypeKey(String typeKey) {
        DEFAULT_TYPE_KEY = typeKey;
        ParserConfig.global.symbolTable.addSymbol(typeKey, 
                                                  0, 
                                                  typeKey.length(), 
                                                  typeKey.hashCode());
    }
    
    public static Object parse(String text) {
        return parse(text, DEFAULT_PARSER_FEATURE);
    }

    public static Object parse(String text, int features) {
        if (text == null) {
            return null;
        }

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance(), features);
        Object value = parser.parse();

        parser.handleResovleTask(value);

        parser.close();

        return value;
    }

    public static Object parse(byte[] input, Feature... features) {
        char[] chars = allocateChars(input.length);
        int len = IOUtils.decodeUTF8(input, 0, input.length, chars);
        return parse(new String(chars, 0, len), features);
    }

    public static Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, Feature... features) {
        if (input == null || input.length == 0) {
            return null;
        }

        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        return parse(input, off, len, charsetDecoder, featureValues);
    }

    public static Object parse(byte[] input, int off, int len, CharsetDecoder charsetDecoder, int features) {
        charsetDecoder.reset();

        int scaleLength = (int) (len * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = allocateChars(scaleLength);

        ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
        CharBuffer charBuf = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, byteBuf, charBuf);

        int position = charBuf.position();

        DefaultJSONParser parser = new DefaultJSONParser(chars, position, ParserConfig.getGlobalInstance(), features);
        Object value = parser.parse();

        parser.handleResovleTask(value);

        parser.close();

        return value;
    }

    public static Object parse(String text, Feature... features) {
        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        return parse(text, featureValues);
    }

    public static JSONObject parseObject(String text, Feature... features) {
        return (JSONObject) parse(text, features);
    }

    public static JSONObject parseObject(String text) {
        Object obj = parse(text);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }

        return (JSONObject) JSON.toJSON(obj);
    }

    /**
     * <pre>
     * String jsonStr = "[{\"id\":1001,\"name\":\"Jobs\"}]";
     * List&lt;Model&gt; models = JSON.parseObject(jsonStr, new TypeReference&lt;List&lt;Model&gt;&gt;() {});
     * </pre>
     * @param text json string
     * @param type type refernce
     * @param features
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
        return (T) parseObject(text, type.type, ParserConfig.global, DEFAULT_PARSER_FEATURE, features);
    }

    /**
     * 
     * This method deserializes the specified Json into an object of the specified class. It is not
     * suitable to use if the specified class is a generic type since it will not have the generic
     * type information because of the Type Erasure feature of Java. Therefore, this method should not
     * be used if the desired type is a generic type. Note that this method works fine if the any of
     * the fields of the specified object are generics, just the object itself should not be a
     * generic type. For the cases when the object is of generic type, invoke
     * {@link #parseObject(String, Type, Feature[])}. If you have the Json in a {@link InputStream} instead of
     * a String, use {@link #parseObject(InputStream, Type, Feature[])} instead.
     *
     * @param json the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param features parser features
     * @return an object of type T from the string
     * classOfT
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String json, Class<T> clazz, Feature... features) {
        return (T) parseObject(json, (Type) clazz, ParserConfig.global, null, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String text, Class<T> clazz, ParseProcess processor, Feature... features) {
        return (T) parseObject(text, (Type) clazz, ParserConfig.global, processor, DEFAULT_PARSER_FEATURE,
                               features);
    }

    /**
     * This method deserializes the specified Json into an object of the specified type. This method
     * is useful if the specified object is a generic type. For non-generic objects, use
     * {@link #parseObject(String, Class, Feature[])} instead. If you have the Json in a {@link InputStream} instead of
     * a String, use {@link #parseObject(InputStream, Type, Feature[])} instead.
     *
     * @param <T> the type of the desired object
     * @param json the string from which the object is to be deserialized
     * @param type The specific genericized type of src. You can obtain this type by using the
     * {@link com.alibaba.fastjson.TypeReference} class. For example, to get the type for
     * {@code Collection<Foo>}, you should use:
     * <pre>
     * Type type = new TypeReference&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * </pre>
     * @return an object of type T from the string
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String json, Type type, Feature... features) {
        return (T) parseObject(json, type, ParserConfig.global, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, ParseProcess processor, Feature... features) {
        return (T) parseObject(input, clazz, ParserConfig.global, processor, DEFAULT_PARSER_FEATURE, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, ParserConfig.getGlobalInstance(), featureValues);
        T value = (T) parser.parseObject(clazz);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }
    
    /**
     * @since 1.2.11
     */
    public static <T> T parseObject(String input, Type clazz, ParserConfig config, Feature... features) {
        return parseObject(input, clazz, config, null, DEFAULT_PARSER_FEATURE, features);
    }

    public static <T> T parseObject(String input, Type clazz, ParserConfig config, int featureValues,
                                          Feature... features) {
        return parseObject(input, clazz, config, null, featureValues, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String input, Type clazz, ParserConfig config, ParseProcess processor,
                                          int featureValues, Feature... features) {
        if (input == null) {
            return null;
        }

        if (features != null) {
            for (Feature feature : features) {
                featureValues |= feature.mask;
            }
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, config, featureValues);

        if (processor != null) {
            if (processor instanceof ExtraTypeProvider) {
                parser.getExtraTypeProviders().add((ExtraTypeProvider) processor);
            }

            if (processor instanceof ExtraProcessor) {
                parser.getExtraProcessors().add((ExtraProcessor) processor);
            }

            if (processor instanceof FieldTypeResolver) {
                parser.setFieldTypeResolver((FieldTypeResolver) processor);
            }
        }

        T value = (T) parser.parseObject(clazz, null);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] bytes, Type clazz, Feature... features) {
        return (T) parseObject(bytes, 0, bytes.length, IOUtils.UTF8, clazz, features);
    }
    
    /**
     * @since 1.2.11
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] bytes, int offset, int len, Charset charset, Type clazz, Feature... features) {
        if (charset == null) {
            charset = IOUtils.UTF8;
        }
        
        String strVal;
        if (charset == IOUtils.UTF8) {
            char[] chars = allocateChars(bytes.length);
            int chars_len = IOUtils.decodeUTF8(bytes, offset, len, chars);
            strVal = new String(chars, 0, chars_len);
        } else {
            strVal = new String(bytes, offset, len, charset);
        }
        return (T) parseObject(strVal, clazz, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(byte[] input, //
                                    int off, //
                                    int len, //
                                    CharsetDecoder charsetDecoder, //
                                    Type clazz, //
                                    Feature... features) {
        charsetDecoder.reset();

        int scaleLength = (int) (len * (double) charsetDecoder.maxCharsPerByte());
        char[] chars = allocateChars(scaleLength);

        ByteBuffer byteBuf = ByteBuffer.wrap(input, off, len);
        CharBuffer charByte = CharBuffer.wrap(chars);
        IOUtils.decode(charsetDecoder, byteBuf, charByte);

        int position = charByte.position();

        return (T) parseObject(chars, position, clazz, features);
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(char[] input, int length, Type clazz, Feature... features) {
        if (input == null || input.length == 0) {
            return null;
        }

        int featureValues = DEFAULT_PARSER_FEATURE;
        for (Feature feature : features) {
            featureValues = Feature.config(featureValues, feature, true);
        }

        DefaultJSONParser parser = new DefaultJSONParser(input, length, ParserConfig.getGlobalInstance(), featureValues);
        T value = (T) parser.parseObject(clazz);

        parser.handleResovleTask(value);

        parser.close();

        return (T) value;
    }
    
    /**
     * @since 1.2.11
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(InputStream is, //
                                    Type type, //
                                    Feature... features) throws IOException {
        return (T) parseObject(is, IOUtils.UTF8, type, features);
    }
    
    /**
     * @since 1.2.11
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(InputStream is, //
                                    Charset charset, //
                                    Type type, //
                                    Feature... features) throws IOException {
        if (charset == null) {
            charset = IOUtils.UTF8;
        }
        
        byte[] bytes = allocateBytes(1024 * 64);
        int offset = 0;
        for (;;) {
            int readCount = is.read(bytes, offset, bytes.length - offset);
            if (readCount == -1) {
                break;
            }
            offset += readCount;
            if (offset == bytes.length) {
                byte[] newBytes = new byte[bytes.length * 3 / 2];
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
                bytes = newBytes;
            }
        }
        
        return (T) parseObject(bytes, 0, offset, charset, type, features);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return parseObject(text, clazz, new Feature[0]);
    }

    public static JSONArray parseArray(String text) {
        if (text == null) {
            return null;
        }

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());

        JSONArray array;

        JSONLexer lexer = parser.lexer;
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            array = null;
        } else if (lexer.token() == JSONToken.EOF) {
            array = null;
        } else {
            array = new JSONArray();
            parser.parseArray(array);

            parser.handleResovleTask(array);
        }

        parser.close();

        return array;
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (text == null) {
            return null;
        }

        List<T> list;

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
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

    public static List<Object> parseArray(String text, Type[] types) {
        if (text == null) {
            return null;
        }

        List<Object> list;

        DefaultJSONParser parser = new DefaultJSONParser(text, ParserConfig.getGlobalInstance());
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

    /**
     * This method serializes the specified object into its equivalent Json representation. Note that this method works fine if the any of the object fields are of generic type,
     * just the object itself should not be of a generic type. If you want to write out the object to a
     * {@link Writer}, use {@link #writeJSONString(Writer, Object, SerializerFeature[])} instead.
     *
     * @param object the object for which json representation is to be created setting for fastjson
     * @return Json representation of {@code object}.
     */
    public static String toJSONString(Object object) {
        return toJSONString(object, emptyFilters);
    }

    public static String toJSONString(Object object, SerializerFeature... features) {
        return toJSONString(object, DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.11
     */
    public static String toJSONString(Object object, int defaultFeatures, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter((Writer) null, defaultFeatures, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }

    /**
     * @since 1.1.14
     */
    public static String toJSONStringWithDateFormat(Object object, String dateFormat,
                                                          SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, null, dateFormat, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJSONString(Object object, SerializeFilter filter, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, new SerializeFilter[] {filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJSONString(Object object, SerializeFilter[] filters, SerializerFeature... features) {
        return toJSONString(object, SerializeConfig.globalInstance, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static byte[] toJSONBytes(Object object, SerializerFeature... features) {
        return toJSONBytes(object, DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static byte[] toJSONBytes(Object object, int defaultFeatures, SerializerFeature... features) {
        return toJSONBytes(object, SerializeConfig.globalInstance, defaultFeatures, features);
    }

    public static String toJSONString(Object object, SerializeConfig config, SerializerFeature... features) {
        return toJSONString(object, config, (SerializeFilter) null, features);
    }

    public static String toJSONString(Object object, //
                                      SerializeConfig config, //
                                      SerializeFilter filter, //
                                      SerializerFeature... features) {
        return toJSONString(object, config, new SerializeFilter[] {filter}, null, DEFAULT_GENERATE_FEATURE, features);
    }

    public static String toJSONString(Object object, //
                                      SerializeConfig config, //
                                      SerializeFilter[] filters, //
                                      SerializerFeature... features) {
        return toJSONString(object, config, filters, null, DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.9
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
            
            if (dateFormat != null && dateFormat.length() != 0) {
                serializer.setDateFormat(dateFormat);
                serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            }

            if (filters != null) {
                for (SerializeFilter filter : filters) {
                    serializer.addFilter(filter);
                }
            }

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
    }

    /**
     * @deprecated
     */
    public static String toJSONStringZ(Object object, SerializeConfig mapping, SerializerFeature... features) {
        return toJSONString(object, mapping, emptyFilters, null, 0, features);
    }

    public static byte[] toJSONBytes(Object object, SerializeConfig config, SerializerFeature... features) {
        return toJSONBytes(object, config, DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static byte[] toJSONBytes(Object object, SerializeConfig config, int defaultFeatures, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(null, defaultFeatures, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out, config);
            serializer.write(object);
            return out.toBytes(IOUtils.UTF8);
        } finally {
            out.close();
        }
    }

    public static String toJSONString(Object object, boolean prettyFormat) {
        if (!prettyFormat) {
            return toJSONString(object);
        }

        return toJSONString(object, SerializerFeature.PrettyFormat);
    }
    
    /**
     * @deprecated use writeJSONString
     */
    public static void writeJSONStringTo(Object object, Writer writer, SerializerFeature... features) {
        writeJSONString(writer, object, features);
    }

    /**
     * This method serializes the specified object into its equivalent json representation.
     *
     * @param writer Writer to which the json representation needs to be written
     * @param object the object for which json representation is to be created setting for fastjson
     * @param features serializer features
     * @since 1.2.11
     */
    public static void writeJSONString(Writer writer, Object object, SerializerFeature... features) {
        writeJSONString(writer, object, JSON.DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static void writeJSONString(Writer writer, Object object, int defaultFeatures, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter(writer, defaultFeatures, features);

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.write(object);
        } finally {
            out.close();
        }
    }
    
    /**
     * write object as json to OutputStream
     * @param os output stream
     * @param object
     * @param features serializer features
     * @since 1.2.11
     * @throws IOException
     */
    public static final int writeJSONString(OutputStream os, // 
                                             Object object, // 
                                             SerializerFeature... features) throws IOException {
        return writeJSONString(os, object, DEFAULT_GENERATE_FEATURE, features);
    }
    
    /**
     * @since 1.2.11 
     */
    public static final int writeJSONString(OutputStream os, // 
                                            Object object, // 
                                            int defaultFeatures, //
                                            SerializerFeature... features) throws IOException {
       return writeJSONString(os,  //
                              IOUtils.UTF8, // 
                              object, //
                              SerializeConfig.globalInstance, //
                              null, //
                              null, // 
                              defaultFeatures, //
                              features);
   }
    
    public static final int writeJSONString(OutputStream os, // 
                                             Charset charset, // 
                                             Object object, // 
                                             SerializerFeature... features) throws IOException {
        return writeJSONString(os, //
                               charset, //
                               object, //
                               SerializeConfig.globalInstance, //
                               null, //
                               null, //
                               DEFAULT_GENERATE_FEATURE, //
                               features);
    }
    
    public static final int writeJSONString(OutputStream os, // 
                                             Charset charset, // 
                                             Object object, // 
                                             SerializeConfig config, //
                                             SerializeFilter[] filters, //
                                             String dateFormat, //
                                             int defaultFeatures, //
                                             SerializerFeature... features) throws IOException {
        SerializeWriter writer = new SerializeWriter(null, defaultFeatures, features);

        try {
            JSONSerializer serializer = new JSONSerializer(writer, config);
            
            if (dateFormat != null && dateFormat.length() != 0) {
                serializer.setDateFormat(dateFormat);
                serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
            }

            if (filters != null) {
                for (SerializeFilter filter : filters) {
                    serializer.addFilter(filter);
                }
            }
            
            serializer.write(object);
            
            int len = writer.writeToEx(os, charset);
            return len;
        } finally {
            writer.close();
        }
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

    /**
     * This method serializes the specified object into its equivalent representation as a tree of
     * {@link JSONObject}s. 
     *
     */
    public static Object toJSON(Object javaObject) {
        return toJSON(javaObject, SerializeConfig.globalInstance);
    }

    /**
     * @deprecated
     */
    public static Object toJSON(Object javaObject, ParserConfig parserConfig) {
        return toJSON(javaObject, SerializeConfig.globalInstance);
    }
    
    @SuppressWarnings("unchecked")
    public static Object toJSON(Object javaObject, SerializeConfig config) {
        if (javaObject == null) {
            return null;
        }

        if (javaObject instanceof JSON) {
            return javaObject;
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
        
        ObjectSerializer serializer = config.getObjectWriter(clazz);
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
        
        String text = JSON.toJSONString(javaObject);
        return JSON.parse(text);
    }

    public static <T> T toJavaObject(JSON json, Class<T> clazz) {
        return TypeUtils.cast(json, clazz, ParserConfig.getGlobalInstance());
    }
    
    /**
     * @since 1.2.9
     */
    public <T> T toJavaObject(Class<T> clazz) {
        return TypeUtils.cast(this, clazz, ParserConfig.getGlobalInstance());
    }
    
    private final static ThreadLocal<byte[]> bytesLocal = new ThreadLocal<byte[]>();
    private static byte[] allocateBytes(int length) {
        byte[] chars = bytesLocal.get();

        if (chars == null) {
            if (length <= 1024 * 64) {
                chars = new byte[1024 * 64];
                bytesLocal.set(chars);
            } else {
                chars = new byte[length];
            }
        } else if (chars.length < length) {
            chars = new byte[length];
        }

        return chars;
    }
    
    private final static ThreadLocal<char[]> charsLocal = new ThreadLocal<char[]>();
    private static char[] allocateChars(int length) {
        char[] chars = charsLocal.get();

        if (chars == null) {
            if (length <= 1024 * 64) {
                chars = new char[1024 * 64];
                charsLocal.set(chars);
            } else {
                chars = new char[length];
            }
        } else if (chars.length < length) {
            chars = new char[length];
        }

        return chars;
    }

    public final static String VERSION = "1.2.25";
}
