package data.media;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class MediaGenDecoder extends ASMJavaBeanDeserializer implements ObjectDeserializer {
    private char[] size_gen_prefix__ = "\"size\":".toCharArray();
    private char[] uri_gen_prefix__ = "\"uri\":".toCharArray();
    private char[] title_gen_prefix__ = "\"title\":".toCharArray();
    private char[] width_gen_prefix__ = "\"width\":".toCharArray();
    private char[] height_gen_prefix__ = "\"height\":".toCharArray();
    private char[] duration_gen_prefix__ = "\"duration\":".toCharArray();
    private char[] bitrate_gen_prefix__ = "\"bitrate\":".toCharArray();
    private char[] persons_gen_prefix__ = "\"persons\":".toCharArray();
    private char[] player_gen_prefix__ = "\"player\":".toCharArray();
    private char[] copyright_gen_prefix__ = "\"copyright\":".toCharArray();
    private char[] format_gen_prefix__ = "\"format\":".toCharArray();
    
    private ObjectDeserializer uri_gen_deser__;
    private ObjectDeserializer title_gen_deser__;
    private ObjectDeserializer persons_gen_list_item_deser__;
    private Type persons_gen_list_item_type__ = java.lang.String.class;
    private ObjectDeserializer copyright_gen_deser__;
    private ObjectDeserializer format_gen_deser__;
    
    public MediaGenDecoder (ParserConfig config, Class clazz) {
        super(config, clazz);
        
    }
    
    public Object createInstance(DefaultJSONParser parser, Type type) {
        return new Media();
        
    }
    public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();
        
        if (!lexer.isEnabled(Feature.SortFeidFastMatch)) {
            return super.deserialze(parser, type, fieldName);
        }
        
        if (isSupportArrayToBean(lexer)) {
            // deserialzeArrayMapping
        }
        
        if (lexer.scanType("Department") == JSONLexerBase.NOT_MATCH) {
            return super.deserialze(parser, type, fieldName);
        }
        
        ParseContext mark_context = parser.getContext();
        int matchedCount = 0;
        Media instance = new Media();
        
        ParseContext context = parser.getContext();
        ParseContext childContext = parser.setContext(context, instance, fieldName);
        
        if (lexer.matchStat == JSONLexerBase.END) {
            return instance;
        }
        
        int matchStat = 0;
        int _asm_flag_0 = 0;
        int bitrate_gen = 0;
        String copyright_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            copyright_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 2;
        } else {
            copyright_gen = null;
        }
        long duration_gen = 0;
        String format_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            format_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 8;
        } else {
            format_gen = null;
        }
        int height_gen = 0;
        java.util.List persons_gen = null;
        data.media.Media.Player player_gen = null;
        long size_gen = 0;
        String title_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            title_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 256;
        } else {
            title_gen = null;
        }
        String uri_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            uri_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 512;
        } else {
            uri_gen = null;
        }
        int width_gen = 0;
        boolean endFlag = false, restFlag = false;
        
        if ((!endFlag) && (!restFlag)) {
            bitrate_gen = lexer.scanFieldInt(bitrate_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 1;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            copyright_gen = lexer.scanFieldString(copyright_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 2;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            duration_gen = lexer.scanFieldLong(duration_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 4;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            format_gen = lexer.scanFieldString(format_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 8;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            height_gen = lexer.scanFieldInt(height_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 16;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            persons_gen = (java.util.List) lexer.scanFieldStringArray(persons_gen_prefix__, java.util.List.class);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 32;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            String player_gen_enum_name = lexer.scanFieldSymbol(player_gen_prefix__, parser.getSymbolTable());
            if (player_gen_enum_name == null) {
                player_gen = data.media.Media.Player.valueOf(player_gen_enum_name);
            }
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 64;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            size_gen = lexer.scanFieldLong(size_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 128;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            title_gen = lexer.scanFieldString(title_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 256;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            uri_gen = lexer.scanFieldString(uri_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 512;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat == JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            width_gen = lexer.scanFieldInt(width_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 1024;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat != JSONLexerBase.END) {
                restFlag = true;
            }
            
        }
        if ((_asm_flag_0 & 1) != 0) {
            instance.setBitrate(bitrate_gen);
            
        }
        if ((_asm_flag_0 & 2) != 0) {
            instance.setCopyright(copyright_gen);
            
        }
        if ((_asm_flag_0 & 4) != 0) {
            instance.setDuration(duration_gen);
            
        }
        if ((_asm_flag_0 & 8) != 0) {
            instance.setFormat(format_gen);
            
        }
        if ((_asm_flag_0 & 16) != 0) {
            instance.setHeight(height_gen);
            
        }
        if ((_asm_flag_0 & 32) != 0) {
            instance.setPersons(persons_gen);
            
        }
        if ((_asm_flag_0 & 64) != 0) {
            instance.setPlayer(player_gen);
            
        }
        if ((_asm_flag_0 & 128) != 0) {
            instance.setSize(size_gen);
            
        }
        if ((_asm_flag_0 & 256) != 0) {
            instance.setTitle(title_gen);
            
        }
        if ((_asm_flag_0 & 512) != 0) {
            instance.setUri(uri_gen);
            
        }
        if ((_asm_flag_0 & 1024) != 0) {
            instance.setWidth(width_gen);
            
        }
        
        if (restFlag) {
            return super.parseRest(parser, type, fieldName, instance);
        }
        
        return instance;
        
    }
}

