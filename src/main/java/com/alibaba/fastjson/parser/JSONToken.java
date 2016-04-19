/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.parser;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class JSONToken {

    //
    public final static int ERROR                = 1;
    //
    public final static int LITERAL_INT          = 2;
    //
    public final static int LITERAL_FLOAT        = 3;
    //
    public final static int LITERAL_STRING       = 4;
    //
    public final static int LITERAL_ISO8601_DATE = 5;

    public final static int TRUE                 = 6;
    //
    public final static int FALSE                = 7;
    //
    public final static int NULL                 = 8;
    //
    public final static int NEW                  = 9;
    //
    public final static int LPAREN               = 10; // ("("),
    //
    public final static int RPAREN               = 11; // (")"),
    //
    public final static int LBRACE               = 12; // ("{"),
    //
    public final static int RBRACE               = 13; // ("}"),
    //
    public final static int LBRACKET             = 14; // ("["),
    //
    public final static int RBRACKET             = 15; // ("]"),
    //
    public final static int COMMA                = 16; // (","),
    //
    public final static int COLON                = 17; // (":"),
    //
    public final static int IDENTIFIER           = 18;
    //
    public final static int FIELD_NAME           = 19;

    public final static int EOF                  = 20;

    public final static int SET                  = 21;
    public final static int TREE_SET             = 22;
    
    public final static int UNDEFINED            = 23; // undefined

    public static String name(int value) {
        switch (value) {
            case ERROR:
                return "error";
            case LITERAL_INT:
                return "int";
            case LITERAL_FLOAT:
                return "float";
            case LITERAL_STRING:
                return "string";
            case LITERAL_ISO8601_DATE:
                return "iso8601";
            case TRUE:
                return "true";
            case FALSE:
                return "false";
            case NULL:
                return "null";
            case NEW:
                return "new";
            case LPAREN:
                return "(";
            case RPAREN:
                return ")";
            case LBRACE:
                return "{";
            case RBRACE:
                return "}";
            case LBRACKET:
                return "[";
            case RBRACKET:
                return "]";
            case COMMA:
                return ",";
            case COLON:
                return ":";
            case IDENTIFIER:
                return "ident";
            case FIELD_NAME:
                return "fieldName";
            case EOF:
                return "EOF";
            case SET:
                return "Set";
            case TREE_SET:
                return "TreeSet";
            case UNDEFINED:
                return "undefined";
            default:
                return "Unknown";
        }
    }
}
