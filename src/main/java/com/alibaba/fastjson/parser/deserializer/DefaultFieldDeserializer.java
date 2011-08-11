package com.alibaba.fastjson.parser.deserializer;

import java.util.List;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.DefaultExtJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;

public class DefaultFieldDeserializer extends FieldDeserializer {

    private ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultExtJSONParser parser, Object object) {
        if (fieldValueDeserilizer == null) {
            fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldInfo);
        }

        Object value = fieldValueDeserilizer.deserialze(parser, getFieldType());
        if (parser.getReferenceResolveStat() == DefaultExtJSONParser.NeedToResolve) {
            List<ResolveTask> tasks = parser.getResolveTaskList();
            ResolveTask task = tasks.get(tasks.size() - 1);
            task.setFieldDeserializer(this);
            parser.setReferenceResolveStat(DefaultExtJSONParser.NONE);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        if (fieldValueDeserilizer != null) {
            return fieldValueDeserilizer.getFastMatchToken();
        }

        return JSONToken.LITERAL_INT;
    }
}
