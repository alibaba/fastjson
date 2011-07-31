package data.media;

import java.lang.reflect.Type;
import java.util.List;

import com.alibaba.fastjson.parser.DefaultExtJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import data.media.Media.Player;

public class MediaDeserializer implements ObjectDeserializer {

    private char[] size_     = "\"size\":".toCharArray();
    private char[] format_   = "\"format\":".toCharArray();
    private char[] uri_      = "\"uri\":".toCharArray();
    private char[] title_   = "\"title\":".toCharArray();
    private char[] width_    = "\"width\":".toCharArray();
    private char[] height_   = "\"height\":".toCharArray();
    private char[] duration_ = "\"duration\":".toCharArray();
    private char[] bitrate_  = "\"bitrate\":".toCharArray();
    private char[] persons_  = "\"persons\":".toCharArray();
    private char[] player_   = "\"player\":".toCharArray();
    private char[] copyright_   = "\"copyright\":".toCharArray();

    public <T> T deserialze(DefaultExtJSONParser parser, Type clazz) {
        // "size":58982400,"format":"video/mpg4","uri":"http://javaone.com/keynote.mpg","title":"Javaone Keynote","width":640,"height":480,
        // "duration":18000000,"bitrate":262144,"persons":["Bill Gates","Steve Jobs"],"player":"JAVA"
        
        final JSONScanner lexer = (JSONScanner) parser.getLexer();

        String uri;
        String title; // Can be unset.
        int width;
        int height;
        String format;
        long duration;
        long size;
        int bitrate; // Can be unset.
        List<String> persons;
        Player player;
        String copyright; // Can be unset.
        
        int mark = lexer.getBufferPosition();
        char mark_ch = lexer.getCurrent();
        int mark_token = lexer.token();
        
        {
            size = lexer.scanFieldLong(size_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                lexer.reset(mark, mark_ch, mark_token);
                throw new UnsupportedOperationException();
            }
        }
        {
            format = lexer.scanFieldString(format_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            uri = lexer.scanFieldString(uri_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            title = lexer.scanFieldString(title_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            width = lexer.scanFieldInt(width_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            height = lexer.scanFieldInt(height_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            duration = lexer.scanFieldLong(duration_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            bitrate = lexer.scanFieldInt(bitrate_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            persons = lexer.scanFieldStringArray(persons_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
        }
        {
            String value = lexer.scanFieldString(player_);
            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
                throw new UnsupportedOperationException();
            }
            player = Player.valueOf(value);
        }
//        {
//            copyright = lexer.scanFieldString(copyright_);
//            if (lexer.matchStat == JSONScanner.NOT_MATCH) {
//                throw new UnsupportedOperationException();
//            }
//        }

//        String uri;
//        String title; // Can be unset.
//        int width;
//        int height;
//        String format;
        
//        long duration;
//        long size;
//        int bitrate; // Can be unset.
//        List<String> persons;
//        Player player;
//        String copyright; // Can be unset.
        
        Media media = new Media();
        media.setUri(uri);
        media.setTitle(title);
        media.setWidth(width);
        media.setHeight(height);
        media.setFormat(format);
        
        media.setDuration(duration);
        media.setSize(size);
        media.setBitrate(bitrate);
        media.setPersons(persons);
        media.setPlayer(player);
        
       // media.setCopyright(copyright);
        
        return (T) media;
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }

}
