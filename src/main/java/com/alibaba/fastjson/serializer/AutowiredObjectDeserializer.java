package com.alibaba.fastjson.serializer;

import java.lang.reflect.Type;
import java.util.Set;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public interface AutowiredObjectDeserializer extends ObjectDeserializer{
	Set<Type> getAutowiredFor();
}
