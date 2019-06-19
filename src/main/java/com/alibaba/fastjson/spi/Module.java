package com.alibaba.fastjson.spi;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

public interface Module {
    ObjectDeserializer createDeserializer(ParserConfig config, Class type);
    ObjectSerializer createSerializer(SerializeConfig config, Class type);
}
