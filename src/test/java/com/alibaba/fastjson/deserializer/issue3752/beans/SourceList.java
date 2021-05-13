package com.alibaba.fastjson.deserializer.issue3752.beans;

import java.util.List;

public interface SourceList extends List<String> {
    String getFieldListToString(String fieldName);
}