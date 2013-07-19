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

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Image image = new Image();

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("error");
        }
        
        int height = lexer.scanInt(',');
        int width = lexer.scanInt(',');
        String sizeName = lexer.scanSymbolWithSeperator(parser.getSymbolTable(), ',');
        String title = lexer.scanString(',');
        String uri = lexer.scanString(']');
        
        lexer.nextToken(JSONToken.COMMA);
        
        image.setHeight(height);
        image.setWidth(width);
        image.setSize(Size.valueOf(sizeName));
        image.setTitle(title);
        image.setUri(uri);
        return (T) image;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

}