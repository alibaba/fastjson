package com.alibaba.fastjson;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public abstract class JSONValidator implements Cloneable {
    public enum Type {
        Object, Array, Value
    }

    protected boolean eof;
    protected int pos = -1;
    protected char ch;
    protected Type type;

    protected int count = 0;
    protected boolean supportMultiValue = true;

    public static JSONValidator fromUtf8(byte[] jsonBytes) {
        return new UTF8Validator(jsonBytes);
    }

    public static JSONValidator fromUtf8(InputStream is) {
        return new UTF8InputStreamValidator(is);
    }

    public static JSONValidator from(String jsonStr) {
        return new UTF16Validator(jsonStr);
    }

    public static JSONValidator from(Reader r) {
        return new ReaderValidator(r);
    }

    public Type getType() {
        return type;
    }

    abstract void next();

    public boolean validate() {

        for (;;) {
            any();
            count++;

            if (supportMultiValue && !eof) {
                skipWhiteSpace();
                if (eof) {
                    break;
                }
                continue;
            } else {
                break;
            }
        }

        return true;
    }

    public void close() throws IOException {

    }

    void any() {
        switch (ch) {
            case '{':
                next();
                skipWhiteSpace();
                if (ch == '}') {
                    next();
                    type = Type.Object;
                    return;
                }

                for (;;) {
                    if (ch == '"') {
                        fieldName();
                    }
                    else {
                        error();
                    }

                    skipWhiteSpace();
                    if (ch == ':') {
                        next();
                    }
                    else {
                        error();
                    }
                    skipWhiteSpace();
                    any();

                    skipWhiteSpace();
                    if (ch == ',') {
                        next();
                        skipWhiteSpace();
                        continue;
                    } else if (ch == '}') {
                        next();
                        type = Type.Object;
                        return;
                    }
                }
            case '[':
                next();
                skipWhiteSpace();

                if (ch == ']') {
                    next();
                    type = Type.Array;
                    return;
                }

                for (; ; ) {
                    any();

                    skipWhiteSpace();
                    if (ch == ',') {
                        next();
                        skipWhiteSpace();
                    } else if (ch == ']') {
                        next();
                        type = Type.Array;
                        return;
                    }
                    else {
                        error();
                    }
                }
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '+':
            case '-':
                if (ch == '-' || ch == '+') {
                    next();
                    skipWhiteSpace();
                    if (ch < '0' || ch > '9') {
                        error();
                    }
                }

                do {
                    next();
                }
                while (ch >= '0' && ch <= '9');

                if (ch == '.') {
                    next();
                    // bug fix: 0.e7 should not pass the test
                    if (ch < '0' || ch > '9') {
                        error();
                    }
                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                if (ch == 'e' || ch == 'E') {
                    next();
                    if (ch == '-' || ch == '+') {
                        next();
                    }

                    if (ch >= '0' && ch <= '9') {
                        next();
                    }
                    else {
                        error();
                    }

                    while (ch >= '0' && ch <= '9') {
                        next();
                    }
                }

                type = Type.Value;
                break;
            case '"':
                next();
                for (;;) {
                    if (ch == '\\') {
                        next();
                        if (ch == 'u') {
                            next();

                            next();
                            next();
                            next();
                            next();
                        } else {
                            next();
                        }
                    } else if (ch == '"') {
                        next();
                        type = Type.Value;
                        return;
                    } else {
                        next();
                    }
                }
            case 't':
                next();

                if (ch != 'r') {
                    error();
                }
                next();

                if (ch != 'u') {
                    error();
                }
                next();

                if (ch != 'e') {
                    error();
                }
                next();

                if (isWhiteSpace(ch) || ch == ',' || ch == ']' || ch == '}' || ch == '\0') {
                    type = Type.Value;
                    return;
                }
                error();
            case 'f':
                next();

                if (ch != 'a') {
                    error();
                }
                next();

                if (ch != 'l') {
                    error();
                }
                next();

                if (ch != 's') {
                    error();
                }
                next();

                if (ch != 'e') {
                    error();
                }
                next();

                if (isWhiteSpace(ch) || ch == ',' || ch == ']' || ch == '}' || ch == '\0') {
                    type = Type.Value;
                    return;
                }
                error();
            case 'n':
                next();

                if (ch != 'u') {
                    error();
                }
                next();

                if (ch != 'l') {
                    error();
                }
                next();

                if (ch != 'l') {
                    error();
                }
                next();

                if (isWhiteSpace(ch) || ch == ',' || ch == ']' || ch == '}' || ch == '\0') {
                    type = Type.Value;
                    return;
                }
                error();
            default:
                error();
        }
    }

    protected void fieldName()
    {
        next();
        for (; ; ) {
            if (ch == '\\') {
                next();

                if (ch == 'u') {
                    next();

                    next();
                    next();
                    next();
                    next();
                } else {
                    next();
                }
            }
            else if (ch == '"') {
                next();
                break;
            }
            else {
                next();
            }
        }
    }

    void error() {
        throw new JSONException("error : " + pos);
    }

    void skipWhiteSpace() {
        while (isWhiteSpace(ch)) {
            next();
        }
    }

    static final boolean isWhiteSpace(char ch) {
        return ch == ' '
                || ch == '\t'
                || ch == '\r'
                || ch == '\n'
                || ch == '\f'
                || ch == '\b'
                ;
    }

    static class UTF8Validator extends JSONValidator {
        private final byte[] bytes;

        public UTF8Validator(byte[] bytes) {
            this.bytes = bytes;
            next();
            skipWhiteSpace();
        }

        void next() {
            ++pos;

            if (pos >= bytes.length) {
                ch = '\0';
                eof = true;
            } else {
                ch = (char) bytes[pos];
            }
        }
    }

    static class UTF8InputStreamValidator extends JSONValidator {
        private final static ThreadLocal<byte[]> bufLocal = new ThreadLocal<byte[]>();

        private final InputStream is;
        private byte[] buf;
        private int end = -1;
        private int readCount = 0;

        public UTF8InputStreamValidator(InputStream is) {
            this.is = is;
            buf = bufLocal.get();
            if (buf != null) {
                bufLocal.set(null);
            } else {
                buf = new byte[1024 * 8];
            }

            next();
            skipWhiteSpace();
        }

        void next() {
            if (pos < end) {
                ch = (char) buf[++pos];
            } else {
                if (!eof) {
                    int len;
                    try {
                        len = is.read(buf, 0, buf.length);
                        readCount++;
                    } catch (IOException ex) {
                        throw new JSONException("read error");
                    }

                    if (len > 0) {
                        ch = (char) buf[0];
                        pos = 0;
                        end = len - 1;
                    }
                    else if (len == -1) {
                        pos = 0;
                        end = 0;
                        buf = null;
                        ch = '\0';
                        eof = true;
                    } else {
                        pos = 0;
                        end = 0;
                        buf = null;
                        ch = '\0';
                        eof = true;
                        throw new JSONException("read error");
                    }
                }
            }
        }

        void error() {
            throw new JSONException("error, readCount " + readCount + ", valueCount : " + count + ", pos " + pos);
        }

        public void close() throws IOException {
            bufLocal.set(buf);
            is.close();
        }
    }

    static class UTF16Validator extends JSONValidator {
        private final String str;

        public UTF16Validator(String str) {
            this.str = str;
            next();
            skipWhiteSpace();
        }

        void next() {
            ++pos;

            if (pos >= str.length()) {
                ch = '\0';
                eof = true;
            } else {
                ch = str.charAt(pos);
            }
        }
    }

    static class ReaderValidator extends JSONValidator {
        private final static ThreadLocal<char[]> bufLocal = new ThreadLocal<char[]>();

        final Reader r;

        private char[] buf;
        private int end = -1;
        private int readCount = 0;

        ReaderValidator(Reader r) {
            this.r = r;
            buf = bufLocal.get();
            if (buf != null) {
                bufLocal.set(null);
            } else {
                buf = new char[1024 * 8];
            }

            next();
            skipWhiteSpace();
        }

        void next() {
            if (pos < end) {
                ch = buf[++pos];
            } else {
                if (!eof) {
                    int len;
                    try {
                        len = r.read(buf, 0, buf.length);
                        readCount++;
                    } catch (IOException ex) {
                        throw new JSONException("read error");
                    }

                    if (len > 0) {
                        ch = buf[0];
                        pos = 0;
                        end = len - 1;
                    }
                    else if (len == -1) {
                        pos = 0;
                        end = 0;
                        buf = null;
                        ch = '\0';
                        eof = true;
                    } else {
                        pos = 0;
                        end = 0;
                        buf = null;
                        ch = '\0';
                        eof = true;
                        throw new JSONException("read error");
                    }
                }
            }
        }

        void error() {
            throw new JSONException("error, readCount " + readCount + ", valueCount : " + count + ", pos " + pos);
        }

        public void close() throws IOException {
            bufLocal.set(buf);
            r.close();;
        }
    }
}
