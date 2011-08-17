package data.media;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import data.media.Image.Size;

public class ImageDeserializer extends ASMJavaBeanDeserializer implements ObjectDeserializer {

    public ImageDeserializer(ParserConfig mapping, Class<?> clazz){
        super(mapping, clazz);
    }

    public ImageDeserializer(){
        super(ParserConfig.getGlobalInstance(), Image.class);
    }

    // "size":"LARGE","uri":"http://javaone.com/keynote_large.jpg","title":"Javaone Keynote","width":1024,"height":768
    private char[] size_   = "\"size\":".toCharArray();
    private char[] uri_    = "\"uri\":".toCharArray();
    private char[] titile_ = "\"title\":".toCharArray();
    private char[] width_  = "\"width\":".toCharArray();
    private char[] height_ = "\"height\":".toCharArray();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        final JSONScanner lexer = (JSONScanner) parser.getLexer();

        int height;
        Size size;
        String title;
        String uri;
        int width;
        

        int mark = lexer.getBufferPosition();
        char mark_ch = lexer.getCurrent();
        int mark_token = lexer.token();

        {
            height = lexer.scanFieldInt(height_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                // 退出快速模式, 进入常规模式
                lexer.reset(mark, mark_ch, mark_token);
                return (T) super.deserialze(parser, clazz, fieldName);
            }
        }
        {
            String value = lexer.scanFieldString(size_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                // 退出快速模式, 进入常规模式
                lexer.reset(mark, mark_ch, mark_token);
                return (T) super.deserialze(parser, clazz, fieldName);
            }
            size = Size.valueOf(value);
        }
        {
            title = lexer.scanFieldString(titile_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                // 退出快速模式, 进入常规模式
                lexer.reset(mark, mark_ch, mark_token);
                return (T) super.deserialze(parser, clazz, fieldName);
            }
        }
        {
            uri = lexer.scanFieldString(uri_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                // 退出快速模式, 进入常规模式
                lexer.reset(mark, mark_ch, mark_token);
                return (T) super.deserialze(parser, clazz, fieldName);
            }
        }
        {
            width = lexer.scanFieldInt(width_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                // 退出快速模式, 进入常规模式
                lexer.reset(mark, mark_ch, mark_token);
                return (T) super.deserialze(parser, clazz, fieldName);
            }
        }


        if (lexer.matchStat != JSONScanner.END) {
            // 退出快速模式, 进入常规模式
            lexer.reset(mark, mark_ch, mark_token);
            return (T) super.deserialze(parser, clazz, fieldName);
        }

        Image image = new Image();
        image.setSize(size);
        image.setUri(uri);
        image.setTitle(title);
        image.setWidth(width);
        image.setHeight(height);

        return (T) image;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

    @Override
    public Object createInstance(DefaultJSONParser parser, Type type) {
        return new Image();
    }

}
