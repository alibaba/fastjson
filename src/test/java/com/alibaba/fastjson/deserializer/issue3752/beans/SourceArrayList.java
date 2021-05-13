package com.alibaba.fastjson.deserializer.issue3752.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class SourceArrayList extends ArrayList<String> implements SourceList, Serializable {
    @Override
    public String getFieldListToString(String fieldName) {
        return null;
    }
}