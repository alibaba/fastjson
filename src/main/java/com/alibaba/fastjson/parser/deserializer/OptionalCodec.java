package com.alibaba.fastjson.parser.deserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.util.TypeUtils;

public class OptionalCodec implements ObjectSerializer, ObjectDeserializer {

    public static OptionalCodec instance = new OptionalCodec();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        type = TypeUtils.unwrapOptional(type);
        Object value = parser.parseObject(type);
        return (T) Optional.of(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {

        Optional<?> optional = (Optional<?>) object;
        Object value = optional.get();
        serializer.write(value);
    }

}
