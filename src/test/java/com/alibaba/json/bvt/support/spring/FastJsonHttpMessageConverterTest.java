package com.alibaba.json.bvt.support.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import junit.framework.TestCase;

import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonHttpMessageConverterTest extends TestCase {

    public void test_read() throws Exception {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setCharset(Charset.forName("UTF-8"));
        converter.setFeatures(SerializerFeature.BrowserCompatible);
        Assert.assertEquals(Charset.forName("UTF-8"), converter.getCharset());

        Assert.assertEquals(1, converter.getFeatures().length);
        
        Method method = FastJsonHttpMessageConverter.class.getDeclaredMethod("supports", Class.class);
        method.setAccessible(true);
        method.invoke(converter, int.class);

        HttpInputMessage input = new HttpInputMessage() {

            public HttpHeaders getHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("{\"id\":123}".getBytes(Charset.forName("UTF-8")));
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
        Assert.assertEquals("{\"id\":123}", new String(bytes, "UTF-8"));
    }

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
