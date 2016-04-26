package com.alibaba.fastjson.support.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;

/**
 * Fastjson for Spring MVC Converter.
 * 
 * Compatible Spring MVC version 4.2- (Below 4.2)
 *
 * @author VictorZeng
 * @since 1.2.10
 * 
 * @see AbstractHttpMessageConverter
 * @see GenericHttpMessageConverter
 */

public class FastJsonHttpMessageConverter //
        extends AbstractHttpMessageConverter<Object> //
        implements GenericHttpMessageConverter<Object> {

	/** with fastJson config */
	private FastJsonConfig fastJsonConfig = new FastJsonConfig(); 

	/**
	 * @since 1.2.11
	 * 
	 * @return the fastJsonConfig.
	 */
	public FastJsonConfig getFastJsonConfig() {
		return fastJsonConfig;
	}

	/**
	 * @since 1.2.11
	 * 
	 * @param fastJsonConfig the fastJsonConfig to set.
	 */
	public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
		this.fastJsonConfig = fastJsonConfig;
	}
	
	/**
	 *	Can serialize/deserialize all types.
	 */
	public FastJsonHttpMessageConverter() {
		
		super(MediaType.ALL);
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
        return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        OutputStream out = outputMessage.getBody();
        int len = JSON.writeJSONString(obj, //
                                       out, //
                                       fastJsonConfig.getCharset(), //
                                       fastJsonConfig.getSerializeConfig(), //
                                       fastJsonConfig.getSerializeFilters(), //
                                       fastJsonConfig.getDateFormat(), //
                                       JSON.DEFAULT_GENERATE_FEATURE, //
                                       fastJsonConfig.getSerializerFeatures());
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
		return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
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
