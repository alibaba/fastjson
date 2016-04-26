package com.alibaba.fastjson.support.spring;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 * Fastjson for Spring MVC Converter.
 * 
 * Compatible Spring MVC version 4.2+
 *
 * @author Victor.Zxy
 * @since 1.2.10
 * @see AbstractGenericHttpMessageConverter
 */
public class FastJsonHttpMessageConverter4 //
        extends AbstractGenericHttpMessageConverter<Object> {

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
	public FastJsonHttpMessageConverter4() {

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
