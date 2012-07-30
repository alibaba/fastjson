package com.alibaba.fastjson.serializer;

public interface NamePreFilter extends SerializeFilter {

    boolean apply(Object source, String name);
}
