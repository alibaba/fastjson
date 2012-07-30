package com.alibaba.fastjson.serializer;

public interface NamePreFilter extends SerializeFilter {

    boolean apply(JSONSerializer serializer, Object source, String name);
}
