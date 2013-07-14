package com.alibaba.fastjson.parser.deserializer;



public interface ExtraProcessor extends ParseProcess {
    void process(Object object, String key, Object value);
}
