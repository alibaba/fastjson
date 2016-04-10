package com.alibaba.fastjson.parser;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.util.FieldInfo;

final class StringFieldDeserializer extends FieldDeserializer {
    public StringFieldDeserializer(ParserConfig config, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo, 0);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        String value;

        final JSONLexer lexer = parser.lexer;
        if (lexer.token == JSONToken.LITERAL_STRING) {
            value = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
        } else {

            Object obj = parser.parse();

            if (obj == null) {
                value = null;
            } else {
                value = obj.toString();
            }
        }

        if (object == null) {
            fieldValues.put(fieldInfo.name, value);
        } else {
            setValue(object, value);
        }
    }
}
