package com.alibaba.fastjson.support.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;

/**
 * Fastjson for Spring MVC Converter.
 * 
 * Compatible Spring MVC version 4.2+
 *
 * @author Victor.Zxy
 * @since 1.2.11
 * 
 * @see AbstractGenericHttpMessageConverter
 */
public class FastJsonHttpMessageConverter4 //
        extends AbstractGenericHttpMessageConverter<Object> {

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
	public FastJsonHttpMessageConverter4() {

		super(MediaType.ALL);
	}
	
	@Override
	protected boolean supports(Class<?> paramClass) {
		return true;
	}

    @Override
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
    }

	@Override
    protected void writeInternal(Object obj, //
                                 Type type, //
                                 HttpOutputMessage outputMessage //
    ) throws IOException, HttpMessageNotWritableException {
	    
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = JSON.writeJSONString(outnew, //
                                       fastJsonConfig.getCharset(), //
                                       obj, //
                                       fastJsonConfig.getSerializeConfig(), //
                                       fastJsonConfig.getSerializeFilters(), //
                                       fastJsonConfig.getDateFormat(), //
                                       JSON.DEFAULT_GENERATE_FEATURE, //
                                       fastJsonConfig.getSerializerFeatures());
        headers.setContentLength(len);
        OutputStream out = outputMessage.getBody();
        out.write(outnew.toByteArray());
        outnew.close();
	}

	@Override
    protected Object readInternal(Class<? extends Object> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {

		InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
	}
}
