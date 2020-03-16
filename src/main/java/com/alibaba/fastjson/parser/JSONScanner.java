/*
 * Copyright 1999-2017 Alibaba Group.
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;

import java.math.BigDecimal;
import java.util.*;

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
        if (token == JSONToken.HEX) {
            int start = np + 1, len = sp;
            if (len % 2 != 0) {
                throw new JSONException("illegal state. " + len);
            }

            byte[] bytes = new byte[len / 2];
            for (int i = 0; i < bytes.length; ++i) {
                char c0 = text.charAt(start + i * 2);
                char c1 = text.charAt(start + i * 2 + 1);

                int b0 = c0 - (c0 <= 57 ? 48 : 55);
                int b1 = c1 - (c1 <= 57 ? 48 : 55);
                bytes[i] = (byte) ((b0 << 4) | b1);
            }

            return bytes;
        }

        if (!hasSpecial) {
            return IOUtils.decodeBase64(text, np + 1, sp);
        } else {
            String escapedText = new String(sbuf, 0, sp);
            return IOUtils.decodeBase64(escapedText);
        }
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
        return scanISO8601DateIfMatch(strict, rest);
    }

    private boolean scanISO8601DateIfMatch(boolean strict, int rest) {
        if (rest < 8) {
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

        if ((!strict) && rest > 13) {
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
                String numberText = this.subString(offset, bp + plusIndex - offset);
                long millis = Long.parseLong(numberText);

                calendar = Calendar.getInstance(timeZone, locale);
                calendar.setTimeInMillis(millis);

                token = JSONToken.LITERAL_ISO8601_DATE;
                return true;
            }
        }

        char c10;
        if (rest == 8
                || rest == 14
                || (rest == 16 && ((c10 = charAt(bp + 10)) == 'T' || c10 == ' '))
                || (rest == 17 && charAt(bp + 6) != '-')) {
            if (strict) {
                return false;
            }

            char y0, y1, y2, y3, M0, M1, d0, d1;



            char c8 = charAt(bp + 8);

            final boolean c_47 = c4 == '-' && c7 == '-';
            final boolean sperate16 = c_47 && rest == 16;
            final boolean sperate17 = c_47 && rest == 17;
            if (sperate17 || sperate16) {
                y0 = c0;
                y1 = c1;
                y2 = c2;
                y3 = c3;
                M0 = c5;
                M1 = c6;
                d0 = c8;
                d1 = charAt(bp + 9);
            } else if (c4 == '-' && c6 == '-') {
                y0 = c0;
                y1 = c1;
                y2 = c2;
                y3 = c3;
                M0 = '0';
                M1 = c5;
                d0 = '0';
                d1 = c7;
            } else {
                y0 = c0;
                y1 = c1;
                y2 = c2;
                y3 = c3;
                M0 = c4;
                M1 = c5;
                d0 = c6;
                d1 = c7;
            }


            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }

            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);

            int hour, minute, seconds, millis;
            if (rest != 8) {
                char c9 = charAt(bp + 9);
                c10 = charAt(bp + 10);
                char c11 = charAt(bp + 11);
                char c12 = charAt(bp + 12);
                char c13 = charAt(bp + 13);

                char h0, h1, m0, m1, s0, s1;

                if ((sperate17 && c10 == 'T' && c13 == ':' && charAt(bp + 16) == 'Z')
                        || (sperate16 && (c10 == ' ' || c10 == 'T') && c13 == ':')) {
                    h0 = c11;
                    h1 = c12;
                    m0 = charAt(bp + 14);
                    m1 = charAt(bp + 15);
                    s0 = '0';
                    s1 = '0';
                } else {
                    h0 = c8;
                    h1 = c9;
                    m0 = c10;
                    m1 = c11;
                    s0 = c12;
                    s1 = c13;
                }

                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }

                if (rest == 17 && !sperate17) {
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

        char c8 = charAt(bp + 8);
        char c9 = charAt(bp + 9);

        int date_len = 10;
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

            if (c9 == ' ') {
                d0 = '0';
                d1 = c8;
                date_len = 9;
            } else {
                d0 = c8;
                d1 = c9;
            }
        } else if ((c4 == '-' && c6 == '-') // cn yyyy-m-dd
        ) {
            y0 = c0;
            y1 = c1;
            y2 = c2;
            y3 = c3;
            M0 = '0';
            M1 = c5;

            if (c8 == ' ') {
                d0 = '0';
                d1 = c7;
                date_len = 8;
            } else {
                d0 = c7;
                d1 = c8;
                date_len = 9;
            }
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
        } else if (c8 == 'T') {
            y0 = c0;
            y1 = c1;
            y2 = c2;
            y3 = c3;
            M0 = c4;
            M1 = c5;
            d0 = c6;
            d1 = c7;
            date_len = 8;
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
                        date_len = 11;
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

        char t = charAt(bp + date_len);
        if (t == 'T' && rest == 16 && date_len == 8 && charAt(bp + 15) == 'Z') {
            char h0 = charAt(bp + date_len + 1);
            char h1 = charAt(bp + date_len + 2);
            char m0 = charAt(bp + date_len + 3);
            char m1 = charAt(bp + date_len + 4);
            char s0 = charAt(bp + date_len + 5);
            char s1 = charAt(bp + date_len + 6);

            if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                return false;
            }

            setTime(h0, h1, m0, m1, s0, s1);
            calendar.set(Calendar.MILLISECOND, 0);

            if (calendar.getTimeZone().getRawOffset() != 0) {
                String[] timeZoneIDs = TimeZone.getAvailableIDs(0);
                if (timeZoneIDs.length > 0) {
                    TimeZone timeZone = TimeZone.getTimeZone(timeZoneIDs[0]);
                    calendar.setTimeZone(timeZone);
                }
            }

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        } else if (t == 'T' || (t == ' ' && !strict)) {
            if (rest < date_len + 9) { // "0000-00-00T00:00:00".length()
                return false;
            }
        } else if (t == '"' || t == EOI || t == '日' || t == '일') {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += date_len);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        } else if (t == '+' || t == '-') {
            if (len == date_len + 6) {
                if (charAt(bp + date_len + 3) != ':' //
                        || charAt(bp + date_len + 4) != '0' //
                        || charAt(bp + date_len + 5) != '0') {
                    return false;
                }

                setTime('0', '0', '0', '0', '0', '0');
                calendar.set(Calendar.MILLISECOND, 0);
                setTimeZone(t, charAt(bp + date_len + 1), charAt(bp + date_len + 2));
                return true;
            }
            return false;
        } else {
            return false;
        }

        if (charAt(bp + date_len + 3) != ':') {
            return false;
        }
        if (charAt(bp + date_len + 6) != ':') {
            return false;
        }

        char h0 = charAt(bp + date_len + 1);
        char h1 = charAt(bp + date_len + 2);
        char m0 = charAt(bp + date_len + 4);
        char m1 = charAt(bp + date_len + 5);
        char s0 = charAt(bp + date_len + 7);
        char s1 = charAt(bp + date_len + 8);

        if (!checkTime(h0, h1, m0, m1, s0, s1)) {
            return false;
        }

        setTime(h0, h1, m0, m1, s0, s1);

        char dot = charAt(bp + date_len + 9);
        int millisLen = -1; // 有可能没有毫秒区域，没有毫秒区域的时候下一个字符位置有可能是'Z'、'+'、'-'
        int millis = 0;
        if (dot == '.') { // 0000-00-00T00:00:00.000
            if (rest < date_len + 11) {
                return false;
            }

            char S0 = charAt(bp + date_len + 10);
            if (S0 < '0' || S0 > '9') {
                return false;
            }
            millis = S0 - '0';
            millisLen = 1;

            if (rest > date_len + 11) {
                char S1 = charAt(bp + date_len + 11);
                if (S1 >= '0' && S1 <= '9') {
                    millis = millis * 10 + (S1 - '0');
                    millisLen = 2;
                }
            }

            if (millisLen == 2) {
                char S2 = charAt(bp + date_len + 12);
                if (S2 >= '0' && S2 <= '9') {
                    millis = millis * 10 + (S2 - '0');
                    millisLen = 3;
                }
            }
        }
        calendar.set(Calendar.MILLISECOND, millis);

        int timzeZoneLength = 0;
        char timeZoneFlag = charAt(bp + date_len + 10 + millisLen);

        if (timeZoneFlag == ' ') {
            millisLen++;
            timeZoneFlag = charAt(bp + date_len + 10 + millisLen);
        }

        if (timeZoneFlag == '+' || timeZoneFlag == '-') {
            char t0 = charAt(bp + date_len + 10 + millisLen + 1);
            if (t0 < '0' || t0 > '1') {
                return false;
            }

            char t1 = charAt(bp + date_len + 10 + millisLen + 2);
            if (t1 < '0' || t1 > '9') {
                return false;
            }

            char t2 = charAt(bp + date_len + 10 + millisLen + 3);
            char t3 = '0', t4 = '0';
            if (t2 == ':') { // ThreeLetterISO8601TimeZone
                t3 = charAt(bp + date_len + 10 + millisLen + 4);
                t4 = charAt(bp + date_len + 10 + millisLen + 5);

                if(t3 == '4' && t4 == '5') {
                    // handle some special timezones like xx:45

                    if (t0 == '1' && (t1 == '2' || t1 == '3')) {
                        // NZ-CHAT          => +12:45
                        // Pacific/Chatham  => +12:45
                        // NZ-CHAT          => +13:45 (DST)
                        // Pacific/Chatham  => +13:45 (DST)
                    } else if (t0 == '0' && (t1 == '5' || t1 == '8')) {
                        // Asia/Kathmandu   => +05:45
                        // Asia/Katmandu    => +05:45
                        // Australia/Eucla  => +08:45
                    } else {
                        return false;
                    }
                } else {
                    //handle normal timezone like xx:00 and xx:30
                    if (t3 != '0' && t3 != '3') {
                        return false;
                    }

                    if (t4 != '0') {
                        return false;
                    }
                }

                timzeZoneLength = 6;
            } else if (t2 == '0') { // TwoLetterISO8601TimeZone
                t3 = charAt(bp + date_len + 10 + millisLen + 4);
                if (t3 != '0' && t3 != '3') {
                    return false;
                }
                timzeZoneLength = 5;
            } else if (t2 == '3' && charAt(bp + date_len + 10 + millisLen + 4) == '0') {
                t3 = '3';
                t4 = '0';
                timzeZoneLength = 5;
            } else if (t2 == '4' && charAt(bp + date_len + 10 + millisLen + 4) == '5') {
                t3 = '4';
                t4 = '5';
                timzeZoneLength = 5;
            } else {
                timzeZoneLength = 3;
            }

            setTimeZone(timeZoneFlag, t0, t1, t3, t4);

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

        char end = charAt(bp + (date_len + 10 + millisLen + timzeZoneLength));
        if (end != EOI && end != '"') {
            return false;
        }
        ch = charAt(bp += (date_len + 10 + millisLen + timzeZoneLength));

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
        setTimeZone(timeZoneFlag, t0, t1, '0', '0');
    }

    protected void setTimeZone(char timeZoneFlag, char t0, char t1, char t3, char t4) {
        int timeZoneOffset = ((t0 - '0') * 10 + (t1 - '0')) * 3600 * 1000;

        timeZoneOffset += ((t3 - '0') * 10 + (t4 - '0')) * 60 * 1000;

        if (timeZoneFlag == '-') {
            timeZoneOffset = -timeZoneOffset;
        }

        if (calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
            calendar.setTimeZone(new SimpleTimeZone(timeZoneOffset, Integer.toString(timeZoneOffset)));
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
//        calendar.set(year, month, day);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if (y0 < '0' || y0 > '9') {
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
        return bp == len || (ch == EOI && bp + 1 >= len);
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

        final boolean quote = ch == '"';

        if (quote) {
            ch = charAt(index++);
        }

        final boolean negative = ch == '-';
        if (negative) {
            ch = charAt(index++);
        }

        int value;
        if (ch >= '0' && ch <= '9') {
            value = ch - '0';
            for (;;) {
                ch = charAt(index++);
                if (ch >= '0' && ch <= '9') {
                    int value_10 = value * 10;
                    if (value_10 < value) {
                        matchStat = NOT_MATCH;
                        return 0;
                    }

                    value = value_10 + (ch - '0');
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

            if (quote) {
                if (ch != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    ch = charAt(index++);
                }
            }

            for (;;) {
                if (ch == ',' || ch == '}') {
                    bp = index - 1;
                    break;
                } else if(isWhitespace(ch)) {
                    ch = charAt(index++);
                    continue;
                } else {
                    matchStat = NOT_MATCH;
                    return 0;
                }
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
            bp = index - 1;
            ch = charAt(++bp);
            for (; ; ) {
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
                    break;
                } else if (isWhitespace(ch)) {
                    ch = charAt(++bp);
                    continue;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
                    matchStat = NOT_MATCH;
                    return 0;
                }
            }
            matchStat = END;
        }

        return negative ? -value : value;
    }

    public String scanFieldString(char[] fieldName) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        for (;;) {
            if (!charArrayCompare(text, bp, fieldName)) {
                if (isWhitespace(ch)) {
                    next();
                    continue;
                }
                matchStat = NOT_MATCH_NAME;
                return stringDefaultValue();
            } else {
                break;
            }
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

            for (;;) {
                if (ch == ',' || ch == '}') {
                    bp = endIndex + 1;
                    this.ch = ch;
                    strVal = stringVal;
                    break;
                } else if (isWhitespace(ch)) {
                    endIndex++;
                    ch = charAt(endIndex + 1);
                } else {
                    matchStat = NOT_MATCH;

                    return stringDefaultValue();
                }
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

    public java.util.Date scanFieldDate(char[] fieldName) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        int index = bp + fieldName.length;

        char ch = charAt(index++);

        final java.util.Date dateVal;
        if (ch == '"') {
            int startIndex = index;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int rest = endIndex - startIndex;
            bp = index;
            if (scanISO8601DateIfMatch(false, rest)) {
                dateVal = calendar.getTime();
            } else {
                bp = startPos;
                matchStat = NOT_MATCH;
                return null;
            }
            ch = charAt(endIndex + 1);
            bp = startPos;

            for (; ; ) {
                if (ch == ',' || ch == '}') {
                    bp = endIndex + 1;
                    this.ch = ch;
                    break;
                } else if (isWhitespace(ch)) {
                    endIndex++;
                    ch = charAt(endIndex + 1);
                } else {
                    matchStat = NOT_MATCH;

                    return null;
                }
            }
        } else if (ch == '-' || (ch >= '0' && ch <= '9')) {
            long millis = 0;

            boolean negative = false;
            if (ch == '-') {
                ch = charAt(index++);
                negative = true;
            }

            if (ch >= '0' && ch <= '9') {
                millis = ch - '0';
                for (; ; ) {
                    ch = charAt(index++);
                    if (ch >= '0' && ch <= '9') {
                        millis = millis * 10 + (ch - '0');
                    } else {
                        if (ch == ',' || ch == '}') {
                            bp = index - 1;
                        }
                        break;
                    }
                }
            }

            if (millis < 0) {
                matchStat = NOT_MATCH;
                return null;
            }

            if (negative) {
                millis = -millis;
            }

            dateVal = new java.util.Date(millis);
        } else {
            matchStat = NOT_MATCH;

            return null;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return dateVal;
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
                return null;
            }
            matchStat = END;
        }
        return dateVal;
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

        long hash = 0xcbf29ce484222325L;
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
            hash *= 0x100000001b3L;
        }

        for (;;) {
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
                break;
            } else if (isWhitespace(ch)) {
                ch = charAt(++bp);
                continue;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
        }

        return hash;
    }

    @SuppressWarnings("unchecked")
    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        matchStat = UNKNOWN;

        while (ch == '\n' || ch == ' ') {
            int index = ++bp;
            ch = (index >= this.len ? //
                    EOI //
                    : text.charAt(index));
        }

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return null;
        }

        Collection<String> list = newCollectionByType(type);

//        if (type.isAssignableFrom(HashSet.class)) {
//            list = new HashSet<String>();
//        } else if (type.isAssignableFrom(ArrayList.class)) {
//            list = new ArrayList<String>();
//        } else {
//            try {
//                list = (Collection<String>) type.newInstance();
//            } catch (Exception e) {
//                throw new JSONException(e.getMessage(), e);
//            }
//        }

        int startPos = this.bp;
        char startChar = this.ch;

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
            this.ch = startChar;
            bp = startPos;
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

        final boolean quote = ch == '"';
        if (quote) {
            ch = charAt(index++);
        }

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
                    if (quote) {
                        if (ch != '"') {
                            matchStat = NOT_MATCH;
                            return 0;
                        } else {
                            ch = charAt(index++);
                        }
                    }

                    if (ch == ',' || ch == '}') {
                        bp = index - 1;
                    }
                    break;
                }
            }

            boolean valid = value >= 0 || (value == -9223372036854775808L && negative);
            if (!valid) {
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

        for (;;) {
            if (ch == ',') {
                this.ch = charAt(++bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                return negative ? -value : value;
            } else if (ch == '}') {
                ch = charAt(++bp);
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
                        break;
                    } else if (isWhitespace(ch)) {
                        ch = charAt(++bp);
                    } else {
                        this.bp = startPos;
                        this.ch = startChar;
                        matchStat = NOT_MATCH;
                        return 0;
                    }
                }
                matchStat = END;
                break;
            } else if (isWhitespace(ch)) {
                bp = index;
                ch = charAt(index++);
                continue;
            } else {
                matchStat = NOT_MATCH;
                return 0;
            }
        }

        return negative ? -value : value;
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        matchStat = UNKNOWN;

        if (!charArrayCompare(text, bp, fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return false;
        }

        int startPos = bp;
        int index = bp + fieldName.length;

        char ch = charAt(index++);

        final boolean quote = ch == '"';
        if (quote) {
            ch = charAt(index++);
        }

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

            if (quote && charAt(index++) != '"') {
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

            if (quote && charAt(index++) != '"') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = false;
        } else if (ch == '1') {
            if (quote && charAt(index++) != '"') {
                matchStat = NOT_MATCH;
                return false;
            }

            bp = index;
            ch = charAt(bp);
            value = true;
        } else if (ch == '0') {
            if (quote && charAt(index++) != '"') {
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

        for (;;) {
            if (ch == ',') {
                this.ch = charAt(++bp);
                matchStat = VALUE;
                token = JSONToken.COMMA;
                break;
            } else if (ch == '}') {
                ch = charAt(++bp);
                for (;;) {
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
                    } else if (isWhitespace(ch)) {
                        ch = charAt(++bp);
                        continue;
                    } else {
                        matchStat = NOT_MATCH;
                        return false;
                    }
                    break;
                }
                matchStat = END;
                break;
            } else if (isWhitespace(ch)) {
                ch = charAt(++bp);
            } else {
                bp = startPos;
                ch = charAt(bp);
                matchStat = NOT_MATCH;
                return false;
            }
        }

        return value;
    }

    public final int scanInt(char expectNext) {
        matchStat = UNKNOWN;

        final int mark = bp;
        int offset = bp;
        char chLocal = charAt(offset++);

        while (isWhitespace(chLocal)) {
            chLocal = charAt(offset++);
        }

        final boolean quote = chLocal == '"';

        if (quote) {
            chLocal = charAt(offset++);
        }

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
                    int value_10 = value * 10;
                    if (value_10 < value) {
                        throw new JSONException("parseInt error : "
                                + subString(mark, offset - 1));
                    }
                    value = value_10 + (chLocal - '0');
                } else if (chLocal == '.') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    if (quote) {
                        if (chLocal != '"') {
                            matchStat = NOT_MATCH;
                            return 0;
                        } else {
                            chLocal = charAt(offset++);
                        }
                    }
                    break;
                }
            }
            if (value < 0) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else if (chLocal == 'n'
                && charAt(offset++) == 'u'
                && charAt(offset++) == 'l'
                && charAt(offset++) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            chLocal = charAt(offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
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

    public  double scanDouble(char seperator) {
        matchStat = UNKNOWN;

        int offset = bp;
        char chLocal = charAt(offset++);
        final boolean quote = chLocal == '"';
        if (quote) {
            chLocal = charAt(offset++);
        }

        boolean negative = chLocal == '-';
        if (negative) {
            chLocal = charAt(offset++);
        }

        double value;
        if (chLocal >= '0' && chLocal <= '9') {
            long intVal = chLocal - '0';
            for (; ; ) {
                chLocal = charAt(offset++);
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    continue;
                } else {
                    break;
                }
            }

            long power = 1;
            boolean small = (chLocal == '.');
            if (small) {
                chLocal = charAt(offset++);
                if (chLocal >= '0' && chLocal <= '9') {
                    intVal = intVal * 10 + (chLocal - '0');
                    power = 10;
                    for (; ; ) {
                        chLocal = charAt(offset++);
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
                chLocal = charAt(offset++);
                if (chLocal == '+' || chLocal == '-') {
                    chLocal = charAt(offset++);
                }
                for (; ; ) {
                    if (chLocal >= '0' && chLocal <= '9') {
                        chLocal = charAt(offset++);
                    } else {
                        break;
                    }
                }
            }

            int start, count;
            if (quote) {
                if (chLocal != '"') {
                    matchStat = NOT_MATCH;
                    return 0;
                } else {
                    chLocal = charAt(offset++);
                }
                start = bp + 1;
                count = offset - start - 2;
            } else {
                start = bp;
                count = offset - start - 1;
            }

            if (!exp && count < 18) {
                value = ((double) intVal) / power;
                if (negative) {
                    value = -value;
                }
            } else {
                String text = this.subString(start, count);
                value = Double.parseDouble(text);
            }
        } else if (chLocal == 'n'
                && charAt(offset++) == 'u'
                && charAt(offset++) == 'l'
                && charAt(offset++) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            chLocal = charAt(offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        if (chLocal == seperator) {
            bp = offset;
            this.ch = this.charAt(bp);
            matchStat = VALUE;
            token = JSONToken.COMMA;
            return value;
        } else {
            matchStat = NOT_MATCH;
            return value;
        }
    }

    public long scanLong(char seperator) {
        matchStat = UNKNOWN;

        int offset = bp;
        char chLocal = charAt(offset++);
        final boolean quote = chLocal == '"';

        if (quote) {
            chLocal = charAt(offset++);
        }

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
                    if (quote) {
                        if (chLocal != '"') {
                            matchStat = NOT_MATCH;
                            return 0;
                        } else {
                            chLocal = charAt(offset++);
                        }
                    }
                    break;
                }
            }

            boolean valid = value >= 0 || (value == -9223372036854775808L && negative);
            if (!valid) {
                matchStat = NOT_MATCH;
                return 0;
            }
        } else if (chLocal == 'n'
                && charAt(offset++) == 'u'
                && charAt(offset++) == 'l'
                && charAt(offset++) == 'l') {
            matchStat = VALUE_NULL;
            value = 0;
            chLocal = charAt(offset++);

            if (quote && chLocal == '"') {
                chLocal = charAt(offset++);
            }

            for (;;) {
                if (chLocal == ',') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.COMMA;
                    return value;
                } else if (chLocal == ']') {
                    bp = offset;
                    this.ch = charAt(bp);
                    matchStat = VALUE_NULL;
                    token = JSONToken.RBRACKET;
                    return value;
                } else if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset++);
                    continue;
                }
                break;
            }
            matchStat = NOT_MATCH;
            return 0;
        } else {
            matchStat = NOT_MATCH;
            return 0;
        }

        for (;;) {
            if (chLocal == seperator) {
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

    public java.util.Date scanDate(char seperator) {
        matchStat = UNKNOWN;
        int startPos = this.bp;
        char startChar = this.ch;

        int index = bp;

        char ch = charAt(index++);

        final java.util.Date dateVal;
        if (ch == '"') {
            int startIndex = index;
            int endIndex = indexOf('"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }

            int rest = endIndex - startIndex;
            bp = index;
            if (scanISO8601DateIfMatch(false, rest)) {
                dateVal = calendar.getTime();
            } else {
                bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return null;
            }
            ch = charAt(endIndex + 1);
            bp = startPos;

            for (; ; ) {
                if (ch == ',' || ch == ']') {
                    bp = endIndex + 1;
                    this.ch = ch;
                    break;
                } else if (isWhitespace(ch)) {
                    endIndex++;
                    ch = charAt(endIndex + 1);
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
                    matchStat = NOT_MATCH;

                    return null;
                }
            }
        } else if (ch == '-' || (ch >= '0' && ch <= '9')) {
            long millis = 0;

            boolean negative = false;
            if (ch == '-') {
                ch = charAt(index++);
                negative = true;
            }

            if (ch >= '0' && ch <= '9') {
                millis = ch - '0';
                for (; ; ) {
                    ch = charAt(index++);
                    if (ch >= '0' && ch <= '9') {
                        millis = millis * 10 + (ch - '0');
                    } else {
                        if (ch == ',' || ch == ']') {
                            bp = index - 1;
                        }
                        break;
                    }
                }
            }

            if (millis < 0) {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return null;
            }

            if (negative) {
                millis = -millis;
            }

            dateVal = new java.util.Date(millis);
        } else if (ch == 'n'
                && charAt(index++) == 'u'
                && charAt(index++) == 'l'
                && charAt(index++) == 'l') {
            dateVal = null;
            ch = charAt(index);
            bp = index;
        } else {
            this.bp = startPos;
            this.ch = startChar;
            matchStat = NOT_MATCH;

            return null;
        }

        if (ch == ',') {
            this.ch = charAt(++bp);
            matchStat = VALUE;
            return dateVal;
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
                this.ch = EOI;
                token = JSONToken.EOF;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                matchStat = NOT_MATCH;
                return null;
            }
            matchStat = END;
        }
        return dateVal;
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        text.getChars(srcPos, srcPos + length, dest, destPos);
    }

    public String info() {
        StringBuilder buf = new StringBuilder();

//        buf.append("pos ").append(bp);
//        return "pos " + bp //
//                + ", json : " //
//                + (text.length() < 65536 //
//                ? text //
//                : text.substring(0, 65536));

        int line = 1;
        int column = 1;
        for (int i = 0; i < bp; ++i, column++) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                column = 1;
                line++;
            }
        }

        buf.append("pos ").append(bp)
                .append(", line ").append(line)
                .append(", column ").append(column);

        if (text.length() < 65535) {
            buf.append(text);
        } else {
            buf.append(text.substring(0, 65535));
        }

        return buf.toString();
    }

    // for hsf support
    public String[] scanFieldStringArray(char[] fieldName, int argTypesCount, SymbolTable typeSymbolTable) {
        int startPos = bp;
        char starChar = ch;

        while (isWhitespace(ch)) {
            next();
        }

        int offset;
        char ch;
        if (fieldName != null) {
            matchStat = UNKNOWN;
            if (!charArrayCompare(fieldName)) {
                matchStat = NOT_MATCH_NAME;
                return null;
            }

            offset = bp + fieldName.length;
            ch = text.charAt(offset++);
            while (isWhitespace(ch)) {
                ch = text.charAt(offset++);
            }

            if (ch == ':') {
                ch = text.charAt(offset++);
            } else {
                matchStat = NOT_MATCH;
                return null;
            }

            while (isWhitespace(ch)) {
                ch = text.charAt(offset++);
            }
        } else {
            offset = bp + 1;
            ch = this.ch;
        }

        if (ch == '[') {
            bp = offset;
            this.ch = text.charAt(bp);
        } else if (ch == 'n' && text.startsWith("ull", bp + 1)) {
            bp += 4;
            this.ch = text.charAt(bp);
            return null;
        } else {
            matchStat = NOT_MATCH;
            return null;
        }

        String[] types = argTypesCount >= 0 ? new String[argTypesCount] : new String[4];
        int typeIndex = 0;
        for (;;) {
            while (isWhitespace(this.ch)) {
                next();
            }

            if (this.ch != '\"') {
                this.bp = startPos;
                this.ch = starChar;
                matchStat = NOT_MATCH;
                return null;
            }

            String type = scanSymbol(typeSymbolTable, '"');
            if (typeIndex == types.length) {
                int newCapacity = types.length + (types.length >> 1) + 1;
                String[] array = new String[newCapacity];
                System.arraycopy(types, 0, array, 0, types.length);
                types = array;
            }
            types[typeIndex++] = type;
            while (isWhitespace(this.ch)) {
                next();
            }
            if (this.ch == ',') {
                next();
                continue;
            }
            break;
        }
        if (types.length != typeIndex) {
            String[] array = new String[typeIndex];
            System.arraycopy(types, 0, array, 0, typeIndex);
            types = array;
        }

        while (isWhitespace(this.ch)) {
            next();
        }

        if (this.ch == ']') {
            next();
        } else {
            this.bp = startPos;
            this.ch = starChar;
            matchStat = NOT_MATCH;
            return null;
        }

        return types;
    }

    public boolean matchField2(char[] fieldName) {
        while (isWhitespace(ch)) {
            next();
        }

        if (!charArrayCompare(fieldName)) {
            matchStat = NOT_MATCH_NAME;
            return false;
        }

        int offset = bp + fieldName.length;
        char ch = text.charAt(offset++);
        while (isWhitespace(ch)) {
            ch = text.charAt(offset++);
        }

        if (ch == ':') {
            this.bp = offset;
            this.ch = charAt(bp);
            return true;
        } else {
            matchStat = NOT_MATCH_NAME;
            return false;
        }
    }

    public final void skipObject() {
        skipObject(false);
    }

    public final void skipObject(boolean valid) {
        boolean quote = false;
        int braceCnt = 0;
        int i = bp;
        for (; i < text.length(); ++i) {
            final char ch = text.charAt(i);
            if (ch == '\\') {
                if (i < len - 1) {
                    ++i;
                    continue;
                } else {
                    this.ch = ch;
                    this.bp = i;
                    throw new JSONException("illegal str, " + info());
                }
            } else if (ch == '"') {
                quote = !quote;
            } else if (ch == '{') {
                if (quote) {
                    continue;
                }
                braceCnt++;
            } else if (ch == '}') {
                if (quote) {
                    continue;
                } else {
                    braceCnt--;
                }
                if (braceCnt == -1) {
                    this.bp = i + 1;
                    if (this.bp == text.length()) {
                        this.ch = EOI;
                        this.token = JSONToken.EOF;
                        return;
                    }
                    this.ch = text.charAt(this.bp);
                    if (this.ch == ',') {
                        token = JSONToken.COMMA;
                        int index = ++bp;
                        this.ch = (index >= text.length() //
                                ? EOI //
                                : text.charAt(index));
                        return;
                    } else if (this.ch == '}') {
                        token = JSONToken.RBRACE;
                        next();
                        return;
                    } else if (this.ch == ']') {
                        token = JSONToken.RBRACKET;
                        next();
                        return;
                    } else {
                        nextToken(JSONToken.COMMA);
                    }
                    return;
                }
            }
        }

        for (int j = 0; j < bp; j++) {
            if (j < text.length() && text.charAt(j) == ' ') {
                i++;
            }
        }

        if (i == text.length()) {
            throw new JSONException("illegal str, " + info());
        }
    }

    public final void skipArray() {
        skipArray(false);
    }

    public final void skipArray(boolean valid) {
        boolean quote = false;
        int bracketCnt = 0;
        int i = bp;
        for (; i < text.length(); ++i) {
            char ch = text.charAt(i);
            if (ch == '\\') {
                if (i < len - 1) {
                    ++i;
                    continue;
                } else {
                    this.ch = ch;
                    this.bp = i;
                    throw new JSONException("illegal str, " + info());
                }
            } else if (ch == '"') {
                quote = !quote;
            } else if (ch == '[') {
                if (quote) {
                    continue;
                }
                bracketCnt++;
            } else if (ch == '{' && valid) {
                {
                    int index = ++bp;
                    this.ch = (index >= text.length() //
                            ? EOI //
                            : text.charAt(index));
                }

                skipObject(valid);
            } else if (ch == ']') {
                if (quote) {
                    continue;
                } else {
                    bracketCnt--;
                }
                if (bracketCnt == -1) {
                    this.bp = i + 1;
                    if (this.bp == text.length()) {
                        this.ch = EOI;
                        token = JSONToken.EOF;
                        return;
                    }
                    this.ch = text.charAt(this.bp);
                    nextToken(JSONToken.COMMA);
                    return;
                }
            }
        }

        if (i == text.length()) {
            throw new JSONException("illegal str, " + info());
        }
    }

    public final void skipString() {
        if (ch == '"') {
            for (int i = bp + 1; i < text.length(); ++i) {
                char c = text.charAt(i);
                if (c == '\\') {
                    if (i < len - 1) {
                        ++i;
                        continue;
                    }
                } else if (c == '"') {
                    this.ch = text.charAt(bp = i + 1);
                    return;
                }
            }
            throw new JSONException("unclosed str");
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean seekArrayToItem(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index must > 0, but " + index);
        }

        if (token == JSONToken.EOF) {
            return false;
        }

        if (token != JSONToken.LBRACKET) {
            throw new UnsupportedOperationException();
        }
//        nextToken();

        for (int i = 0; i < index; ++i) {
            skipWhitespace();
            if (ch == '"' || ch == '\'') {
                skipString();
                if (ch == ',') {
                    next();
                    continue;
                } else if (ch == ']') {
                    next();
                    nextToken(JSONToken.COMMA);
                    return false;
                } else {
                    throw new JSONException("illegal json.");
                }
            } else if (ch == '{') {
                next();
                token = JSONToken.LBRACE;
                skipObject(false);
            } else if (ch == '[') {
                next();
                token = JSONToken.LBRACKET;
                skipArray(false);
            } else {
                boolean match = false;
                for (int j = bp + 1; j < text.length(); ++j) {
                    char c = text.charAt(j);
                    if (c == ',') {
                        match = true;
                        bp = j + 1;
                        ch = charAt(bp);
                        break;
                    } else if (c == ']') {
                        bp = j + 1;
                        ch = charAt(bp);
                        nextToken();
                        return false;
                    }
                }

                if (!match) {
                    throw new JSONException("illegal json.");
                }

                continue;
            }

            if (token == JSONToken.COMMA) {
                continue;
            } else if (token == JSONToken.RBRACKET) {
                return false;
            } else {
                throw new UnsupportedOperationException();
            }

        }

        nextToken();
        return true;
    }

    public int seekObjectToField(long fieldNameHash, boolean deepScan) {
        if (token == JSONToken.EOF) {
            return JSONLexer.NOT_MATCH;
        }

        if (token == JSONToken.RBRACE || token == JSONToken.RBRACKET) {
            nextToken();
            return JSONLexer.NOT_MATCH;
        }

        if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
            throw new UnsupportedOperationException(JSONToken.name(token));
        }

        for (;;) {
            if (ch == '}') {
                next();
                nextToken();
                return JSONLexer.NOT_MATCH;
            }
            if (ch == EOI) {
                return JSONLexer.NOT_MATCH;
            }

            if (ch != '"') {
                skipWhitespace();
            }

            long hash;
            if (ch == '"') {
                hash = 0xcbf29ce484222325L;

                for (int i = bp + 1; i < text.length(); ++i) {
                    char c = text.charAt(i);
                    if (c == '\\') {
                        ++i;
                        if (i == text.length()) {
                            throw new JSONException("unclosed str, " + info());
                        }
                        c = text.charAt(i);
                    }

                    if (c == '"') {
                        bp = i + 1;
                        ch = (bp >= text.length() //
                                ? EOI //
                                : text.charAt(bp));
                        break;
                    }

                    hash ^= c;
                    hash *= 0x100000001b3L;
                }
            } else {
                throw new UnsupportedOperationException();
            }

            if (hash == fieldNameHash) {
                if (ch != ':') {
                    skipWhitespace();
                }
                if (ch == ':') {
                    {
                        int index = ++bp;
                        ch = (index >= text.length() //
                                ? EOI //
                                : text.charAt(index));
                    }
                    if (ch == ',') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.COMMA;
                    } else if (ch == ']') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.RBRACKET;
                    } else if (ch == '}') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.RBRACE;
                    } else if (ch >= '0' && ch <= '9') {
                        sp = 0;
                        pos = bp;
                        scanNumber();
                    } else {
                        nextToken(JSONToken.LITERAL_INT);
                    }
                }
                return VALUE;
            }

            if (ch != ':') {
                skipWhitespace();
            }

            if (ch == ':') {
                int index = ++bp;
                ch = (index >= text.length() //
                        ? EOI //
                        : text.charAt(index));
            } else {
                throw new JSONException("illegal json, " + info());
            }

            if (ch != '"'
                    && ch != '\''
                    && ch != '{'
                    && ch != '['
                    && ch != '0'
                    && ch != '1'
                    && ch != '2'
                    && ch != '3'
                    && ch != '4'
                    && ch != '5'
                    && ch != '6'
                    && ch != '7'
                    && ch != '8'
                    && ch != '9'
                    && ch != '+'
                    && ch != '-') {
                skipWhitespace();
            }

            // skip fieldValues
            if (ch == '-' || ch == '+' || (ch >= '0' && ch <= '9')) {
                next();
                while (ch >= '0' && ch <= '9') {
                    next();
                }

                // scale
                if (ch == '.') {
                    next();
                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                // exp
                if (ch == 'E' || ch == 'e') {
                    next();
                    if (ch == '-' || ch == '+') {
                        next();
                    }
                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                if (ch != ',') {
                    skipWhitespace();
                }
                if (ch == ',') {
                    next();
                }
            } else if (ch == '"') {
                skipString();

                if (ch != ',' && ch != '}') {
                    skipWhitespace();
                }

                if (ch == ',') {
                    next();
                }
            } else if (ch == 't') {
                next();
                if (ch == 'r') {
                    next();
                    if (ch == 'u') {
                        next();
                        if (ch == 'e') {
                            next();
                        }
                    }
                }

                if (ch != ',' && ch != '}') {
                    skipWhitespace();
                }

                if (ch == ',') {
                    next();
                }
            } else if (ch == 'n') {
                next();
                if (ch == 'u') {
                    next();
                    if (ch == 'l') {
                        next();
                        if (ch == 'l') {
                            next();
                        }
                    }
                }

                if (ch != ',' && ch != '}') {
                    skipWhitespace();
                }

                if (ch == ',') {
                    next();
                }
            } else if (ch == 'f') {
                next();
                if (ch == 'a') {
                    next();
                    if (ch == 'l') {
                        next();
                        if (ch == 's') {
                            next();
                            if (ch == 'e') {
                                next();
                            }
                        }
                    }
                }

                if (ch != ',' && ch != '}') {
                    skipWhitespace();
                }

                if (ch == ',') {
                    next();
                }
            } else if (ch == '{') {
                {
                    int index = ++bp;
                    ch = (index >= text.length() //
                            ? EOI //
                            : text.charAt(index));
                }
                if (deepScan) {
                    token = JSONToken.LBRACE;
                    return OBJECT;
                }

                skipObject(false);
                if (token == JSONToken.RBRACE) {
                    return JSONLexer.NOT_MATCH;
                }
            } else if (ch == '[') {
                next();
                if (deepScan) {
                    token = JSONToken.LBRACKET;
                    return ARRAY;
                }
                skipArray(false);
                if (token == JSONToken.RBRACE) {
                    return JSONLexer.NOT_MATCH;
                }
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    public int seekObjectToField(long[] fieldNameHash) {
        if (token != JSONToken.LBRACE && token != JSONToken.COMMA) {
            throw new UnsupportedOperationException();
        }

        for (;;) {
            if (ch == '}') {
                next();
                nextToken();
                this.matchStat = JSONLexer.NOT_MATCH;
                return -1;
            }
            if (ch == EOI) {
                this.matchStat = JSONLexer.NOT_MATCH;
                return -1;
            }

            if (ch != '"') {
                skipWhitespace();
            }

            long hash;
            if (ch == '"') {
                hash = 0xcbf29ce484222325L;

                for (int i = bp + 1; i < text.length(); ++i) {
                    char c = text.charAt(i);
                    if (c == '\\') {
                        ++i;
                        if (i == text.length()) {
                            throw new JSONException("unclosed str, " + info());
                        }
                        c = text.charAt(i);
                    }

                    if (c == '"') {
                        bp = i + 1;
                        ch = (bp >= text.length() //
                                ? EOI //
                                : text.charAt(bp));
                        break;
                    }

                    hash ^= c;
                    hash *= 0x100000001b3L;
                }
            } else {
                throw new UnsupportedOperationException();
            }

            int matchIndex = -1;
            for (int i = 0; i < fieldNameHash.length; i++) {
                if (hash == fieldNameHash[i]) {
                    matchIndex = i;
                    break;
                }
            }

            if (matchIndex != -1) {
                if (ch != ':') {
                    skipWhitespace();
                }
                if (ch == ':') {
                    {
                        int index = ++bp;
                        ch = (index >= text.length() //
                                ? EOI //
                                : text.charAt(index));
                    }
                    if (ch == ',') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.COMMA;
                    } else if (ch == ']') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.RBRACKET;
                    } else if (ch == '}') {
                        {
                            int index = ++bp;
                            ch = (index >= text.length() //
                                    ? EOI //
                                    : text.charAt(index));
                        }
                        token = JSONToken.RBRACE;
                    } else if (ch >= '0' && ch <= '9') {
                        sp = 0;
                        pos = bp;
                        scanNumber();
                    } else {
                        nextToken(JSONToken.LITERAL_INT);
                    }
                }

                matchStat = VALUE;
                return matchIndex;
            }

            if (ch != ':') {
                skipWhitespace();
            }

            if (ch == ':') {
                int index = ++bp;
                ch = (index >= text.length() //
                        ? EOI //
                        : text.charAt(index));
            } else {
                throw new JSONException("illegal json, " + info());
            }

            if (ch != '"'
                    && ch != '\''
                    && ch != '{'
                    && ch != '['
                    && ch != '0'
                    && ch != '1'
                    && ch != '2'
                    && ch != '3'
                    && ch != '4'
                    && ch != '5'
                    && ch != '6'
                    && ch != '7'
                    && ch != '8'
                    && ch != '9'
                    && ch != '+'
                    && ch != '-') {
                skipWhitespace();
            }

            // skip fieldValues
            if (ch == '-' || ch == '+' || (ch >= '0' && ch <= '9')) {
                next();
                while (ch >= '0' && ch <= '9') {
                    next();
                }

                // scale
                if (ch == '.') {
                    next();
                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                // exp
                if (ch == 'E' || ch == 'e') {
                    next();
                    if (ch == '-' || ch == '+') {
                        next();
                    }
                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                if (ch != ',') {
                    skipWhitespace();
                }
                if (ch == ',') {
                    next();
                }
            } else if (ch == '"') {
                skipString();

                if (ch != ',' && ch != '}') {
                    skipWhitespace();
                }

                if (ch == ',') {
                    next();
                }
            } else if (ch == '{') {
                {
                    int index = ++bp;
                    ch = (index >= text.length() //
                            ? EOI //
                            : text.charAt(index));
                }

                skipObject(false);
            } else if (ch == '[') {
                next();

                skipArray(false);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    public String scanTypeName(SymbolTable symbolTable) {
        if (text.startsWith("\"@type\":\"", bp)) {
            int p = text.indexOf('"', bp + 9);
            if (p != -1) {
                bp += 9;
                int h = 0;
                for (int i = bp; i < p; i++) {
                    h = 31 * h + text.charAt(i);
                }
                String typeName = addSymbol(bp, p - bp, h, symbolTable);
                char separator = text.charAt(p + 1);
                if (separator != ',' && separator != ']') {
                    return null;
                }
                bp = p + 2;
                ch = text.charAt(bp);
                return typeName;
            }
        }

        return null;
    }
}
