package com.alibaba.fastjson.parser.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.SymbolTable;

public class JSONScannerField extends JSONScanner{
	private String text;
	 
	public JSONScannerField(String input){
       super(input);
    }

    public JSONScannerField(char[] input, int inputLength){
        super(input, inputLength);
    }

    public JSONScannerField(char[] input, int inputLength, int features){
        super(input, inputLength, features);
    }
	
	public JSONScannerField(String input, int features) {
		super(input, features);
	}
	
	public int scanFieldInt(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        int value;
        if (ch >= '0' && ch <= '9') {
            value = digits[ch];
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + digits[ch];
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    bp = index - 1;
                    break;
                }
            }
            if (value < 0) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        }

        return value;
    }

    public String scanFieldString(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        // final int fieldNameLength = fieldName.length;
        // for (int i = 0; i < fieldNameLength; ++i) {
        // if (fieldName[i] != buf[bp + i]) {
        // matchStat = NOT_MATCH_NAME;
        //
        // return stringDefaultValue();
        // }
        // }

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return stringDefaultValue();
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);
        if (ch != '"') {
            matchStat = NOT_MATCH;

            return stringDefaultValue();
        }

        boolean hasSpecial = false;
        final String strVal;
        {
            int startIndex = index;
            int endIndex = text.indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            String stringVal = subString(startIndex, endIndex - startIndex);
            for (int i = 0; i < stringVal.length(); ++i) {
                if (stringVal.charAt(i) == '\\') {
                    hasSpecial = true;
                    break;
                }
            }

            if (hasSpecial) {
                matchStat = NOT_MATCH;

                return stringDefaultValue();
            }

            bp = endIndex + 1;
            this.ch = ch = charAt(bp);
            strVal = stringVal;
            // this.stringVal = stringVal;
            // int pos = endIndex + 1;
            // char ch = charAt(pos);
            // if (ch != '\'') {
            // this.pos = pos;
            // this.ch = ch;
            // token = LITERAL_CHARS;
            // return;
            // }
        }

        // final int start = index;
        // for (;;) {
        // ch = charAt(index++);
        // if (ch == '\"') {
        // bp = index;
        // this.ch = ch = charAt(bp);
        // strVal = text.substring(start, index - 1);
        // // strVal = new String(buf, start, index - start - 1);
        // break;
        // }
        //
        // if (ch == '\\') {
        // matchStat = NOT_MATCH;
        //
        // return stringDefaultValue();
        // }
        // }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return strVal;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return stringDefaultValue();
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;

            return stringDefaultValue();
        }

        return strVal;
    }

    public String scanFieldSymbol(char[] fieldName, final SymbolTable symbolTable) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);
        if (ch != '"') {
            matchStat = NOT_MATCH;
            return null;
        }

        String strVal;
        int start = index;
        int hash = 0;
        for (;;) {
            ch = charAt(index++);
            if (ch == '\"') {
                bp = index;
                this.ch = ch = charAt(bp);
                // strVal = text.substring(start, index - 1).intern();
                strVal = symbolTable.addSymbol(text, start, index - start - 1, hash);
                break;
            }

            hash = 31 * hash + ch;

            if (ch == '\\') {
                matchStat = NOT_MATCH;
                return null;
            }
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return strVal;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return strVal;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        Collection<String> list;

        if (type.isAssignableFrom(HashSet.class)) {
            list = new HashSet<String>();
        } else if (type.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList<String>();
        } else {
            try {
                list = (Collection<String>) type.newInstance();
            } catch (Exception e) {
                throw new JSONException(e.getMessage(), e);
            }
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        if (ch != '[') {
            matchStat = NOT_MATCH;
            return null;
        }

        ch = charAt(index++);

        for (;;) {
            if (ch != '"') {
                matchStat = NOT_MATCH;
                return null;
            }

            String strVal;
            int start = index;
            for (;;) {
                ch = charAt(index++);
                if (ch == '\"') {
                    strVal = text.substring(start, index - 1);
                    // strVal = new String(buf, start, index - start - 1);
                    list.add(strVal);
                    ch = charAt(index++);
                    break;
                }

                if (ch == '\\') {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }

            if (ch == ',') {
                ch = charAt(index++);
                continue;
            }

            if (ch == ']') {
                ch = charAt(index++);
                break;
            }

            matchStat = NOT_MATCH;
            return null;
        }

        bp = index;
        if (ch == ',') {
            this.ch = charAt(bp);
            matchStat = VALUE;
            return list;
        } else if (ch == '}') {
            ch = charAt(bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
                this.ch = ch;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return list;
    }

    public long scanFieldLong(char[] fieldName) {
        matchStat = UNKOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        long value;
        if (ch >= '0' && ch <= '9') {
            value = digits[ch];
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + digits[ch];
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    bp = index - 1;
                    break;
                }
            }
            if (value < 0) {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
        } else {
            this.bp = startPos;
            this.ch = startChar;
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return value;
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        matchStat = UNKOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return false;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        boolean value;
        if (ch == 't') {
            if (charAt(index++) != 'r') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'u') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = true;
        } else if (ch == 'f') {
            if (charAt(index++) != 'a') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'l') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 's') {
                matchStat = NOT_MATCH;
                return false;
            }
            if (charAt(index++) != 'e') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = false;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
        } else if (ch == '}') {
            ch = charAt(++bp);
            if (ch == ',') {
                token = JSONToken.COMMA;
                this.ch = charAt(++bp);
            } else if (ch == ']') {
                token = JSONToken.RBRACKET;
                this.ch = charAt(++bp);
            } else if (ch == '}') {
                token = JSONToken.RBRACE;
                this.ch = charAt(++bp);
            } else if (ch == EOI) {
                token = JSONToken.EOF;
            } else {
                matchStat = NOT_MATCH;
                return false;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        return value;
    }


}
