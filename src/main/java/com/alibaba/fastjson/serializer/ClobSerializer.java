package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;

import com.alibaba.fastjson.util.IOUtils;

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
            String text = IOUtils.readAll(reader, capacity);

            serializer.write(text);
        } catch (SQLException e) {
            // constructor IOException(String, Throwable) requires Java 6+
            throw IOUtils.newIOException("write clob error", e);
        }
    }

}
