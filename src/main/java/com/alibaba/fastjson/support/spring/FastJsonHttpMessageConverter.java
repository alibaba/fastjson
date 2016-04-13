package com.alibaba.fastjson.support.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    public final static Charset UTF8            = Charset.forName("UTF-8");

    private Charset             charset         = UTF8;

    private SerializerFeature[] features        = new SerializerFeature[0];

    protected SerializeFilter[] serialzeFilters = new SerializeFilter[0];

    protected String            dateFormat;

    public FastJsonHttpMessageConverter(){
        super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
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

    @Override
    protected Object readInternal(Class<? extends Object> clazz,
                                  HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

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
        return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), clazz);
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
                                                                              HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        String text = JSON.toJSONString(obj, // 
                                        SerializeConfig.globalInstance, // 
                                        serialzeFilters, // 
                                        dateFormat, // 
                                        JSON.DEFAULT_GENERATE_FEATURE, // 
                                        features);
        byte[] bytes = text.getBytes(charset);
        headers.setContentLength(bytes.length);
        OutputStream out = outputMessage.getBody();
        out.write(bytes);
    }

    public void addSerializeFilter(SerializeFilter filter) {
        if (filter == null) {
            return;
        }

        SerializeFilter[] filters = new SerializeFilter[this.serialzeFilters.length + 1];
        System.arraycopy(this.serialzeFilters, 0, filter, 0, this.serialzeFilters.length);
        filters[filters.length - 1] = filter;
        this.serialzeFilters = filters;
    }

}
