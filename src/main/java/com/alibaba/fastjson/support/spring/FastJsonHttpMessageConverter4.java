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
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * Spring MVC Converter for fastjson.
 * 
 * Spring MVC version 4.2+
 *
 * @author Victor.Zxy
 *
 */
public class FastJsonHttpMessageConverter4 //
        extends AbstractGenericHttpMessageConverter<Object> {

	// default charset
	private Charset charset = IOUtils.UTF8;

	private SerializerFeature[] features = new SerializerFeature[0];

	private SerializeFilter[] filters = new SerializeFilter[0];

	private String dateFormat;

	public FastJsonHttpMessageConverter4() {

		super(MediaType.ALL);
	}

	public Charset getCharset() {
		return this.charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public SerializerFeature[] getFeatures() {
		return features;
	}

	public void setFeatures(SerializerFeature... features) {
		this.features = features;
	}

	public SerializeFilter[] getFilters() {
		return filters;
	}

	public void setFilters(SerializeFilter... filters) {
		this.filters = filters;
	}

	public void addSerializeFilter(SerializeFilter filter) {
		if (filter == null) {
			return;
		}

		SerializeFilter[] filters = new SerializeFilter[this.filters.length + 1];
		System.arraycopy(this.filters, 0, filters, 0, this.filters.length);
		filters[filters.length - 1] = filter;
		this.filters = filters;
	}

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
	protected boolean supports(Class<?> paramClass) {
		return true;
	}

    @Override
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, charset, type);
    }

	@Override
    protected void writeInternal(Object obj, //
                                 Type type, //
                                 HttpOutputMessage outputMessage //
    ) throws IOException, HttpMessageNotWritableException {
	    
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

	@Override
    protected Object readInternal(Class<? extends Object> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {

		InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, charset, clazz);
	}
}
