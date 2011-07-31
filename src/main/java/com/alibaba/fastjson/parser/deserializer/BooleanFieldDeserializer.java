package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;

public class BooleanFieldDeserializer extends FieldDeserializer {

    public BooleanFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultExtJSONParser parser, Object object) {
        Boolean value;

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.TRUE) {
            lexer.nextToken(JSONToken.COMMA);
            setValue(object, true);
            return;
        }

        if (lexer.token() == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            if (val == 1) {
                setValue(object, true);
            } else {
                setValue(object, false);
            }
            return;
        }

        if (lexer.token() == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);

            if (getFieldClass() == boolean.class) {
                // skip
                return;
            }

            setValue(object, null);
            return;
        }

        if (lexer.token() == JSONToken.FALSE) {
            lexer.nextToken(JSONToken.COMMA);
            setValue(object, false);
            return;
        }

        Object obj = parser.parse();

        value = TypeUtils.castToBoolean(obj);

        if (value == null && getFieldClass() == boolean.class) {
            // skip
            return;
        }

        setValue(object, value);
    }

    public int getFastMatchToken() {
        return JSONToken.TRUE;
    }
}
