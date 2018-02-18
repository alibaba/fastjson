package com.alibaba.fastjson.support.spring.messaging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeType;

import java.nio.charset.Charset;

/**
 * Fastjson for Spring Messaging Json Converter.
 * <p>
 * Compatible Spring Messaging version 4+
 *
 * @author KimmKing
 * @see AbstractMessageConverter
 * @since 1.2.47
 */
public class MappingFastJsonMessageConverter extends AbstractMessageConverter {

    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    /**
     * @return the fastJsonConfig.
     * @since 1.2.47
     */
    public FastJsonConfig getFastJsonConfig() {
        return fastJsonConfig;
    }

    /**
     * @param fastJsonConfig the fastJsonConfig to set.
     * @since 1.2.47
     */
    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public MappingFastJsonMessageConverter() {
        super(new MimeType("application", "json", Charset.forName("UTF-8")));
    }

    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected boolean canConvertFrom(Message<?> message, Class<?> targetClass) {
        return supports(targetClass);
    }

    @Override
    protected boolean canConvertTo(Object payload, MessageHeaders headers) {
        return supports(payload.getClass());
    }

    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        // parse byte[] or String payload to Java Object
        Object payload = message.getPayload();
        Object obj = null;
        if (payload instanceof byte[]) {
            obj = JSON.parseObject((byte[]) payload, 0, ((byte[]) payload).length,
                    fastJsonConfig.getCharset(), targetClass, fastJsonConfig.getFeatures());
        }else if(payload instanceof String) {
            obj = JSON.parseObject((String) payload,targetClass,fastJsonConfig.getFeatures());
        }

        return obj;
    }

    @Override
    protected Object convertToInternal(Object payload, MessageHeaders headers, Object conversionHint) {
        // encode payload to json string
        return JSON.toJSONString(payload,fastJsonConfig.getSerializeConfig(),
                fastJsonConfig.getSerializeFilters(),fastJsonConfig.getSerializerFeatures());

    }
}
