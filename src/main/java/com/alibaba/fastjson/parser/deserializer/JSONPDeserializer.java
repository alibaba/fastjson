package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 21/02/2017.
 */
public class JSONPDeserializer implements ObjectDeserializer {
    public static final JSONPDeserializer instance = new JSONPDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();

        String funcName = lexer.scanSymbolUnQuoted(parser.getSymbolTable());
        lexer.nextToken();

        JSONPObject jsonp = new JSONPObject(funcName);

        int tok = lexer.token();
        if (tok != JSONToken.LPAREN) {
            throw new JSONException("illegal jsonp : " + lexer.info());
        }
        lexer.nextToken();
        for (;;) {
            Object arg = parser.parse();
            jsonp.addParameter(arg);

            tok = lexer.token();
            if (tok == JSONToken.COMMA) {
                lexer.nextToken();
            } else if (tok == JSONToken.RPAREN) {
                lexer.nextToken();
                break;
            } else {
                throw new JSONException("illegal jsonp : " + lexer.info());
            }
         }
        tok = lexer.token();
        if (tok == JSONToken.SEMI) {
            lexer.nextToken();
        }

        return (T) jsonp;
    }

    public int getFastMatchToken() {
        return 0;
    }
}
