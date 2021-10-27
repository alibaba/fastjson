package com.alibaba.fastjson;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.alibaba.fastjson.parser.JSONLexerBase;

public abstract class JSONValidator implements Cloneable, Closeable {
    public enum Type {
        Object, Array, Value
    }

    protected boolean eof;
    /** current index */
    protected int pos = -1;
    /** exclude index */
    protected int end = -1;
    protected char ch;
    protected Type type;
    private Boolean validateResult;

    protected int count = 0;
    protected boolean supportMultiValue = false;

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

    public boolean isSupportMultiValue() {
        return supportMultiValue;
    }

    public JSONValidator setSupportMultiValue(boolean supportMultiValue) {
        this.supportMultiValue = supportMultiValue;
        return this;
    }

    public Type getType() {
        if (type == null) {
            // FIXME 返回值没被使用，是否需要根据返回值抛出异常？
            validate();
        }

        return type;
    }

    void next() {
        if (eof) {
            return;
        }
        pos++;
        if (pos < end) {
            ch = charAt(pos);
        } else {
            int len;
            try {
                len = read();
            } catch (IOException e) {
                throw new JSONException("read error", e);
            }

            afterRead(len);
        }
    }

    abstract char charAt(int index);

    int read() throws IOException {
        // default does nothing
        return 0;
    }

    void afterRead(int len) throws JSONException {
        if (len > 0) {
            pos = 0;
            ch = charAt(pos);
            end = len;
        } else {
            pos = -1;
            end = -1;
            ch = '\0';
            // 目前看来，上面这几个属性的重置操作貌似是多余的？
            eof = true;
            if (len < -1) {
                throw new JSONException("read error");
            }
        }
    }

    public boolean validate() {
        if (validateResult != null) {
            return validateResult;
        }

        for (;;) {
            if (!any()) {
                return validateResult = false;
            }
            skipWhiteSpace();

            count++;
            if (eof) {
                return validateResult = true;
            }

            if (supportMultiValue) {
                skipWhiteSpace();
                if (eof) {
                    break;
                }
            } else {
                return validateResult = false;
            }
        }

        return validateResult = true;
    }

    public void close() throws IOException {

    }

    private boolean any() {
        switch (ch) {
            case '{':
                next();

                while (JSONLexerBase.isWhitespace(ch)) {
                    next();
                }

                if (ch == '}') {
                    next();
                    type = Type.Object;
                    return true;
                }

                for (;;) {
                    if (ch == '"') {
                        fieldName();
                    } else {
                        return false;
                    }

                    skipWhiteSpace();
                    if (ch == ':') {
                        next();
                    } else {
                        return false;
                    }
                    skipWhiteSpace();

                    if (!any()) {
                        return false;
                    }

                    // kv 结束时，只能是 "," 或 "}"
                    skipWhiteSpace();
                    if (ch == ',') {
                        next();
                        skipWhiteSpace();
                    } else if (ch == '}') {
                        next();
                        type = Type.Object;
                        return true;
                    } else {
                        return false;
                    }
                }
            case '[':
                next();
                skipWhiteSpace();

                if (ch == ']') {
                    next();
                    type = Type.Array;
                    return true;
                }

                for (;;) {
                    if (!any()) {
                        return false;
                    }

                    skipWhiteSpace();
                    if (ch == ',') {
                        next();
                        skipWhiteSpace();
                    } else if (ch == ']') {
                        next();
                        type = Type.Array;
                        return true;
                    }
                    else {
                        return false;
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
                        return false;
                    }
                }

                do {
                    next();
                } while (ch >= '0' && ch <= '9');

                if (ch == '.') {
                    next();
                    // bug fix: 0.e7 should not pass the test
                    if (ch < '0' || ch > '9') {
                        return false;
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
                        return false;
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
                    if (eof) {
                        return false;
                    }

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
                        return true;
                    } else {
                        next();
                    }
                }
            case 't':
                if (nextMatchKeyword("true", 1)) {
                    type = Type.Value;
                    return true;
                }
                return false;
            case 'f':
                if (nextMatchKeyword("false", 1)) {
                    type = Type.Value;
                    return true;
                }
                return false;
            case 'n':
                if (nextMatchKeyword("null", 1)) {
                    type = Type.Value;
                    return true;
                }
                return false;
            default:
                return false;
        }
        return true;
    }

    boolean nextMatchKeyword(String keyword, int startIndex) {
        for (int i = startIndex, size = keyword.length(); i < size; i++) {
            next();
            if (ch != keyword.charAt(i)) {
                return false;
            }
        }
        next();
        return isWordBoundary(ch);
    }

    protected boolean isWordBoundary(char ch) {
        return JSONLexerBase.isWhitespace(ch) || ch == ',' || ch == ']' || ch == '}' || ch == '\0';
    }

    protected void fieldName() {
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
            } else if (ch == '"') {
                next();
                break;
            } else {
                next();
            }
        }
    }

    protected boolean string() {
        next();
        while (!eof) {
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
                return true;
            } else {
                next();
            }
        }

        return false;
    }

    void skipWhiteSpace() {
        while (JSONLexerBase.isWhitespace(ch)) {
            next();
        }
    }

    static class UTF8Validator extends JSONValidator {
        private final byte[] bytes;

        public UTF8Validator(byte[] bytes) {
            this.bytes = bytes;
            this.end = bytes.length;
            next();
            skipWhiteSpace();
        }

        @Override
        char charAt(int index) {
            return (char) bytes[index];
        }

    }

    static class UTF8InputStreamValidator extends JSONValidator {
        private final static ThreadLocal<byte[]> bufLocal = new ThreadLocal<byte[]>();

        private final InputStream is;
        private byte[] buf;
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

        @Override
        char charAt(int index) {
            return (char) buf[index];
        }

        @Override
        int read() throws IOException {
            int len = is.read(buf);
            readCount++;
            return len;
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
            this.end = str.length();
            next();
            skipWhiteSpace();
        }

        @Override
        char charAt(int index) {
            return str.charAt(index);
        }

        protected final void fieldName() {
            for (int i = pos + 1; i < str.length(); ++i) {
                char ch = str.charAt(i);
                if (ch == '\\') {
                    break;
                }
                if (ch == '"') {
                    this.ch = str.charAt(i + 1);
                    pos = i + 1;
                    return;
                }
            }

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
                } else if (ch == '"') {
                    next();
                    break;
                } else if (eof) {
                    break;
                } else {
                    next();
                }
            }
        }

    }

    static class ReaderValidator extends JSONValidator {
        private final static ThreadLocal<char[]> bufLocal = new ThreadLocal<char[]>();

        final Reader r;

        private char[] buf;
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

        @Override
        char charAt(int index) {
            return buf[index];
        }

        @Override
        int read() throws IOException {
            int len = r.read(buf);
            readCount++;
            return len;
        }

        public void close() throws IOException {
            bufLocal.set(buf);
            r.close();
        }
    }
}
