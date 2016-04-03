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
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class MiscCodec implements ObjectSerializer, ObjectDeserializer {

    public final static MiscCodec instance = new MiscCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;
        
        if (object == null) {
            out.writeNull();
            return;
        }
        
        if (object.getClass() == SimpleDateFormat.class) {
            String pattern = ((SimpleDateFormat) object).toPattern();

            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                if (object.getClass() != fieldType) {
                    out.write('{');
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", pattern);
                    out.write('}');
                    return;
                }
            }
            
            out.writeString(pattern);
            
            return;
        }
        
        serializer.write(object.toString());
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {

        JSONLexer lexer = parser.lexer;
        
        Object objVal;
        
        if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
            parser.resolveStatus = DefaultJSONParser.NONE;
            parser.accept(JSONToken.COMMA);

            if (lexer.token() == JSONToken.LITERAL_STRING) {
                if (!"val".equals(lexer.stringVal())) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error");
            }

            parser.accept(JSONToken.COLON);

            objVal = parser.parse();

            parser.accept(JSONToken.RBRACE);
        } else {
            objVal = parser.parse();
        }
        
        String strVal = (String) objVal;

        if (strVal == null || strVal.length() == 0) {
            return null;
        }

        if (clazz == UUID.class) {
            return (T) UUID.fromString(strVal);
        }

        if (clazz == URI.class) {
            return (T) URI.create(strVal);
        }

        if (clazz == URL.class) {
            try {
                return (T) new URL(strVal);
            } catch (MalformedURLException e) {
                throw new JSONException("create url error", e);
            }
        }

        if (clazz == Pattern.class) {
            return (T) Pattern.compile(strVal);
        }

        if (clazz == Locale.class) {
            String[] items = strVal.split("_");

            if (items.length == 1) {
                return (T) new Locale(items[0]);
            }

            if (items.length == 2) {
                return (T) new Locale(items[0], items[1]);
            }

            return (T) new Locale(items[0], items[1], items[2]);
        }
        
        if (clazz == SimpleDateFormat.class) {
            return (T) new SimpleDateFormat(strVal);
        }
        
        throw new JSONException("MiscCodec not support " + clazz);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
