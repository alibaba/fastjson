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

    public final static char[]    digits                     = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F'                         };

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
        specicalFlags_doubleQuotes['\0'] = true;
        specicalFlags_doubleQuotes['\1'] = true;
        specicalFlags_doubleQuotes['\2'] = true;
        specicalFlags_doubleQuotes['\3'] = true;
        specicalFlags_doubleQuotes['\4'] = true;
        specicalFlags_doubleQuotes['\5'] = true;
        specicalFlags_doubleQuotes['\6'] = true;
        specicalFlags_doubleQuotes['\7'] = true;
        specicalFlags_doubleQuotes['\b'] = true; // 8
        specicalFlags_doubleQuotes['\t'] = true; // 9
        specicalFlags_doubleQuotes['\n'] = true; // 10
        specicalFlags_doubleQuotes['\u000B'] = true; // 11
        specicalFlags_doubleQuotes['\f'] = true;
        specicalFlags_doubleQuotes['\r'] = true;
        specicalFlags_doubleQuotes['\"'] = true;
        specicalFlags_doubleQuotes['\\'] = true;

        specicalFlags_singleQuotes['\0'] = true;
        specicalFlags_singleQuotes['\1'] = true;
        specicalFlags_singleQuotes['\2'] = true;
        specicalFlags_singleQuotes['\3'] = true;
        specicalFlags_singleQuotes['\4'] = true;
        specicalFlags_singleQuotes['\5'] = true;
        specicalFlags_singleQuotes['\6'] = true;
        specicalFlags_singleQuotes['\7'] = true;
        specicalFlags_singleQuotes['\b'] = true; // 8
        specicalFlags_singleQuotes['\t'] = true; // 9
        specicalFlags_singleQuotes['\n'] = true; // 10
        specicalFlags_singleQuotes['\u000B'] = true; // 11
        specicalFlags_singleQuotes['\f'] = true; // 12
        specicalFlags_singleQuotes['\r'] = true;
        specicalFlags_singleQuotes['\''] = true;
        specicalFlags_singleQuotes['\\'] = true;

        replaceChars['\0'] = '0';
        replaceChars['\1'] = '1';
        replaceChars['\2'] = '2';
        replaceChars['\3'] = '3';
        replaceChars['\4'] = '4';
        replaceChars['\5'] = '5';
        replaceChars['\6'] = '6';
        replaceChars['\7'] = '7';
        replaceChars['\b'] = 'b'; // 8
        replaceChars['\t'] = 't'; // 9
        replaceChars['\n'] = 'n'; // 10
        replaceChars['\u000B'] = 'v'; // 11
        replaceChars['\f'] = 'f'; // 12
        replaceChars['\r'] = 'r'; // 13
        replaceChars['\"'] = '"'; // 34
        replaceChars['\''] = '\''; // 39
        replaceChars['/'] = '/'; // 47
        replaceChars['\\'] = '\\'; // 92
    }

    public final static char[] ASCII_CHARS  = { '0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0', '5', '0', '6',
            '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F', '1', '0', '1',
            '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1', 'A', '1', 'B',
            '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4', '2', '5', '2',
            '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2', 'F', };

}
