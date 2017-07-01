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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public final class DateCodec implements ObjectSerializer, ObjectDeserializer {

    public final static DateCodec instance = new DateCodec();

    private DateCodec() {
        
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        if ((out.features & SerializerFeature.WriteClassName.mask) != 0) {
            if (object.getClass() != fieldType) {
                if (object.getClass() == java.util.Date.class) {
                    out.write("new Date(");
                    out.writeLong(((Date) object).getTime());
                    out.write(')');
                } else {
                    out.write('{');
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY, false);
                    serializer.write(object.getClass().getName());
                    out.write(',');
                    out.writeFieldName("val", false);
                    out.writeLong(((Date) object).getTime());
                    out.write('}');
                }
                return;
            }
        }
        
        Date date;
        if (object instanceof Calendar) {
            Calendar calendar = (Calendar) object;
            date = calendar.getTime();            
        } else {
            date = (Date) object;
        }
        
        if ((out.features & SerializerFeature.WriteDateUseDateFormat.mask) != 0) {
            DateFormat format = serializer.getDateFormat();
            if (format == null) {
                format = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT, serializer.locale);
                format.setTimeZone(serializer.timeZone);
            }
            String text = format.format(date);
            out.writeString(text);
            return;
        }

        long time = date.getTime();
        
        if ((out.features & SerializerFeature.UseISO8601DateFormat.mask) != 0) {
            if ((out.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                out.write('\'');
            } else {
                out.write('\"');
            }

            Calendar calendar = Calendar.getInstance(serializer.timeZone, serializer.locale);
            calendar.setTimeInMillis(time);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int millis = calendar.get(Calendar.MILLISECOND);

            char[] buf;
            if (millis != 0) {
                buf = "0000-00-00T00:00:00.000".toCharArray();
                SerializeWriter.getChars(millis, 23, buf);
                SerializeWriter.getChars(second, 19, buf);
                SerializeWriter.getChars(minute, 16, buf);
                SerializeWriter.getChars(hour, 13, buf);
                SerializeWriter.getChars(day, 10, buf);
                SerializeWriter.getChars(month, 7, buf);
                SerializeWriter.getChars(year, 4, buf);

            } else {
                if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    SerializeWriter.getChars(day, 10, buf);
                    SerializeWriter.getChars(month, 7, buf);
                    SerializeWriter.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    SerializeWriter.getChars(second, 19, buf);
                    SerializeWriter.getChars(minute, 16, buf);
                    SerializeWriter.getChars(hour, 13, buf);
                    SerializeWriter.getChars(day, 10, buf);
                    SerializeWriter.getChars(month, 7, buf);
                    SerializeWriter.getChars(year, 4, buf);
                }
            }

            out.write(buf);

            if ((out.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
                out.write('\'');
            } else {
                out.write('\"');
            }
        } else {
            out.writeLong(time);
        }
    }
    
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return deserialze(parser, clazz, fieldName, null);   
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName, String format) {
        JSONLexer lexer = parser.lexer;

        Object val;
        int token = lexer.token();
        if (token == JSONToken.LITERAL_INT) {
            val = lexer.longValue();
            lexer.nextToken(JSONToken.COMMA);
        } else if (token == JSONToken.LITERAL_STRING) {
            String strVal = lexer.stringVal();
            val = strVal;
            lexer.nextToken(JSONToken.COMMA);
            
            if ((lexer.features & Feature.AllowISO8601DateFormat.mask) != 0) {
                JSONLexer iso8601Lexer = new JSONLexer(strVal);
                if (iso8601Lexer.scanISO8601DateIfMatch(true)) {
                    Calendar calendar = iso8601Lexer.calendar;
                    if (clazz == Calendar.class) {
                        iso8601Lexer.close();
                        return (T) calendar;
                    }
                    val = calendar.getTime();
                }
                iso8601Lexer.close();
            }
        } else if (token == JSONToken.NULL) {
            lexer.nextToken();
            val = null;
        } else if (token == JSONToken.LBRACE) {
            lexer.nextToken();
            
            String key;
            if (lexer.token() == JSONToken.LITERAL_STRING) {
                key = lexer.stringVal();
                
                if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                    lexer.nextToken();
                    parser.accept(JSONToken.COLON);
                    
                    String typeName = lexer.stringVal();
                    Class<?> type = TypeUtils.loadClass(typeName, parser.config.defaultClassLoader);
                    if (type != null) {
                        clazz = type;
                    }
                    
                    parser.accept(JSONToken.LITERAL_STRING);
                    parser.accept(JSONToken.COMMA);
                }
                
                lexer.nextTokenWithChar(':');
            } else {
                throw new JSONException("syntax error");
            }
            
            long timeMillis;
            token = lexer.token();
            if (token == JSONToken.LITERAL_INT) {
                timeMillis = lexer.longValue();
                lexer.nextToken();
            } else {
                throw new JSONException("syntax error : " + JSONToken.name(token));
            }
            
            val = timeMillis;
            
            parser.accept(JSONToken.RBRACE);
        } else if (parser.resolveStatus == DefaultJSONParser.TypeNameRedirect) {
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

            val = parser.parse();

            parser.accept(JSONToken.RBRACE);
        } else {
            val = parser.parse();
        }

        Object obj = cast(parser, clazz, fieldName, val, format); 
        if (clazz == Calendar.class) {
            if (obj instanceof Calendar) {
                return (T) obj;
            }
            
            Date date = (Date) obj;
            if (date == null) {
                return null;
            }
            
            Calendar calendar = Calendar.getInstance(lexer.timeZone, lexer.locale);
            calendar.setTime(date);
            
            return (T) calendar;
        }
        return (T) obj;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val, String format) {

        if (val == null) {
            return null;
        }

        if (val instanceof java.util.Date) {
            return (T) val;
        } else if (val instanceof Number) {
            return (T) new java.util.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            JSONLexer dateLexer = new JSONLexer(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    Calendar calendar = dateLexer.calendar;
                    
                    if (clazz == Calendar.class) {
                        return (T) calendar;
                    }
                    
                    return (T) calendar.getTime();
                }
            } finally {
                dateLexer.close();
            }

            if ("0000-00-00".equals(strVal)
                    || "0000-00-00T00:00:00".equalsIgnoreCase(strVal)
                    || "0001-01-01T00:00:00+08:00".equalsIgnoreCase(strVal)) {
                return null;
            }

            DateFormat dateFormat;
            
            if (format != null) {
                dateFormat = new SimpleDateFormat(format);
            } else {
                dateFormat = parser.getDateFormat();    
            }
            try {
                return (T) dateFormat.parse(strVal);
            } catch (ParseException e) {
                // skip
            }

            long longVal = Long.parseLong(strVal);
            return (T) new java.util.Date(longVal);
        }

        throw new JSONException("parse error");
    }
}
