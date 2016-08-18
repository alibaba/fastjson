package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Fastjson for Spring MVC Converter.
 * <p>
 * Compatible Spring MVC version 4.2- (Below 4.2)
 *
 * @author VictorZeng
 * @see AbstractHttpMessageConverter
 * @see GenericHttpMessageConverter
 * @since 1.2.10
 */

public class FastJsonHttpMessageConverter //
        extends AbstractHttpMessageConverter<Object> //
        implements GenericHttpMessageConverter<Object> {

    @Deprecated
    protected Charset charset = IOUtils.UTF8;

    @Deprecated
    protected SerializerFeature[] features = new SerializerFeature[0];

    @Deprecated
    protected SerializeFilter[] filters = new SerializeFilter[0];

    @Deprecated
    protected String dateFormat;

    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    /**
     * @return the fastJsonConfig.
     * @since 1.2.11
     */
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    /**
     * @param fastJsonConfig the fastJsonConfig to set.
     * @since 1.2.11
     */
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    /**
     * Can serialize/deserialize all types.
     */
    public FastJsonHttpMessageConverter() {

        super(MediaType.ALL);
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    @Deprecated
    public void addSerializeFilter(SerializeFilter filter) {
        if (filter == null) {
            return;
        }

        int length = this.fastJsonConfig.getSerializeFilters().length;
        SerializeFilter[] filters = new SerializeFilter[length + 1];
        System.arraycopy(this.fastJsonConfig.getSerializeFilters(), 0, filters, 0, length);
        filters[filters.length - 1] = filter;
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    @Override
    protected boolean supports(Class<?> clazz) {

        return true;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = JSON.writeJSONString(outnew, //
                fastJsonConfig.getCharset(), //
                obj, //
                fastJsonConfig.getSerializeConfig(), //
                fastJsonConfig.getSerializeFilters(), //
                fastJsonConfig.getDateFormat(), //
                JSON.DEFAULT_GENERATE_FEATURE, //
                fastJsonConfig.getSerializerFeatures());
        headers.setContentLength(len);
        OutputStream out = outputMessage.getBody();
        outnew.writeTo(out);
        outnew.close();
    }

    /*
     * @see org.springframework.http.converter.GenericHttpMessageConverter#canRead(java.lang.reflect.Type, java.lang.Class, org.springframework.http.MediaType)
     */
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(contextClass, mediaType);
    }

    /*
     * @see org.springframework.http.converter.GenericHttpMessageConverter#canWrite(java.lang.reflect.Type, java.lang.Class, org.springframework.http.MediaType)
     */
    public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canWrite(contextClass, mediaType);
    }

    /*
     * @see org.springframework.http.converter.GenericHttpMessageConverter#read(java.lang.reflect.Type, java.lang.Class, org.springframework.http.HttpInputMessage)
     */
    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
    }

    /*
     * @see org.springframework.http.converter.GenericHttpMessageConverter#write(java.lang.Object, java.lang.reflect.Type, org.springframework.http.MediaType, org.springframework.http.HttpOutputMessage)
     */
    public void write(Object t, //
                      Type type, //
                      MediaType contentType, //
                      HttpOutputMessage outputMessage //
    ) throws IOException, HttpMessageNotWritableException {

        HttpHeaders headers = outputMessage.getHeaders();
        if (headers.getContentType() == null) {
            if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
                contentType = getDefaultContentType(t);
            }
            if (contentType != null) {
                headers.setContentType(contentType);
            }
        }
        if (headers.getContentLength() == -1) {
            Long contentLength = getContentLength(t, headers.getContentType());
            if (contentLength != null) {
                headers.setContentLength(contentLength);
            }
        }
        writeInternal(t, outputMessage);
        outputMessage.getBody().flush();
    }

}
