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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;

//这个类，为了性能优化做了很多特别处理，一切都是为了性能！！！

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class JSONScanner extends JSONLexerBase {

    private final String text;
    private final int    len;

    public JSONScanner(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features){
        super(features);

        text = input;
        len = text.length();
        bp = -1;

        next();
        if (ch == 65279) { // utf-8 bom
            next();
        }
    }

    public final char charAt(int index) {
        if (index >= len) {
            return EOI;
        }

        return text.charAt(index);
    }

    public final char next() {
        int index = ++bp;
        return ch = (index >= this.len ? //
            EOI //
            : text.charAt(index));
    }

    public JSONScanner(char[] input, int inputLength){
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features){
        this(new String(input, 0, inputLength), features);
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        text.getChars(offset, offset + count, dest, 0);
    }

    static boolean charArrayCompare(String src, int offset, char[] dest) {
        final int destLen = dest.length;
        if (destLen + offset > src.length()) {
            return false;
        }

        for (int i = 0; i < destLen; ++i) {
            if (dest[i] != src.charAt(offset + i)) {
                return false;
            }
        }

        return true;
    }

    public final boolean charArrayCompare(char[] chars) {
        return charArrayCompare(text, bp, chars);
    }

    public final int indexOf(char ch, int startIndex) {
        return text.indexOf(ch, startIndex);
    }

    public final String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable) {
        return symbolTable.addSymbol(text, offset, len, hash);
    }

    public byte[] bytesValue() {
        return IOUtils.decodeBase64(text, np + 1, sp);
    }

    /**
     * The value of a literal token, recorded as a string. For integers, leading 0x and 'l' suffixes are suppressed.
     */
    public final String stringVal() {
        if (!hasSpecial) {
            return this.subString(np + 1, sp);
        } else {
            return new String(sbuf, 0, sp);
        }
    }

    public final String subString(int offset, int count) {
        if (ASMUtils.IS_ANDROID) {
            if (count < sbuf.length) {
                text.getChars(offset, offset + count, sbuf, 0);
                return new String(sbuf, 0, count);
            } else {
                char[] chars = new char[count];
                text.getChars(offset, offset + count, chars, 0);
                return new String(chars);
            }
        } else {
            return text.substring(offset, offset + count);
        }
    }

    public final char[] sub_chars(int offset, int count) {
        if (ASMUtils.IS_ANDROID && count < sbuf.length) {
            text.getChars(offset, offset + count, sbuf, 0);
            return sbuf;
        } else {
            char[] chars = new char[count];
            text.getChars(offset, offset + count, chars, 0);
            return chars;
        }
    }

    public final String numberString() {
        char chLocal = charAt(np + sp - 1);

        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }

        return this.subString(np, sp);
    }

    public final BigDecimal decimalValue() {
        char chLocal = charAt(np + sp - 1);

        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }

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

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = len - bp;

        if ((!strict) && rest > 13) {
            char c0 = charAt(bp);
            char c1 = charAt(bp + 1);
            char c2 = charAt(bp + 2);
            char c3 = charAt(bp + 3);
            char c4 = charAt(bp + 4);
            char c5 = charAt(bp + 5);

            char c_r0 = charAt(bp + rest - 1);
            char c_r1 = charAt(bp + rest - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/'
                && c_r1 == ')') {
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
                long millis = Long.parseLong(numberText);

                calendar = Calendar.getInstance(timeZone, locale);
                calendar.setTimeInMillis(millis);

                token = JSONToken.LITERAL_ISO8601_DATE;
                return true;
            }
        }

        if (rest == 8 || rest == 14 || rest == 17) {
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

                    millis = (S0 - '0') * 100 + (S1 - '0') * 10 + (S2 - '0');
                } else {
                    millis = 0;
                }

                hour = (h0 - '0') * 10 + (h1 - '0');
                minute = (m0 - '0') * 10 + (m1 - '0');
                seconds = (s0 - '0') * 10 + (s1 - '0');
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis = 0;
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, millis);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        }

        if (rest < 9) {
            return false;
        }

        char c0 = charAt(bp);
        char c1 = charAt(bp + 1);
        char c2 = charAt(bp + 2);
        char c3 = charAt(bp + 3);
        char c4 = charAt(bp + 4);
        char c5 = charAt(bp + 5);
        char c6 = charAt(bp + 6);
        char c7 = charAt(bp + 7);
        char c8 = charAt(bp + 8);
        char c9 = charAt(bp + 9);

        char y0, y1, y2, y3, M0, M1, d0, d1;
        if ((c4 == '-' && c7 == '-') // cn
                ||  (c4 == '/' && c7 == '/') // tw yyyy/mm/dd
                ) {
            y0 = c0;
            y1 = c1;
            y2 = c2;
            y3 = c3;
            M0 = c5;
            M1 = c6;
            d0 = c8;
            d1 = c9;
        } else if ((c2 == '.' && c5 == '.') // de dd.mm.yyyy
                || (c2 == '-' && c5 == '-') // in dd-mm-yyyy
                ) { 
            d0 = c0;
            d1 = c1;
            M0 = c3;
            M1 = c4;
            y0 = c6;
            y1 = c7;
            y2 = c8;
            y3 = c9;
        } else {
            if (c4 == '年' || c4 == '년') {
                y0 = c0;
                y1 = c1;
                y2 = c2;
                y3 = c3;

                if (c7 == '月' || c7 == '월') {
                    M0 = c5;
                    M1 = c6;
                    if (c9 == '日' || c9 == '일') {
                        d0 = '0';
                        d1 = c8;
                    } else if (charAt(bp + 10) == '日' || charAt(bp + 10) == '일'){
                        d0 = c8;
                        d1 = c9;
                    } else {
                        return false;
                    }
                } else if (c6 == '月' || c6 == '월') {
                    M0 = '0';
                    M1 = c5;
                    if (c8 == '日' || c8 == '일') {
                        d0 = '0';
                        d1 = c7;
                    } else if (c9 == '日' || c9 == '일'){
                        d0 = c7;
                        d1 = c8;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
            return false;
        }

        setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

        char t = charAt(bp + 10);
        if (t == 'T' || (t == ' ' && !strict)) {
            if (rest < 19) { // "0000-00-00T00:00:00".length()
                return false;
            }
        } else if (t == '"' || t == EOI || t == '日' || t == '일') {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 10);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        } else if (t == '+' || t == '-') {
            if (len == 16) {
                if (charAt(bp + 13) != ':' //
                    || charAt(bp + 14) != '0' //
                    || charAt(bp + 15) != '0') {
                    return false;
                }

                setTime('0', '0', '0', '0', '0', '0');
                calendar.set(Calendar.MILLISECOND, 0);
                setTimeZone(t, charAt(bp + 11), charAt(bp + 12));
                return true;
            }
            return false;
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

        setTime(h0, h1, m0, m1, s0, s1);

        char dot = charAt(bp + 19);
        if (dot == '.') {
            if (rest < 21) { //  // 0000-00-00T00:00:00.000
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
        int millis = S0 - '0';
        int millisLen = 1;

        if (rest > 21) {
            char S1 = charAt(bp + 21);
            if (S1 >= '0' && S1 <= '9') {
                millis = millis * 10 + (S1 - '0');
                millisLen = 2;
            }
        }

        if (millisLen == 2) {
            char S2 = charAt(bp + 22);
            if (S2 >= '0' && S2 <= '9') {
                millis = millis * 10 + (S2 - '0');
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

            setTimeZone(timeZoneFlag, t0, t1);

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

    protected void setTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        int hour = (h0 - '0') * 10 + (h1 - '0');
        int minute = (m0 - '0') * 10 + (m1 - '0');
        int seconds = (s0 - '0') * 10 + (s1 - '0');
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);
    }

    protected void setTimeZone(char timeZoneFlag, char t0, char t1) {
        int timeZoneOffset = ((t0 - '0') * 10 + (t1 - '0')) * 3600 * 1000;
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
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
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

    @Override
    public boolean isEOF() {
        return bp == len || ch == EOI && bp + 1 == len;
    }

    public int scanFieldInt(char[] fieldName) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        boolean negative = false;
        if (ch == '-') {
            ch = charAt(index++);
            negative = true;
        }

        int value;
        if (ch >= '0' && ch <= '9') {
            value = ch - '0';
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + (ch - '0');
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    break;
                }
            }

            if (value < 0) {
                matchStat = NOT_MATCH;
                return 0;
            }

            if (ch == ',' || ch == '}') {
                bp = index - 1;
            }
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return negative ? -value : value;
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

        return negative ? -value : value;
    }

    public String scanFieldString(char[] fieldName) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

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

        final String strVal;
        {
            int startIndex = index;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            String stringVal = subString(startIndex, endIndex - startIndex);
            if (stringVal.indexOf('\\') != -1) {
                for (;;) {
                    int slashCount = 0;
                    for (int i = endIndex - 1; i >= 0; --i) {
                        if (charAt(i) == '\\') {
                            slashCount++;
                        } else {
                            break;
                        }
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('"', endIndex + 1);
                }

                int chars_len = endIndex - (bp + fieldName.length + 1);
                char[] chars = sub_chars(bp + fieldName.length + 1, chars_len);

                stringVal = readString(chars, chars_len);
            }

            ch = charAt(endIndex + 1);

            if (ch == ',' || ch == '}') {
                bp = endIndex + 1;
                this.ch = ch;
                strVal = stringVal;
            } else {
                matchStat = NOT_MATCH;

                return stringDefaultValue();
            }
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return strVal;
        } else {
            //condition ch == '}' is always 'true'
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
        }
        return strVal;
    }

    public long scanFieldSymbol(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);
        if (ch != '"') {
            matchStat = NOT_MATCH;
            return 0;
        }

        long hash = 0x811c9dc5;
        for (;;) {
            ch = charAt(index++);
            if (ch == '\"') {
                bp = index;
                this.ch = ch = charAt(bp);
                break;
            } else if (index > len) {
                matchStat = NOT_MATCH;
                return 0;
            }

            hash ^= ch;
            hash *= 0x1000193;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return hash;
        } else if (ch == '}') {
            next();
            skipWhitespace();
            ch = getCurrent();
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
                return 0;
            }
            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        return hash;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        matchStat = UNKNOWN;

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

        if (ch == '[') {
            ch = charAt(index++);

            for (;;) {
                if (ch == '"') {
                    int startIndex = index;
                    int endIndex = indexOf('"', startIndex);
                    if (endIndex == -1) {
                        throw new JSONException("unclosed str");
                    }

                    String stringVal = subString(startIndex, endIndex - startIndex);
                    if (stringVal.indexOf('\\') != -1) {
                        for (;;) {
                            int slashCount = 0;
                            for (int i = endIndex - 1; i >= 0; --i) {
                                if (charAt(i) == '\\') {
                                    slashCount++;
                                } else {
                                    break;
                                }
                            }
                            if (slashCount % 2 == 0) {
                                break;
                            }
                            endIndex = indexOf('"', endIndex + 1);
                        }

                        int chars_len = endIndex - startIndex;
                        char[] chars = sub_chars(startIndex, chars_len);

                        stringVal = readString(chars, chars_len);
                    }

                    index = endIndex + 1;
                    ch = charAt(index++);

                    list.add(stringVal);
                } else if (ch == 'n' && text.startsWith("ull", index)) {
                    index += 3;
                    ch = charAt(index++);
                    list.add(null);
                } else if (ch == ']' && list.size() == 0) {
                    ch = charAt(index++);
                    break;
                } else {
                    matchStat = NOT_MATCH;
                    return null;
                }

                if (ch == ',') {
                    ch = charAt(index++);
                    continue;
                }

                if (ch == ']') {
                    ch = charAt(index++);
                    while (isWhitespace(ch)) {
                        ch = charAt(index++);
                    }
                    break;
                }

                matchStat = NOT_MATCH;
                return null;
            }
        } else if (text.startsWith("ull", index)) {
            index += 3;
            ch = charAt(index++);
            list = null;
        } else {
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
            for (;;) {
                if (ch == ',') {
                    token = JSONToken.COMMA;
                    this.ch = charAt(++bp);
                    break;
                } else if (ch == ']') {
                    token = JSONToken.RBRACKET;
                    this.ch = charAt(++bp);
                    break;
                } else if (ch == '}') {
                    token = JSONToken.RBRACE;
                    this.ch = charAt(++bp);
                    break;
                } else if (ch == EOI) {
                    token = JSONToken.EOF;
                    this.ch = ch;
                    break;
                } else {
                    boolean space = false;
                    while (isWhitespace(ch)) {
                        ch = charAt(index++);
                        bp = index;
                        space = true;
                    }
                    if (space) {
                        continue;
                    }

                    matchStat = NOT_MATCH;
                    return null;
                }
            }

            matchStat = END;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        return list;
    }

    public long scanFieldLong(char[] fieldName) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return 0;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        boolean negative = false;
        if (ch == '-') {
            ch = charAt(index++);
            negative = true;
        }

        long value;
        if (ch >= '0' && ch <= '9') {
            value = ch - '0';
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    value = value * 10 + (ch - '0');
                } else if (ch == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    if (ch == ',' || ch == '}') {
                        bp = index - 1;
                    }
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
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return negative ? -value : value;
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

        return negative ? -value : value;
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        matchStat = UNKNOWN;

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

    public final int scanInt(char expectNext) {
        matchStat = UNKNOWN;

        int offset = bp;
        char chLocal = charAt(offset++);

        final boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(offset++);
        }

        int value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(offset++);
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
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

        for (;;) {
            if (chLocal == expectNext) {
                bp = offset;
                this.ch = charAt(bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return negative ? -value : value;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset++);
                    continue;
                }
                matchStat = NOT_MATCH;
                return negative ? -value : value;
            }
        }
    }

    public long scanLong(char expectNextChar) {
        matchStat = UNKNOWN;

        int offset = bp;
        char chLocal = charAt(offset++);

        final boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(offset++);
        }

        long value;
        if (chLocal >= '0' && chLocal <= '9') {
            value = chLocal - '0';
            for (;;) {
                chLocal = charAt(offset++);
                if (chLocal >= '0' && chLocal <= '9') {
                    value = value * 10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
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

        for (;;) {
            if (chLocal == expectNextChar) {
                bp = offset;
                this.ch = charAt(bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return negative ? -value : value;
            } else {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset++);
                    continue;
                }

                matchStat = NOT_MATCH;
                return value;
            }
        }
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        text.getChars(srcPos, srcPos + length, dest, destPos);
    }

    public String info() {
        return "pos " + bp //
               + ", json : " //
               + (text.length() < 65536 //
                   ? text //
                   : text.substring(0, 65536));
    }
}
