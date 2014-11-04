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

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.Base64;

//这个类，为了性能优化做了很多特别处理，一切都是为了性能！！！

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class JSONScanner extends JSONLexerBase {

    private final String text;

    public JSONScanner(String input){
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features){
        this.features = features;

        text = input;
        bp = -1;

        next();
        if (ch == 65279) {
            next();
        }
    }

    public final char charAt(int index) {
        if (index >= text.length()) {
            return EOI;
        }

        return text.charAt(index);
    }

    public final char next() {
        return ch = charAt(++bp);
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

    protected final static char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();

    public final int indexOf(char ch, int startIndex) {
        return text.indexOf(ch, startIndex);
    }

    public final String addSymbol(int offset, int len, int hash, final SymbolTable symbolTable) {
        return symbolTable.addSymbol(text, offset, len, hash);
    }

    public byte[] bytesValue() {
        return Base64.decodeFast(text, np + 1, sp);
    }

    // public int scanField2(char[] fieldName, Object object, FieldDeserializer fieldDeserializer) {
    // return NOT_MATCH;
    // }

    /**
     * The value of a literal token, recorded as a string. For integers, leading 0x and 'l' suffixes are suppressed.
     */
    public final String stringVal() {
        if (!hasSpecial) {
            // return text.substring(np + 1, np + 1 + sp);
            return this.subString(np + 1, sp);
        } else {
            return new String(sbuf, 0, sp);
        }
    }

    public final String subString(int offset, int count) {
        char[] chars = new char[count];
        for (int i = offset; i < offset + count; ++i) {
            chars[i - offset] = text.charAt(i);
        }
        return new String(chars);
    }

    public final String numberString() {
        char chLocal = charAt(np + sp - 1);

        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }

        // return text.substring(np, np + sp);
        return this.subString(np, sp);
    }

    public final int ISO8601_LEN_0 = "0000-00-00".length();
    public final int ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
    public final int ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = text.length() - bp;

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

                Locale local = Locale.getDefault();
                calendar = Calendar.getInstance(TimeZone.getDefault(), local);
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

                    millis = digits[S0] * 100 + digits[S1] * 10 + digits[S2];
                } else {
                    millis = 0;
                }

                hour = digits[h0] * 10 + digits[h1];
                minute = digits[m0] * 10 + digits[m1];
                seconds = digits[s0] * 10 + digits[s1];
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

        if (rest < ISO8601_LEN_0) {
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
            if (rest < ISO8601_LEN_1) {
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

        int hour = digits[h0] * 10 + digits[h1];
        int minute = digits[m0] * 10 + digits[m1];
        int seconds = digits[s0] * 10 + digits[s1];
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);

        char dot = charAt(bp + 19);
        if (dot == '.') {
            if (rest < ISO8601_LEN_2) {
                return false;
            }
        } else {
            calendar.set(Calendar.MILLISECOND, 0);

            ch = charAt(bp += 19);

            token = JSONToken.LITERAL_ISO8601_DATE;
            return true;
        }

        char S0 = charAt(bp + 20);
        if (S0 < '0' || S0 > '9') {
            return false;
        }
        int millis = digits[S0];
        int millisLen = 1;

        {
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

        }

        char end = charAt(bp + (20 + millisLen + timzeZoneLength));
        if (end != EOI && end != '"') {
            return false;
        }
        ch = charAt(bp += (20 + millisLen + timzeZoneLength));

        token = JSONToken.LITERAL_ISO8601_DATE;
        return true;
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
        Locale local = Locale.getDefault();
        calendar = Calendar.getInstance(TimeZone.getDefault(), local);
        int year = digits[y0] * 1000 + digits[y1] * 100 + digits[y2] * 10 + digits[y3];
        int month = digits[M0] * 10 + digits[M1] - 1;
        int day = digits[d0] * 10 + digits[d1];
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
        return bp == text.length() || ch == EOI && bp + 1 == text.length();
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        text.getChars(srcPos, srcPos + length, dest, destPos);
    }
}
