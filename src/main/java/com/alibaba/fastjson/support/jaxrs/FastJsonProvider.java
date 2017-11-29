package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
     * Set charset. the default charset is UTF-8
     */
    @Deprecated
    public FastJsonProvider(String charset) {
        this.fastJsonConfig.setCharset(Charset.forName(charset));
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
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

	/*
     * /********************************************************** /* Partial
	 * MessageBodyWriter impl
	 * /**********************************************************
	 */

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
                List<SerializerFeature> featureList = new ArrayList<SerializerFeature>(Arrays
                        .asList(serializerFeatures));
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = featureList.toArray(serializerFeatures);
            }
            fastJsonConfig.setSerializerFeatures(serializerFeatures);
        }

        try {
            int len = JSON.writeJSONString(entityStream, //
                    fastJsonConfig.getCharset(), //
                    obj, //
                    fastJsonConfig.getSerializeConfig(), //
                    fastJsonConfig.getSerializeFilters(), //
                    fastJsonConfig.getDateFormat(), //
                    JSON.DEFAULT_GENERATE_FEATURE, //
                    fastJsonConfig.getSerializerFeatures());

//            // add Content-Length
//            if (fastJsonConfig.isWriteContentLength()) {
//                httpHeaders.add("Content-Length", String.valueOf(len));
//            }

            entityStream.flush();

        } catch (JSONException ex) {

            throw new WebApplicationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

	/*
     * /********************************************************** /*
	 * MessageBodyReader impl
	 * /**********************************************************
	 */

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

            return JSON.parseObject(entityStream, fastJsonConfig.getCharset(), genericType, fastJsonConfig.getFeatures());

        } catch (JSONException ex) {

            throw new WebApplicationException("JSON parse error: " + ex.getMessage(), ex);
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
