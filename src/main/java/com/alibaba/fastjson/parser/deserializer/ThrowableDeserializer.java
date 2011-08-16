package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

public class ThrowableDeserializer extends JavaBeanDeserializer {

    public ThrowableDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz, Object fieldName) {
        Object jsonValue = parser.parse();
        return (T) TypeUtils.cast(jsonValue, clazz, parser.getConfig());
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
