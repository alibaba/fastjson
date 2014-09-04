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
package com.alibaba.fastjson.serializer;

import static com.alibaba.fastjson.parser.CharTypes.replaceChars;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.CharTypes;
import com.alibaba.fastjson.util.Base64;
import com.alibaba.fastjson.util.IOUtils;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public final class SerializeWriter extends Writer {

    /**
     * The buffer where data is stored.
     */
    protected char                                          buf[];

    /**
     * The number of chars in the buffer.
     */
    protected int                                           count;

    private final static ThreadLocal<SoftReference<char[]>> bufLocal = new ThreadLocal<SoftReference<char[]>>();

    private int                                             features;

    private final Writer                                    writer;

    public SerializeWriter(){
        this((Writer) null);
    }

    public SerializeWriter(Writer writer){
        this.writer = writer;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;

        SoftReference<char[]> ref = bufLocal.get();

        if (ref != null) {
            buf = ref.get();
            bufLocal.set(null);
        }

        if (buf == null) {
            buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... features){
        this(null, features);
    }

    public SerializeWriter(Writer writer, SerializerFeature... features){
        this.writer = writer;

        SoftReference<char[]> ref = bufLocal.get();

        if (ref != null) {
            buf = ref.get();
            bufLocal.set(null);
        }

        if (buf == null) {
            buf = new char[1024];
        }

        int featuresValue = 0;
        for (SerializerFeature feature : features) {
            featuresValue |= feature.getMask();
        }
        this.features = featuresValue;
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int initialSize){
        this(null, initialSize);
    }

    public SerializeWriter(Writer writer, int initialSize){
        this.writer = writer;

        if (initialSize <= 0) {
            throw new IllegalArgumentException("Negative initial size: " + initialSize);
        }
        buf = new char[initialSize];
    }

    public void config(SerializerFeature feature, boolean state) {
        if (state) {
            features |= feature.getMask();
        } else {
            features &= ~feature.getMask();
        }
    }

    public boolean isEnabled(SerializerFeature feature) {
        return SerializerFeature.isEnabled(this.features, feature);
    }

    /**
     * Writes a character to the buffer.
     */
    public void write(int c) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        buf[count] = (char) c;
        count = newcount;
    }

    public void write(char c) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        buf[count] = c;
        count = newcount;
    }

    /**
     * Writes characters to the buffer.
     * 
     * @param c the data to be written
     * @param off the start offset in the data
     * @param len the number of chars that are written
     */
    public void write(char c[], int off, int len) {
        if (off < 0 //
            || off > c.length //
            || len < 0 //
            || off + len > c.length //
            || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        int newcount = count + len;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                do {
                    int rest = buf.length - count;
                    System.arraycopy(c, off, buf, count, rest);
                    count = buf.length;
                    flush();
                    len -= rest;
                    off += rest;
                } while (len > buf.length);
                newcount = len;
            }
        }
        System.arraycopy(c, off, buf, count, len);
        count = newcount;

    }

    public void expandCapacity(int minimumCapacity) {
        int newCapacity = (buf.length * 3) / 2 + 1;

        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }
        char newValue[] = new char[newCapacity];
        System.arraycopy(buf, 0, newValue, 0, count);
        buf = newValue;
    }

    /**
     * Write a portion of a string to the buffer.
     * 
     * @param str String to be written from
     * @param off Offset from which to start reading characters
     * @param len Number of characters to be written
     */
    public void write(String str, int off, int len) {
        int newcount = count + len;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                do {
                    int rest = buf.length - count;
                    str.getChars(off, off + rest, buf, count);
                    count = buf.length;
                    flush();
                    len -= rest;
                    off += rest;
                } while (len > buf.length);
                newcount = len;
            }
        }
        str.getChars(off, off + len, buf, count);
        count = newcount;
    }

    /**
     * Writes the contents of the buffer to another character stream.
     * 
     * @param out the output stream to write to
     * @throws IOException If an I/O error occurs.
     */
    public void writeTo(Writer out) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        out.write(buf, 0, count);
    }

    public void writeTo(OutputStream out, String charsetName) throws IOException {
        writeTo(out, Charset.forName(charsetName));
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        byte[] bytes = new String(buf, 0, count).getBytes(charset);
        out.write(bytes);
    }

    public SerializeWriter append(CharSequence csq) {
        String s = (csq == null ? "null" : csq.toString());
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(CharSequence csq, int start, int end) {
        String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write(c);
        return this;
    }

    public void reset() {
        count = 0;
    }

    /**
     * Returns a copy of the input data.
     * 
     * @return an array of chars copied from the input data.
     */
    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }

        char[] newValue = new char[count];
        System.arraycopy(buf, 0, newValue, 0, count);
        return newValue;
    }

    public byte[] toBytes(String charsetName) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }

        if (charsetName == null) {
            charsetName = "UTF-8";
        }

        Charset cs = Charset.forName(charsetName);
        SerialWriterStringEncoder encoder = new SerialWriterStringEncoder(cs);

        return encoder.encode(buf, 0, count);
    }

    public int size() {
        return count;
    }

    public String toString() {
        return new String(buf, 0, count);
    }

    /**
     * Close the stream. This method does not release the buffer, since its contents might still be required. Note:
     * Invoking this method in this class will have no effect.
     */
    public void close() {
        if (writer != null && count > 0) {
            flush();
        }
        if (buf.length <= 1024 * 8) {
            bufLocal.set(new SoftReference<char[]>(buf));
        }

        this.buf = null;
    }

    public void write(String text) {
        if (text == null) {
            writeNull();
            return;
        }

        write(text, 0, text.length());
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount = count + size;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }

        IOUtils.getChars(i, newcount, buf);

        count = newcount;
    }

    public void writeByteArray(byte[] bytes) {
        int bytesLen = bytes.length;
        if (bytesLen == 0) {
            write("\"\"");
            return;
        }

        final char[] CA = Base64.CA;

        int eLen = (bytesLen / 3) * 3; // Length of even 24-bits.
        int charsLen = ((bytesLen - 1) / 3 + 1) << 2; // base64 character count
        // char[] chars = new char[charsLen];
        int offset = count;
        int newcount = count + charsLen + 2;
        if (newcount > buf.length) {
            if (writer != null) {
                write('\"');

                for (int s = 0; s < eLen;) {
                    // Copy next three bytes into lower 24 bits of int, paying attension to sign.
                    int i = (bytes[s++] & 0xff) << 16 | (bytes[s++] & 0xff) << 8 | (bytes[s++] & 0xff);

                    // Encode the int into four chars
                    write(CA[(i >>> 18) & 0x3f]);
                    write(CA[(i >>> 12) & 0x3f]);
                    write(CA[(i >>> 6) & 0x3f]);
                    write(CA[i & 0x3f]);
                }

                // Pad and encode last bits if source isn't even 24 bits.
                int left = bytesLen - eLen; // 0 - 2.
                if (left > 0) {
                    // Prepare the int
                    int i = ((bytes[eLen] & 0xff) << 10) | (left == 2 ? ((bytes[bytesLen - 1] & 0xff) << 2) : 0);

                    // Set last four chars
                    write(CA[i >> 12]);
                    write(CA[(i >>> 6) & 0x3f]);
                    write(left == 2 ? CA[i & 0x3f] : '=');
                    write('=');
                }

                write('\"');
                return;
            }
            expandCapacity(newcount);
        }
        count = newcount;
        buf[offset++] = '\"';

        // Encode even 24-bits
        for (int s = 0, d = offset; s < eLen;) {
            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            int i = (bytes[s++] & 0xff) << 16 | (bytes[s++] & 0xff) << 8 | (bytes[s++] & 0xff);

            // Encode the int into four chars
            buf[d++] = CA[(i >>> 18) & 0x3f];
            buf[d++] = CA[(i >>> 12) & 0x3f];
            buf[d++] = CA[(i >>> 6) & 0x3f];
            buf[d++] = CA[i & 0x3f];
        }

        // Pad and encode last bits if source isn't even 24 bits.
        int left = bytesLen - eLen; // 0 - 2.
        if (left > 0) {
            // Prepare the int
            int i = ((bytes[eLen] & 0xff) << 10) | (left == 2 ? ((bytes[bytesLen - 1] & 0xff) << 2) : 0);

            // Set last four chars
            buf[newcount - 5] = CA[i >> 12];
            buf[newcount - 4] = CA[(i >>> 6) & 0x3f];
            buf[newcount - 3] = left == 2 ? CA[i & 0x3f] : '=';
            buf[newcount - 2] = '=';
        }
        buf[newcount - 1] = '\"';
    }

    public void writeFloatAndChar(float value, char c) {
        String text = Float.toString(value);
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        write(text);
        write(c);
    }

    public void writeDoubleAndChar(double value, char c) {
        String text = Double.toString(value);
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        write(text);
        write(c);
    }

    public void writeBooleanAndChar(boolean value, char c) {
        if (value) {
            if (c == ',') {
                write("true,");
            } else if (c == ']') {
                write("true]");
            } else {
                write("true");
                write(c);
            }
        } else {
            if (c == ',') {
                write("false,");
            } else if (c == ']') {
                write("false]");
            } else {
                write("false");
                write(c);
            }
        }
    }

    public void writeCharacterAndChar(char value, char c) {
        writeString(Character.toString(value));
        write(c);
    }

    public void writeEnum(Enum<?> value, char c) {
        if (value == null) {
            writeNull();
            write(',');
            return;
        }

        if (isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            if (isEnabled(SerializerFeature.UseSingleQuotes)) {
                write('\'');
                write(value.name());
                write('\'');
                write(c);

            } else {
                write('\"');
                write(value.name());
                write('\"');
                write(c);
            }
            return;
        }

        writeIntAndChar(value.ordinal(), c);
    }

    public void writeIntAndChar(int i, char c) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            write(c);
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount0 = count + size;
        int newcount1 = newcount0 + 1;

        if (newcount1 > buf.length) {
            if (writer != null) {
                writeInt(i);
                write(c);
                return;
            }
            expandCapacity(newcount1);
        }

        IOUtils.getChars(i, newcount0, buf);
        buf[newcount0] = c;

        count = newcount1;
    }

    public void writeLongAndChar(long i, char c) throws IOException {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            write(c);
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount0 = count + size;
        int newcount1 = newcount0 + 1;

        if (newcount1 > buf.length) {
            if (writer != null) {
                writeLong(i);
                write(c);
                return;
            }
            expandCapacity(newcount1);
        }

        IOUtils.getChars(i, newcount0, buf);
        buf[newcount0] = c;

        count = newcount1;
    }

    public void writeLong(long i) {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }

        int size = (i < 0) ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);

        int newcount = count + size;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }

        IOUtils.getChars(i, newcount, buf);

        count = newcount;
    }

    public void writeNull() {
        write("null");
    }

    private void writeStringWithDoubleQuote(String text, final char seperator) {
        writeStringWithDoubleQuote(text, seperator, true);
    }

    private void writeStringWithDoubleQuote(String text, final char seperator, boolean checkSpecial) {
        if (text == null) {
            writeNull();
            if (seperator != 0) {
                write(seperator);
            }
            return;
        }

        int len = text.length();
        int newcount = count + len + 2;
        if (seperator != 0) {
            newcount++;
        }

        if (newcount > buf.length) {
            if (writer != null) {
                write('"');

                for (int i = 0; i < text.length(); ++i) {
                    char ch = text.charAt(i);

                    if (isEnabled(SerializerFeature.BrowserCompatible)) {
                        if (ch == '\b' //
                            || ch == '\f' //
                            || ch == '\n' //
                            || ch == '\r' //
                            || ch == '\t' //
                            || ch == '"' //
                            || ch == '/' //
                            || ch == '\\') {
                            write('\\');
                            write(replaceChars[(int) ch]);
                            continue;
                        }

                        if (ch < 32) {
                            write('\\');
                            write('u');
                            write('0');
                            write('0');
                            write(CharTypes.ASCII_CHARS[ch * 2]);
                            write(CharTypes.ASCII_CHARS[ch * 2 + 1]);
                            continue;
                        }

                        if (ch >= 127) {
                            write('\\');
                            write('u');
                            write(CharTypes.digits[(ch >>> 12) & 15]);
                            write(CharTypes.digits[(ch >>> 8) & 15]);
                            write(CharTypes.digits[(ch >>> 4) & 15]);
                            write(CharTypes.digits[ch & 15]);
                            continue;
                        }
                    } else {
                        if (ch < CharTypes.specicalFlags_doubleQuotes.length
                            && CharTypes.specicalFlags_doubleQuotes[ch] != 0 //
                            || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            write('\\');
                            write(replaceChars[(int) ch]);
                            continue;
                        }
                    }

                    write(ch);
                }

                write('"');
                if (seperator != 0) {
                    write(seperator);
                }
                return;
            }
            expandCapacity(newcount);
        }

        int start = count + 1;
        int end = start + len;

        buf[count] = '\"';
        text.getChars(0, len, buf, start);

        count = newcount;

        if (isEnabled(SerializerFeature.BrowserCompatible)) {
            int lastSpecialIndex = -1;

            for (int i = start; i < end; ++i) {
                char ch = buf[i];

                if (ch == '"' //
                    || ch == '/' //
                    || ch == '\\') {
                    lastSpecialIndex = i;
                    newcount += 1;
                    continue;
                }

                if (ch == '\b' //
                    || ch == '\f' //
                    || ch == '\n' //
                    || ch == '\r' //
                    || ch == '\t') {
                    lastSpecialIndex = i;
                    newcount += 1;
                    continue;
                }

                if (ch < 32) {
                    lastSpecialIndex = i;
                    newcount += 5;
                    continue;
                }

                if (ch >= 127) {
                    lastSpecialIndex = i;
                    newcount += 5;
                    continue;
                }
            }

            if (newcount > buf.length) {
                expandCapacity(newcount);
            }
            count = newcount;

            for (int i = lastSpecialIndex; i >= start; --i) {
                char ch = buf[i];

                if (ch == '\b' //
                    || ch == '\f'//
                    || ch == '\n' //
                    || ch == '\r' //
                    || ch == '\t') {
                    System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = replaceChars[(int) ch];
                    end += 1;
                    continue;
                }

                if (ch == '"' //
                    || ch == '/' //
                    || ch == '\\') {
                    System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = ch;
                    end += 1;
                    continue;
                }

                if (ch < 32) {
                    System.arraycopy(buf, i + 1, buf, i + 6, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = 'u';
                    buf[i + 2] = '0';
                    buf[i + 3] = '0';
                    buf[i + 4] = CharTypes.ASCII_CHARS[ch * 2];
                    buf[i + 5] = CharTypes.ASCII_CHARS[ch * 2 + 1];
                    end += 5;
                    continue;
                }

                if (ch >= 127) {
                    System.arraycopy(buf, i + 1, buf, i + 6, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = 'u';
                    buf[i + 2] = CharTypes.digits[(ch >>> 12) & 15];
                    buf[i + 3] = CharTypes.digits[(ch >>> 8) & 15];
                    buf[i + 4] = CharTypes.digits[(ch >>> 4) & 15];
                    buf[i + 5] = CharTypes.digits[ch & 15];
                    end += 5;
                }
            }

            if (seperator != 0) {
                buf[count - 2] = '\"';
                buf[count - 1] = seperator;
            } else {
                buf[count - 1] = '\"';
            }

            return;
        }

        int specialCount = 0;
        int lastSpecialIndex = -1;
        int firstSpecialIndex = -1;
        char lastSpecial = '\0';
        if (checkSpecial) {
            for (int i = start; i < end; ++i) {
                char ch = buf[i];

                if (ch == '\u2028') {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;
                    newcount += 4;

                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                    continue;
                }

                if (ch >= ']') {
                    if (ch >= 0x7F && ch <= 0xA0) {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = i;
                        }

                        specialCount++;
                        lastSpecialIndex = i;
                        lastSpecial = ch;
                        newcount += 4;
                    }
                    continue;
                }

                if (isSpecial(ch, this.features)) {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;

                    if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                        && CharTypes.specicalFlags_doubleQuotes[ch] == 4 //
                    ) {
                        newcount += 4;
                    }

                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                }
            }

            if (specialCount > 0) {
                newcount += specialCount;
                if (newcount > buf.length) {
                    expandCapacity(newcount);
                }
                count = newcount;

                if (specialCount == 1) {
                    if (lastSpecial == '\u2028') {
                        int srcPos = lastSpecialIndex + 1;
                        int destPos = lastSpecialIndex + 6;
                        int LengthOfCopy = end - lastSpecialIndex - 1;
                        System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);
                        buf[lastSpecialIndex] = '\\';
                        buf[++lastSpecialIndex] = 'u';
                        buf[++lastSpecialIndex] = '2';
                        buf[++lastSpecialIndex] = '0';
                        buf[++lastSpecialIndex] = '2';
                        buf[++lastSpecialIndex] = '8';
                    } else {
                        final char ch = lastSpecial;
                        if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                            && CharTypes.specicalFlags_doubleQuotes[ch] == 4) {
                            int srcPos = lastSpecialIndex + 1;
                            int destPos = lastSpecialIndex + 6;
                            int LengthOfCopy = end - lastSpecialIndex - 1;
                            System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);

                            int bufIndex = lastSpecialIndex;
                            buf[bufIndex++] = '\\';
                            buf[bufIndex++] = 'u';
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                            buf[bufIndex++] = CharTypes.digits[ch & 15];
                        } else {
                            int srcPos = lastSpecialIndex + 1;
                            int destPos = lastSpecialIndex + 2;
                            int LengthOfCopy = end - lastSpecialIndex - 1;
                            System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);
                            buf[lastSpecialIndex] = '\\';
                            buf[++lastSpecialIndex] = replaceChars[(int) ch];
                        }
                    }
                } else if (specialCount > 1) {
                    int textIndex = firstSpecialIndex - start;
                    int bufIndex = firstSpecialIndex;
                    for (int i = textIndex; i < text.length(); ++i) {
                        char ch = text.charAt(i);

                        if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                            && CharTypes.specicalFlags_doubleQuotes[ch] != 0 //
                            || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            buf[bufIndex++] = '\\';
                            if (CharTypes.specicalFlags_doubleQuotes[ch] == 4) {
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                                buf[bufIndex++] = CharTypes.digits[ch & 15];
                                end += 5;
                            } else {
                                buf[bufIndex++] = replaceChars[(int) ch];
                                end++;
                            }
                        } else {
                            if (ch == '\u2028') {
                                buf[bufIndex++] = '\\';
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                                buf[bufIndex++] = CharTypes.digits[ch & 15];
                                end += 5;
                            } else {
                                buf[bufIndex++] = ch;
                            }
                        }
                    }
                }
            }
        }

        if (seperator != 0) {
            buf[count - 2] = '\"';
            buf[count - 1] = seperator;
        } else {
            buf[count - 1] = '\"';
        }
    }

    public void writeFieldNull(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        writeNull();
    }

    public void writeFieldEmptyList(char seperator, String key) {
        write(seperator);
        writeFieldName(key);
        write("[]");
    }

    public void writeFieldNullString(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
            writeString("");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullBoolean(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullBooleanAsFalse)) {
            write("false");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullList(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
            write("[]");
        } else {
            writeNull();
        }
    }

    public void writeFieldNullNumber(char seperator, String name) {
        write(seperator);
        writeFieldName(name);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            write('0');
        } else {
            writeNull();
        }
    }

    public void writeFieldValue(char seperator, String name, char value) {
        write(seperator);
        writeFieldName(name);
        if (value == 0) {
            writeString("\u0000");
        } else {
            writeString(Character.toString(value));
        }
    }

    public void writeFieldValue(char seperator, String name, boolean value) {

        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';

        int intSize = value ? 4 : 5;

        int nameLen = name.length();
        int newcount = count + nameLen + 4 + intSize;
        if (newcount > buf.length) {
            if (writer != null) {
                write(seperator);
                writeString(name);
                write(':');
                write(value);
                return;
            }
            expandCapacity(newcount);
        }

        int start = count;
        count = newcount;

        buf[start] = seperator;

        int nameEnd = start + nameLen + 1;

        buf[start + 1] = keySeperator;

        name.getChars(0, nameLen, buf, start + 2);

        buf[nameEnd + 1] = keySeperator;

        if (value) {
            System.arraycopy(":true".toCharArray(), 0, buf, nameEnd + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, buf, nameEnd + 2, 6);
        }
    }

    public void write(boolean value) {
        if (value) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char seperator, String name, int value) {
        if (value == Integer.MIN_VALUE || (!isEnabled(SerializerFeature.QuoteFieldNames))) {
            writeFieldValue1(seperator, name, value);
            return;
        }

        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';

        int intSize = (value < 0) ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);

        int nameLen = name.length();
        int newcount = count + nameLen + 4 + intSize;
        if (newcount > buf.length) {
            if (writer != null) {
                writeFieldValue1(seperator, name, value);
                return;
            }
            expandCapacity(newcount);
        }

        int start = count;
        count = newcount;

        buf[start] = seperator;

        int nameEnd = start + nameLen + 1;

        buf[start + 1] = keySeperator;

        name.getChars(0, nameLen, buf, start + 2);

        buf[nameEnd + 1] = keySeperator;
        buf[nameEnd + 2] = ':';

        IOUtils.getChars(value, count, buf);
    }

    public void writeFieldValue1(char seperator, String name, int value) {
        write(seperator);
        writeFieldName(name);
        writeInt(value);
    }

    public void writeFieldValue(char seperator, String name, long value) {
        if (value == Long.MIN_VALUE || (!isEnabled(SerializerFeature.QuoteFieldNames))) {
            writeFieldValue1(seperator, name, value);
            return;
        }

        char keySeperator = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '"';

        int intSize = (value < 0) ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);

        int nameLen = name.length();
        int newcount = count + nameLen + 4 + intSize;
        if (newcount > buf.length) {
            if (writer != null) {
                write(seperator);
                writeFieldName(name);
                writeLong(value);
                return;
            }
            expandCapacity(newcount);
        }

        int start = count;
        count = newcount;

        buf[start] = seperator;

        int nameEnd = start + nameLen + 1;

        buf[start + 1] = keySeperator;

        name.getChars(0, nameLen, buf, start + 2);

        buf[nameEnd + 1] = keySeperator;
        buf[nameEnd + 2] = ':';

        IOUtils.getChars(value, count, buf);
    }

    public void writeFieldValue1(char seperator, String name, long value) {
        write(seperator);
        writeFieldName(name);
        writeLong(value);
    }

    public void writeFieldValue(char seperator, String name, float value) {
        write(seperator);
        writeFieldName(name);
        if (value == 0) {
            write('0');
        } else if (Float.isNaN(value)) {
            writeNull();
        } else if (Float.isInfinite(value)) {
            writeNull();
        } else {
            String text = Float.toString(value);
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            write(text);
        }
    }

    public void writeFieldValue(char seperator, String name, double value) {
        write(seperator);
        writeFieldName(name);
        if (value == 0) {
            write('0');
        } else if (Double.isNaN(value)) {
            writeNull();
        } else if (Double.isInfinite(value)) {
            writeNull();
        } else {
            String text = Double.toString(value);
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            write(text);
        }
    }

    public void writeFieldValue(char seperator, String name, String value) {
        if (isEnabled(SerializerFeature.QuoteFieldNames)) {
            if (isEnabled(SerializerFeature.UseSingleQuotes)) {
                write(seperator);
                writeFieldName(name);
                if (value == null) {
                    writeNull();
                } else {
                    writeString(value);
                }
            } else {
                if (isEnabled(SerializerFeature.BrowserCompatible)) {
                    write(seperator);
                    writeStringWithDoubleQuote(name, ':');
                    writeStringWithDoubleQuote(value, (char) 0);
                } else {
                    writeFieldValueStringWithDoubleQuote(seperator, name, value, true);
                }
            }
        } else {
            write(seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        }
    }

    private void writeFieldValueStringWithDoubleQuote(char seperator, String name, String value, boolean checkSpecial) {
        int nameLen = name.length();
        int valueLen;

        int newcount = count;

        if (value == null) {
            valueLen = 4;
            newcount += nameLen + 8;
        } else {
            valueLen = value.length();
            newcount += nameLen + valueLen + 6;
        }

        if (newcount > buf.length) {
            if (writer != null) {
                write(seperator);
                writeStringWithDoubleQuote(name, ':', checkSpecial);
                writeStringWithDoubleQuote(value, (char) 0, checkSpecial);
                return;
            }
            expandCapacity(newcount);
        }

        buf[count] = seperator;

        int nameStart = count + 2;
        int nameEnd = nameStart + nameLen;

        buf[count + 1] = '\"';
        name.getChars(0, nameLen, buf, nameStart);

        count = newcount;

        buf[nameEnd] = '\"';

        int index = nameEnd + 1;
        buf[index++] = ':';

        if (value == null) {
            buf[index++] = 'n';
            buf[index++] = 'u';
            buf[index++] = 'l';
            buf[index++] = 'l';
            return;
        }

        buf[index++] = '"';

        int valueStart = index;
        int valueEnd = valueStart + valueLen;

        value.getChars(0, valueLen, buf, valueStart);

        if (checkSpecial && !isEnabled(SerializerFeature.DisableCheckSpecialChar)) {
            int specialCount = 0;
            int lastSpecialIndex = -1;
            int firstSpecialIndex = -1;
            char lastSpecial = '\0';

            for (int i = valueStart; i < valueEnd; ++i) {
                char ch = buf[i];

                if (ch == '\u2028') {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;
                    newcount += 4;

                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                    continue;
                }

                if (ch >= ']') {
                    if (ch >= 0x7F && ch <= 0xA0) {
                        if (firstSpecialIndex == -1) {
                            firstSpecialIndex = i;
                        }

                        specialCount++;
                        lastSpecialIndex = i;
                        lastSpecial = ch;
                        newcount += 4;
                    }

                    continue;
                }

                if (isSpecial(ch, this.features)) {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;

                    if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                        && CharTypes.specicalFlags_doubleQuotes[ch] == 4 //
                    ) {
                        newcount += 4;
                    }

                    if (firstSpecialIndex == -1) {
                        firstSpecialIndex = i;
                    }
                }
            }

            if (specialCount > 0) {
                newcount += specialCount;
                if (newcount > buf.length) {
                    expandCapacity(newcount);
                }
                count = newcount;

                if (specialCount == 1) {
                    if (lastSpecial == '\u2028') {
                        int srcPos = lastSpecialIndex + 1;
                        int destPos = lastSpecialIndex + 6;
                        int LengthOfCopy = valueEnd - lastSpecialIndex - 1;
                        System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);
                        buf[lastSpecialIndex] = '\\';
                        buf[++lastSpecialIndex] = 'u';
                        buf[++lastSpecialIndex] = '2';
                        buf[++lastSpecialIndex] = '0';
                        buf[++lastSpecialIndex] = '2';
                        buf[++lastSpecialIndex] = '8';
                    } else {
                        final char ch = lastSpecial;
                        if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                            && CharTypes.specicalFlags_doubleQuotes[ch] == 4) {
                            int srcPos = lastSpecialIndex + 1;
                            int destPos = lastSpecialIndex + 6;
                            int LengthOfCopy = valueEnd - lastSpecialIndex - 1;
                            System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);
                            
                            int bufIndex = lastSpecialIndex;
                            buf[bufIndex++] = '\\';
                            buf[bufIndex++] = 'u';
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                            buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                            buf[bufIndex++] = CharTypes.digits[ch & 15];
                        } else {
                            int srcPos = lastSpecialIndex + 1;
                            int destPos = lastSpecialIndex + 2;
                            int LengthOfCopy = valueEnd - lastSpecialIndex - 1;
                            System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);
                            buf[lastSpecialIndex] = '\\';
                            buf[++lastSpecialIndex] = replaceChars[(int) ch];
                        }
                    }
                } else if (specialCount > 1) {
                    int textIndex = firstSpecialIndex - valueStart;
                    int bufIndex = firstSpecialIndex;
                    for (int i = textIndex; i < value.length(); ++i) {
                        char ch = value.charAt(i);

                        if (ch < CharTypes.specicalFlags_doubleQuotes.length //
                            && CharTypes.specicalFlags_doubleQuotes[ch] != 0 //
                            || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                            buf[bufIndex++] = '\\';
                            if (CharTypes.specicalFlags_doubleQuotes[ch] == 4) {
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                                buf[bufIndex++] = CharTypes.digits[ch & 15];
                                valueEnd += 5;
                            } else {
                                buf[bufIndex++] = replaceChars[(int) ch];
                                valueEnd++;
                            }
                        } else {
                            if (ch == '\u2028') {
                                buf[bufIndex++] = '\\';
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 12) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 8) & 15];
                                buf[bufIndex++] = CharTypes.digits[(ch >>> 4) & 15];
                                buf[bufIndex++] = CharTypes.digits[ch & 15];
                                valueEnd += 5;
                            } else {
                                buf[bufIndex++] = ch;
                            }
                        }
                    }
                }
            }
        }

        buf[count - 1] = '\"';
    }

    final static boolean isSpecial(char ch, int features) {
        // if (ch > ']') {
        // return false;
        // }

        if (ch == ' ') {
            return false;
        }

        if (ch == '/' && SerializerFeature.isEnabled(features, SerializerFeature.WriteSlashAsSpecial)) {
            return true;
        }

        if (ch > '#' && ch != '\\') {
            return false;
        }

        if (ch <= 0x1F || ch == '\\' || ch == '"') {
            return true;
        }

        return false;
    }

    // writeStringWithSingleQuote

    public void writeFieldValue(char seperator, String name, Enum<?> value) {
        if (value == null) {
            write(seperator);
            writeFieldName(name);
            writeNull();
            return;
        }

        if (isEnabled(SerializerFeature.WriteEnumUsingToString)) {
            if (isEnabled(SerializerFeature.UseSingleQuotes)) {
                writeFieldValue(seperator, name, value.name());
            } else {
                writeFieldValueStringWithDoubleQuote(seperator, name, value.name(), false);
                return;
            }

            // writeStringWithDoubleQuote
        } else {
            writeFieldValue(seperator, name, value.ordinal());
        }
    }

    public void writeFieldValue(char seperator, String name, BigDecimal value) {
        write(seperator);
        writeFieldName(name);
        if (value == null) {
            writeNull();
        } else {
            write(value.toString());
        }
    }

    public void writeString(String text, char seperator) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeStringWithSingleQuote(text);
            write(seperator);
        } else {
            writeStringWithDoubleQuote(text, seperator);
        }
    }

    public void writeString(String text) {
        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            writeStringWithSingleQuote(text);
        } else {
            writeStringWithDoubleQuote(text, (char) 0);
        }
    }

    private void writeStringWithSingleQuote(String text) {
        if (text == null) {
            int newcount = count + 4;
            if (newcount > buf.length) {
                expandCapacity(newcount);
            }
            "null".getChars(0, 4, buf, count);
            count = newcount;
            return;
        }

        int len = text.length();
        int newcount = count + len + 2;
        if (newcount > buf.length) {
            if (writer != null) {
                write('\'');
                for (int i = 0; i < text.length(); ++i) {
                    char ch = text.charAt(i);
                    if (ch <= 13 || ch == '\\' || ch == '\'' //
                        || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write('\\');
                        write(replaceChars[(int) ch]);
                    } else {
                        write(ch);
                    }
                }
                write('\'');
                return;
            }
            expandCapacity(newcount);
        }

        int start = count + 1;
        int end = start + len;

        buf[count] = '\'';
        text.getChars(0, len, buf, start);
        count = newcount;

        int specialCount = 0;
        int lastSpecialIndex = -1;
        char lastSpecial = '\0';
        for (int i = start; i < end; ++i) {
            char ch = buf[i];
            if (ch <= 13 || ch == '\\' || ch == '\'' //
                || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                specialCount++;
                lastSpecialIndex = i;
                lastSpecial = ch;
            }
        }

        newcount += specialCount;
        if (newcount > buf.length) {
            expandCapacity(newcount);
        }
        count = newcount;

        if (specialCount == 1) {
            System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
            buf[lastSpecialIndex] = '\\';
            buf[++lastSpecialIndex] = replaceChars[(int) lastSpecial];
        } else if (specialCount > 1) {
            System.arraycopy(buf, lastSpecialIndex + 1, buf, lastSpecialIndex + 2, end - lastSpecialIndex - 1);
            buf[lastSpecialIndex] = '\\';
            buf[++lastSpecialIndex] = replaceChars[(int) lastSpecial];
            end++;
            for (int i = lastSpecialIndex - 2; i >= start; --i) {
                char ch = buf[i];

                if (ch <= 13 || ch == '\\' || ch == '\'' //
                    || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = replaceChars[(int) ch];
                    end++;
                }
            }
        }

        buf[count - 1] = '\'';
    }

    public void writeFieldName(String key) {
        writeFieldName(key, false);
    }

    public void writeFieldName(String key, boolean checkSpecial) {
        if (key == null) {
            write("null:");
            return;
        }

        if (isEnabled(SerializerFeature.UseSingleQuotes)) {
            if (isEnabled(SerializerFeature.QuoteFieldNames)) {
                writeStringWithSingleQuote(key);
                write(':');
            } else {
                writeKeyWithSingleQuoteIfHasSpecial(key);
            }
        } else {
            if (isEnabled(SerializerFeature.QuoteFieldNames)) {
                writeStringWithDoubleQuote(key, ':', checkSpecial);
            } else {
                writeKeyWithDoubleQuoteIfHasSpecial(key);
            }
        }
    }

    private void writeKeyWithDoubleQuoteIfHasSpecial(String text) {
        final byte[] specicalFlags_doubleQuotes = CharTypes.specicalFlags_doubleQuotes;

        int len = text.length();
        int newcount = count + len + 1;
        if (newcount > buf.length) {
            if (writer != null) {
                if (len == 0) {
                    write('"');
                    write('"');
                    write(':');
                    return;
                }

                boolean hasSpecial = false;
                for (int i = 0; i < len; ++i) {
                    char ch = text.charAt(i);
                    if (ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != 0) {
                        hasSpecial = true;
                        break;
                    }
                }

                if (hasSpecial) {
                    write('"');
                }
                for (int i = 0; i < len; ++i) {
                    char ch = text.charAt(i);
                    if (ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != 0) {
                        write('\\');
                        write(replaceChars[(int) ch]);
                    } else {
                        write(ch);
                    }
                }
                if (hasSpecial) {
                    write('"');
                }
                write(':');
                return;
            }
            expandCapacity(newcount);
        }

        if (len == 0) {
            int newCount = count + 3;
            if (newCount > buf.length) {
                expandCapacity(count + 3);
            }
            buf[count++] = '"';
            buf[count++] = '"';
            buf[count++] = ':';
            return;
        }

        int start = count;
        int end = start + len;

        text.getChars(0, len, buf, start);
        count = newcount;

        boolean hasSpecial = false;

        for (int i = start; i < end; ++i) {
            char ch = buf[i];
            if (ch < specicalFlags_doubleQuotes.length && specicalFlags_doubleQuotes[ch] != 0) {
                if (!hasSpecial) {
                    newcount += 3;
                    if (newcount > buf.length) {
                        expandCapacity(newcount);
                    }
                    count = newcount;

                    System.arraycopy(buf, i + 1, buf, i + 3, end - i - 1);
                    System.arraycopy(buf, 0, buf, 1, i);
                    buf[start] = '"';
                    buf[++i] = '\\';
                    buf[++i] = replaceChars[(int) ch];
                    end += 2;
                    buf[count - 2] = '"';

                    hasSpecial = true;
                } else {
                    newcount++;
                    if (newcount > buf.length) {
                        expandCapacity(newcount);
                    }
                    count = newcount;

                    System.arraycopy(buf, i + 1, buf, i + 2, end - i);
                    buf[i] = '\\';
                    buf[++i] = replaceChars[(int) ch];
                    end++;
                }
            }
        }

        buf[count - 1] = ':';
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String text) {
        final byte[] specicalFlags_singleQuotes = CharTypes.specicalFlags_singleQuotes;

        int len = text.length();
        int newcount = count + len + 1;
        if (newcount > buf.length) {
            if (writer != null) {
                if (len == 0) {
                    write('\'');
                    write('\'');
                    write(':');
                    return;
                }

                boolean hasSpecial = false;
                for (int i = 0; i < len; ++i) {
                    char ch = text.charAt(i);
                    if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != 0) {
                        hasSpecial = true;
                        break;
                    }
                }

                if (hasSpecial) {
                    write('\'');
                }
                for (int i = 0; i < len; ++i) {
                    char ch = text.charAt(i);
                    if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != 0) {
                        write('\\');
                        write(replaceChars[(int) ch]);
                    } else {
                        write(ch);
                    }
                }
                if (hasSpecial) {
                    write('\'');
                }
                write(':');
                return;
            }

            expandCapacity(newcount);
        }

        if (len == 0) {
            int newCount = count + 3;
            if (newCount > buf.length) {
                expandCapacity(count + 3);
            }
            buf[count++] = '\'';
            buf[count++] = '\'';
            buf[count++] = ':';
            return;
        }

        int start = count;
        int end = start + len;

        text.getChars(0, len, buf, start);
        count = newcount;

        boolean hasSpecial = false;

        for (int i = start; i < end; ++i) {
            char ch = buf[i];
            if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != 0) {
                if (!hasSpecial) {
                    newcount += 3;
                    if (newcount > buf.length) {
                        expandCapacity(newcount);
                    }
                    count = newcount;

                    System.arraycopy(buf, i + 1, buf, i + 3, end - i - 1);
                    System.arraycopy(buf, 0, buf, 1, i);
                    buf[start] = '\'';
                    buf[++i] = '\\';
                    buf[++i] = replaceChars[(int) ch];
                    end += 2;
                    buf[count - 2] = '\'';

                    hasSpecial = true;
                } else {
                    newcount++;
                    if (newcount > buf.length) {
                        expandCapacity(newcount);
                    }
                    count = newcount;

                    System.arraycopy(buf, i + 1, buf, i + 2, end - i);
                    buf[i] = '\\';
                    buf[++i] = replaceChars[(int) ch];
                    end++;
                }
            }
        }

        buf[newcount - 1] = ':';
    }

    public void flush() {
        if (writer == null) {
            return;
        }

        try {
            writer.write(buf, 0, count);
            writer.flush();
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
        count = 0;
    }
}
