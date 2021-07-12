package com.alibaba.json.bvt.support.spring;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;

import com.alibaba.fastjson.util.IOUtils;
import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.http.*;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonHttpMessageConverterBigDataTest extends TestCase {

	private String json;

	protected void setUp() throws Exception {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("json/Big_data_Test.json");
		InputStreamReader reader = new InputStreamReader(is);
		json = IOUtils.readAll(reader);
		IOUtils.close(reader);
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
			String text;
			public HttpHeaders getHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			public InputStream getBody() throws IOException {
				  return new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8")));
			}

		};
		VO vo = (VO) converter.read(VO.class, VO.class, input);
        String actual1 = "{\"str\":\"" + vo.getStr() + "\"}";
        Assert.assertEquals(json, actual1);

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
		Assert.assertEquals(json, new String(bytes, "UTF-8"));

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
	public static class VO {

		private String str;

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}

	}
}
