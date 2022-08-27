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

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.getLexer();

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("error");
        }

        Media media = new Media();

        int bitrate = lexer.scanInt(',');
        int height = lexer.scanInt(',');
        int width = lexer.scanInt(',');
        String copyright = lexer.scanString(',');
        long duration = lexer.scanLong(',');
        String format = lexer.scanString(',');

        List<String> persons = new ArrayList<String>();
        lexer.scanStringArray(persons, ',');

        String playerName = lexer.scanSymbolWithSeperator(parser.getSymbolTable(), ',');
        long size = lexer.scanLong(',');
        String title = lexer.scanString(',');
        String uri = lexer.scanString(']');

        lexer.nextToken(JSONToken.COMMA);

        media.setBitrate(bitrate);
        media.setHeight(height);
        media.setWidth(width);
        media.setCopyright(copyright);
        media.setDuration(duration);
        media.setFormat(format);
        media.setPersons(persons);
        media.setPlayer(playerName == null ? null : Player.valueOf(playerName));
        media.setSize(size);
        media.setTitle(title);
        media.setUri(uri);

        return (T) media;

    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}
