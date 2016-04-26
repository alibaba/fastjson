package com.alibaba.fastjson.support.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * Fastjson for JAX-RS Provider.
 *
 * @author smallnest
 * @author VictorZeng
 * @since 1.2.9
 * 
 * @see MessageBodyReader
 * @see MessageBodyWriter
 */

@Provider
@Consumes({ MediaType.WILDCARD })
@Produces({ MediaType.WILDCARD })
public class FastJsonProvider //
        implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

	/** default charset */
	private Charset charset = IOUtils.UTF8;

	/** allow serialize/deserialize types in clazzes */
	private Class<?>[] clazzes = null;
	
	/** serializer features */
	private SerializerFeature[] features = new SerializerFeature[0];

	/** serialize filter */
	private SerializeFilter[] filters = new SerializeFilter[0];

	/** date format */
	private String dateFormat;
	
	@javax.ws.rs.core.Context
	javax.ws.rs.core.UriInfo uriInfo;
	
	/**
	 * Can serialize/deserialize all types.
	 */
	public FastJsonProvider() {

	}

	/**
	 * Set charset. the default charset is UTF-8
	 */
	public FastJsonProvider(String charset) {
		this.charset = Charset.forName(charset);
	}

	/**
	 * Only serialize/deserialize all types in clazzes.
	 */
	public FastJsonProvider(Class<?>[] clazzes) {
		this.clazzes = clazzes;
	}

	/**
	 * Get charset.
	 *
	 * @return charset
	 */
	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Set charset.
	 * 
	 * @param charset Charset
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Get dateFormat.
	 * 
	 * @return dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set dateFormat.
	 *
	 * @param dateFormat String
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Get features.
	 *
	 * @return features SerializerFeature[]
	 */
	public SerializerFeature[] getFeatures() {
		return features;
	}

	/**
	 * Set features.
	 *
	 * @param features SerializerFeature[]
	 */
	public void setFeatures(SerializerFeature... features) {
		this.features = features;
	}

	/**
	 * Get filters.
	 *
	 * @return filters SerializeFilter[]
	 */
	public SerializeFilter[] getFilters() {
		return filters;
	}

	/**
	 * Set filters.
	 * 
	 * @param filters SerializeFilter[]
	 */
	public void setFilters(SerializeFilter... filters) {
		this.filters = filters;
	}
	
	/**
	 * Add SerializeFilter
	 *
	 * @param filter SerializeFilter
	 */
	public void addSerializeFilter(SerializeFilter filter) {
		if (filter == null) {
			return;
		}
		
		SerializeFilter[] filters = new SerializeFilter[this.filters.length + 1];
		List<SerializeFilter> filterList = new ArrayList<>(Arrays.asList(this.filters));
		filterList.add(filter);
		this.filters = filterList.toArray(filters);
	}

	/**
	 * Add SerializerFeature
	 *
	 * @param feature SerializerFeature
	 */
	public void addSerializerFeature(SerializerFeature feature) {
		if (feature == null) {
			return;
		}
		
		SerializerFeature[] features = new SerializerFeature[this.features.length + 1];
		List<SerializerFeature> featureList = new ArrayList<>(Arrays.asList(this.features));
		featureList.add(feature);
		this.features = featureList.toArray(features);
	}
	
	/**
	 * Check whether a class can be serialized or deserialized. It can check
	 * based on packages, annotations on entities or explicit classes.
	 *
	 * @param type
	 *            class need to check
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

		SerializerFeature[] serializerFeatures = this.features;
		if (uriInfo != null
				&& uriInfo.getQueryParameters().containsKey("pretty")) {
			if (serializerFeatures == null)
				serializerFeatures = new SerializerFeature[] { SerializerFeature.PrettyFormat };
			else {
				List<SerializerFeature> featureList = new ArrayList<>(Arrays
						.asList(serializerFeatures));
				featureList.add(SerializerFeature.PrettyFormat);
				serializerFeatures = featureList.toArray(serializerFeatures);
			}
		}

        JSON.writeJSONString(obj, //
                             entityStream, //
                             charset, //
                             SerializeConfig.globalInstance, //
                             filters, //
                             dateFormat, //
                             JSON.DEFAULT_GENERATE_FEATURE, //
                             serializerFeatures);
		
		entityStream.flush();

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
        return JSON.parseObject(entityStream, charset, genericType);
    }
}
