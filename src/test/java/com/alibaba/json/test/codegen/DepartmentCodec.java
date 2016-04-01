package com.alibaba.json.test.codegen;

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

public class DepartmentCodec extends ASMJavaBeanDeserializer implements ObjectDeserializer {
    private char[] name_gen_prefix__ = "\"name\":".toCharArray();
    private char[] root_gen_prefix__ = "\"root\":".toCharArray();
    private char[] type_gen_prefix__ = "\"type\":".toCharArray();
    private char[] id_gen_prefix__ = "\"id\":".toCharArray();
    private char[] leader_gen_prefix__ = "\"leader\":".toCharArray();
    private char[] members_gen_prefix__ = "\"members\":".toCharArray();
    
    private ObjectDeserializer name_gen_deser__;
    private ObjectDeserializer leader_gen_deser__;
    private ObjectDeserializer members_gen_list_item_deser__;
    private Type members_gen_list_item_type__ = com.alibaba.json.test.codegen.Employee.class;
    
    public DepartmentCodec (ParserConfig config, Class clazz) {
        super(config, clazz);
        
    }
    
    public Object createInstance(DefaultJSONParser parser, Type type) {
        return new Department();
        
    }
    public Object deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = (JSONLexerBase) parser.getLexer();
        
        if (lexer.isEnabled(Feature.SortFeidFastMatch)) {
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
        Department instance = new Department();
        
        ParseContext context = parser.getContext();
        ParseContext childContext = parser.setContext(context, instance, fieldName);
        
        if (lexer.matchStat == JSONLexerBase.END) {
            return instance;
        }
        
        int matchStat = 0;
        int _asm_flag_0 = 0;
        int id_gen = 0;
        com.alibaba.json.test.codegen.Employee leader_gen = null;
        java.util.List members_gen = null;
        String name_gen;
        if (lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
            name_gen = lexer.stringDefaultValue();
            _asm_flag_0 |= 8;
        } else {
            name_gen = null;
        }
        boolean root_gen = false;
        com.alibaba.json.test.codegen.DepartmentType type_gen = null;
        boolean endFlag = false, restFlag = false;
        
        if ((!endFlag) && (!restFlag)) {
            id_gen = lexer.scanFieldInt(id_gen_prefix__);
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
            if (lexer.matchField(leader_gen_prefix__)) {
                _asm_flag_0 |= 2;
                matchedCount++;
                if(parser.getResolveStatus() == DefaultJSONParser.NeedToResolve) {
                    ResolveTask resolveTask = parser.getLastResolveTask();
                    resolveTask.ownerContext = parser.getContext();
                    resolveTask.fieldDeserializer = this.getFieldDeserializer("leader");
                    parser.setResolveStatus(DefaultJSONParser.NONE);
                }
            }
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
            if (lexer.matchField(members_gen_prefix__)) {
                _asm_flag_0 |= 4;
                if (lexer.token() == JSONToken.NULL) {
                    lexer.nextToken(JSONToken.COMMA);
                } else {
                    if (lexer.token() == JSONToken.LBRACKET) {
                        if(members_gen_list_item_deser__ == null) {
                            members_gen_list_item_deser__ = parser.getConfig().getDeserializer(com.alibaba.json.test.codegen.Employee.class);
                        }
                        final int fastMatchToken = members_gen_list_item_deser__.getFastMatchToken();
                        lexer.nextToken(fastMatchToken);
                        members_gen = new java.util.ArrayList();
                        ParseContext listContext = parser.getContext();
                        parser.setContext(members_gen, "members");
            
                        for(int i = 0; ;++i) {
                            if (lexer.token() == JSONToken.RBRACKET) {
                                break;
                            }
                            com.alibaba.json.test.codegen.Employee itemValue = members_gen_list_item_deser__.deserialze(parser, members_gen_list_item_type__, i);
                            members_gen.add(itemValue);
                            parser.checkListResolve(members_gen);
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
            name_gen = lexer.scanFieldString(name_gen_prefix__);
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
            root_gen = lexer.scanFieldBoolean(root_gen_prefix__);
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
            String type_gen_enum_name = lexer.scanFieldSymbol(type_gen_prefix__, parser.getSymbolTable());
            if (type_gen_enum_name == null) {
                type_gen = com.alibaba.json.test.codegen.DepartmentType.valueOf(type_gen_enum_name);
            }
            if(lexer.matchStat > 0) {
                _asm_flag_0 |= 32;
                matchedCount++;
            }
            if(lexer.matchStat == JSONLexerBase.NOT_MATCH) {
                restFlag = true;
            }
            if(lexer.matchStat != JSONLexerBase.END) {
                restFlag = true;
            }
            
        }
        
        if (restFlag) {
            return super.parseRest(parser, type, fieldName, instance);
        }
        
        return instance;
        
    }
}

