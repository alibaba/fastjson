package com.alibaba.json.bvt.issue_1800;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


public class Issue1868 extends TestCase {
    public void test() throws Exception {
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(config);

        converter.canRead(VO.class, MediaType.APPLICATION_JSON_UTF8);
        converter.canWrite(VO.class, MediaType.APPLICATION_JSON_UTF8);

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

        Date date = new Date();
        VO vo = new VO();
        vo.setDate(date);
        converter.write(vo, VO.class, MediaType.APPLICATION_JSON_UTF8, out);

        byte[] bytes = byteOut.toByteArray();
        Assert.assertEquals(JSON.toJSONString(vo), new String(bytes, "UTF-8"));
    }

    public static class VO {
        @JSONField(format = "yyyy-MM-dd")
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
