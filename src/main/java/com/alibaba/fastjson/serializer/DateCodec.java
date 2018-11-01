/*
 * Copyright 1999-2018 Alibaba Group.
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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class DateCodec extends AbstractDateDeserializer implements ObjectSerializer, ObjectDeserializer {

    public final static DateCodec instance = new DateCodec();
    
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }
        
        Date date;
        if (object instanceof Date) {
            date = (Date) object;
        } else {
            date = TypeUtils.castToDate(object);
        }
        
        if (out.isEnabled(SerializerFeature.WriteDateUseDateFormat)) {
            DateFormat format = serializer.getDateFormat();
            if (format == null) {
                format = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT, serializer.locale);
                format.setTimeZone(serializer.timeZone);
            }
            String text = format.format(date);
            out.writeString(text);
            return;
        }
        
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            if (object.getClass() != fieldType) {
                if (object.getClass() == java.util.Date.class) {
                    out.write("new Date(");
                    out.writeLong(((Date) object).getTime());
                    out.write(')');
                } else {
                    out.write('{');
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    serializer.write(object.getClass().getName());
                    out.writeFieldValue(',', "val", ((Date) object).getTime());
                    out.write('}');
                }
                return;
            }
        }

        long time = date.getTime();
        if (out.isEnabled(SerializerFeature.UseISO8601DateFormat)) {
            char quote = out.isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"'; 
            out.write(quote);

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
                IOUtils.getChars(millis, 23, buf);
                IOUtils.getChars(second, 19, buf);
                IOUtils.getChars(minute, 16, buf);
                IOUtils.getChars(hour, 13, buf);
                IOUtils.getChars(day, 10, buf);
                IOUtils.getChars(month, 7, buf);
                IOUtils.getChars(year, 4, buf);

            } else {
                if (second == 0 && minute == 0 && hour == 0) {
                    buf = "0000-00-00".toCharArray();
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                } else {
                    buf = "0000-00-00T00:00:00".toCharArray();
                    IOUtils.getChars(second, 19, buf);
                    IOUtils.getChars(minute, 16, buf);
                    IOUtils.getChars(hour, 13, buf);
                    IOUtils.getChars(day, 10, buf);
                    IOUtils.getChars(month, 7, buf);
                    IOUtils.getChars(year, 4, buf);
                }
            }
            
            out.write(buf);

            int timeZone = calendar.getTimeZone().getOffset(calendar.getTimeInMillis()) / (3600 * 1000);
            if (timeZone == 0) {
                out.write('Z');
            } else {
                if (timeZone > 9) {
                    out.write('+');
                    out.writeInt(timeZone);
                } else if (timeZone > 0) {
                    out.write('+');
                    out.write('0');
                    out.writeInt(timeZone);
                } else if (timeZone < -9) {
                    out.write('-');
                    out.writeInt(timeZone);
                } else if (timeZone < 0) {
                    out.write('-');
                    out.write('0');
                    out.writeInt(-timeZone);
                }

                out.append(":00");
            }

            out.write(quote);
        } else {
            out.writeLong(time);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {

        if (val == null) {
            return null;
        }

        if (val instanceof java.util.Date) {
            return (T) val;
        } else if (val instanceof BigDecimal) {
            return (T) new java.util.Date(TypeUtils.longValue((BigDecimal) val));
        } else if (val instanceof Number) {
            return (T) new java.util.Date(((Number) val).longValue());
        } else if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }

            {
                JSONScanner dateLexer = new JSONScanner(strVal);
                try {
                    if (dateLexer.scanISO8601DateIfMatch(false)) {
                        Calendar calendar = dateLexer.getCalendar();

                        if (clazz == Calendar.class) {
                            return (T) calendar;
                        }

                        return (T) calendar.getTime();
                    }
                } finally {
                    dateLexer.close();
                }
            }
            
            if (strVal.length() == parser.getDateFomartPattern().length()
                    || (strVal.length() == 22 && parser.getDateFomartPattern().equals("yyyyMMddHHmmssSSSZ"))) {
                DateFormat dateFormat = parser.getDateFormat();
                try {
                    return (T) dateFormat.parse(strVal);
                } catch (ParseException e) {
                    // skip
                }
            }
            
            if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                String dotnetDateStr = strVal.substring(6, strVal.length() - 2);
                strVal = dotnetDateStr;
            }

            if ("0000-00-00".equals(strVal)
                    || "0000-00-00T00:00:00".equalsIgnoreCase(strVal)
                    || "0001-01-01T00:00:00+08:00".equalsIgnoreCase(strVal)) {
                return null;
            }

            int index = strVal.lastIndexOf('|');
            if (index > 20) {
                String tzStr = strVal.substring(index + 1);
                TimeZone timeZone = TimeZone.getTimeZone(tzStr);
                if (!"GMT".equals(timeZone.getID())) {
                    String subStr = strVal.substring(0, index);
                    JSONScanner dateLexer = new JSONScanner(subStr);
                    try {
                        if (dateLexer.scanISO8601DateIfMatch(false)) {
                            Calendar calendar = dateLexer.getCalendar();

                            calendar.setTimeZone(timeZone);

                            if (clazz == Calendar.class) {
                                return (T) calendar;
                            }

                            return (T) calendar.getTime();
                        }
                    } finally {
                        dateLexer.close();
                    }
                }
            }

            // 2017-08-14 19:05:30.000|America/Los_Angeles
//            
            long longVal = Long.parseLong(strVal);
            return (T) new java.util.Date(longVal);
        }

        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

}
