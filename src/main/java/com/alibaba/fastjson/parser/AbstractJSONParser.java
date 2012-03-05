package com.alibaba.fastjson.parser;

import static com.alibaba.fastjson.parser.JSONToken.EOF;
import static com.alibaba.fastjson.parser.JSONToken.FALSE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_FLOAT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_INT;
import static com.alibaba.fastjson.parser.JSONToken.LITERAL_STRING;
import static com.alibaba.fastjson.parser.JSONToken.NEW;
import static com.alibaba.fastjson.parser.JSONToken.NULL;
import static com.alibaba.fastjson.parser.JSONToken.RBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.SET;
import static com.alibaba.fastjson.parser.JSONToken.TREE_SET;
import static com.alibaba.fastjson.parser.JSONToken.TRUE;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public abstract class AbstractJSONParser {

    @SuppressWarnings("rawtypes")
    public Object parseObject(final Map object) {
        return parseObject(object, null);
    }

    @SuppressWarnings("rawtypes")
    public abstract Object parseObject(final Map object, Object fieldName);
    
    @SuppressWarnings("rawtypes")
    public abstract void checkListResolve(Collection array);

    public JSONObject parseObject() {
        JSONObject object = new JSONObject();
        parseObject(object);
        return object;
    }
    
    @SuppressWarnings("rawtypes")
    public final void parseArray(final Collection array) {
        parseArray(array, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void parseArray(final Collection array, Object fieldName) {
        final JSONLexer lexer = getLexer();
        
        if (lexer.token() == JSONToken.SET || lexer.token() == JSONToken.TREE_SET) {
            lexer.nextToken();
        }

        if (lexer.token() != JSONToken.LBRACKET) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(lexer.token()));
        }

        lexer.nextToken(JSONToken.LITERAL_STRING);

        for (;;) {
            if (isEnabled(Feature.AllowArbitraryCommas)) {
                while (lexer.token() == JSONToken.COMMA) {
                    lexer.nextToken();
                    continue;
                }
            }

            Object value;
            switch (lexer.token()) {
                case LITERAL_INT:
                    value = lexer.integerValue();
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                case LITERAL_FLOAT:
                    if (lexer.isEnabled(Feature.UseBigDecimal)) {
                        value = lexer.decimalValue(true);
                    } else {
                        value = lexer.decimalValue(false);
                    }
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                case LITERAL_STRING:
                    String stringLiteral = lexer.stringVal();
                    lexer.nextToken(JSONToken.COMMA);

                    if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                        JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            value = iso8601Lexer.getCalendar().getTime();
                        } else {
                            value = stringLiteral;
                        }
                    } else {
                        value = stringLiteral;
                    }

                    break;
                case TRUE:
                    value = Boolean.TRUE;
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                case FALSE:
                    value = Boolean.FALSE;
                    lexer.nextToken(JSONToken.COMMA);
                    break;
                case LBRACE:
                    JSONObject object = new JSONObject();
                    value = parseObject(object);
                    break;
                case LBRACKET:
                    Collection items = new JSONArray();
                    parseArray(items);
                    value = items;
                    break;
                case NULL:
                    value = null;
                    lexer.nextToken(JSONToken.LITERAL_STRING);
                    break;
                case RBRACKET:
                    lexer.nextToken(JSONToken.COMMA);
                    return;
                default:
                    value = parse();
                    break;
            }

            array.add(value);
            checkListResolve(array);

            if (lexer.token() == JSONToken.COMMA) {
                lexer.nextToken(JSONToken.LITERAL_STRING);
                continue;
            }
        }
    }

    public Object parse() {
        return parse(null);
    }

    public Object parse(Object fieldName) {
        final JSONLexer lexer = getLexer();
        switch (lexer.token()) {
            case SET:
                lexer.nextToken();
                HashSet<Object> set = new HashSet<Object>();
                parseArray(set, fieldName);
                return set;
            case TREE_SET:
                lexer.nextToken();
                TreeSet<Object> treeSet = new TreeSet<Object>();
                parseArray(treeSet, fieldName);
                return treeSet;
            case LBRACKET:
                JSONArray array = new JSONArray();
                parseArray(array, fieldName);
                return array;
            case LBRACE:
                JSONObject object = new JSONObject();
                return parseObject(object, fieldName);
            case LITERAL_INT:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue;
            case LITERAL_FLOAT:
                Object value = lexer.decimalValue(isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return value;
            case LITERAL_STRING:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(JSONToken.COMMA);

                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                    if (iso8601Lexer.scanISO8601DateIfMatch()) {
                        return iso8601Lexer.getCalendar().getTime();
                    }
                }

                return stringLiteral;
            case NULL:
                lexer.nextToken();
                return null;
            case TRUE:
                lexer.nextToken();
                return Boolean.TRUE;
            case FALSE:
                lexer.nextToken();
                return Boolean.FALSE;
            case NEW:
                lexer.nextToken(JSONToken.IDENTIFIER);

                if (lexer.token() != JSONToken.IDENTIFIER) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(JSONToken.LPAREN);

                accept(JSONToken.LPAREN);
                long time = ((Number) lexer.integerValue()).longValue();
                accept(JSONToken.LITERAL_INT);

                accept(JSONToken.RPAREN);

                return new Date(time);
            case EOF:
                if (lexer.isBlankInput()) {
                    return null;
                }
            default:
                throw new JSONException("TODO " + lexer.tokenName() + " " + lexer.stringVal());
        }
    }

    public void config(Feature feature, boolean state) {
        getLexer().config(feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return getLexer().isEnabled(feature);
    }

    public abstract JSONLexer getLexer();

    public final void accept(final int token) {
        final JSONLexer lexer = getLexer();
        if (lexer.token() == token) {
            lexer.nextToken();
        } else {
            throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual "
                                    + JSONToken.name(lexer.token()));
        }
    }

    public void close() {
        final JSONLexer lexer = getLexer();

        if (isEnabled(Feature.AutoCloseSource)) {
            if (!lexer.isEOF()) {
                throw new JSONException("not close json text, token : " + JSONToken.name(lexer.token()));
            }
        }
    }
}
