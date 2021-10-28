package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONException;

public class ClobSerializer implements ObjectSerializer {

    public final static ClobSerializer instance = new ClobSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try {
            if (object == null) {
                serializer.writeNull();
                return;
            }

            Clob clob = (Clob) object;
            Reader reader = clob.getCharacterStream();

            long length;
            // JDBC driver may not support clob.length()
            try {
                length = clob.length();
            } catch (SQLException e) {
                length = 2048;
            }

            // initialize capacity
            int capacity = (int) Math.min(length, Integer.MAX_VALUE);
            StringBuilder buf = new StringBuilder(capacity);
            final char[] chars = new char[Math.min(capacity, 2048)];
            try {
                for (int len; (len = reader.read(chars)) != -1; ) {
                    buf.append(chars, 0, len);
                }
            } catch (Exception ex) {
                throw new JSONException("read string from reader error", ex);
            } finally {
                reader.close();
            }

            String text = buf.toString();
            serializer.write(text);
        } catch (SQLException e) {
            // constructor IOException(String, Throwable) requires Java 6+
            IOException ioe = new IOException("write clob error");
            ioe.initCause(e);
            throw ioe;
        }
    }

}
