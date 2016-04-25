package com.alibaba.fastjson.support.spring;

import java.io.ByteArrayOutputStream;
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
public class FastJsonHttpMessageConverterNew extends
		AbstractGenericHttpMessageConverter<Object> {

	private Charset charset = IOUtils.UTF8;

	private SerializerFeature[] features = new SerializerFeature[0];

	protected SerializeFilter[] filters = new SerializeFilter[0];

	protected String dateFormat;

	public FastJsonHttpMessageConverterNew() {

		super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED);
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

	@Override
	protected boolean supports(Class<?> paramClass) {

		return true;
	}

	@Override
	public Object read(Type type, Class<?> contextClass,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream in = inputMessage.getBody();

		byte[] buf = new byte[1024];
		for (;;) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}

			if (len > 0) {
				baos.write(buf, 0, len);
			}
		}

		byte[] bytes = baos.toByteArray();

		return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(),
				type);
	}

	@Override
	protected void writeInternal(Object obj, Type type,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {

		HttpHeaders headers = outputMessage.getHeaders();

		String text = JSON.toJSONString(obj, //
				SerializeConfig.globalInstance, //
				filters, //
				dateFormat, //
				JSON.DEFAULT_GENERATE_FEATURE, //
				features);

		byte[] bytes = text.getBytes(charset);

		headers.setContentLength(bytes.length);

		OutputStream out = outputMessage.getBody();

		out.write(bytes);
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		// This method should not be executed.

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream in = inputMessage.getBody();

		byte[] buf = new byte[1024];
		for (;;) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}

			if (len > 0) {
				baos.write(buf, 0, len);
			}
		}

		byte[] bytes = baos.toByteArray();

		return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(),
				clazz);
	}
}
