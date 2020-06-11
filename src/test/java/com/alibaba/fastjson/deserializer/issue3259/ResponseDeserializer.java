package com.alibaba.fastjson.deserializer.issue3259;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;


public class ResponseDeserializer implements ObjectDeserializer, BaseSerializable {


    public static final ResponseDeserializer instance = new ResponseDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {

        JSONLexer lexer = parser.getLexer();

        Response res = new Response();
        String responseClz = null;
        int token;
        for (; ; ) {
            String key = lexer.scanSymbol(parser.getSymbolTable());
            lexer.nextTokenWithColon(JSONToken.LITERAL_STRING);
            if (RES_CLA.equals(key)) {
                token = lexer.token();
                if (token == JSONToken.LITERAL_STRING) {
                    responseClz = lexer.stringVal();
                }
                lexer.nextToken();
            } else if (RES.equals(key)) {
                try {
                    res.setResponse(parser.parseObject(Class.forName(responseClz)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            token = lexer.token();
            if (token == JSONToken.RBRACE) {
                lexer.nextToken(JSONToken.COMMA);
                break;
            }
        }
        return (T) res;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
