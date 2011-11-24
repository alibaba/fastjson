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
 * @author wenshao<szujobs@hotmail.com>
 */
public final class CharTypes {

    public final static boolean[] firstIdentifierFlags       = new boolean[256];
    static {
        for (char c = 0; c < firstIdentifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                firstIdentifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                firstIdentifierFlags[c] = true;
            } else if (c == '_') {
                firstIdentifierFlags[c] = true;
            }
        }
    }

    public final static boolean[] identifierFlags            = new boolean[256];

    static {
        for (char c = 0; c < identifierFlags.length; ++c) {
            if (c >= 'A' && c <= 'Z') {
                identifierFlags[c] = true;
            } else if (c >= 'a' && c <= 'z') {
                identifierFlags[c] = true;
            } else if (c == '_') {
                identifierFlags[c] = true;
            } else if (c >= '0' && c <= '9') {
                identifierFlags[c] = true;
            }
        }
    }

    public final static boolean[] specicalFlags_doubleQuotes = new boolean[128];
    public final static boolean[] specicalFlags_singleQuotes = new boolean[128];

    public static boolean isSpecial_doubleQuotes(char ch) {
        return ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch];
    }

    public final static char[] replaceChars = new char[128];
    static {
        specicalFlags_doubleQuotes['\b'] = true;
        specicalFlags_doubleQuotes['\n'] = true;
        specicalFlags_doubleQuotes['\f'] = true;
        specicalFlags_doubleQuotes['\r'] = true;
        specicalFlags_doubleQuotes['\"'] = true;
        specicalFlags_doubleQuotes['\\'] = true;
        specicalFlags_doubleQuotes['\u000B'] = true;

        specicalFlags_singleQuotes['\b'] = true;
        specicalFlags_singleQuotes['\n'] = true;
        specicalFlags_singleQuotes['\f'] = true;
        specicalFlags_singleQuotes['\r'] = true;
        specicalFlags_singleQuotes['\''] = true;
        specicalFlags_singleQuotes['\\'] = true;
        specicalFlags_singleQuotes['\u000B'] = true;
        
        replaceChars['\b'] = 'b';
        replaceChars['\n'] = 'n';
        replaceChars['\f'] = 'f';
        replaceChars['\r'] = 'r';
        replaceChars['\"'] = '"';
        replaceChars['\''] = '\'';
        replaceChars['\\'] = '\\';
        replaceChars['\t'] = 't';
        replaceChars['/'] = '/';
        replaceChars['\u000B'] = 'v';
    }

    public final static char [] ASCII_CHARS = {
                                      '0', '0', '0', '1', '0', '2', '0', '3', 
                                      '0', '4', '0', '5', '0', '6', '0', '7', 
                                      '0', '8', '0', '9', '0', 'A', '0', 'B',
                                      '0', 'C', '0', 'D', '0', 'E', '0', 'F',
                                      '1', '0', '1', '1', '1', '2', '1', '3', 
                                      '1', '4', '1', '5', '1', '6', '1', '7', 
                                      '1', '8', '1', '9', '1', 'A', '1', 'B',
                                      '1', 'C', '1', 'D', '1', 'E', '1', 'F',
                                      } ; 
    
    public final static boolean isEmoji(char ch) {
        if (ch >= '\uE001' && ch <= '\uE05A') {
            return true;
        }
        
        if (ch >= '\uE101' && ch <= '\uE15A') {
            return true;
        }
        
        if (ch >= '\uE201' && ch <= '\uE253') {
            return true;
        }
        
        if (ch >= '\uE401' && ch <= '\uE44C') {
            return true;
        }
        
        if (ch >= '\uE501' && ch <= '\uE437') {
            return true;
        }
        
        return false;
    }
}
