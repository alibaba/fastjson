package com.alibaba.json.test;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * @project fastjson
 * @desc:
 * @date 2021-06-15 15:18
 */
public class Issue3805 {

    @Data
    private class TestModel {
        private LocalDateTime createTime;
    }

    @Test
    public void test() throws Exception {
        
        String dateFormat = "yyyy";
        TestModel model = new TestModel();
        model.setCreateTime(LocalDateTime.of(2021,5,6,7,8,9,5));

        FastJsonConfig config = new FastJsonConfig();

        config.setDateFormat(dateFormat);

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(config);

        converter.canRead(TestModel.class, MediaType.APPLICATION_JSON_UTF8);
        converter.canWrite(TestModel.class, MediaType.APPLICATION_JSON_UTF8);

        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        HttpOutputMessage out = new HttpOutputMessage() {
            public HttpHeaders getHeaders() {
                return new HttpHeaders() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public MediaType getContentType() {
                        return MediaType.APPLICATION_JSON;
                    }
                };
            }

            public OutputStream getBody() throws IOException {
                return byteOut;
            }
        };

        converter.write(model, TestModel.class, MediaType.APPLICATION_JSON_UTF8, out);
        byte[] bytes = byteOut.toByteArray();
        String jsonString = new String(bytes, "UTF-8");
//        System.out.println(jsonString);
        Assert.assertEquals(jsonString, "{\"createTime\":\"2021\"}");
     
    }

    
}
