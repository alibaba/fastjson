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

public class MediaContentGenDecoder extends ASMJavaBeanDeserializer implements ObjectDeserializer {
    private char[] media_gen_prefix__ = "\"media\":".toCharArray();
    private char[] images_gen_prefix__ = "\"images\":".toCharArray();
    
    private ObjectDeserializer media_gen_deser__;
    private ObjectDeserializer images_gen_list_item_deser__;
    private Type images_gen_list_item_type__ = data.media.Image.class;
    
    public MediaContentGenDecoder (ParserConfig config, Class clazz) {
        super(config, clazz);
        
    }
    
    public Object createInstance(DefaultJSONParser parser, Type type) {
        return new MediaContent();
        
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
        MediaContent instance = new MediaContent();
        
        ParseContext context = parser.getContext();
        ParseContext childContext = parser.setContext(context, instance, fieldName);
        
        if (lexer.matchStat == JSONLexerBase.END) {
            return instance;
        }
        
        int matchStat = 0;
        int _asm_flag_0 = 0;
        java.util.List images_gen = null;
        data.media.Media media_gen = null;
        boolean endFlag = false, restFlag = false;
        
        if ((!endFlag) && (!restFlag)) {
            if (lexer.matchField(images_gen_prefix__)) {
                _asm_flag_0 |= 1;
                if (lexer.token() == JSONToken.NULL) {
                    lexer.nextToken(JSONToken.COMMA);
                } else {
                    if (lexer.token() == JSONToken.LBRACKET) {
                        if(images_gen_list_item_deser__ == null) {
                            images_gen_list_item_deser__ = parser.getConfig().getDeserializer(data.media.Image.class);
                        }
                        final int fastMatchToken = images_gen_list_item_deser__.getFastMatchToken();
                        lexer.nextToken(fastMatchToken);
                        images_gen = new java.util.ArrayList();
                        ParseContext listContext = parser.getContext();
                        parser.setContext(images_gen, "images");
            
                        for(int i = 0; ;++i) {
                            if (lexer.token() == JSONToken.RBRACKET) {
                                break;
                            }
                            data.media.Image itemValue = images_gen_list_item_deser__.deserialze(parser, images_gen_list_item_type__, i);
                            images_gen.add(itemValue);
                            parser.checkListResolve(images_gen);
                            if (lexer.token() == JSONToken.COMMA) {
                                lexer.nextToken(fastMatchToken);
                            }
                        }
                        parser.setContext(listContext);
                        if (lexer.token() != JSONToken.RBRACKET) {
                            restFlag = true;
                        }
                        lexer.nextToken(JSONToken.COMMA);
            
                    } else {
                        restFlag = true;
                    }
                }
            }
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 1;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat != JSONLexerBase.END) {
                endFlag = true;
            }
            
        }
        if ((!endFlag) && (!restFlag)) {
            if (lexer.matchField(media_gen_prefix__)) {
                _asm_flag_0 |= 2;
                matchedCount++;
            if (media_gen_deser__ == null) {
                media_gen_deser__ = parser.getConfig().getDeserializer(data.media.Media.class);
            }
                media_gen_deser__.deserialze(parser, data.media.Media.class,"media");
                if(parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
                    ResolveTask resolveTask = parser.getLastResolveTask();
                    resolveTask.setOwnerContext(parser.getContext());
                    resolveTask.setFieldDeserializer(this.getFieldDeserializer("media"));
                    parser.setResolveStatus(DefaultJSONParser.NONE);
                }
            }
            if (matchedCount <= 0 || lexer.token() != JSONToken.RBRACE) {
                restFlag = true;
            } else if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken();
            }
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 2;
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
            instance.setImages(images_gen);
            
        }
        if ((_asm_flag_0 & 2) != 0) {
            instance.setMedia(media_gen);
            
        }
        
        if (restFlag) {
            return super.parseRest(parser, type, fieldName, instance);
        }
        
        return instance;
        
    }
}

