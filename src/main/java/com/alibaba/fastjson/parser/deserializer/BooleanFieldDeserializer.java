package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class BooleanFieldDeserializer extends FieldDeserializer {

    public BooleanFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo, JSONToken.TRUE);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Boolean value;

        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        if (token == JSONToken.TRUE) {
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.name, Boolean.TRUE);
            } else {
                setValue(object, Boolean.TRUE);
            }
            return;
        }

        if (token == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            boolean booleanValue = val == 1;
            if (object == null) {
                fieldValues.put(fieldInfo.name, booleanValue);
            } else {
                setValue(object, booleanValue ? Boolean.TRUE : Boolean.FALSE);
            }
            return;
        }

        if (token == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);

            if (fieldInfo.fieldClass == boolean.class) {
                // skip
                return;
            }

            if (object != null) {
                setValue(object, null);
            }
            return;
        }

        if (token == JSONToken.FALSE) {
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.name, Boolean.FALSE);
            } else {
                setValue(object, Boolean.FALSE);
            }
            return;
        }

        Object obj = parser.parse();

        value = TypeUtils.castToBoolean(obj);

        if (value == null && fieldInfo.fieldClass == boolean.class) {
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
