package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonStringFieldDeserializer extends DefaultFieldDeserializer {
    public JsonStringFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(mapping, clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        String json = parser.lexer.stringVal();

        Type fieldType = fieldInfo.fieldType;
        Object value ;
        if(fieldType instanceof ParameterizedType){
            value = JSONArray.parseArray(json,((ParameterizedType)fieldType).getActualTypeArguments());
        }else{
            value = JSON.parseObject(json,fieldType);

        }
        setValue(object, value);

    }
}
