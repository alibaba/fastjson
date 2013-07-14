package com.alibaba.fastjson.parser.deserializer;



public interface RedundantProcessor extends ParseProcess {
    void process(Object object, String key, Object value);
}
