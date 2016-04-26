package com.alibaba.fastjson.support.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * Fastjson for Spring MVC Converter.
 * 
 * Compatible Spring MVC version 4.2- (Below 4.2)
 *
 * @author VictorZeng
 * @since 1.2.10
 * @see AbstractHttpMessageConverter
 * @see GenericHttpMessageConverter
 */

public class FastJsonHttpMessageConverter //
        extends AbstractHttpMessageConverter<Object> //
        implements GenericHttpMessageConverter<Object> {

	/** default charset */
	private Charset charset = IOUtils.UTF8;

	/** serializer features */
	private SerializerFeature[] features = new SerializerFeature[0];

	/** serialize filter */
	private SerializeFilter[] filters = new SerializeFilter[0];

	/** dateFormat */
	private String dateFormat;

	/**
	 *	Can serialize/deserialize all types.
	 */
	public FastJsonHttpMessageConverter() {
		
		super(MediaType.ALL);
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
		System.arraycopy(this.filters, 0, filters, 0,
				this.filters.length);
		filters[filters.length - 1] = filter;
		this.filters = filters;
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
		System.arraycopy(this.features, 0, features, 0, this.features.length);
		features[features.length - 1] = feature;
		this.features = features;
	}
	
	@Override
	protected boolean supports(Class<?> clazz) {
		
		return true;
	}
	
	@Override
    protected Object readInternal(Class<? extends Object> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
	    
		InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, charset, clazz);
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        OutputStream out = outputMessage.getBody();
        int len = JSON.writeJSONString(obj, //
                                       out, //
                                       charset, //
                                       SerializeConfig.globalInstance, //
                                       filters, //
                                       dateFormat, //
                                       JSON.DEFAULT_GENERATE_FEATURE, //
                                       features);
        headers.setContentLength(len);
    }

	/* 
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#canRead(java.lang.reflect.Type, java.lang.Class, org.springframework.http.MediaType)
	 */
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return super.canRead(contextClass, mediaType);
	}

	/* 
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#canWrite(java.lang.reflect.Type, java.lang.Class, org.springframework.http.MediaType)
	 */
	public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
		return super.canWrite(contextClass, mediaType);
	}
	
	/* 
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#read(java.lang.reflect.Type, java.lang.Class, org.springframework.http.HttpInputMessage)
	 */
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        
		InputStream in = inputMessage.getBody();
		return JSON.parseObject(in, charset, type);
	}

	/* 
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#write(java.lang.Object, java.lang.reflect.Type, org.springframework.http.MediaType, org.springframework.http.HttpOutputMessage)
	 */
    public void write(Object t, //
                      Type type, //
                      MediaType contentType, //
                      HttpOutputMessage outputMessage //
    ) throws IOException, HttpMessageNotWritableException {

		HttpHeaders headers = outputMessage.getHeaders();
		if (headers.getContentType() == null) {
			if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
				contentType = getDefaultContentType(t);
			}
			if (contentType != null) {
				headers.setContentType(contentType);
			}
		}
		if (headers.getContentLength() == -1) {
			Long contentLength = getContentLength(t, headers.getContentType());
			if (contentLength != null) {
				headers.setContentLength(contentLength);
			}
		}
		writeInternal(t, outputMessage);
		outputMessage.getBody().flush();
	}

}
