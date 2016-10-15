package com.alibaba.fastjson.support.spring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;

/**
 * Fastjson for Spring MVC Converter.
 * <p>
 * Compatible Spring MVC version 4.2+
 * <p>
 * support to write JSONP
 *
 * <pre>
 * Configuration in xml:
 * <code>
 *     &lt;mvc:annotation-driven&gt;
 *         &lt;mvc:message-converters&gt;
 *             &lt;bean
 *                 class=&quot;com.alibaba.fastjson.support.spring.FastJsonpHttpMessageConverter4&quot;&gt;
 *                 &lt;property name=&quot;supportedMediaTypes&quot;&gt;
 *                     &lt;list&gt;
 *                         &lt;value&gt;application/json;charset=UTF-8&lt;/value&gt;
 *                     &lt;/list&gt;
 *                 &lt;/property&gt;
 *             &lt;/bean&gt;
 *         &lt;/mvc:message-converters&gt;
 *     &lt;/mvc:annotation-driven&gt;
 * 
 *     &lt;bean id=&quot;fastJsonpResponseBodyAdvice&quot; class=&quot;com.alibaba.fastjson.support.spring.FastJsonpResponseBodyAdvice&quot;&gt;
 *         &lt;constructor-arg&gt;
 *             &lt;list&gt;
 *                 &lt;value&gt;callback&lt;/value&gt;
 *                 &lt;value&gt;jsonp&lt;/value&gt;
 *             &lt;/list&gt;
 *         &lt;/constructor-arg&gt;
 *     &lt;/bean&gt;
 * </code>
 * </pre>
 *
 * <pre>
 * Configuration in java:
 *     &#064;EnableWebMvc
 *     &#064;Configuration
 *     public class Config extends WebMvcConfigurerAdapter {
 *         &#064;ean
 *         public FastJsonpResponseBodyAdvice fastJsonpResponseBodyAdvice() {
 *             return new FastJsonpResponseBodyAdvice("callback", "jsonp");
 *         }
 * 
 *         &#064;Override
 *         public void extendMessageConverters(List&lt;HttpMessageConverter&lt;?&gt;&gt; converters) {
 *             converters.add(0, new FastJsonpHttpMessageConverter4());
 *             super.extendMessageConverters(converters);
 *         }
 *     }
 * <code>
 * </code>
 * </pre>
 *
 * @author Jerry.Chen
 * @since 1.2.20
 */
public class FastJsonpHttpMessageConverter4 extends AbstractGenericHttpMessageConverter<Object> {
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
    public FastJsonpHttpMessageConverter4() {

        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> paramClass) {
        return true;
    }

    @Override
    public Object read(Type type, //
            Class<?> contextClass, //
            HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, //
            HttpInputMessage inputMessage //
    ) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
    }

    @Override
    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int len = writePrefix(outnew, obj);
        Object value = obj;
        if (obj instanceof MappingFastJsonValue) {
            MappingFastJsonValue container = (MappingFastJsonValue) obj;
            value = container.getValue();
        }
        len += JSON.writeJSONString(outnew, //
                fastJsonConfig.getCharset(), //
                value, //
                fastJsonConfig.getSerializeConfig(), //
                fastJsonConfig.getSerializeFilters(), //
                fastJsonConfig.getDateFormat(), //
                JSON.DEFAULT_GENERATE_FEATURE, //
                fastJsonConfig.getSerializerFeatures());
        len += writeSuffix(outnew, obj);
        headers.setContentLength(len);
        OutputStream out = outputMessage.getBody();
        outnew.writeTo(out);
        outnew.close();
    }

    private static final byte[] JSONP_FUNCTION_PREFIX_BYTES = "/**/".getBytes(IOUtils.UTF8);
    private static final byte[] JSONP_FUNCTION_SUFFIX_BYTES = ");".getBytes(IOUtils.UTF8);

    /**
     * Write a prefix before the main content.
     */
    protected int writePrefix(ByteArrayOutputStream out, Object object) throws IOException {
        String jsonpFunction = (object instanceof MappingFastJsonValue ? ((MappingFastJsonValue) object)
                .getJsonpFunction() : null);
        int length = 0;
        if (jsonpFunction != null) {
            out.write(JSONP_FUNCTION_PREFIX_BYTES);
            byte[] bytes = (jsonpFunction + "(").getBytes(IOUtils.UTF8);
            out.write(bytes);
            length += JSONP_FUNCTION_PREFIX_BYTES.length + bytes.length;
        }
        return length;
    }

    /**
     * Write a suffix after the main content.
     */
    protected int writeSuffix(ByteArrayOutputStream out, Object object) throws IOException {
        String jsonpFunction = (object instanceof MappingFastJsonValue ? ((MappingFastJsonValue) object)
                .getJsonpFunction() : null);
        int length = 0;
        if (jsonpFunction != null) {
            out.write(JSONP_FUNCTION_SUFFIX_BYTES);
            length += JSONP_FUNCTION_SUFFIX_BYTES.length;
        }
        return length;
    }
}
