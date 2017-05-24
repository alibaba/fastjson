package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fastjson for Spring MVC Converter.
 * <p>
 * Compatible Spring MVC version 4.2+
 *
 * @author Victor.Zxy
 * @see AbstractGenericHttpMessageConverter
 * @since 1.2.11
 */
public class FastJsonHttpMessageConverter4 //
        extends AbstractGenericHttpMessageConverter<Object> {
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
    public FastJsonHttpMessageConverter4() {

        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> paramClass) {
        return true;
    }

    public Object read(Type type, //
                       Class<?> contextClass, //
                       HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
    }

    @Override
    protected void writeInternal(Object obj, //
                                 Type type, //
                                 HttpOutputMessage outputMessage //
    ) throws IOException, HttpMessageNotWritableException {

        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();

        Object value = obj;
        //获取全局配置的filter
        SerializeFilter[] globalFilters = fastJsonConfig.getSerializeFilters();
        List<SerializeFilter> allFilters = new ArrayList<SerializeFilter>(Arrays.asList(globalFilters));

        if(obj instanceof FastJsonContainer){
            PropertyPreFilters filters = ((FastJsonContainer) obj).getFilters();
            allFilters.addAll(filters.getFilters());
            value = ((FastJsonContainer) obj).getValue();
        }

        SerializeFilter[] serializeFilters = new SerializeFilter[allFilters.size()];
        int len = JSON.writeJSONString(outnew, //
                fastJsonConfig.getCharset(), //
                value, //
                fastJsonConfig.getSerializeConfig(), //
                //fastJsonConfig.getSerializeFilters(), //
                allFilters.toArray(serializeFilters),
                fastJsonConfig.getDateFormat(), //
                JSON.DEFAULT_GENERATE_FEATURE, //
                fastJsonConfig.getSerializerFeatures());
        if (fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength(len);
        }
        OutputStream out = outputMessage.getBody();
        outnew.writeTo(out);
        outnew.close();
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, //
                                  HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
    }
}
