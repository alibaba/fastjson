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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONLexer;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class SerializeWriter extends Writer {

    /**
     * The buffer where data is stored.
     */
    protected char[]                                        buf;

    protected int                                           count;

    private final static ThreadLocal<char[]> bufLocal = new ThreadLocal<char[]>();

    protected int                                           features;

    protected final Writer                                  writer;

    public SerializeWriter(){
        this((Writer) null);
    }

    public SerializeWriter(Writer writer){
        this.writer = writer;
        this.features = JSON.DEFAULT_GENERATE_FEATURE;

        buf = bufLocal.get();

        if (bufLocal != null) {
            bufLocal.set(null);
        }

        if (buf == null) {
            buf = new char[1024];
        }
    }

    public SerializeWriter(SerializerFeature... features){
        this(null, 0, features);
    }

    public SerializeWriter(Writer writer, int featuresValue, SerializerFeature[] features){
        this.writer = writer;

        buf = bufLocal.get();
        if (buf != null) {
            bufLocal.set(null);
        }

        if (buf == null) {
            buf = new char[1024];
        }

        for (SerializerFeature feature : features) {
            featuresValue |= feature.mask;
        }
        this.features = featuresValue;
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
            features |= feature.mask;
        } else {
            features &= ~feature.mask;
        }
    }

    public boolean isEnabled(SerializerFeature feature) {
        return (features & feature.mask) != 0;
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

    protected void expandCapacity(int minimumCapacity) {
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
        byte[] bytes = new String(buf, 0, count).getBytes(charset.name());
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

    public byte[] toBytes(String charsetName) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }

        if (charsetName == null) {
            charsetName = "UTF-8";
        }

        try {
            return new String(buf, 0, count).getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new JSONException("toBytes error", e);
        }
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
        if (buf.length <= 8192) {
            bufLocal.set(buf);
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
        if (i == 0x80000000 /* Integer.MIN_VALUE*/) {
            write("-2147483648");
            return;
        }

        int size;
        final int x = i < 0 ? -i : i;
        for (int j = 0;; j++) {
            if (x <= SerializeWriter.sizeTable[j]) {
                size = j + 1;
                break;
            }
        }
        if (i < 0) {
            size ++;
        }

        int newcount = count + size;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }

        getChars(i, newcount, buf);

        count = newcount;
    }

    public void writeByteArray(byte[] bytes) {
        int bytesLen = bytes.length;
        
        final boolean singleQuote = (features & SerializerFeature.UseSingleQuotes.mask) != 0;
        final char quote = singleQuote ? '\'' : '"';

        if (bytesLen == 0) {
            String emptyString = singleQuote ? "''" : "\"\"";
            write(emptyString);
            return;
        }

        final char[] CA = JSONLexer.CA;

        int eLen = (bytesLen / 3) * 3; // Length of even 24-bits.
        int charsLen = ((bytesLen - 1) / 3 + 1) << 2; // base64 character count
        // char[] chars = new char[charsLen];
        int offset = count;
        int newcount = count + charsLen + 2;
        if (newcount > buf.length) {
            if (writer != null) {
                write(quote);

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

                write(quote);
                return;
            }
            expandCapacity(newcount);
        }
        count = newcount;
        buf[offset++] = quote;

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
        buf[newcount - 1] = quote;
    }

    public void writeLong(long i) {
        if (i == Long.MIN_VALUE) {
            write("-9223372036854775808");
            return;
        }

        long val = i < 0 ? -i : i;
        int size = 0;
        long p = 10;
          for (int j = 1; j < 19; j++) {
              if (val < p) {
                  size = j;
                  break;
              }
              p = 10 * p;
          }
          
          if (size == 0) {
              size = 19;
          }
          if (i < 0) {
              size++;
          }

        int newcount = count + size;
        if (newcount > buf.length) {
            if (writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }

        getChars(i, newcount, buf);

        count = newcount;
    }

    public void writeNull() {
        write("null");
    }

    protected void writeStringWithDoubleQuote(String text, final char seperator, boolean checkSpecial) {
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
                    
                    if ((features & SerializerFeature.BrowserCompatible.mask) != 0) {
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
                            write(ascii_chars[ch * 2]);
                            write(ascii_chars[ch * 2 + 1]);
                            continue;
                        }

                        if (ch >= 127) {
                            write('\\');
                            write('u');
                            write(DIGITS[(ch >>> 12) & 15]);
                            write(DIGITS[(ch >>> 8) & 15]);
                            write(DIGITS[(ch >>> 4) & 15]);
                            write(DIGITS[ch & 15]);
                            continue;
                        }
                    } else {
                        if (ch < specicalFlags_doubleQuotes.length
                            && specicalFlags_doubleQuotes[ch] != 0 //
                            || (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
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

        if ((features & SerializerFeature.BrowserCompatible.mask) != 0) {
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
                    buf[i + 4] = ascii_chars[ch * 2];
                    buf[i + 5] = ascii_chars[ch * 2 + 1];
                    end += 5;
                    continue;
                }

                if (ch >= 127) {
                    System.arraycopy(buf, i + 1, buf, i + 6, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = 'u';
                    buf[i + 2] = DIGITS[(ch >>> 12) & 15];
                    buf[i + 3] = DIGITS[(ch >>> 8) & 15];
                    buf[i + 4] = DIGITS[(ch >>> 4) & 15];
                    buf[i + 5] = DIGITS[ch & 15];
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
                    if (ch >= 0x7F && ch < 0xA0) {
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
                
                final boolean isSpecial;
                {
                    if (ch == ' ') {
                        isSpecial = false;
                    } else if (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0) {
                        isSpecial = true;
                    } else if (ch > '#' && ch != '\\') {
                        isSpecial = false;
                    } else if (ch <= 0x1F || ch == '\\' || ch == '"') {
                        isSpecial = true;
                    } else {
                        isSpecial = false;
                    }
                }

                if (isSpecial) {
                    specialCount++;
                    lastSpecialIndex = i;
                    lastSpecial = ch;

                    if (ch < specicalFlags_doubleQuotes.length //
                        && specicalFlags_doubleQuotes[ch] == 4 //
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
                        if (ch < specicalFlags_doubleQuotes.length //
                            && specicalFlags_doubleQuotes[ch] == 4) {
                            int srcPos = lastSpecialIndex + 1;
                            int destPos = lastSpecialIndex + 6;
                            int LengthOfCopy = end - lastSpecialIndex - 1;
                            System.arraycopy(buf, srcPos, buf, destPos, LengthOfCopy);

                            int bufIndex = lastSpecialIndex;
                            buf[bufIndex++] = '\\';
                            buf[bufIndex++] = 'u';
                            buf[bufIndex++] = DIGITS[(ch >>> 12) & 15];
                            buf[bufIndex++] = DIGITS[(ch >>> 8) & 15];
                            buf[bufIndex++] = DIGITS[(ch >>> 4) & 15];
                            buf[bufIndex++] = DIGITS[ch & 15];
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

                        if (ch < specicalFlags_doubleQuotes.length //
                            && specicalFlags_doubleQuotes[ch] != 0 //
                            || (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
                            buf[bufIndex++] = '\\';
                            if (specicalFlags_doubleQuotes[ch] == 4) {
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = DIGITS[(ch >>> 12) & 15];
                                buf[bufIndex++] = DIGITS[(ch >>> 8) & 15];
                                buf[bufIndex++] = DIGITS[(ch >>> 4) & 15];
                                buf[bufIndex++] = DIGITS[ch & 15];
                                end += 5;
                            } else {
                                buf[bufIndex++] = replaceChars[(int) ch];
                                end++;
                            }
                        } else {
                            if (ch == '\u2028') {
                                buf[bufIndex++] = '\\';
                                buf[bufIndex++] = 'u';
                                buf[bufIndex++] = DIGITS[(ch >>> 12) & 15];
                                buf[bufIndex++] = DIGITS[(ch >>> 8) & 15];
                                buf[bufIndex++] = DIGITS[(ch >>> 4) & 15];
                                buf[bufIndex++] = DIGITS[ch & 15];
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

    public void write(boolean value) {
        write(value ? "true" : "false");
    }

    public void writeString(String text) {
        if ((features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            writeStringWithSingleQuote(text);
        } else {
            writeStringWithDoubleQuote(text, (char) 0, true);
        }
    }

    protected void writeStringWithSingleQuote(String text) {
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
                        || (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
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
                || (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
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
                    || (ch == '/' && (features & SerializerFeature.WriteSlashAsSpecial.mask) != 0)) {
                    System.arraycopy(buf, i + 1, buf, i + 2, end - i - 1);
                    buf[i] = '\\';
                    buf[i + 1] = replaceChars[(int) ch];
                    end++;
                }
            }
        }

        buf[count - 1] = '\'';
    }

    public void writeFieldName(String key, boolean checkSpecial) {
        if ((features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            if ((features & SerializerFeature.QuoteFieldNames.mask) != 0) {
                writeStringWithSingleQuote(key);
                write(':');
            } else {
                writeKeyWithSingleQuoteIfHasSpecial(key);
            }
        } else {
            if ((features & SerializerFeature.QuoteFieldNames.mask) != 0) {
                writeStringWithDoubleQuote(key, ':', checkSpecial);
            } else {
                writeKeyWithDoubleQuoteIfHasSpecial(key);
            }
        }
    }

    private void writeKeyWithDoubleQuoteIfHasSpecial(String text) {
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
            count = 0;
        } catch (IOException e) {
            throw new JSONException(e.getMessage(), e);
        }
    }
    
    final static int[]  sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 0x7fffffff /*Integer.MAX_VALUE*/ };

    protected static void getChars(long i, int index, char[] buf) {
        long q;
        int r;
        int charPos = index;
        char sign = 0;

        if (i < 0) {
            sign = '-';
            i = -i;
        }

        // Get 2 digits/iteration using longs until quotient fits into an int
        while (i > 0x7fffffff /* Integer.MAX_VALUE */ ) {
            q = i / 100;
            // really: r = i - (q * 100);
            r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
            i = q;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Get 2 digits/iteration using ints
        int q2;
        int i2 = (int) i;
        while (i2 >= 65536) {
            q2 = i2 / 100;
            // really: r = i2 - (q * 100);
            r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
            i2 = q2;
            buf[--charPos] = DigitOnes[r];
            buf[--charPos] = DigitTens[r];
        }

        // Fall thru to fast mode for smaller numbers
        // assert(i2 <= 65536, i2);
        for (;;) {
            q2 = (i2 * 52429) >>> (16 + 3);
            r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
            buf[--charPos] = digits[r];
            i2 = q2;
            if (i2 == 0) break;
        }
        if (sign != 0) {
            buf[--charPos] = sign;
        }
    }

    /**
     * All possible chars for representing a number as a String
     */
    final static char[] digits    = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    final static char[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1',
            '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3',
            '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
            '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', };

    final static char[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };


    final static char[]    ascii_chars                = { '0', '0', '0', '1', '0', '2', '0', '3', '0', '4', '0',
                                                                 '5', '0', '6', '0', '7', '0', '8', '0', '9', '0', 'A', '0', 'B', '0', 'C', '0', 'D', '0', 'E', '0', 'F',
                                                                 '1', '0', '1', '1', '1', '2', '1', '3', '1', '4', '1', '5', '1', '6', '1', '7', '1', '8', '1', '9', '1',
                                                                 'A', '1', 'B', '1', 'C', '1', 'D', '1', 'E', '1', 'F', '2', '0', '2', '1', '2', '2', '2', '3', '2', '4',
                                                                 '2', '5', '2', '6', '2', '7', '2', '8', '2', '9', '2', 'A', '2', 'B', '2', 'C', '2', 'D', '2', 'E', '2',
                                                                 'F',                                            };
    
    final static byte[]    specicalFlags_doubleQuotes = new byte[161];
    final static byte[]    specicalFlags_singleQuotes = new byte[161];

    final static char[]    replaceChars               = new char[93];
    static {
        specicalFlags_doubleQuotes['\0'] = 4;
        specicalFlags_doubleQuotes['\1'] = 4;
        specicalFlags_doubleQuotes['\2'] = 4;
        specicalFlags_doubleQuotes['\3'] = 4;
        specicalFlags_doubleQuotes['\4'] = 4;
        specicalFlags_doubleQuotes['\5'] = 4;
        specicalFlags_doubleQuotes['\6'] = 4;
        specicalFlags_doubleQuotes['\7'] = 4;
        specicalFlags_doubleQuotes['\b'] = 1; // 8
        specicalFlags_doubleQuotes['\t'] = 1; // 9
        specicalFlags_doubleQuotes['\n'] = 1; // 10
        specicalFlags_doubleQuotes['\u000B'] = 4; // 11
        specicalFlags_doubleQuotes['\f'] = 1;
        specicalFlags_doubleQuotes['\r'] = 1;
        specicalFlags_doubleQuotes['\"'] = 1;
        specicalFlags_doubleQuotes['\\'] = 1;

        specicalFlags_singleQuotes['\0'] = 4;
        specicalFlags_singleQuotes['\1'] = 4;
        specicalFlags_singleQuotes['\2'] = 4;
        specicalFlags_singleQuotes['\3'] = 4;
        specicalFlags_singleQuotes['\4'] = 4;
        specicalFlags_singleQuotes['\5'] = 4;
        specicalFlags_singleQuotes['\6'] = 4;
        specicalFlags_singleQuotes['\7'] = 4;
        specicalFlags_singleQuotes['\b'] = 1; // 8
        specicalFlags_singleQuotes['\t'] = 1; // 9
        specicalFlags_singleQuotes['\n'] = 1; // 10
        specicalFlags_singleQuotes['\u000B'] = 4; // 11
        specicalFlags_singleQuotes['\f'] = 1; // 12
        specicalFlags_singleQuotes['\r'] = 1; // 13
        specicalFlags_singleQuotes['\\'] = 1;
        specicalFlags_singleQuotes['\''] = 1;

        for (int i = 14; i <= 31; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

        for (int i = 127; i < 160; ++i) {
            specicalFlags_doubleQuotes[i] = 4;
            specicalFlags_singleQuotes[i] = 4;
        }

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
    
    public final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                                          'F' };
}
