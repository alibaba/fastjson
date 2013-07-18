package data.media.writeAsArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import data.media.Media;
import data.media.Media.Player;

public class MediaDeserializer implements ObjectDeserializer {

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.getLexer();
        parser.accept(JSONToken.LBRACKET, JSONToken.LITERAL_INT);

        Media media = new Media();

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        media.setBitrate(lexer.intValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_INT);

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        media.setHeight(lexer.intValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_INT);

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        media.setWidth(lexer.intValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);

        if (lexer.token() == JSONToken.NULL) {
            media.setCopyright(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            media.setCopyright(lexer.stringVal());
        }
       
        lexer.nextTokenWithComma(JSONToken.LITERAL_INT);

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        media.setDuration(lexer.longValue());
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);

        if (lexer.token() == JSONToken.NULL) {
            media.setFormat(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            media.setFormat(lexer.stringVal());
        }
        lexer.nextTokenWithComma(JSONToken.LBRACKET);

        List<String> persons = new ArrayList<String>();
        parser.accept(JSONToken.LBRACKET, JSONToken.LITERAL_STRING);
        for (;;) {
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                persons.add(lexer.stringVal());
                lexer.nextToken(JSONToken.COMMA);
                if (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                } else {
                    break;
                }
            }
        }
        parser.accept(JSONToken.RBRACKET, JSONToken.COMMA);
        media.setPersons(persons);
        parser.accept(JSONToken.COMMA, JSONToken.LITERAL_STRING);

        if (lexer.token() == JSONToken.NULL) {
            media.setPlayer(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            media.setPlayer(Player.valueOf(lexer.stringVal()));
        }
        lexer.nextTokenWithComma(JSONToken.LITERAL_INT);

        if (lexer.token() != JSONToken.LITERAL_INT) {
            throw new JSONException("error");
        }
        media.setSize(lexer.longValue());
        lexer.nextToken(JSONToken.COMMA);
        parser.accept(JSONToken.COMMA, JSONToken.LITERAL_STRING);

        if (lexer.token() == JSONToken.NULL) {
            media.setTitle(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            media.setTitle(lexer.stringVal());
        }
        lexer.nextTokenWithComma(JSONToken.LITERAL_STRING);

        if (lexer.token() == JSONToken.NULL) {
            media.setUri(null);
        } else {
            if (lexer.token() != JSONToken.LITERAL_STRING) {
                throw new JSONException("error");
            }
            media.setUri(lexer.stringVal());
        }
        lexer.nextToken(JSONToken.RBRACKET);
        parser.accept(JSONToken.RBRACKET, JSONToken.COMMA);

        return (T) media;

    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
