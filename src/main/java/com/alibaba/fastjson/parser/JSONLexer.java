package com.alibaba.fastjson.parser;

import static com.alibaba.fastjson.parser.JSONToken.COLON;
import static com.alibaba.fastjson.parser.JSONToken.COMMA;
import static com.alibaba.fastjson.parser.JSONToken.EOF;
import static com.alibaba.fastjson.parser.JSONToken.ERROR;
import static com.alibaba.fastjson.parser.JSONToken.LBRACE;
import static com.alibaba.fastjson.parser.JSONToken.LBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.LPAREN;
import static com.alibaba.fastjson.parser.JSONToken.RBRACE;
import static com.alibaba.fastjson.parser.JSONToken.RBRACKET;
import static com.alibaba.fastjson.parser.JSONToken.RPAREN;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

public final class JSONLexer {

    public final static char EOI            = 0x1A;
    public final static int  NOT_MATCH      = -1;
    public final static int  NOT_MATCH_NAME = -2;
    public final static int  UNKNOWN        = 0;
    public final static int  VALUE          = 3;
    public final static int  END            = 4;

    private static boolean   V6;                   // android 6
    static {
        int version = -1;

        try {
            Class<?> clazz = Class.forName("android.os.Build$VERSION");
            Field field = clazz.getField("SDK_INT");
            version = field.getInt(null);
        } catch (Exception e) {
            // skip
        }

        V6 = version >= 23;
    }

    protected int                            token;
    protected int                            pos;
    public int                               features  = JSON.DEFAULT_PARSER_FEATURE;

    protected char                           ch;
    protected int                            bp;

    protected int                            eofPos;

    /**
     * A character buffer for literals.
     */
    protected char[]                         sbuf;
    protected int                            sp;
    protected boolean                        exp = false;
    protected boolean                        isDouble = false;

    /**
     * number start position
     */
    protected int                            np;

    protected boolean                        hasSpecial;

    public TimeZone                          timeZone  = JSON.defaultTimeZone;
    public Locale                            locale    = JSON.defaultLocale;
    public Calendar                          calendar  = null;

    public int                               matchStat = UNKNOWN;

    private final static ThreadLocal<char[]> sbufLocal = new ThreadLocal<char[]>();

    protected final String                   text;
    protected final int                      len;
    
    protected String                         stringDefaultValue;
    public boolean                           disableCircularReferenceDetect;

    protected long                           fieldHash;

    public JSONLexer(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] input, int inputLength){
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONLexer(char[] input, int inputLength, int features){
        this(new String(input, 0, inputLength), features);
    }

    public JSONLexer(String input, int features){
        sbuf = sbufLocal.get();

        if (sbuf == null) {
            sbuf = new char[512];
        }

        this.features = features;

        text = input;
        len = text.length();
        bp = -1;

        // next();
        {
            int index = ++bp;
            this.ch = (index >= this.len ? //
                EOI //
                : text.charAt(index));
        }
        if (ch == 65279) {
            next();
        }
        
        stringDefaultValue = (features & Feature.InitStringFieldAsEmpty.mask) != 0 //
            ? "" //
            : null;
        disableCircularReferenceDetect = (features & Feature.DisableCircularReferenceDetect.mask) != 0;
    }

    public final int token() {
        return token;
    }

    public void close() {
        if (sbuf.length <= 8196) {
            sbufLocal.set(sbuf);
        }
        this.sbuf = null;
    }

    public char next() {
        int index = ++bp;
        return ch = (index >= this.len ? //
            EOI //
            : text.charAt(index));
    }

    public final void config(Feature feature, boolean state) {
        if (state) {
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }
        
        if (feature == Feature.InitStringFieldAsEmpty) {
            stringDefaultValue = state //
                ? "" //
                : null;
        }
        disableCircularReferenceDetect = (features & Feature.DisableCircularReferenceDetect.mask) != 0;
    }

    public final boolean isEnabled(Feature feature) {
        return (features & feature.mask) != 0;
    }

    public final void nextTokenWithChar(char expect) {
        sp = 0;

        for (;;) {
            if (ch == expect) {
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
                nextToken();
                return;
            }

            if (ch == ' ' //
                || ch == '\n' //
                || ch == '\r'//
                || ch == '\t'//
                || ch == '\f'//
                || ch == '\b') {
                next();
                continue;
            }

            throw new JSONException("not match " + expect + " - " + ch);
        }
    }

    public final String numberString() {
        int index = np + sp - 1;
        char chLocal = text.charAt(index);

        int sp = this.sp;
        if (chLocal == 'L' //
            || chLocal == 'S' //
            || chLocal == 'B' //
            || chLocal == 'F' //
            || chLocal == 'D') {
            sp--;
        }

        // return text.substring(np, np + sp);
        return this.subString(np, sp);
    }

    protected char charAt(int index) {
        return index >= this.len ? //
            EOI //
            : text.charAt(index);
    }

    public final void nextToken() {
        sp = 0;

        for (;;) {
            pos = bp;

            if (ch == '/') {
                skipComment();
                continue;
            }

            if (ch == '"') {
                scanString();
                return;
            }

            if ((ch >= '0' && ch <= '9') || ch == '-') {
                scanNumber();
                return;
            }

            if (ch == ',') {
                next();
                token = COMMA;
                return;
            }

            switch (ch) {
                case '\'':
                    scanString();
                    return;
                case ' ':
                case '\t':
                case '\b':
                case '\f':
                case '\n':
                case '\r':
                    next();
                    break;
                case 't': // true
                {
                    if (text.startsWith("true", bp)) {
                        bp += 4;
                        ch = charAt(bp);

                        if (ch == ' ' //
                                || ch == ',' //
                                || ch == '}' //
                                || ch == ']' //
                                || ch == '\n' //
                                || ch == '\r' //
                                || ch == '\t' //
                                || ch == EOI //
                                || ch == '\f' //
                                || ch == '\b' //
                                || ch == ':') {
                            token = JSONToken.TRUE;
                            return;
                        }
                    }
                    throw new JSONException("scan true error");
                }
                case 'T': // treeSet
                case 'S': // set
                case 'u': //undefined
                    scanIdent();
                    return;
                case 'f': // false
                {
                    if (text.startsWith("false", bp)) {
                        bp += 5;
                        ch = charAt(bp);

                        if (ch == ' '//
                                || ch == ','//
                                || ch == '}'//
                                || ch == ']'//
                                || ch == '\n'//
                                || ch == '\r'//
                                || ch == '\t'//
                                || ch == EOI//
                                || ch == '\f'//
                                || ch == '\b'//
                                || ch == ':') {
                            token = JSONToken.FALSE;
                            return;
                        }
                    }
                    throw new JSONException("scan false error");
                }
                case 'n': // new,null
                {
                    int token = 0;
                    if (text.startsWith("null", bp)) {
                        bp += 4;
                        token = JSONToken.NULL;
                    } else if (text.startsWith("new", bp)) {
                        bp += 3;
                        token = JSONToken.NEW;
                    }

                    if (token != 0) {
                        ch = charAt(bp);
                        if (ch == ' ' //
                                || ch == ',' //
                                || ch == '}' //
                                || ch == ']' //
                                || ch == '\n' //
                                || ch == '\r' //
                                || ch == '\t' //
                                || ch == EOI //
                                || ch == '\f' //
                                || ch == '\b') {
                            this.token = token;
                            return;
                        }
                    }

                    throw new JSONException("scan null/new error");
                }
                case '(':
                    next();
                    token = LPAREN;
                    return;
                case ')':
                    next();
                    token = RPAREN;
                    return;
                case '[':
                // next();
                {
                    int index = ++bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
                    token = LBRACKET;
                    return;
                case ']':
                    next();
                    token = RBRACKET;
                    return;
                case '{':
                // next();
                {
                    int index = ++bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
                    token = LBRACE;
                    return;
                case '}':
                // next();
                {
                    int index = ++bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
                    token = RBRACE;
                    return;
                case ':':
                    next();
                    token = COLON;
                    return;
                default:
                    boolean eof = (bp == len || ch == EOI && bp + 1 == len);
                    if (eof) { // JLS
                        if (token == EOF) {
                            throw new JSONException("EOF error");
                        }

                        token = EOF;
                        pos = bp = eofPos;
                    } else {
                        if (ch <= 31 || ch == 127) {
                            next();
                            break;
                        }
                        token = ERROR;
                        next();
                    }

                    return;
            }
        }

    }

    public final void nextToken(int expect) {
        sp = 0;

        for (;;) {
            switch (expect) {
                case JSONToken.LBRACE:
                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }
                    break;
                case JSONToken.COMMA:
                    if (ch == ',') {
                        token = JSONToken.COMMA;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }

                    if (ch == '}') {
                        token = JSONToken.RBRACE;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }

                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }

                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                case JSONToken.LITERAL_INT:
                    if (ch >= '0' && ch <= '9') {
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '"') {
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }

                    break;
                case JSONToken.LITERAL_STRING:
                    if (ch == '"') {
                        pos = bp;
                        scanString();
                        return;
                    }

                    if (ch >= '0' && ch <= '9') {
                        pos = bp;
                        scanNumber();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        // next();
                        {
                            int index = ++bp;
                            this.ch = (index >= this.len ? //
                                EOI //
                                : text.charAt(index));
                        }
                        return;
                    }
                    break;
                case JSONToken.LBRACKET:
                    if (ch == '[') {
                        token = JSONToken.LBRACKET;
                        next();
                        return;
                    }

                    if (ch == '{') {
                        token = JSONToken.LBRACE;
                        next();
                        return;
                    }
                    break;
                case JSONToken.RBRACKET:
                    if (ch == ']') {
                        token = JSONToken.RBRACKET;
                        next();
                        return;
                    }
                case JSONToken.EOF:
                    if (ch == EOI) {
                        token = JSONToken.EOF;
                        return;
                    }
                    break;
                case JSONToken.IDENTIFIER:
                    nextIdent();
                    return;
                default:
                    break;
            }

            if (ch == ' '//
                || ch == '\n'//
                || ch == '\r'//
                || ch == '\t'//
                || ch == '\f'//
                || ch == '\b') {
                next();
                continue;
            }

            nextToken();
            break;
        }
    }

    public final void nextIdent() {
        for (;;) {
            boolean whitespace = ch <= ' ' //
                                 && (ch == ' ' //
                                     || ch == '\n' //
                                     || ch == '\r' //
                                     || ch == '\t' //
                                     || ch == '\f' //
                                     || ch == '\b');
            if (!whitespace) {
                break;
            }
            next();
        }

        if (ch == '_' //
            || Character.isLetter(ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final Number integerValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        long limit;
        int digit;

        char type = ' ';

        int charIndex = max - 1;
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        switch (chLocal) {
            case 'L':
                max--;
                type = 'L';
                break;
            case 'S':
                max--;
                type = 'S';
                break;
            case 'B':
                max--;
                type = 'B';
                break;
            default:
                break;
        }

        chLocal = np >= this.len ? //
                EOI //
                : text.charAt(np);
        if (chLocal == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i++;
        } else {
            limit = -Long.MAX_VALUE;
        }
        if (i < max) {
            charIndex = i++;
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
            digit = chLocal - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            charIndex = i++;
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
            digit = chLocal - '0';
            if (result < -922337203685477580L) { // Long.MIN_VALUE / 10
                return new BigInteger(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                return new BigInteger(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                if (result >= Integer.MIN_VALUE && type != 'L') {
                    if (type == 'S') {
                        return (short) result;
                    }

                    if (type == 'B') {
                        return (byte) result;
                    }

                    return (int) result;
                }
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            result = -result;
            if (result <= Integer.MAX_VALUE && type != 'L') {
                if (type == 'S') {
                    return (short) result;
                } else if (type == 'B') {
                    return (byte) result;
                } else {
                    return (int) result;
                }
            }
            return result;
        }
    }

    public final String scanSymbol(final SymbolTable symbolTable) {
        for (;;) {
            if (ch == ' ' //
                || ch == '\n' //
                || ch == '\r' //
                || ch == '\t' //
                || ch == '\f' //
                || ch == '\b') {
                next();
            } else {
                break;
            }
        }

        if (ch == '"') {
            return scanSymbol(symbolTable, '"');
        }

        if (ch == '\'') {
            return scanSymbol(symbolTable, '\'');
        }

        if (ch == '}') {
            next();
            token = JSONToken.RBRACE;
            return null;
        }

        if (ch == ',') {
            next();
            token = JSONToken.COMMA;
            return null;
        }

        if (ch == EOI) {
            token = JSONToken.EOF;
            return null;
        }

        return scanSymbolUnQuoted(symbolTable);
    }

    public String scanSymbol(final SymbolTable symbolTable, char quoteChar) {
        int hash = 0;

        boolean hasSpecial = false;
        int startIndex = bp + 1;
        int endIndex = text.indexOf(quoteChar, startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str, " + info());
        }

        int chars_len;
        char[] chars;

        chars_len = endIndex - startIndex;
        chars = sub_chars(bp + 1, chars_len);
        while ((chars_len > 0 //
                && chars[chars_len - 1] == '\\')) {

            int slashCount = 1;
            for (int i = chars_len - 2; i >= 0; --i) {
                if (chars[i] == '\\') {
                    slashCount++;
                } else {
                    break;
                }
            }
            if (slashCount % 2 == 0) {
                break;
            }

            int nextIndex = text.indexOf(quoteChar, endIndex + 1);
            int nextLen = nextIndex - endIndex;
            int next_chars_len = chars_len + nextLen;

            if (next_chars_len >= chars.length) {
                int newLen = chars.length * 3 /2;
                if (newLen < next_chars_len) {
                    newLen = next_chars_len;
                }
                char[] newChars = new char[newLen];
                System.arraycopy(chars, 0, newChars, 0, chars.length);
                chars = newChars;
            }
            text.getChars(endIndex, nextIndex, chars, chars_len);
            
            chars_len = next_chars_len;
            endIndex = nextIndex;
            hasSpecial = true;
        }

        final String strVal;
        if (!hasSpecial) {
            for (int i = 0; i < chars_len; ++i) {
                char ch = chars[i];
                hash = 31 * hash + ch;
                if (ch == '\\') {
                    hasSpecial = true;
                }
            }

            strVal = hasSpecial //
                ? readString(chars, chars_len) //
                : chars_len < 20 //
                    ? symbolTable.addSymbol(chars, 0, chars_len, hash) //
                    : new String(chars, 0, chars_len);
        } else {
            strVal = readString(chars, chars_len);
        }

        bp = endIndex + 1;
        // ch = charAt(bp);
        {
            int index = bp;
            this.ch = index >= this.len ? //
                EOI //
                : text.charAt(index);
        }

        return strVal;
    }

    private static String readString(char[] chars, int chars_len) {
        char[] sbuf = new char[chars_len];
        int len = 0;
        for (int i = 0; i < chars_len; ++i) {
            char ch = chars[i];

            if (ch != '\\') {
                sbuf[len++] = ch;
                continue;
            }
            ch = chars[++i];

            switch (ch) {
                case '0':
                    sbuf[len++] = '\0';
                    break;
                case '1':
                    sbuf[len++] = '\1';
                    break;
                case '2':
                    sbuf[len++] = '\2';
                    break;
                case '3':
                    sbuf[len++] = '\3';
                    break;
                case '4':
                    sbuf[len++] = '\4';
                    break;
                case '5':
                    sbuf[len++] = '\5';
                    break;
                case '6':
                    sbuf[len++] = '\6';
                    break;
                case '7':
                    sbuf[len++] = '\7';
                    break;
                case 'b': // 8
                    sbuf[len++] = '\b';
                    break;
                case 't': // 9
                    sbuf[len++] = '\t';
                    break;
                case 'n': // 10
                    sbuf[len++] = '\n';
                    break;
                case 'v': // 11
                    sbuf[len++] = '\u000B';
                    break;
                case 'f': // 12
                case 'F':
                    sbuf[len++] = '\f';
                    break;
                case 'r': // 13
                    sbuf[len++] = '\r';
                    break;
                case '"': // 34
                    sbuf[len++] = '"';
                    break;
                case '\'': // 39
                    sbuf[len++] = '\'';
                    break;
                case '/': // 47
                    sbuf[len++] = '/';
                    break;
                case '\\': // 92
                    sbuf[len++] = '\\';
                    break;
                case 'x':
                    sbuf[len++] = (char) (digits[chars[++i]] * 16 + digits[chars[++i]]);
                    break;
                case 'u':
                    sbuf[len++] = (char) Integer.parseInt(new String(new char[] { chars[++i], //
                                                                                  chars[++i], //
                                                                                  chars[++i], //
                                                                                  chars[++i] }),
                                                          16);
                    break;
                default:
                    throw new JSONException("unclosed.str.lit");
            }
        }
        return new String(sbuf, 0, len);
    }

    public String info() {
        return "pos " + bp //
               + ", json : " //
               + (len < 65536 //
                   ? text //
                   : text.substring(0, 65536));
    }

    protected void skipComment() {
        next();
        if (ch == '/') {
            for (;;) {
                next();
                if (ch == '\n') {
                    next();
                    return;
                }
            }
        } else if (ch == '*') {
            next();

            for (; ch != EOI;) {
                if (ch == '*') {
                    next();
                    if (ch == '/') {
                        next();
                        break;
                    } else {
                        continue;
                    }
                }
                next();
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbolUnQuoted(final SymbolTable symbolTable) {
        final char first = ch;

        final boolean firstFlag = ch >= firstIdentifierFlags.length || firstIdentifierFlags[first];
        if (!firstFlag) {
            throw new JSONException("illegal identifier : " + ch //
                                    + ", " + info());
        }

        int hash = first;

        np = bp;
        sp = 1;
        for (;;) {
            char ch = next();

            if (ch < identifierFlags.length) {
                if (!identifierFlags[ch]) {
                    break;
                }
            }

            hash = 31 * hash + ch;

            sp++;
            continue;
        }

        this.ch = charAt(bp);
        token = JSONToken.IDENTIFIER;

        if (sp == 4 //
            && text.startsWith("null", np)) {
            return null;
        }

        return symbolTable.addSymbol(text, np, sp, hash);
    }

    public final void scanString() {
        final char quoteChar = ch;
        boolean hasSpecial = false;
        int startIndex = bp + 1;
        int endIndex = text.indexOf(quoteChar, startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str, " + info());
        }

        int chars_len;
        char[] chars;

        chars_len = endIndex - startIndex;
        chars = sub_chars(bp + 1, chars_len);
        while ((chars_len > 0 //
                && chars[chars_len - 1] == '\\')) {

            int slashCount = 1;
            for (int i = chars_len - 2; i >= 0; --i) {
                if (chars[i] == '\\') {
                    slashCount++;
                } else {
                    break;
                }
            }
            if (slashCount % 2 == 0) {
                break;
            }

            int nextIndex = text.indexOf(quoteChar, endIndex + 1);
            int nextLen = nextIndex - endIndex;
            int next_chars_len = chars_len + nextLen;

            if (next_chars_len >= chars.length) {
                int newLen = chars.length * 3 /2;
                if (newLen < next_chars_len) {
                    newLen = next_chars_len;
                }
                char[] newChars = new char[newLen];
                System.arraycopy(chars, 0, newChars, 0, chars.length);
                chars = newChars;
            }
            text.getChars(endIndex, nextIndex, chars, chars_len);
            
            chars_len = next_chars_len;
            endIndex = nextIndex;
            hasSpecial = true;
        }

        if (!hasSpecial) {
            for (int i = 0; i < chars_len; ++i) {
                if (chars[i] == '\\') {
                    hasSpecial = true;
                }
            }
        }

        sbuf = chars;
        sp = chars_len;
        np = bp;
        this.hasSpecial = hasSpecial;

        bp = endIndex + 1;
        // ch = charAt(bp);
        {
            int index = bp;
            this.ch = index >= this.len ? //
                EOI //
                : text.charAt(index);
        }

        token = JSONToken.LITERAL_STRING;
    }

    public String scanStringValue(char quoteChar) {
        int startIndex = bp + 1;
        int endIndex = text.indexOf(quoteChar, startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str, " + info());
        }
        
        String strVal = null;
        if (V6) {
            strVal = text.substring(startIndex, endIndex);
        } else {
            int chars_len = endIndex - startIndex;
            char[] chars = sub_chars(bp + 1, chars_len);
            strVal = new String(chars, 0, chars_len);
        }
        
        if (strVal.indexOf('\\') != -1) {
            for (;;) {
                int slashCount = 0;
                for (int i = endIndex - 1; i >= 0; --i) {
                    if (text.charAt(i) == '\\') {
                        slashCount++;
                    } else {
                        break;
                    }
                }
                if (slashCount % 2 == 0) {
                    break;
                }
                endIndex = text.indexOf(quoteChar, endIndex + 1);
            }
    
            int chars_len = endIndex - startIndex;
            char[] chars = sub_chars(bp + 1, chars_len);
            strVal = readString(chars, chars_len);
        }

        bp = endIndex + 1;
        // ch = charAt(bp);
        {
            int index = bp;
            ch = index >= this.len ? //
                EOI //
                : text.charAt(index);
        }

        return strVal;
    }

//    public Calendar getCalendar() {
//        return this.calendar;
//    }

    public final int intValue() {
        // final int INT_MULTMIN_RADIX_TEN = -214748364; // Integer.MIN_VALUE / 10;
        // final int INT_N_MULTMAX_RADIX_TEN = -214748364; // -Integer.MAX_VALUE / 10;

        int result = 0;
        boolean negative = false;
        int i = np, max = np + sp;
        int limit;
        int digit;

        char chLocal = np >= this.len ? //
                EOI //
                : text.charAt(np);
        if (chLocal == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i++;
        } else {
            limit = -Integer.MAX_VALUE;
        }
        final int multmin = -214748364; // negative ? INT_MULTMIN_RADIX_TEN : INT_N_MULTMAX_RADIX_TEN;
        if (i < max) {
            int charIndex = i++;
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);

            digit = chLocal - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            // char ch = charAt(i++);
            int charIndex = i++;
            char ch = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);

            if (ch == 'L' || ch == 'S' || ch == 'B') {
                break;
            }

            digit = ch - '0';

            if (result < multmin) {
                throw new NumberFormatException(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            return -result;
        }
    }

    public byte[] bytesValue() {
        return decodeFast(text, np + 1, sp);
    }

    private void scanIdent() {
        np = bp - 1;
        hasSpecial = false;

        for (;;) {
            sp++;

            next();
            if (Character.isLetterOrDigit(ch)) {
                continue;
            }

            String ident = stringVal();

            if (ident.equals("null")) {
                token = JSONToken.NULL;
            } else if (ident.equals("true")) {
                token = JSONToken.TRUE;
            } else if (ident.equals("false")) {
                token = JSONToken.FALSE;
            } else if (ident.equals("new")) {
                token = JSONToken.NEW;
            } else if (ident.equals("undefined")) {
                token = JSONToken.UNDEFINED;
            } else if (ident.equals("Set")) {
                token = JSONToken.SET;
            } else if (ident.equals("TreeSet")) {
                token = JSONToken.TREE_SET;
            } else {
                token = JSONToken.IDENTIFIER;
            }

            return;
        }
    }

    public final String stringVal() {
        return hasSpecial //
            ? readString(sbuf, sp) //
            : subString(np + 1, sp);
    }

    private final String subString(int offset, int count) {
        if (count < sbuf.length) {
            text.getChars(offset, offset + count, sbuf, 0);
            return new String(sbuf, 0, count);
        } else {
            char[] chars = new char[count];
            text.getChars(offset, offset + count, chars, 0);
            return new String(chars);
        }
    }

    final char[] sub_chars(int offset, int count) {
        if (count < sbuf.length) {
            text.getChars(offset, offset + count, sbuf, 0);
            return sbuf;
        } else {
            char[] chars = sbuf = new char[count];
            text.getChars(offset, offset + count, chars, 0);
            return chars;
        }
    }

    public final boolean isBlankInput() {
        for (int i = 0;; ++i) {
            char ch = charAt(i);
            if (ch == EOI) {
                break;
            }

            boolean whitespace = ch <= ' '//
                                 && (ch == ' '//
                                     || ch == '\n'//
                                     || ch == '\r'//
                                     || ch == '\t'//
                                     || ch == '\f'//
                                     || ch == '\b');
            if (!whitespace) {
                return false;
            }
        }

        return true;
    }

    final void skipWhitespace() {
        for (;;) {
            if (ch <= '/') {
                if (ch == ' '//
                    || ch == '\r'//
                    || ch == '\n'//
                    || ch == '\t'//
                    || ch == '\f'//
                    || ch == '\b') {
                    next();
                    continue;
                } else if (ch == '/') {
                    skipComment();
                    continue;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public final void scanNumber() {
        np = bp;
        exp = false;

        if (ch == '-') {
            sp++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
        }

        for (;;) {
            if (ch >= '0'//
                && ch <= '9') {
                sp++;
            } else {
                break;
            }
            // next();
            {
                int index = ++bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
        }

        isDouble = false;

        if (ch == '.') {
            sp++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
            isDouble = true;

            for (;;) {
                if (ch >= '0'//
                    && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }
        }

        if (ch == 'L') {
            sp++;
            next();
        } else if (ch == 'S') {
            sp++;
            next();
        } else if (ch == 'B') {
            sp++;
            next();
        } else if (ch == 'F') {
            sp++;
            next();
            isDouble = true;
        } else if (ch == 'D') {
            sp++;
            next();
            isDouble = true;
        } else if (ch == 'e'//
                   || ch == 'E') {
            sp++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }

            if (ch == '+' //
                || ch == '-') {
                sp++;
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }

            for (;;) {
                if (ch >= '0'//
                    && ch <= '9') {
                    sp++;
                } else {
                    break;
                }
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }

            if (ch == 'D'//
                || ch == 'F') {
                sp++;
                next();
            }

            exp = true;
            isDouble = true;
        }

        if (isDouble) {
            token = JSONToken.LITERAL_FLOAT;
        } else {
            token = JSONToken.LITERAL_INT;
        }
    }
    
    public boolean scanBoolean() {
        int offset;
        boolean value;
        if (text.startsWith("false", bp)) {
            offset = 5;
            value = false;
        } else if (text.startsWith("true", bp)) {
            offset = 4;
            value = true;
        } else if (ch == '1') {
            offset = 1;
            value = true;
        } else if (ch == '0') {
            offset = 1;
            value = false;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }
        
        bp += offset;
        ch = charAt(bp);

        return value;
    }

    public final Number scanNumberValue() {
        final int start = bp;

        boolean overflow = false;
        Number number = null;
        np = 0;
        final boolean negative;

        final long limit;
        if (ch == '-') {
            negative = true;
            limit = Long.MIN_VALUE;

            np++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
        } else {
            negative = false;
            limit = -Long.MAX_VALUE;
        }

        long longValue = 0;
        for (;;) {
            if (ch >= '0'//
                && ch <= '9') {
                int digit = (ch - '0');
                if (longValue < -922337203685477580L) { // Long.MIN_VALUE / 10
                    overflow = true;
                }

                longValue *= 10;
                if (longValue < limit + digit) {
                    overflow = true;
                }
                longValue -= digit;
            } else {
                break;
            }

            np++;
            // next();
            {
                int index = ++bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
        }

        if (!negative) {
            longValue = -longValue;
        }

        if (ch == 'L') {
            np++;
            next();
            number = longValue;
        } else if (ch == 'S') {
            np++;
            next();
            number = (short) longValue;
        } else if (ch == 'B') {
            np++;
            next();
            number = (byte) longValue;
        } else if (ch == 'F') {
            np++;
            next();
            number = (float) longValue;
        } else if (ch == 'D') {
            np++;
            next();
            number = (double) longValue;
        }

        boolean isDouble = false, exp = false;
        if (ch == '.') {
            isDouble = true;

            np++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }

            for (;;) {
                if (ch >= '0'//
                    && ch <= '9') {
                    np++;
                } else {
                    break;
                }
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }
        }

        char type = 0;
        if (ch == 'e'//
            || ch == 'E') {
            np++;
            // next();
            {
                int index = ++this.bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }

            if (ch == '+'//
                || ch == '-') {
                np++;
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }

            for (;;) {
                if (ch >= '0'//
                    && ch <= '9') {
                    np++;
                } else {
                    break;
                }
                // next();
                {
                    int index = ++this.bp;
                    this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
                }
            }

            if (ch == 'D'//
                || ch == 'F') {
                np++;
                type = ch;
                next();
            }

            exp = true;
        }

        if ((!isDouble) //
            && (!exp)) {
            if (overflow) {
                int len = bp - start;
                char[] chars = new char[len];
                text.getChars(start, bp, chars, 0);
                String strVal = new String(chars);
                number = new BigInteger(strVal);
            }
            if (number == null) {
                if (longValue > Integer.MIN_VALUE && longValue < Integer.MAX_VALUE) {
                    number = (int) longValue;
                } else {
                    number = longValue;
                }
            }
            return number;
        }

        int len = bp - start;
        if (type != 0) {
            len --;
        }

        char[] chars;
        if (len < sbuf.length) {
            text.getChars(start, start + len, sbuf, 0);
            chars = sbuf;
        } else {
            chars = new char[len];
            text.getChars(start, start + len, chars, 0);
        }

        // text.getChars(start, start + len, chars, 0);

        if ((!exp)//
            && (features & Feature.UseBigDecimal.mask) != 0) {
            number = new BigDecimal(chars, 0, len);
        } else {

            try {
                if (len <= 9 && !exp) {
                    int i = 0;
                    char c = chars[i++];
                    if (c == '-' || c == '+') {
                        c = chars[i++];
                    }

                    int intVal = c - '0';
                    int power = 0;
                    for (; i < len; ++i) {
                        c = chars[i];

                        if (c == '.') {
                            power = 1;
                            continue;
                        }
                        int digit = c - '0';
                        intVal = intVal * 10 + digit;

                        if (power != 0) {
                            power *= 10;
                        }
                    }

                    if (type == 'F') {
                        float floatVal = ((float) intVal) / power;
                        if (negative) {
                            floatVal = -floatVal;
                        }

                        return floatVal;
                    }

                    double doubleVal = ((double) intVal) / power;
                    if (negative) {
                        doubleVal = -doubleVal;
                    }

                    return doubleVal;
                }

                String strVal = new String(chars, 0, len);
                if (type == 'F') {
                    number = Float.valueOf(strVal);
                } else {
                    number = Double.parseDouble(strVal);
                }
            } catch (NumberFormatException ex) {
                throw new JSONException(ex.getMessage() + ", " + info(), ex);
            }
        }

        return number;
    }

    public final long scanLongValue() {
        np = 0;
        final boolean negative;

        final long limit;
        if (ch == '-') {
            negative = true;
            limit = Long.MIN_VALUE;

            np++;
            // next();
            {
                int index = ++this.bp;
                if (index >= this.len) {
                    throw new JSONException("syntax error, " + info());
                }
                this.ch = text.charAt(index);
            }
        } else {
            negative = false;
            limit = -Long.MAX_VALUE;
        }

        long longValue = 0;
        for (;;) {
            if (ch >= '0'//
                && ch <= '9') {
                int digit = (ch - '0');
                if (longValue < -922337203685477580L) { // Long.MIN_VALUE / 10
                    throw new JSONException("error long value, " + longValue + ", " + info());
                }

                longValue *= 10;
                if (longValue < limit + digit) {
                    throw new JSONException("error long value, " + longValue + ", " + info());
                }
                longValue -= digit;
            } else {
                break;
            }

            np++;
            // next();
            {
                int index = ++bp;
                this.ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
            }
        }

        if (!negative) {
            longValue = -longValue;
        }

        return longValue;
    }

    public final long longValue() throws NumberFormatException {
        long result = 0;
        boolean negative = false;
        int i = np, max = np + sp;

        int digit;

        final long limit;
        if (charAt(np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i++;
        } else {
            limit = -Long.MAX_VALUE;
        }
        if (i < max) {
            digit = charAt(i++) - '0';
            result = -digit;
        }
        while (i < max) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            // char chLocal = charAt(i++);
            char chLocal;
            {
                int index = i++;
                chLocal = index >= this.len ? //
                    EOI //
                    : text.charAt(index);
            }

            if (chLocal == 'L'//
                || chLocal == 'S'//
                || chLocal == 'B') {
                break;
            }

            digit = chLocal - '0';
            if (result < -922337203685477580L) { // Long.MIN_VALUE / 10
                throw new NumberFormatException(numberString());
            }
            result *= 10;
            if (result < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result -= digit;
        }

        if (negative) {
            if (i > np + 1) {
                return result;
            } else { /* Only got "-" */
                throw new NumberFormatException(numberString());
            }
        } else {
            return -result;
        }
    }

    public final Number decimalValue(boolean decimal) {
        // char chLocal = charAt(np + sp - 1);
        int charIndex = np + sp - 1;
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        try {
            if (chLocal == 'F') {
                return Float.parseFloat(numberString());
            }

            if (chLocal == 'D') {
                return Double.parseDouble(numberString());
            }

            if (decimal) {
                return decimalValue();
            } else {
                int index = np + sp - 1;
                chLocal = text.charAt(index);

                int sp = this.sp;
                if (chLocal == 'L' //
                        || chLocal == 'S' //
                        || chLocal == 'B' //
                        || chLocal == 'F' //
                        || chLocal == 'D') {
                    sp--;
                }

                // return text.substring(np, np + sp);
                // String str = this.subString(np, sp);
                String str;
                int offset = np, count = sp;
                char[] chars;
                if (count < sbuf.length) {
                    text.getChars(offset, offset + count, sbuf, 0);
                    chars = sbuf;
                } else {
                    chars = new char[count];
                    text.getChars(offset, offset + count, chars, 0);
                }

                if (count <= 9 && !exp) {
                    boolean negative = false;

                    int i = 0;
                    char c = chars[i++];
                    int off;
                    if (c == '-') {
                        negative = true;
                        c = chars[i++];
                        off = 1;
                    } else if (c == '+') {
                        c = chars[i++];
                        off = 1;
                    } else {
                        off = 0;
                    }

                    int intVal = c - '0';
                    int power = 0;
                    for (; i < count; ++i) {
                        c = chars[i];

                        if (c == '.') {
                            power = 1;
                            continue;
                        }
                        int digit = c - '0';
                        intVal = intVal * 10 + digit;

                        if (power != 0) {
                            power *= 10;
                        }
                    }

                    double doubleVal = ((double) intVal) / power;
                    if (negative) {
                        doubleVal = -doubleVal;
                    }

                    return doubleVal;
                }
                str = new String(chars, 0, count);

                return Double.parseDouble(str);
            }
        } catch (NumberFormatException ex) {
            throw new JSONException(ex.getMessage() + ", " + info());
        }
    }

    public final BigDecimal decimalValue() {
        int index = np + sp - 1;
        char chLocal = text.charAt(index);

        int sp = this.sp;
        if (chLocal == 'L' //
                || chLocal == 'S' //
                || chLocal == 'B' //
                || chLocal == 'F' //
                || chLocal == 'D') {
            sp--;
        }

        // return text.substring(np, np + sp);

        int offset = np, count = sp;
        if (count < sbuf.length) {
            text.getChars(offset, offset + count, sbuf, 0);
            return new BigDecimal(sbuf, 0, count);
        } else {
            char[] chars = new char[count];
            text.getChars(offset, offset + count, chars, 0);
            return new BigDecimal(chars);
        }
    }

    protected final static int[] digits = new int[(int) 'f' + 1];

    static {
        for (int i = '0'; i <= '9'; ++i) {
            digits[i] = i - '0';
        }

        for (int i = 'a'; i <= 'f'; ++i) {
            digits[i] = (i - 'a') + 10;
        }
        for (int i = 'A'; i <= 'F'; ++i) {
            digits[i] = (i - 'A') + 10;
        }
    }

    public boolean matchField(long fieldHashCode) {
        int offset = 1, charIndex;
        char fieldQuote = ch;
        int fieldStartIndex = bp + 1;
        for (;;) {
            if (fieldQuote == '"') {
                break;
            } else if (fieldQuote == '\'') {
                break;
            } else if (fieldQuote <= ' ' //
                    && (fieldQuote == ' ' //
                    || fieldQuote == '\n' //
                    || fieldQuote == '\r' //
                    || fieldQuote == '\t' //
                    || fieldQuote == '\f' //
                    || fieldQuote == '\b')) {
                charIndex = bp + (offset++);
                fieldQuote = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
            } else {
                fieldHash = 0L;
                matchStat = NOT_MATCH_NAME;
                return false;
            }
        }

        long hash = 0x811c9dc5;
        for (int i = fieldStartIndex; i < len; ++i) {
            char ch = text.charAt(i);
            if (ch == fieldQuote) {
                //this.fieldHashCode = hash;
                offset += (i - fieldStartIndex + 1);
                break;
            }

            hash ^= ch;
            hash *= 0x1000193;
        }

        if (hash != fieldHashCode) {
            matchStat = NOT_MATCH_NAME;
            fieldHash = hash;
            return false;
        }

        charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        for (;;) {
            if (chLocal == ':') {
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
                break;
            }

            if (chLocal <= ' ' //
                    && (chLocal == ' ' //
                    || chLocal == '\n' //
                    || chLocal == '\r' //
                    || chLocal == '\t' //
                    || chLocal == '\f' //
                    || chLocal == '\b')) {
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
                continue;
            }

            throw new JSONException("match feild error expect ':'");
        }

        if (chLocal == '{') {
            bp = charIndex + 1;
            this.ch = bp >= this.len ? //
                    EOI //
                    : text.charAt(bp);
            token = JSONToken.LBRACE;
        } else if (chLocal == '[') {
            bp = charIndex + 1;
            this.ch = bp >= this.len ? //
                    EOI //
                    : text.charAt(bp);
            token = JSONToken.LBRACKET;
        } else {
            bp = charIndex;
            this.ch = bp >= this.len ? //
                    EOI //
                    : text.charAt(bp);
            nextToken();
        }

        return true;
    }

    private int matchFieldHash(long fieldHashCode) {
        int offset = 1, charIndex;
        char fieldQuote = ch;
        int fieldStartIndex = bp + 1;
        for (;;) {
            if (fieldQuote == '"') {
                break;
            } else if (fieldQuote == '\'') {
                break;
            } else if (fieldQuote <= ' ' //
                    && (fieldQuote == ' ' //
                    || fieldQuote == '\n' //
                    || fieldQuote == '\r' //
                    || fieldQuote == '\t' //
                    || fieldQuote == '\f' //
                    || fieldQuote == '\b')) {
                charIndex = bp + (offset++);
                fieldQuote = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
            } else {
                fieldHash = 0;
                matchStat = NOT_MATCH_NAME;
                return 0;
            }
        }

        long hash = 0x811c9dc5;
        for (int i = fieldStartIndex; i < len; ++i) {
            char ch = text.charAt(i);
            if (ch == fieldQuote) {
                //this.fieldHashCode = hash;
                offset += (i - fieldStartIndex);
                break;
            }

            hash ^= ch;
            hash *= 0x1000193;
        }

        if (hash != fieldHashCode) {
            fieldHash = hash;
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        charIndex = bp + (++offset);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        for (;;) {
            if (chLocal == ':') {
                offset++;
                break;
            }

            if (chLocal <= ' ' //
                    && (chLocal == ' ' //
                    || chLocal == '\n' //
                    || chLocal == '\r' //
                    || chLocal == '\t' //
                    || chLocal == '\f' //
                    || chLocal == '\b')) {
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
                continue;
            }

            throw new JSONException("match feild error expect ':'");
        }

        return offset;
    }

    public int scanFieldInt(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return 0;
        }

        // char chLocal = charAt(bp + (offset++));
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        boolean quote = false;
        if (chLocal == '"') {
            quote = true;

            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
        }

        int value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                // chLocal = charAt(bp + (offset++));
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else if (chLocal == '\"') {
                    if (!quote) {
                        matchStat = NOT_MATCH;
                        return 0;
                    }
                    int index = bp + (offset++);
                    chLocal = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                    break;
                } else {
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

        if (chLocal == ',') {
            bp += (offset - 1);
            // this.next();
            {
                int index = ++bp;
                this.ch = index >= this.len ? //
                        EOI //
                        : text.charAt(index);
            }
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
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

    public final int[] scanFieldIntArray(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }
        // char chLocal = charAt(bp + (offset++));
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }
        // chLocal = charAt(bp + (offset++));
        charIndex = bp + (offset++);
        chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);


        int[] array = new int[16];
        int arrayIndex = 0;

        if (chLocal == ']') {
            // chLocal = charAt(bp + (offset++));
            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
        } else {
            for (;;) {
                boolean nagative = false;
                if (chLocal == '-') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    nagative = true;
                }
                if (chLocal >= '0' && chLocal <= '9') {
                    int value = chLocal - '0';
                    for (; ; ) {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);

                        if (chLocal >= '0' && chLocal <= '9') {
                            value = value * 10 + (chLocal - '0');
                        } else {
                            break;
                        }
                    }

                    if (arrayIndex >= array.length) {
                        int[] tmp = new int[array.length * 3 / 2];
                        System.arraycopy(array, 0, tmp, 0, arrayIndex);
                        array = tmp;
                    }
                    array[arrayIndex++] = nagative ? -value : value;

                    if (chLocal == ',') {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                    } else if (chLocal == ']') {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                        break;
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }
            }
        }


        if (arrayIndex != array.length) {
            int[] tmp = new int[arrayIndex];
            System.arraycopy(array, 0, tmp, 0, arrayIndex);
            array = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return array;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return array;
    }

    public long scanFieldLong(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return 0;
        }

        // char chLocal = charAt(bp + (offset++));
        char chLocal;
        {
            int index = bp + (offset++);
            chLocal = index >= this.len ? //
                    EOI //
                    : text.charAt(index);
        }

        long value;

        boolean quote = false;
        if (chLocal == '"') {
            quote = true;

            {
                int index = bp + (offset++);
                chLocal = index >= this.len ? //
                        EOI //
                        : text.charAt(index);
            }
        }

        if (chLocal >= '0' //
                && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                // chLocal = charAt(bp + (offset++));
                {
                    int index = bp + (offset++);
                    chLocal = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else if (chLocal == '\"') {
                    if (!quote) {
                        matchStat = NOT_MATCH;
                        return 0;
                    }
                    int index = bp + (offset++);
                    chLocal = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                    break;
                } else {
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

        if (chLocal == ',') {
            bp += (offset - 1);
            // this.next();
            {
                int index = ++bp;
                this.ch = index >= this.len ? //
                        EOI //
                        : text.charAt(index);
            }
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
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

    public String scanFieldString(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }

        char chLocal;// = charAt(bp + (offset++));
        {
            int index = bp + (offset++);
            if (index >= this.len) {
                throw new JSONException("unclosed str, " + info());
            } else {
                chLocal = text.charAt(index);
            }
        }

        if (chLocal != '"') {
            matchStat = NOT_MATCH;

            return stringDefaultValue;
        }

        final char quoteChar = '"';
        boolean hasSpecial = false;
        int startIndex = bp + offset;
        int endIndex = text.indexOf(quoteChar, startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str, " + info());
        }

        String strVal = null;
        if (V6) {
            strVal = text.substring(startIndex, endIndex);
        } else {
            int chars_len = endIndex - startIndex;
            char[] chars = sub_chars(bp + offset, chars_len);
            strVal = new String(chars, 0, chars_len);
        }

        if (strVal.indexOf('\\') != -1) {
            for (;;) {
                int slashCount = 0;
                for (int i = endIndex - 1; i >= 0; --i) {
                    if (text.charAt(i) == '\\') {
                        hasSpecial = true;
                        slashCount++;
                    } else {
                        break;
                    }
                }
                if (slashCount % 2 == 0) {
                    break;
                }
                endIndex = text.indexOf(quoteChar, endIndex + 1);
            }

            int chars_len = endIndex - startIndex;
            char[] chars = sub_chars(bp + offset, chars_len);
            if (hasSpecial) {
                strVal = readString(chars, chars_len);
            } else {
                strVal = new String(chars, 0, chars_len);
                if (strVal.indexOf('\\') != -1) {
                    strVal = readString(chars, chars_len);
                }
            }
        }

        // bp = endIndex + 1;
        // ch = charAt(bp);
        {
            int index = ++endIndex;
            chLocal = index >= this.len ? //
                    EOI //
                    : text.charAt(index);
        }

        if (chLocal == ',') {
            bp = endIndex;
//            this.next();
            {
                int index = ++bp;
                this.ch = (index >= this.len ? //
                        EOI //
                        : text.charAt(index));
            }
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return strVal;
        }

        if (chLocal == '}') {
            // chLocal = charAt(++endIndex);
            int charIndex = ++endIndex;
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);

            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp = endIndex;
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp = endIndex;
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp = endIndex;
                this.next();
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp = endIndex;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return stringDefaultValue;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return stringDefaultValue;
        }

        return strVal;
    }

    public boolean scanFieldBoolean(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return false;
        }

        boolean value;
        if (text.startsWith("false", bp + offset)) {
            offset += 5;
            value = false;
        } else if (text.startsWith("true", bp + offset)) {
            offset += 4;
            value = true;
        } else if (text.startsWith("\"false\"", bp + offset)) {
            offset += 7;
            value = false;
        } else if (text.startsWith("\"true\"", bp + offset)) {
            offset += 6;
            value = true;
        } else {
            matchStat = NOT_MATCH;
            return false;
        }

        // char chLocal = charAt(bp + offset++);
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        if (chLocal == ',') {
            bp += (offset - 1);
            // this.next();
            {
                int index = ++bp;
                this.ch = index >= this.len ? //
                        EOI //
                        : text.charAt(index);
            }
            matchStat = VALUE;
            token = JSONToken.COMMA;

            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                // this.next();
                {
                    int index = ++bp;
                    this.ch = index >= this.len ? //
                            EOI //
                            : text.charAt(index);
                }
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
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

    public final float scanFieldFloat(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return 0;
        }

        char chLocal = charAt(bp + (offset++));

        int start = bp + offset - 1;
        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        float value;
        if (chLocal >= '0' && chLocal <= '9') {
            int intVal = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            int power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                if ((chLocal = charAt(bp + (offset++))) >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    for (power = 10; ; ) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int count = bp + offset - start - 1;
            if (!exp && count < 10) {
                value = ((float) intVal) / power;
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Float.parseFloat(text);
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
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

    public final float[] scanFieldFloatArray(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }
        // chLocal = charAt(bp + (offset++));
        charIndex = bp + (offset++);
        chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        float[] array = new float[16];
        int arrayIndex = 0;

        for (;;) {
            int start = bp + offset - 1;

            boolean negative = chLocal == '-';
            if (negative) {
                // chLocal = charAt(bp + (offset++));
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
            }

            if (chLocal >= '0' && chLocal <= '9') {
                int intVal = chLocal - '0';
                for (; ; ) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        continue;
                    } else {
                        break;
                    }
                }

                int power = 1;
                boolean small = (chLocal == '.');
                if (small) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    power *= 10;
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        for (; ; ) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power *= 10;
                                continue;
                            } else {
                                break;
                            }
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                boolean exp = chLocal == 'e' || chLocal == 'E';
                if (exp) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    if (chLocal == '+' || chLocal == '-') {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                    }
                    for (;;) {
                        if (chLocal >= '0' && chLocal <= '9') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                        } else {
                            break;
                        }
                    }
                }

                int count = bp + offset - start - 1;

                float value;
                if (!exp && count < 10) {
                    value = ((float) intVal) / power;
                    if (negative) {
                        value = -value;
                    }
                } else {
                    String text = this.subString(start, count);
                    value = Float.parseFloat(text);
                }

                if (arrayIndex >= array.length) {
                    float[] tmp = new float[array.length * 3 / 2];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }
                array[arrayIndex++] = value;

                if (chLocal == ',') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                } else if (chLocal == ']') {
                    //chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    break;
                }
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        }


        if (arrayIndex != array.length) {
            float[] tmp = new float[arrayIndex];
            System.arraycopy(array, 0, tmp, 0, arrayIndex);
            array = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return array;
        }

        if (chLocal == '}') {
            // chLocal = charAt(bp + (offset++));
            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return array;
    }

    public final float[][] scanFieldFloatArray2(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }
        // chLocal = charAt(bp + (offset++));
        charIndex = bp + (offset++);
        chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        float[][] arrayarray = new float[16][];
        int arrayarrayIndex = 0;

        for (;;) {
            if (chLocal == '[') {
                // chLocal = charAt(bp + (offset++));
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);

                float[] array = new float[16];
                int arrayIndex = 0;

                for (; ; ) {
                    int start = bp + offset - 1;
                    boolean negative = chLocal == '-';
                    if (negative) {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                    }

                    if (chLocal >= '0' && chLocal <= '9') {
                        int intVal = chLocal - '0';
                        for (; ; ) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                continue;
                            } else {
                                break;
                            }
                        }

                        int power = 1;
                        if (chLocal == '.') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power *= 10;
                                for (; ; ) {
                                    // chLocal = charAt(bp + (offset++));
                                    charIndex = bp + (offset++);
                                    chLocal = charIndex >= this.len ? //
                                            EOI //
                                            : text.charAt(charIndex);

                                    if (chLocal >= '0' && chLocal <= '9') {
                                        intVal = intVal * 10 + (chLocal - '0');
                                        power *= 10;
                                        continue;
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                matchStat = NOT_MATCH;
                                return null;
                            }
                        }

                        boolean exp = chLocal == 'e' || chLocal == 'E';
                        if (exp) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                            if (chLocal == '+' || chLocal == '-') {
                                // chLocal = charAt(bp + (offset++));
                                charIndex = bp + (offset++);
                                chLocal = charIndex >= this.len ? //
                                        EOI //
                                        : text.charAt(charIndex);
                            }
                            for (;;) {
                                if (chLocal >= '0' && chLocal <= '9') {
                                    // chLocal = charAt(bp + (offset++));
                                    charIndex = bp + (offset++);
                                    chLocal = charIndex >= this.len ? //
                                            EOI //
                                            : text.charAt(charIndex);
                                } else {
                                    break;
                                }
                            }
                        }

                        int count = bp + offset - start - 1;
                        float value;
                        if (!exp && count < 10) {
                            value = ((float) intVal) / power;
                            if (negative) {
                                value = -value;
                            }
                        } else {
                            String text = this.subString(start, count);
                            value = Float.parseFloat(text);
                        }

                        if (arrayIndex >= array.length) {
                            float[] tmp = new float[array.length * 3 / 2];
                            System.arraycopy(array, 0, tmp, 0, arrayIndex);
                            array = tmp;
                        }
                        array[arrayIndex++] = value;

                        if (chLocal == ',') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                        } else if (chLocal == ']') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                            break;
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                // compact
                if (arrayIndex != array.length) {
                    float[] tmp = new float[arrayIndex];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }

                if (arrayarrayIndex >= arrayarray.length) {
                    float[][] tmp = new float[arrayarray.length * 3 / 2][];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    arrayarray = tmp;
                }
                arrayarray[arrayarrayIndex++] = array;

                if (chLocal == ',') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                } else if (chLocal == ']') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    break;
                }
            }
        }

        // compact
        if (arrayarrayIndex != arrayarray.length) {
            float[][] tmp = new float[arrayarrayIndex][];
            System.arraycopy(arrayarray, 0, tmp, 0, arrayarrayIndex);
            arrayarray = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return arrayarray;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return arrayarray;
    }

    public final double scanFieldDouble(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return 0;
        }

        char chLocal = charAt(bp + (offset++));

        int start = bp + offset - 1;
        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(bp + (offset++));
        }

        double value;
        if (chLocal >= '0' && chLocal <= '9') {
            int intVal = chLocal - '0';
            for (;;) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            int power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(bp + (offset++));
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power *= 10;
                    for (; ; ) {
                        chLocal = charAt(bp + (offset++));
                        if (chLocal >= '0' && chLocal <= '9') {
                            intVal = intVal * 10 + (chLocal - '0');
                            power *= 10;
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }

            boolean exp = chLocal == 'e' || chLocal == 'E';
            if (exp) {
                chLocal = charAt(bp + (offset++));
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(bp + (offset++));
                }
                for (;;) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(bp + (offset++));
                    } else {
                        break;
                    }
                }
            }

            int count = bp + offset - start - 1;
            if (!exp && count < 10) {
                value = ((double) intVal) / power;
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Double.parseDouble(text);
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
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

    public final double[] scanFieldDoubleArray(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);
        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }
        // chLocal = charAt(bp + (offset++));
        charIndex = bp + (offset++);
        chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        double[] array = new double[16];
        int arrayIndex = 0;

        for (;;) {
            int start = bp + offset - 1;

            boolean negative = chLocal == '-';
            if (negative) {
                // chLocal = charAt(bp + (offset++));
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
            }

            if (chLocal >= '0' && chLocal <= '9') {
                int intVal = chLocal - '0';
                for (; ; ) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        continue;
                    } else {
                        break;
                    }
                }

                int power = 1;
                boolean small = (chLocal == '.');
                if (small) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    power *= 10;
                    if (chLocal >= '0' && chLocal <= '9') {
                        intVal = intVal * 10 + (chLocal - '0');
                        for (; ; ) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power *= 10;
                                continue;
                            } else {
                                break;
                            }
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                boolean exp = chLocal == 'e' || chLocal == 'E';
                if (exp) {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    if (chLocal == '+' || chLocal == '-') {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                    }
                    for (;;) {
                        if (chLocal >= '0' && chLocal <= '9') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                        } else {
                            break;
                        }
                    }
                }

                int count = bp + offset - start - 1;

                double value;
                if (!exp && count < 10) {
                    value = ((double) intVal) / power;
                    if (negative) {
                        value = -value;
                    }
                } else {
                    String text = this.subString(start, count);
                    value = Double.parseDouble(text);
                }

                if (arrayIndex >= array.length) {
                    double[] tmp = new double[array.length * 3 / 2];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }
                array[arrayIndex++] = value;

                if (chLocal == ',') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                } else if (chLocal == ']') {
                    //chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    break;
                }
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
        }


        if (arrayIndex != array.length) {
            double[] tmp = new double[arrayIndex];
            System.arraycopy(array, 0, tmp, 0, arrayIndex);
            array = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return array;
        }

        if (chLocal == '}') {
            // chLocal = charAt(bp + (offset++));
            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return array;
    }

    public final double[][] scanFieldDoubleArray2(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return null;
        }
        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        if (chLocal != '[') {
            matchStat = NOT_MATCH;
            return null;
        }
        // chLocal = charAt(bp + (offset++));
        charIndex = bp + (offset++);
        chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        double[][] arrayarray = new double[16][];
        int arrayarrayIndex = 0;

        for (;;) {
            if (chLocal == '[') {
                // chLocal = charAt(bp + (offset++));
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);

                double[] array = new double[16];
                int arrayIndex = 0;

                for (; ; ) {
                    int start = bp + offset - 1;
                    boolean negative = chLocal == '-';
                    if (negative) {
                        // chLocal = charAt(bp + (offset++));
                        charIndex = bp + (offset++);
                        chLocal = charIndex >= this.len ? //
                                EOI //
                                : text.charAt(charIndex);
                    }

                    if (chLocal >= '0' && chLocal <= '9') {
                        int intVal = chLocal - '0';
                        for (; ; ) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                continue;
                            } else {
                                break;
                            }
                        }

                        int power = 1;
                        if (chLocal == '.') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);

                            if (chLocal >= '0' && chLocal <= '9') {
                                intVal = intVal * 10 + (chLocal - '0');
                                power *= 10;
                                for (; ; ) {
                                    // chLocal = charAt(bp + (offset++));
                                    charIndex = bp + (offset++);
                                    chLocal = charIndex >= this.len ? //
                                            EOI //
                                            : text.charAt(charIndex);

                                    if (chLocal >= '0' && chLocal <= '9') {
                                        intVal = intVal * 10 + (chLocal - '0');
                                        power *= 10;
                                        continue;
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                matchStat = NOT_MATCH;
                                return null;
                            }
                        }

                        boolean exp = chLocal == 'e' || chLocal == 'E';
                        if (exp) {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                            if (chLocal == '+' || chLocal == '-') {
                                // chLocal = charAt(bp + (offset++));
                                charIndex = bp + (offset++);
                                chLocal = charIndex >= this.len ? //
                                        EOI //
                                        : text.charAt(charIndex);
                            }
                            for (;;) {
                                if (chLocal >= '0' && chLocal <= '9') {
                                    // chLocal = charAt(bp + (offset++));
                                    charIndex = bp + (offset++);
                                    chLocal = charIndex >= this.len ? //
                                            EOI //
                                            : text.charAt(charIndex);
                                } else {
                                    break;
                                }
                            }
                        }

                        int count = bp + offset - start - 1;
                        double value;
                        if (!exp && count < 10) {
                            value = ((double) intVal) / power;
                            if (negative) {
                                value = -value;
                            }
                        } else {
                            String text = this.subString(start, count);
                            value = Double.parseDouble(text);
                        }

                        if (arrayIndex >= array.length) {
                            double[] tmp = new double[array.length * 3 / 2];
                            System.arraycopy(array, 0, tmp, 0, arrayIndex);
                            array = tmp;
                        }
                        array[arrayIndex++] = value;

                        if (chLocal == ',') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                        } else if (chLocal == ']') {
                            // chLocal = charAt(bp + (offset++));
                            charIndex = bp + (offset++);
                            chLocal = charIndex >= this.len ? //
                                    EOI //
                                    : text.charAt(charIndex);
                            break;
                        }
                    } else {
                        matchStat = NOT_MATCH;
                        return null;
                    }
                }

                // compact
                if (arrayIndex != array.length) {
                    double[] tmp = new double[arrayIndex];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    array = tmp;
                }

                if (arrayarrayIndex >= arrayarray.length) {
                    double[][] tmp = new double[arrayarray.length * 3 / 2][];
                    System.arraycopy(array, 0, tmp, 0, arrayIndex);
                    arrayarray = tmp;
                }
                arrayarray[arrayarrayIndex++] = array;

                if (chLocal == ',') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                } else if (chLocal == ']') {
                    // chLocal = charAt(bp + (offset++));
                    charIndex = bp + (offset++);
                    chLocal = charIndex >= this.len ? //
                            EOI //
                            : text.charAt(charIndex);
                    break;
                }
            }
        }

        // compact
        if (arrayarrayIndex != arrayarray.length) {
            double[][] tmp = new double[arrayarrayIndex][];
            System.arraycopy(arrayarray, 0, tmp, 0, arrayarrayIndex);
            arrayarray = tmp;
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            this.next();
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return arrayarray;
        }

        if (chLocal == '}') {
            chLocal = charAt(bp + (offset++));
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                bp += (offset - 1);
                token = JSONToken.EOF;
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return arrayarray;
    }

    public long scanFieldSymbol(long fieldHashCode) {
        matchStat = UNKNOWN;

        int offset = matchFieldHash(fieldHashCode);
        if (offset == 0) {
            return 0;
        }

        int charIndex = bp + (offset++);
        char chLocal = charIndex >= this.len ? //
                EOI //
                : text.charAt(charIndex);

        if (chLocal != '"') {
            matchStat = NOT_MATCH;
            return 0;
        }

        long hash = 0x811c9dc5;
        int start = bp + offset;
        for (;;) {
            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);

            if (chLocal == '\"') {
                charIndex = bp + (offset++);
                chLocal = charIndex >= this.len ? //
                        EOI //
                        : text.charAt(charIndex);
                break;
            }

            hash ^= chLocal;
            hash *= 0x1000193;

            if (chLocal == '\\') {
                matchStat = NOT_MATCH;
                return 0;
            }
        }

        if (chLocal == ',') {
            bp += (offset - 1);
            // this.next();
            {
                int index = ++bp;
                this.ch = index >= this.len ? //
                        EOI //
                        : text.charAt(index);
            }
            matchStat = VALUE;
            return hash;
        }

        if (chLocal == '}') {
            charIndex = bp + (offset++);
            chLocal = charIndex >= this.len ? //
                    EOI //
                    : text.charAt(charIndex);
            if (chLocal == ',') {
                token = JSONToken.COMMA;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == ']') {
                token = JSONToken.RBRACKET;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == '}') {
                token = JSONToken.RBRACE;
                bp += (offset - 1);
                this.next();
            } else if (chLocal == EOI) {
                token = JSONToken.EOF;
                bp += (offset - 1);
                ch = EOI;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return hash;
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = len - bp;

        if ((!strict) && rest > 13) {
            if (text.startsWith("/Date(", bp) && charAt(bp + rest - 1) == '/' //
                && charAt(bp + rest - 2) == ')' //
            ) {
                int plusIndex = -1;
                for (int i = 6; i < rest; ++i) {
                    char c = charAt(bp + i);
                    if (c == '+') {
                        plusIndex = i;
                    } else if (c < '0' || c > '9') {
                        break;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = bp + 6;
                String numberText = this.subString(offset, plusIndex - offset);
                long millis = Long.parseLong(numberText, 10);

                calendar = Calendar.getInstance(timeZone, locale);
                calendar.setTimeInMillis(millis);

                token = JSONToken.LITERAL_ISO8601_DATE;
                return true;
            }
        }

        if (rest == 8 //
            || rest == 14 //
            || rest == 17) {
            if (strict) {
                return false;
            }

            char y0 = charAt(bp);
            char y1 = charAt(bp + 1);
            char y2 = charAt(bp + 2);
            char y3 = charAt(bp + 3);
            char M0 = charAt(bp + 4);
            char M1 = charAt(bp + 5);
            char d0 = charAt(bp + 6);
            char d1 = charAt(bp + 7);

            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }

            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

            int hour, minute, seconds, millis;
            if (rest != 8) {
                char h0 = charAt(bp + 8);
                char h1 = charAt(bp + 9);
                char m0 = charAt(bp + 10);
                char m1 = charAt(bp + 11);
                char s0 = charAt(bp + 12);
                char s1 = charAt(bp + 13);

                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }

                if (rest == 17) {
                    char S0 = charAt(bp + 14);
                    char S1 = charAt(bp + 15);
                    char S2 = charAt(bp + 16);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    if (S1 < '0' || S1 > '9') {
                        return false;
                    }
                    if (S2 < '0' || S2 > '9') {
                        return false;
                    }

                    millis = (S0  - '0') * 100 + (S1 - '0') * 10 + (S2 - '0');
                } else {
                    millis = 0;
                }

                hour = (h0 - '0') * 10 + (h1 - '0');
                minute = (m0 - '0') * 10 + (m1 - '0');
                seconds = (s0 - '0') * 10 + (s1 - '0');
            } else {
                hour = minute = seconds = millis = 0;
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, millis);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        }

        if (rest < 10) { // 0000-00-00
            return false;
        }

        if (charAt(bp + 4) != '-') {
            return false;
        }
        if (charAt(bp + 7) != '-') {
            return false;
        }

        char y0 = charAt(bp);
        char y1 = charAt(bp + 1);
        char y2 = charAt(bp + 2);
        char y3 = charAt(bp + 3);
        char M0 = charAt(bp + 5);
        char M1 = charAt(bp + 6);
        char d0 = charAt(bp + 8);
        char d1 = charAt(bp + 9);
        if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
            return false;
        }

        setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

        char t = charAt(bp + 10);
        if (t == 'T' || (t == ' ' && !strict)) {
            if (rest < 19) { // 0000-00-00T00:00:00
                return false;
            }
        } else if (t == '"' || t == EOI) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 10);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        } else {
            return false;
        }

        if (charAt(bp + 13) != ':') {
            return false;
        }
        if (charAt(bp + 16) != ':') {
            return false;
        }

        char h0 = charAt(bp + 11);
        char h1 = charAt(bp + 12);
        char m0 = charAt(bp + 14);
        char m1 = charAt(bp + 15);
        char s0 = charAt(bp + 17);
        char s1 = charAt(bp + 18);

        if (!checkTime(h0, h1, m0, m1, s0, s1)) {
            return false;
        }

        int hour = (h0 - '0') * 10 + (h1 - '0');
        int minute = (m0 - '0') * 10 + (m1 - '0');
        int seconds = (s0 - '0') * 10 + (s1 - '0');
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);

        char dot = charAt(bp + 19);
        if (dot == '.') {
            if (rest < 21) { // 0000-00-00T00:00:00.000
                return false;
            }
        } else {
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 19);

            token = JSONToken.LITERAL_ISO8601_DATE;

            if (dot == 'Z') {// UTC
                // bugfix https://github.com/alibaba/fastjson/issues/376
                if (calendar.getTimeZone().getRawOffset() != 0) {
                    String[] timeZoneIDs = TimeZone.getAvailableIDs(0);// 没有+ 和 - 默认相对0
                    if (timeZoneIDs.length > 0) {
                        TimeZone timeZone = TimeZone.getTimeZone(timeZoneIDs[0]);
                        calendar.setTimeZone(timeZone);
                    }
                }
            }
            return true;
        }

        char S0 = charAt(bp + 20);
        if (S0 < '0' || S0 > '9') {
            return false;
        }
        int millis = digits[S0];
        int millisLen = 1;

        if (rest > 21) {
            char S1 = charAt(bp + 21);
            if (S1 >= '0' && S1 <= '9') {
                millis = millis * 10 + digits[S1];
                millisLen = 2;
            }
        }

        if (millisLen == 2) {
            char S2 = charAt(bp + 22);
            if (S2 >= '0' && S2 <= '9') {
                millis = millis * 10 + digits[S2];
                millisLen = 3;
            }
        }

        calendar.set(Calendar.MILLISECOND, millis);

        int timzeZoneLength = 0;
        char timeZoneFlag = charAt(bp + 20 + millisLen);
        if (timeZoneFlag == '+' || timeZoneFlag == '-') {
            char t0 = charAt(bp + 20 + millisLen + 1);
            if (t0 < '0' || t0 > '1') {
                return false;
            }

            char t1 = charAt(bp + 20 + millisLen + 2);
            if (t1 < '0' || t1 > '9') {
                return false;
            }

            char t2 = charAt(bp + 20 + millisLen + 3);
            if (t2 == ':') { // ThreeLetterISO8601TimeZone
                char t3 = charAt(bp + 20 + millisLen + 4);
                if (t3 != '0') {
                    return false;
                }

                char t4 = charAt(bp + 20 + millisLen + 5);
                if (t4 != '0') {
                    return false;
                }
                timzeZoneLength = 6;
            } else if (t2 == '0') { // TwoLetterISO8601TimeZone
                char t3 = charAt(bp + 20 + millisLen + 4);
                if (t3 != '0') {
                    return false;
                }
                timzeZoneLength = 5;
            } else {
                timzeZoneLength = 3;
            }

            int timeZoneOffset = (digits[t0] * 10 + digits[t1]) * 3600 * 1000;
            if (timeZoneFlag == '-') {
                timeZoneOffset = -timeZoneOffset;
            }

            if (calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
                String[] timeZoneIDs = TimeZone.getAvailableIDs(timeZoneOffset);
                if (timeZoneIDs.length > 0) {
                    TimeZone timeZone = TimeZone.getTimeZone(timeZoneIDs[0]);
                    calendar.setTimeZone(timeZone);
                }
            }

        } else if (timeZoneFlag == 'Z') {// UTC
            timzeZoneLength = 1;
            if (calendar.getTimeZone().getRawOffset() != 0) {
                String[] timeZoneIDs = TimeZone.getAvailableIDs(0);
                if (timeZoneIDs.length > 0) {
                    TimeZone timeZone = TimeZone.getTimeZone(timeZoneIDs[0]);
                    calendar.setTimeZone(timeZone);
                }
            }
        }

        char end = charAt(bp + (20 + millisLen + timzeZoneLength));
        if (end != EOI && end != '"') {
            return false;
        }
        ch = charAt(bp += (20 + millisLen + timzeZoneLength));

        token = JSONToken.LITERAL_ISO8601_DATE;
        return true;
    }

    static boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '2') {
            if (h1 < '0' || h1 > '4') {
                return false;
            }
        } else {
            return false;
        }

        if (m0 >= '0' && m0 <= '5') {
            if (m1 < '0' || m1 > '9') {
                return false;
            }
        } else if (m0 == '6') {
            if (m1 != '0') {
                return false;
            }
        } else {
            return false;
        }

        if (s0 >= '0' && s0 <= '5') {
            if (s1 < '0' || s1 > '9') {
                return false;
            }
        } else if (s0 == '6') {
            if (s1 != '0') {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        calendar = Calendar.getInstance(timeZone, locale);
        int year = (y0 - '0') * 1000 + (y1 - '0') * 100 + (y2 - '0') * 10 + (y3 - '0');
        int month = (M0 - '0') * 10 + (M1 - '0') - 1;
        int day = (d0 - '0') * 10 + (d1 - '0');
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if (y0 != '1' && y0 != '2') {
            return false;
        }
        if (y1 < '0' || y1 > '9') {
            return false;
        }
        if (y2 < '0' || y2 > '9') {
            return false;
        }
        if (y3 < '0' || y3 > '9') {
            return false;
        }

        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else if (M0 == '1') {
            if (M1 != '0' && M1 != '1' && M1 != '2') {
                return false;
            }
        } else {
            return false;
        }

        if (d0 == '0') {
            if (d1 < '1' || d1 > '9') {
                return false;
            }
        } else if (d0 == '1' || d0 == '2') {
            if (d1 < '0' || d1 > '9') {
                return false;
            }
        } else if (d0 == '3') {
            if (d1 != '0' && d1 != '1') {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /////////// base 64
    public static final char[] CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    static final int[]         IA = new int[256];
    static {
        Arrays.fill(IA, -1);
        for (int i = 0, iS = CA.length; i < iS; i++) {
            IA[CA[i]] = i;
        }
        IA['='] = 0;
    }

    public final static byte[] decodeFast(String chars, int offset, int charsLen) {
        // Check special case
        if (charsLen == 0) {
            return new byte[0];
        }

        int sIx = offset, eIx = offset + charsLen - 1; // Start and end index after trimming.

        // Trim illegal chars from start
        while (sIx < eIx && IA[chars.charAt(sIx)] < 0)
            sIx++;

        // Trim illegal chars from end
        while (eIx > 0 && IA[chars.charAt(eIx)] < 0)
            eIx--;

        // get the padding count (=) (0, 1 or 2)
        int pad = chars.charAt(eIx) == '=' ? (chars.charAt(eIx - 1) == '=' ? 2 : 1) : 0; // Count '=' at end.
        int cCnt = eIx - sIx + 1; // Content count including possible separators
        int sepCnt = charsLen > 76 ? (chars.charAt(76) == '\r' ? cCnt / 78 : 0) << 1 : 0;

        int len = ((cCnt - sepCnt) * 6 >> 3) - pad; // The number of decoded bytes
        byte[] bytes = new byte[len]; // Preallocate byte[] of exact length

        // Decode all but the last 0 - 2 bytes.
        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen;) {
            // Assemble three bytes into an int from four "valid" characters.
            int i = IA[chars.charAt(sIx++)] << 18 | IA[chars.charAt(sIx++)] << 12 | IA[chars.charAt(sIx++)] << 6
                    | IA[chars.charAt(sIx++)];

            // Add the bytes
            bytes[d++] = (byte) (i >> 16);
            bytes[d++] = (byte) (i >> 8);
            bytes[d++] = (byte) i;

            // If line separator, jump over it.
            if (sepCnt > 0 && ++cc == 19) {
                sIx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            // Decode last 1-3 bytes (incl '=') into 1-3 bytes
            int i = 0;
            for (int j = 0; sIx <= eIx - pad; j++)
                i |= IA[chars.charAt(sIx++)] << (18 - j * 6);

            for (int r = 16; d < len; r -= 8)
                bytes[d++] = (byte) (i >> r);
        }

        return bytes;
    }

    public final static boolean[] firstIdentifierFlags = new boolean[256];
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

    public final static boolean[] identifierFlags = new boolean[256];

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
}
