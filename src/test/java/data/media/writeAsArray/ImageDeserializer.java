package data.media.writeAsArray;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import data.media.Image;
import data.media.Image.Size;

public class ImageDeserializer implements ObjectDeserializer {

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Image image = new Image();

        final JSONLexer lexer = parser.getLexer();
        parser.accept(JSONToken.LBRACKET, JSONToken.LITERAL_INT);

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        image.setHeight(lexer.intValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_INT);
        
        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error : " + lexer.tokenName());
        }
        image.setWidth(lexer.intValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);
        
        if (lexer.token() == JSONToken.NULL) {
            image.setSize(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            image.setSize(Size.valueOf(lexer.stringVal()));
        }
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);
        
        if (lexer.token() == JSONToken.NULL) {
            image.setTitle(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            image.setTitle(lexer.stringVal());
        }
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);
        
        if (lexer.token() == JSONToken.NULL) {
            image.setUri(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            image.setUri(lexer.stringVal());
        }
        lexer.nextToken(JSONToken.RBRACKET);
        parser.accept(JSONToken.RBRACKET, JSONToken.COMMA);
        
        return (T) image;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
