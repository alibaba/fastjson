package com.alibaba.json.bvt.support.spring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.http.*;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonHttpMessageConverterBigDataTest extends TestCase {

	public void test_1() throws Exception {

		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
                final FastJsonHttpMessageConverterBigDataTest bigDataTest = new FastJsonHttpMessageConverterBigDataTest();
		Assert.assertNotNull(converter.getFastJsonConfig());
		converter.setFastJsonConfig(new FastJsonConfig());

		converter.canRead(VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canWrite(VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canRead(VO.class, VO.class, MediaType.APPLICATION_JSON_UTF8);
		converter.canWrite(VO.class, VO.class, MediaType.APPLICATION_JSON_UTF8);

		HttpInputMessage input = new HttpInputMessage() {
                        String text;
                        String path = "..\\..\\..\\..\\..\\..\\..\\resources\\com\\alibaba\\json\\bvt\\support\\spring\\jsonData\\test.json";
			public HttpHeaders getHeaders() {
				// TODO Auto-generated method stub
				return null;
			}

			public InputStream getBody() throws IOException {
                                try{
                                      text=bigDataTest.fileRead(path);
                                   }catch(Exception e){
                                      e.printStackTrace();
                                   }
				  return new ByteArrayInputStream(text.getBytes(Charset.forName("UTF-8")));
			}

		};
                String text=bigDataTest.fileRead("..\\..\\..\\..\\..\\..\\..\\resources\\com\\alibaba\\json\\bvt\\support\\spring\\jsonData\\test.json");
		VO vo = (VO) converter.read(VO.class, VO.class, input);
		Assert.assertEquals(text,"{\"str\":\""+vo.getStr()+"\"}");

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
		Assert.assertEquals(text, new String(bytes, "UTF-8"));

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

	private String fileRead(String path) throws Exception {	
	    File file = new File(path);
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s = bReader.readLine()) != null){
                sb.append(s);          
             }
             bReader.close();
             String str = sb.toString();
             return str;
        };

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
