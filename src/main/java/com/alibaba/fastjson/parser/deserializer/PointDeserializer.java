package com.alibaba.fastjson.parser.deserializer;

import java.awt.Point;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;

public class PointDeserializer implements ObjectDeserializer {

    public final static PointDeserializer instance = new PointDeserializer();
    
    

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        
        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }
        
        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error");
        }
        lexer.nextToken();

        int x = 0, y = 0;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                
                if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                    parser.acceptType("java.awt.Point");
                    continue;
                }
                
                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }
            
            int val;
            if (lexer.token() == JSONToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error : " + lexer.tokenName());
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return (T) new Point(x, y);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
