package com.alibaba.json.bvt.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateFormatPriorityTest extends TestCase {
    Calendar calendar;

    protected void setUp() {
        JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        JSON.defaultLocale = Locale.CHINA;

        calendar = Calendar.getInstance(JSON.defaultTimeZone);
        calendar.set(1995, Calendar.OCTOBER, 26);
    }

    public void test_for_fastJsonConfig() throws IOException {
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM.dd");

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

        VO vo = new VO();
        vo.setDate(calendar.getTime());
        converter.write(vo, VO.class, MediaType.APPLICATION_JSON_UTF8, out);

        byte[] bytes = byteOut.toByteArray();
        String jsonString = new String(bytes, "UTF-8");

        Assert.assertEquals("{\"date\":\"1995-10.26\"}", jsonString);
    }

    public void test_for_toJSONStringWithDateFormat() {
        VO vo = new VO();
        vo.setDate(calendar.getTime());

        String jsonString = JSON.toJSONStringWithDateFormat(vo, "yyyy.MM.dd");

        assertEquals("{\"date\":\"1995.10.26\"}", jsonString);
    }

    public void test_for_Annotation() {
        VO2 vo2 = new VO2();
        vo2.setDate(calendar.getTime());

        String jsonString = JSON.toJSONString(vo2);

        assertEquals("{\"date\":\"1995.10-26\"}", jsonString);
    }

    public void test_for_DEFFAULT_DATE_FORMAT() {
        String defaultDateFormat = JSON.DEFFAULT_DATE_FORMAT;

        JSON.DEFFAULT_DATE_FORMAT = "MM-dd";
        VO vo = new VO();
        vo.setDate(calendar.getTime());

        String jsonString = JSON.toJSONString(vo, SerializerFeature.WriteDateUseDateFormat);
        JSON.DEFFAULT_DATE_FORMAT = defaultDateFormat;

        assertEquals("{\"date\":\"10-26\"}", jsonString);
    }

    //Annotation + FastJsonConfig （Annotation优先)
    public void test_priority_01() throws Exception {
        //FastJsonConfig
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM.dd");
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

        VO2 vo = new VO2();
        vo.setDate(calendar.getTime());
        converter.write(vo, VO.class, MediaType.APPLICATION_JSON_UTF8, out);

        byte[] bytes = byteOut.toByteArray();
        String jsonString = new String(bytes, "UTF-8");

        assertEquals("{\"date\":\"1995.10-26\"}", jsonString);
    }

    //toJSONStringWithDateFormat + Annotation (toJSONStringWithDateFormat优先)
    public void test_priority_02() throws Exception {
        VO2 vo = new VO2();
        vo.setDate(calendar.getTime());

        String jsonString = JSON.toJSONStringWithDateFormat(vo, "yyyy.MM.dd");

        assertEquals("{\"date\":\"1995.10.26\"}", jsonString);
    }

    //Annotation + DEFFAULT_DATE_FORMAT (Annotation优先)
    public void test_priority_03() throws Exception {
        String defaultDateFormat = JSON.DEFFAULT_DATE_FORMAT;

        JSON.DEFFAULT_DATE_FORMAT = "MM-dd";
        VO2 vo = new VO2();
        vo.setDate(calendar.getTime());

        String jsonString = JSON.toJSONString(vo, SerializerFeature.WriteDateUseDateFormat);
        JSON.DEFFAULT_DATE_FORMAT = defaultDateFormat;

        assertEquals("{\"date\":\"1995.10-26\"}", jsonString);
    }

    public static class VO {
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    public static class VO2 {
        @JSONField(format = "yyyy.MM-dd")
        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
