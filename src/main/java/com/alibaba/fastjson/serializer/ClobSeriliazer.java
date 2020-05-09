package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Clob;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONException;

public class ClobSeriliazer implements ObjectSerializer {

    public final static ClobSeriliazer instance = new ClobSeriliazer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try {
            if (object == null) {
                serializer.writeNull();
                return;
            }
            
            Clob clob = (Clob) object;
            Reader reader = clob.getCharacterStream();

            StringBuilder buf = new StringBuilder();
            
            try {
                char[] chars = new char[2048];
                for (;;) {
                    int len = reader.read(chars, 0, chars.length);
                    if (len < 0) {
                        break;
                    }
                    buf.append(chars, 0, len);
                }
            } catch(Exception ex) {
                throw new JSONException("read string from reader error", ex);
            }
            
            String text = buf.toString();
            reader.close();
            serializer.write(text);
        } catch (SQLException e) {
            throw new IOException("write clob error", e);
        }
    }

}
