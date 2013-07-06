package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.util.TypeUtils;

@SuppressWarnings("rawtypes")
public final class ListResolveFieldDeserializer extends FieldDeserializer {

    private final int               index;
    private final List              list;
    private final DefaultJSONParser parser;

    public ListResolveFieldDeserializer(DefaultJSONParser parser, List list, int index){
        super(null, null);
        this.parser = parser;
        this.index = index;
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object object, Object value) {
        list.set(index, value);

        if (list instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) list;
            Object array = jsonArray.getRelatedArray();

            if (array != null) {
                int arrayLength = Array.getLength(array);

                if (arrayLength > index) {
                    Object item;
                    if (jsonArray.getComponentType() != null) {
                        item = TypeUtils.cast(value, jsonArray.getComponentType(), parser.getConfig());
                    } else {
                        item = value;
                    }
                    Array.set(array, index, item);
                }
            }
        }
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {

    }

}
