package com.alibaba.fastjson.support.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.IOUtils;

/**
 * JAX-RS Provider for fastjson.
 * 
 * @author smallnest
 *
 */
@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    private Class<?>[] clazzes = null;
    
    public SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
    public ParserConfig parserConfig = ParserConfig.getGlobalInstance();
    public SerializerFeature[] serializerFeatures = new SerializerFeature[0];
    public Feature[] features = new Feature[0];
    public Map<Class<?>, SerializeFilter> serializeFilters;

    @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo uriInfo;

    // protected FastJsonConfig fastJsonConfig = new FastJsonConfig(new SerializeConfig(), null, null, new ParserConfig(), null);

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
     * @param mediaType
     *            media type
     * @return true if the media type is valid
     */
    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType != null) {
            String subtype = mediaType.getSubtype();
            return "json".equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype);
        }
        return true;
    }

    public String toJSONString(Object object, SerializeFilter filter, SerializerFeature[] features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out, serializeConfig);
            if (features != null) {
                for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                    serializer.config(feature, true);
                }
            }
            
            if (filter != null) {
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

            serializer.write(object);

            return out.toString();
        } finally {
            out.close();
        }
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
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to try to figure out serialized length
     * of given value. always return -1 to denote "not known".
     */
    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    /**
     * Method that JAX-RS container calls to serialize given value.
     */
    public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException, WebApplicationException {
        SerializeFilter filter = null;

        SerializerFeature[] serializerFeatures = this.serializerFeatures; 
        if(uriInfo != null &&  uriInfo.getQueryParameters().containsKey("pretty")) {
            if (serializerFeatures == null)
                serializerFeatures = new  SerializerFeature[]{SerializerFeature.PrettyFormat};
            else {
                List<SerializerFeature> featureList = Arrays.asList(serializerFeatures);
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = featureList.toArray(serializerFeatures);
            }
        }

        if (serializeFilters != null) {
            filter = serializeFilters.get(type);
        }
        
        String jsonStr = toJSONString(t, filter, serializerFeatures);
        if (jsonStr != null) {
            entityStream.write(jsonStr.getBytes());
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
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (!hasMatchingMediaType(mediaType)) {
            return false;
        }

        return isValidType(type, annotations);
    }

    /**
     * Method that JAX-RS container calls to deserialize given value.
     */
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException, WebApplicationException {
        String input = null;
        try {
            input = IOUtils.toString(entityStream);
        } catch (Exception e) {
            // skip ??
        }
        
        if (input == null) {
            return null;
        }
        
        if (features == null) {
            return JSON.parseObject(input, type, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        } else {
            return JSON.parseObject(input, type, parserConfig, JSON.DEFAULT_PARSER_FEATURE, features);
        }
    }

}
