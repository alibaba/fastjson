package com.alibaba.fastjson.parser.deserializer;

/**
 * 
 * @author wenshao[szujobs@hotmail.com]
 * @since 1.2.9 back port to 1.1.49.android
 */
public interface ExtraProcessable {
    void processExtra(String key, Object value);
}
