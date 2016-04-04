package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class LongFieldDeserializer extends FieldDeserializer {

    public LongFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo, 0);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Long value;
        
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token;
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
