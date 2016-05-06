package com.alibaba.json.bvt.support.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonHttpMessageConverterTest extends TestCase {

	@SuppressWarnings("deprecation")
	public void test_read() throws Exception {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		converter.setCharset(Charset.forName("UTF-8"));
		Assert.assertEquals(Charset.forName("UTF-8"), converter.getCharset());

		converter.setFeatures(SerializerFeature.BrowserCompatible);
		Assert.assertEquals(1, converter.getFeatures().length);
		Assert.assertEquals(SerializerFeature.BrowserCompatible,
				converter.getFeatures()[0]);

		Assert.assertNull(converter.getDateFormat());
		converter.setDateFormat("yyyyMMdd");

		converter.setFilters(serializeFilter);
		Assert.assertEquals(1, converter.getFilters().length);
		Assert.assertEquals(serializeFilter, converter.getFilters()[0]);

		converter.addSerializeFilter(serializeFilter);
		Assert.assertEquals(2, converter.getFilters().length);
		converter.addSerializeFilter(null);

		converter.setSupportedMediaTypes(Arrays
				.asList(new MediaType[] { MediaType.APPLICATION_JSON_UTF8 }));
		Assert.assertEquals(1, converter.getSupportedMediaTypes().size());

		Method method = FastJsonHttpMessageConverter.class.getDeclaredMethod(
				"supports", Class.class);
		method.setAccessible(true);
		method.invoke(converter, int.class);

		HttpInputMessage input = new HttpInputMessage() {

			public HttpHeaders getHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			public InputStream getBody() throws IOException {
				return new ByteArrayInputStream("{\"id\":123}".getBytes(Charset
						.forName("UTF-8")));
			}

		};
		VO vo = (VO) converter.read(VO.class, input);
		Assert.assertEquals(123, vo.getId());

		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		HttpOutputMessage out = new HttpOutputMessage() {

			public HttpHeaders getHeaders() {
				return new HttpHeaders();
			}

			public OutputStream getBody() throws IOException {
				return byteOut;
			}
		};
		converter.write(vo, MediaType.TEXT_PLAIN, out);

		byte[] bytes = byteOut.toByteArray();
		Assert.assertEquals("{\"id\":\"123\"}", new String(bytes, "UTF-8"));
	}

	public void test_1() throws Exception {

		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

		Assert.assertNotNull(converter.getFastJsonConfig());
		converter.setFastJsonConfig(new FastJsonConfig());

		converter.canRead(VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canWrite(VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canRead(VO.class, VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canWrite(VO.class, VO.class, MediaType.APPLICATION_JSON_UTF8);

		HttpInputMessage input = new HttpInputMessage() {

			public HttpHeaders getHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			public InputStream getBody() throws IOException {
				return new ByteArrayInputStream("{\"id\":123}".getBytes(Charset
						.forName("UTF-8")));
			}

		};
		VO vo = (VO) converter.read(VO.class, VO.class, input);
		Assert.assertEquals(123, vo.getId());

		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		HttpOutputMessage out = new HttpOutputMessage() {

			public HttpHeaders getHeaders() {
				return new HttpHeaders();
			}

			public OutputStream getBody() throws IOException {
				return byteOut;
			}
		};
		converter.write(vo, VO.class, MediaType.TEXT_PLAIN, out);

		byte[] bytes = byteOut.toByteArray();
		Assert.assertEquals("{\"id\":123}", new String(bytes, "UTF-8"));

		converter.setSupportedMediaTypes(Collections
				.singletonList(MediaType.APPLICATION_JSON));

		converter.write(vo, VO.class, null, out);

		converter.write(vo, VO.class, MediaType.ALL, out);

		HttpOutputMessage out2 = new HttpOutputMessage() {

			public HttpHeaders getHeaders() {

				return new HttpHeaders() {

					private static final long serialVersionUID = 1L;

					@Override
					public MediaType getContentType() {

						return MediaType.APPLICATION_JSON;
					}

					@Override
					public long getContentLength() {

						return 1;
					}
				};
			}

			public OutputStream getBody() throws IOException {
				return byteOut;
			}
		};

		converter.write(vo, VO.class, MediaType.ALL, out2);

	}

	private SerializeFilter serializeFilter = new ValueFilter() {
		@Override
		public Object process(Object object, String name, Object value) {
			if (value == null) {
				return "";
			}
			if (value instanceof Number) {
				return String.valueOf(value);
			}
			return value;
		}
	};

	public static class VO {

		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}
}