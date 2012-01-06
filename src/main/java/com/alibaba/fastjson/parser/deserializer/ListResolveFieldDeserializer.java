package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;

@SuppressWarnings("rawtypes")
public final class ListResolveFieldDeserializer extends FieldDeserializer {

    private final int  index;
    private final List list;

    public ListResolveFieldDeserializer(List list, int index){
        super(null, null);
        this.index = index;
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object object, Object value) {
        list.set(index, value);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {

    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
