package com.alibaba.json.bvt.support.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

import junit.framework.TestCase;

public class FastJsonHttpMessageConverter4Test2 extends TestCase {
    public FastJsonHttpMessageConverter4Test2() {
        
    }

	public void test_1() throws Exception {
		FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();

		Assert.assertNotNull(converter.getFastJsonConfig());
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		converter.setFastJsonConfig(fastJsonConfig);
		
		VO vo = new VO();
		vo.setId(123);
		vo.setStatDate(new Date());

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
		System.out.println(new String(bytes, "UTF-8"));
		
	}
	
	public static class VO {

		private int id;
		
		@JSONField(format = "yyyy-MM-dd")
		private Date statDate;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public Date getStatDate() {
			return this.statDate;
		}
		
		public void setStatDate(Date statDate) {
			this.statDate = statDate;
		}

	}
}
