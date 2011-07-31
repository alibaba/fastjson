package com.alibaba.fastjson.parser.deserializer;

import java.util.ArrayList;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

public class ArrayListStringFieldDeserializer extends FieldDeserializer {

    public ArrayListStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);

    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @Override
    public void parseField(DefaultExtJSONParser parser, Object object) {
        ArrayList<Object> list = new ArrayList<Object>();

        ArrayListStringDeserializer.parseArray(parser, list);

        setValue(object, list);
    }
}
