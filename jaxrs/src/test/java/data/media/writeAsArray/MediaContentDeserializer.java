package data.media.writeAsArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;

public class MediaContentDeserializer implements ObjectDeserializer {

    MediaDeserializer mediaDeser = new MediaDeserializer();
    ImageDeserializer imageDesc  = new ImageDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();

        parser.accept(JSONToken.LBRACKET, JSONToken.LBRACKET);
        Media media = mediaDeser.deserialze(parser, Media.class, "media");
        parser.accept(JSONToken.COMMA, JSONToken.LBRACKET);
        
        parser.accept(JSONToken.LBRACKET, JSONToken.LBRACKET);
        List<Image> images = new ArrayList<Image>();
        int index = 0;
        for (;;) {
            Image image = imageDesc.deserialze(parser, Image.class, index);
            images.add(image);
            index++;
            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LBRACKET);
            } else {
                break;
            }
        }
        parser.accept(JSONToken.RBRACKET, JSONToken.RBRACKET);
        
        parser.accept(JSONToken.RBRACKET, JSONToken.EOF);
        
        MediaContent content = new MediaContent();
        content.setMedia(media);
        content.setImages(images);
        return (T) content;
    }

    public int getFastMatchToken() {
        return 0;
    }

}