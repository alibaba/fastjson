package data.media;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class ImageGenDecoder extends JavaBeanDeserializer implements ObjectDeserializer {
    private char[] size_gen_prefix__ = "\"size\":".toCharArray();
    private char[] uri_gen_prefix__ = "\"uri\":".toCharArray();
    private char[] title_gen_prefix__ = "\"title\":".toCharArray();
    private char[] width_gen_prefix__ = "\"width\":".toCharArray();
    private char[] height_gen_prefix__ = "\"height\":".toCharArray();
    
    private ObjectDeserializer uri_gen_deser__;
    private ObjectDeserializer title_gen_deser__;
    private ObjectDeserializer size_gen_deser__;
    
    public ImageGenDecoder (ParserConfig config, Class clazz) {
        super(config, clazz);
        size_gen_deser__ = config.getDeserializer(data.media.Image.Size.class);
    }
    
    public Object createInstance(DefaultJSONParser parser, Type type) {
        return new Image();
        
    }
    public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();
        
        if (!lexer.isEnabled(Feature.SortFeidFastMatch)) {
            return super.deserialze(parser, type, fieldName);
        }
        
        if (lexer.isEnabled(Feature.SupportArrayToBean)) {
            // deserialzeArrayMapping
        }
        
        if (lexer.scanType("Department") == JSONLexerBase.NOT_MATCH) {
            return super.deserialze(parser, type, fieldName);
        }
        
        ParseContext mark_context = parser.getContext();
        int matchedCount = 0;
        Image instance = new Image();
        
        ParseContext context = parser.getContext();
        ParseContext childContext = parser.setContext(context, instance, fieldName);
        
        if (lexer.matchStat == JSONLexerBase.END) {
            return instance;
        }
        
        int matchStat = 0;
        int _asm_flag_0 = 0;
        int height_gen = 0;
        data.media.Image.Size size_gen = null;
        String title_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            title_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 4;
        } else {
            title_gen = null;
        }
        String uri_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            uri_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 8;
        } else {
            uri_gen = null;
        }
        int width_gen = 0;
        boolean endFlag = false, restFlag = false;
        
        if ((!endFlag) && (!restFlag)) {
            height_gen = lexer.scanFieldInt(height_gen_prefix__);
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
            size_gen = (data.media.Image.Size) this.scanEnum(lexer, size_gen_prefix__, size_gen_deser__);
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
            title_gen = lexer.scanFieldString(title_gen_prefix__);
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
            uri_gen = lexer.scanFieldString(uri_gen_prefix__);
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
            width_gen = lexer.scanFieldInt(width_gen_prefix__);
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 16;
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
            instance.setHeight(height_gen);
            
        }
        if ((_asm_flag_0 & 2) != 0) {
            instance.setSize(size_gen);
            
        }
        if ((_asm_flag_0 & 4) != 0) {
            instance.setTitle(title_gen);
            
        }
        if ((_asm_flag_0 & 8) != 0) {
            instance.setUri(uri_gen);
            
        }
        if ((_asm_flag_0 & 16) != 0) {
            instance.setWidth(width_gen);
            
        }
        
        if (restFlag) {
            return super.parseRest(parser, type, fieldName, instance, 0);
        }
        
        return instance;
        
    }
}

