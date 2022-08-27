package com.alibaba.json.demo;

import junit.framework.TestCase;

/**
 * Created by wenshao on 10/03/2017.
 */
public class Forguard extends TestCase {

    public void test_0() throws Exception {
        String json = "{\"id\":\"a123\", \"name\":\"wxf\"}";

        String value = javaGet(json, "id");
        System.out.println(value);
    }

    public static String javaGet(String json, String key) {
        char[] json_chars = json.toCharArray();
        char[] key_chars = key.toCharArray();

        char[] value_chars = get(json_chars, json_chars.length, key_chars, key_chars.length);

        return new String(value_chars);
    }

    public static char[] get(char[] json, int json_len, char[] key, int key_len) {
        if (json_len == 0) {
            return new char[0];
        }

        Parser parser = new Parser();
        parser.json_chars = json;
        parser.json_len = json_len;
        parser.ch = json[0];
        next_token(parser);

        if (parser.token != Token.LBRACE) {
            throw new IllegalArgumentException("illegal json");
        }
        next_token(parser);

        for (;;) {
            if (parser.token == Token.RBRACE) {
                break;
            }

            if (parser.token != Token.STRING) {
                throw new IllegalArgumentException("illegal json");
            }

            char[] name_chars = parser.str_chars;
            int name_len = parser.str_chars_len;
            next_token(parser);

            if (parser.token != Token.COLON) {
                throw new IllegalArgumentException("illegal json");
            }
            next_token(parser);

            if (parser.token != Token.STRING) {
                throw new IllegalArgumentException("illegal json");
            }

            if (name_len == key_len) {
                boolean eq = true;
                for (int i = 0; i < name_len; ++i) {
                    if (name_chars[i] != key[i]) {
                        eq = false;
                        break;
                    }
                }
                if (eq) {
                    return parser.str_chars;
                }
            }

            next_token(parser);

            if (parser.token == Token.COMMA) {
                next_token(parser);
                continue;
            }

        }

        return null;
    }

    public static class Parser {
        public char[] json_chars;
        public int json_len;

        public char[] str_chars;
        public int str_chars_len;

        public char ch;
        public int  pos;
        public Token token;
    }

    public static void next_char(Parser parser) {
        parser.ch = (++parser.pos) < parser.json_len ? parser.json_chars[parser.pos] : '\0';
    }

    public static void scanString(Parser parser) {
        next_char(parser);

        int start = parser.pos;
        int end;
        for (;;) {
            if (parser.pos >= parser.json_len) {
                throw new IllegalArgumentException("illegal string");
            }
            if (parser.ch == '"') {
                end = parser.pos;
                next_char(parser);
                break;
            }

            if (parser.ch == '\\') {
                throw new IllegalArgumentException("illegal string");
            }
            next_char(parser);
        }

        parser.str_chars_len = end - start;
        parser.str_chars = new char[parser.str_chars_len];
        for (int i = 0; i < parser.str_chars_len; ++i) {
            parser.str_chars[i] = parser.json_chars[start + i];
        }
        parser.token = Token.STRING;
    }

    public static void next_token(Parser parser) {
        for (;;) {
            int ch = parser.ch;
            boolean isWhiteSpace = ch == '\n' || ch == '\r' || ch == ' ' || ch == '\t';
            if (isWhiteSpace) {
                next_char(parser);
                continue;
            }
            if (parser.pos >= parser.json_len) {
                parser.token = Token.EOF;
                return;
            }
            break;
        }

        switch (parser.ch) {
            case '{':
                parser.token = Token.LBRACE;
                next_char(parser);
                break;
            case '}':
                parser.token = Token.RBRACE;
                next_char(parser);
                break;
            case ',':
                parser.token = Token.COMMA;
                next_char(parser);
                break;
            case ':':
                parser.token = Token.COLON;
                next_char(parser);
                break;
            case '"':
                scanString(parser);
                break;
            default:
                throw new IllegalArgumentException("illegal json char");
        }
    }

    public static enum Token {
        STRING, //
        EOF, //
        LBRACE,
        RBRACE,
        COMMA,
        COLON
    }
}
