package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class LongFieldDeserializer extends FieldDeserializer {

    private final ObjectDeserializer fieldValueDeserilizer;

    public LongFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo, 0);

        fieldValueDeserilizer = mapping.getDeserializer(fieldInfo);
        fastMatchToken = fieldValueDeserilizer.getFastMatchToken();
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Long value;
        
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.LITERAL_INT) {
            long val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.name, val);
            } else {
                setValue(object, Long.valueOf(val));
            }
            return;
        } else if (token == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);
 
        } else {
            Object obj = parser.parse();

            value = TypeUtils.castToLong(obj);
        }
        
        if (value == null && fieldInfo.fieldClass == long.class) {
            // skip
            return;
        }
        
        if (object == null) {
            fieldValues.put(fieldInfo.name, value);
        } else {
            setValue(object, value);
        }
    }
}
