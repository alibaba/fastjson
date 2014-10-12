package com.alibaba.fastjson.serializer;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class RectangleCodec implements ObjectSerializer, ObjectDeserializer {

    public final static RectangleCodec instance = new RectangleCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();
        Rectangle rectangle = (Rectangle) object;
        if (rectangle == null) {
            out.writeNull();
            return;
        }

        char sep = '{';
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            out.write('{');
            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
            out.writeString(Rectangle.class.getName());
            sep = ',';
        }

        out.writeFieldValue(sep, "x", rectangle.getX());
        out.writeFieldValue(',', "y", rectangle.getY());
        out.writeFieldValue(',', "width", rectangle.getWidth());
        out.writeFieldValue(',', "height", rectangle.getHeight());
        out.write('}');

    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();

        if (lexer.token() == JSONToken.NULL) {
            lexer.nextToken();
            return null;
        }

        if (lexer.token() != JSONToken.LBRACE && lexer.token() != JSONToken.COMMA) {
            throw new JSONException("syntax error");
        }
        lexer.nextToken();

        int x = 0, y = 0, width = 0, height = 0;
        for (;;) {
            if (lexer.token() == JSONToken.RBRACE) {
                lexer.nextToken();
                break;
            }

            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                lexer.nextTokenWithColon(JSONToken.LITERAL_INT);
            } else {
                throw new JSONException("syntax error");
            }

            int val;
            if (lexer.token() == JSONToken.LITERAL_INT) {
                val = lexer.intValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            if (key.equalsIgnoreCase("x")) {
                x = val;
            } else if (key.equalsIgnoreCase("y")) {
                y = val;
            } else if (key.equalsIgnoreCase("width")) {
                width = val;
            } else if (key.equalsIgnoreCase("height")) {
                height = val;
            } else {
                throw new JSONException("syntax error, " + key);
            }

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
            }
        }

        return (T) new Rectangle(x, y, width, height);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
