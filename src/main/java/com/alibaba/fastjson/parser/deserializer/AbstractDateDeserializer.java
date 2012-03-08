package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;

public abstract class AbstractDateDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object val;
        if (parser.getResolveStatus() == DefaultJSONParser.TypeNameRedirect) {
            parser.accept(JSONToken.COMMA);

            JSONScanner lexer = (JSONScanner) parser.getLexer();
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            parser.accept(JSONToken.COLON);

            val = parser.parse();

            parser.accept(JSONToken.RBRACE);

            parser.setResolveStatus(DefaultJSONParser.NONE);
        } else {
            val = parser.parse();
        }
        
        return (T) cast(parser, clazz, fieldName, val);
    }

    protected abstract <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object value);
}
