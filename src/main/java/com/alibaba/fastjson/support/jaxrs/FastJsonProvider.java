package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fastjson for JAX-RS Provider.
 *
 * @author smallnest
 * @author VictorZeng
 * @see MessageBodyReader
 * @see MessageBodyWriter
 * @since 1.2.9
 */

@Provider
@Consumes({MediaType.WILDCARD})
@Produces({MediaType.WILDCARD})
public class FastJsonProvider //
        implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

    /**
     * These are classes that we never use for reading
     * (never try to deserialize instances of these types).
     */
    public final static Class<?>[] DEFAULT_UNREADABLES = new Class<?>[]{
            InputStream.class, Reader.class
    };

    /**
     * These are classes that we never use for writing
     * (never try to serialize instances of these types).
     */
    public final static Class<?>[] DEFAULT_UNWRITABLES = new Class<?>[]{
            InputStream.class,
            OutputStream.class, Writer.class,
            StreamingOutput.class, Response.class
    };

    @Deprecated
    protected Charset charset = Charset.forName("UTF-8");

    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];

    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];

    @Deprecated
    protected String dateFormat;

    /**
     * Injectable context object used to locate configured
     * instance of {@link FastJsonConfig} to use for actual
     * serialization.
     */
    @Context
    protected Providers providers;

    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    /**
     * allow serialize/deserialize types in clazzes
     */
    private Class<?>[] clazzes = null;

    /**
     * whether set PrettyFormat while exec WriteTo()
     */
    private boolean pretty;


    /**
     * @return the fastJsonConfig.
     * @since 1.2.11
     */
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    /**
     * @param fastJsonConfig the fastJsonConfig to set.
     * @since 1.2.11
     */
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    /**
     * Can serialize/deserialize all types.
     */
    public FastJsonProvider() {

    }

    /**
     * Only serialize/deserialize all types in clazzes.
     */
    public FastJsonProvider(Class<?>[] clazzes) {
        this.clazzes = clazzes;
    }

    /**
     * Set pretty format
     */
    public FastJsonProvider setPretty(boolean p) {
        this.pretty = p;
        return this;
    }

    /**
     * Instantiates a new Fast json provider.
     *
     * @param charset the charset
     * @see FastJsonConfig#setCharset(Charset)
     * @deprecated
     */
    @Deprecated
    public FastJsonProvider(String charset) {
        this.fastJsonConfig.setCharset(Charset.forName(charset));
    }

    /**
     * Gets charset.
     *
     * @return the charset
     * @see FastJsonConfig#getCharset()
     * @deprecated
     */
    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     * @see FastJsonConfig#setCharset(Charset)
     * @deprecated
     */
    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    /**
     * Gets date format.
     *
     * @return the date format
     * @see FastJsonConfig#getDateFormat()
     * @deprecated
     */
    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    /**
     * Sets date format.
     *
     * @param dateFormat the date format
     * @see FastJsonConfig#setDateFormat(String)
     * @deprecated
     */
    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    /**
     * Get features serializer feature [].
     *
     * @return the serializer feature []
     * @see FastJsonConfig#getFeatures()
     * @deprecated
     */
    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    /**
     * Sets features.
     *
     * @param features the features
     * @see FastJsonConfig#setFeatures(Feature...)
     * @deprecated
     */
    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    /**
     * Get filters serialize filter [].
     *
     * @return the serialize filter []
     * @see FastJsonConfig#getSerializeFilters()
     * @deprecated
     */
    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    /**
     * Sets filters.
     *
     * @param filters the filters
     * @see FastJsonConfig#setSerializeFilters(SerializeFilter...)
     * @deprecated
     */
    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    /**
     * Check some are interface/abstract classes to exclude.
     *
     * @param type    the type
     * @param classes the classes
     * @return the boolean
     */
    protected boolean isAssignableFrom(Class<?> type, Class<?>[] classes) {

        if (type == null)
            return false;

        //  there are some other abstract/interface types to exclude too:
        for (Class<?> cls : classes) {
            if (cls.isAssignableFrom(type)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether a class can be serialized or deserialized. It can check
     * based on packages, annotations on entities or explicit classes.
     *
     * @param type class need to check
     * @return true if valid
     */
    protected boolean isValidType(Class<?> type, Annotation[] classAnnotations) {
        if (type == null)
            return false;

        if (clazzes != null) {
            for (Class<?> cls : clazzes) { // must strictly equal. Don't check
                // inheritance
                if (cls == type)
                    return true;
            }

            return false;
        }

        return true;
    }

    /**
     * Check media type like "application/json".
     *
     * @param mediaType media type
     * @return true if the media type is valid
     */
    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subtype = mediaType.getSubtype();

            return (("json".equalsIgnoreCase(subtype)) //
                    || (subtype.endsWith("+json")) //
                    || ("javascript".equals(subtype)) //
                    || ("x-javascript".equals(subtype)) //
                    || ("x-json".equals(subtype)) //
                    || ("x-www-form-urlencoded".equalsIgnoreCase(subtype)) //
                    || (subtype.endsWith("x-www-form-urlencoded")));
        }
        return true;
    }

    /**
     * Method that JAX-RS container calls to try to check whether given value
     * (of specified type) can be serialized by this provider.
     */
    public boolean isWriteable(Class<?> type, //
                               Type genericType, //
                               Annotation[] annotations, //
                               MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        if (!isAssignableFrom(type, DEFAULT_UNWRITABLES))
            return false;

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to try to figure out serialized length
     * of given value. always return -1 to denote "not known".
     */
    public long getSize(Object t, //
                        Class<?> type, //
                        Type genericType, //
                        Annotation[] annotations, //
                        MediaType mediaType) {
        return -1;
    }

    /**
     * Method that JAX-RS container calls to serialize given value.
     */
    public void writeTo(Object obj, //
                        Class<?> type, //
                        Type genericType, //
                        Annotation[] annotations, //
                        MediaType mediaType, //
                        MultivaluedMap<String, Object> httpHeaders, //
                        OutputStream entityStream //
    ) throws IOException, WebApplicationException {

        FastJsonConfig fastJsonConfig = locateConfigProvider(type, mediaType);

        SerializerFeature[] serializerFeatures = fastJsonConfig.getSerializerFeatures();

        if (pretty) {
            if (serializerFeatures == null)
                serializerFeatures = new SerializerFeature[]{SerializerFeature.PrettyFormat};
            else {
                List<SerializerFeature> featureList = new ArrayList<SerializerFeature>(Arrays.asList(serializerFeatures));
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = featureList.toArray(serializerFeatures);
            }
            fastJsonConfig.setSerializerFeatures(serializerFeatures);
        }

        try {
            JSON.writeJSONString(entityStream, //
                    fastJsonConfig.getCharset(), //
                    obj, //
                    fastJsonConfig.getSerializeConfig(), //
                    fastJsonConfig.getSerializeFilters(), //
                    fastJsonConfig.getDateFormat(), //
                    JSON.DEFAULT_GENERATE_FEATURE, //
                    fastJsonConfig.getSerializerFeatures());

            entityStream.flush();

        } catch (JSONException ex) {

            throw new WebApplicationException(ex);
        }
    }

    /**
     * Method that JAX-RS container calls to try to check whether values of
     * given type (and media type) can be deserialized by this provider.
     */
    public boolean isReadable(Class<?> type, //
                              Type genericType, //
                              Annotation[] annotations, //
                              MediaType mediaType) {

        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        if (!isAssignableFrom(type, DEFAULT_UNREADABLES))
            return false;

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to deserialize given value.
     */
    public Object readFrom(Class<Object> type, //
                           Type genericType, //
                           Annotation[] annotations, //
                           MediaType mediaType, //
                           MultivaluedMap<String, String> httpHeaders, //
                           InputStream entityStream) throws IOException, WebApplicationException {

        try {
            FastJsonConfig fastJsonConfig = locateConfigProvider(type, mediaType);

            return JSON.parseObject(entityStream,
                    fastJsonConfig.getCharset(),
                    genericType,
                    fastJsonConfig.getParserConfig(),
                    fastJsonConfig.getParseProcess(),
                    JSON.DEFAULT_PARSER_FEATURE,
                    fastJsonConfig.getFeatures());

        } catch (JSONException ex) {

            throw new WebApplicationException(ex);
        }
    }

    /**
     * Helper method that is called if no config has been explicitly configured.
     */
    protected FastJsonConfig locateConfigProvider(Class<?> type, MediaType mediaType) {

        if (providers != null) {

            ContextResolver<FastJsonConfig> resolver = providers.getContextResolver(FastJsonConfig.class, mediaType);

            if (resolver == null) {

                resolver = providers.getContextResolver(FastJsonConfig.class, null);
            }

            if (resolver != null) {

                return resolver.getContext(type);
            }
        }

        return fastJsonConfig;
    }

}
