/*
 * Copyright 1999-2017 Alibaba Group.
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
package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeUtils{
    private static final Pattern NUMBER_WITH_TRAILING_ZEROS_PATTERN = Pattern.compile("\\.0*$");

    public static boolean compatibleWithJavaBean = false;
    /** 根据field name的大小写输出输入数据 */
    public static boolean compatibleWithFieldName = false;
    private static boolean setAccessibleEnable = true;
    private static boolean oracleTimestampMethodInited = false;
    private static Method oracleTimestampMethod;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleDateMethod;
    private static boolean optionalClassInited = false;
    private static Class<?> optionalClass;
    private static boolean transientClassInited = false;
    private static Class<? extends Annotation> transientClass;

    private static Class<? extends Annotation> class_OneToMany = null;
    private static boolean class_OneToMany_error = false;
    private static Class<? extends Annotation> class_ManyToMany = null;
    private static boolean class_ManyToMany_error = false;

    private static Method method_HibernateIsInitialized = null;
    private static boolean method_HibernateIsInitialized_error = false;
    private static volatile Class kotlin_metadata;
    private static volatile boolean kotlin_metadata_error;
    private static volatile boolean kotlin_class_klass_error;
    private static volatile Constructor kotlin_kclass_constructor;
    private static volatile Method kotlin_kclass_getConstructors;
    private static volatile Method kotlin_kfunction_getParameters;
    private static volatile Method kotlin_kparameter_getName;
    private static volatile boolean kotlin_error;
    private static volatile Map<Class,String[]> kotlinIgnores;
    private static volatile boolean kotlinIgnores_error;
    private static ConcurrentMap<String,Class<?>> mappings = new ConcurrentHashMap<String,Class<?>>(256, 0.75f, 1);
    private static Class<?> pathClass;
    private static boolean pathClass_error = false;

    private static Class<? extends Annotation> class_JacksonCreator = null;
    private static boolean class_JacksonCreator_error = false;

    private static volatile Class class_Clob = null;
    private static volatile boolean class_Clob_error = false;

    private static volatile Class class_XmlAccessType = null;
    private static volatile Class class_XmlAccessorType = null;
    private static volatile boolean classXmlAccessorType_error = false;
    private static volatile Method method_XmlAccessorType_value = null;
    private static volatile Field field_XmlAccessType_FIELD = null;
    private static volatile Object field_XmlAccessType_FIELD_VALUE = null;

    static{
        try{
            TypeUtils.compatibleWithJavaBean = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHJAVABEAN));
            TypeUtils.compatibleWithFieldName = "true".equals(IOUtils.getStringProperty(IOUtils.FASTJSON_COMPATIBLEWITHFIELDNAME));
        } catch(Throwable e){
            // skip
        }
    }

    static{
        addBaseClassMappings();
    }


    public static boolean isXmlField(Class clazz) {
        if (class_XmlAccessorType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch(Throwable ex){
                classXmlAccessorType_error = true;
            }
        }

        if (class_XmlAccessorType == null) {
            return false;
        }

        Annotation annotation = TypeUtils.getAnnotation(clazz, class_XmlAccessorType);
        if (annotation == null) {
            return false;
        }

        if (method_XmlAccessorType_value == null && !classXmlAccessorType_error) {
            try {
                method_XmlAccessorType_value = class_XmlAccessorType.getMethod("value");
            } catch(Throwable ex){
                classXmlAccessorType_error = true;
            }
        }

        if (method_XmlAccessorType_value == null) {
            return false;
        }

        Object value = null;
        if (!classXmlAccessorType_error) {
            try {
                value = method_XmlAccessorType_value.invoke(annotation);
            } catch (Throwable ex) {
                classXmlAccessorType_error = true;
            }
        }
        if (value == null) {
            return false;
        }

        if (class_XmlAccessType == null && !classXmlAccessorType_error) {
            try {
                class_XmlAccessType = Class.forName("javax.xml.bind.annotation.XmlAccessType");
                field_XmlAccessType_FIELD = class_XmlAccessType.getField("FIELD");
                field_XmlAccessType_FIELD_VALUE = field_XmlAccessType_FIELD.get(null);
            } catch(Throwable ex){
                classXmlAccessorType_error = true;
            }
        }

        return value == field_XmlAccessType_FIELD_VALUE;
    }

    public static Annotation getXmlAccessorType(Class clazz) {
        if (class_XmlAccessorType == null && !classXmlAccessorType_error) {

            try{
                class_XmlAccessorType = Class.forName("javax.xml.bind.annotation.XmlAccessorType");
            } catch(Throwable ex){
                classXmlAccessorType_error = true;
            }
        }

        if (class_XmlAccessorType == null) {
            return null;
        }

        return  TypeUtils.getAnnotation(clazz, class_XmlAccessorType);
    }

//
//    public static boolean isXmlAccessType(Class clazz) {
//        if (class_XmlAccessType == null && !class_XmlAccessType_error) {
//
//            try{
//                class_XmlAccessType = Class.forName("javax.xml.bind.annotation.XmlAccessType");
//            } catch(Throwable ex){
//                class_XmlAccessType_error = true;
//            }
//        }
//
//        if (class_XmlAccessType == null) {
//            return false;
//        }
//
//        return  class_XmlAccessType.isAssignableFrom(clazz);
//    }

    public static boolean isClob(Class clazz) {
        if (class_Clob == null && !class_Clob_error) {

            try{
                class_Clob = Class.forName("java.sql.Clob");
            } catch(Throwable ex){
                class_Clob_error = true;
            }
        }

        if (class_Clob == null) {
            return false;
        }

        return  class_Clob.isAssignableFrom(clazz);
    }

    public static String castToString(Object value){
        if(value == null){
            return null;
        }
        return value.toString();
    }

    public static Byte castToByte(Object value){
        if(value == null){
            return null;
        }

        if(value instanceof BigDecimal){
            return byteValue((BigDecimal) value);
        }

        if(value instanceof Number){
            return ((Number) value).byteValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            return Byte.parseByte(strVal);
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Character){
            return (Character) value;
        }
        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0){
                return null;
            }
            if(strVal.length() != 1){
                throw new JSONException("can not cast to char, value : " + value);
            }
            return strVal.charAt(0);
        }
        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value){
        if(value == null){
            return null;
        }

        if(value instanceof BigDecimal){
            return shortValue((BigDecimal) value);
        }

        if(value instanceof Number){
            return ((Number) value).shortValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            return Short.parseShort(strVal);
        }

        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof BigDecimal){
            return (BigDecimal) value;
        }
        if(value instanceof BigInteger){
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if(strVal.length() == 0){
            return null;
        }
        if(value instanceof Map && ((Map) value).size() == 0){
            return null;
        }
        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof BigInteger){
            return (BigInteger) value;
        }
        if(value instanceof Float || value instanceof Double){
            return BigInteger.valueOf(((Number) value).longValue());
        }
        if(value instanceof BigDecimal){
            BigDecimal decimal = (BigDecimal) value;
            int scale = decimal.scale();
            if (scale > -1000 && scale < 1000) {
                return ((BigDecimal) value).toBigInteger();
            }
        }
        String strVal = value.toString();
        if(strVal.length() == 0 //
                || "null".equals(strVal) //
                || "NULL".equals(strVal)){
            return null;
        }
        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Number){
            return ((Number) value).floatValue();
        }
        if(value instanceof String){
            String strVal = value.toString();
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.indexOf(',') != -1){
                strVal = strVal.replaceAll(",", "");
            }
            return Float.parseFloat(strVal);
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Number){
            return ((Number) value).doubleValue();
        }
        if(value instanceof String){
            String strVal = value.toString();
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.indexOf(',') != -1){
                strVal = strVal.replaceAll(",", "");
            }
            return Double.parseDouble(strVal);
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Date castToDate(Object value){
        return castToDate(value, null);
    }

    public static Date castToDate(Object value, String format){
        if(value == null){
            return null;
        }

        if(value instanceof Date){ // 使用频率最高的，应优先处理
            return (Date) value;
        }

        if(value instanceof Calendar){
            return ((Calendar) value).getTime();
        }

        long longValue = -1;

        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
            return new Date(longValue);
        }

        if(value instanceof Number){
            longValue = ((Number) value).longValue();
            if ("unixtime".equals(format)) {
                longValue *= 1000;
            }
            return new Date(longValue);
        }

        if(value instanceof String){
            String strVal = (String) value;
            JSONScanner dateLexer = new JSONScanner(strVal);
            try{
                if(dateLexer.scanISO8601DateIfMatch(false)){
                    Calendar calendar = dateLexer.getCalendar();
                    return calendar.getTime();
                }
            } finally{
                dateLexer.close();
            }

            if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                strVal = strVal.substring(6, strVal.length() - 2);
            }

            if (strVal.indexOf('-') > 0 || strVal.indexOf('+') > 0) {
                if (format == null) {
                    if (strVal.length() == JSON.DEFFAULT_DATE_FORMAT.length()
                            || (strVal.length() == 22 && JSON.DEFFAULT_DATE_FORMAT.equals("yyyyMMddHHmmssSSSZ"))) {
                        format = JSON.DEFFAULT_DATE_FORMAT;
                    } else if (strVal.length() == 10) {
                        format = "yyyy-MM-dd";
                    } else if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    } else if (strVal.length() == 29
                            && strVal.charAt(26) == ':'
                            && strVal.charAt(28) == '0') {
                        format = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
                    } else if (strVal.length() == 23 && strVal.charAt(19) == ',') {
                        format = "yyyy-MM-dd HH:mm:ss,SSS";
                    } else {
                        format = "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
                dateFormat.setTimeZone(JSON.defaultTimeZone);
                try{
                    return dateFormat.parse(strVal);
                } catch(ParseException e){
                    throw new JSONException("can not cast to Date, value : " + strVal);
                }
            }
            if(strVal.length() == 0){
                return null;
            }
            longValue = Long.parseLong(strVal);
        }

        if (longValue == -1) {
            Class<?> clazz = value.getClass();
            if("oracle.sql.TIMESTAMP".equals(clazz.getName())){
                if(oracleTimestampMethod == null && !oracleTimestampMethodInited){
                    try{
                        oracleTimestampMethod = clazz.getMethod("toJdbc");
                    } catch(NoSuchMethodException e){
                        // skip
                    } finally{
                        oracleTimestampMethodInited = true;
                    }
                }
                Object result;
                try{
                    result = oracleTimestampMethod.invoke(value);
                } catch(Exception e){
                    throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e);
                }
                return (Date) result;
            }
            if("oracle.sql.DATE".equals(clazz.getName())){
                if(oracleDateMethod == null && !oracleDateMethodInited){
                    try{
                        oracleDateMethod = clazz.getMethod("toJdbc");
                    } catch(NoSuchMethodException e){
                        // skip
                    } finally{
                        oracleDateMethodInited = true;
                    }
                }
                Object result;
                try{
                    result = oracleDateMethod.invoke(value);
                } catch(Exception e){
                    throw new JSONException("can not cast oracle.sql.DATE to Date", e);
                }
                return (Date) result;
            }

            throw new JSONException("can not cast to Date, value : " + value);
        }

        return new Date(longValue);
    }

    public static java.sql.Date castToSqlDate(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof java.sql.Date){
            return (java.sql.Date) value;
        }
        if(value instanceof java.util.Date){
            return new java.sql.Date(((java.util.Date) value).getTime());
        }
        if(value instanceof Calendar){
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;
        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
        } else if(value instanceof Number){
            longValue = ((Number) value).longValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(isNumber(strVal)){
                longValue = Long.parseLong(strVal);
            } else{
                JSONScanner scanner = new JSONScanner(strVal);
                if(scanner.scanISO8601DateIfMatch(false)){
                    longValue = scanner.getCalendar().getTime().getTime();
                } else{
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }
            }
        }
        if(longValue <= 0){
            throw new JSONException("can not cast to Date, value : " + value); // TODO 忽略 1970-01-01 之前的时间处理？
        }
        return new java.sql.Date(longValue);
    }

    public static long longExtractValue(Number number) {
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).longValueExact();
        }

        return number.longValue();
    }

    public static java.sql.Time castToSqlTime(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof java.sql.Time){
            return (java.sql.Time) value;
        }
        if(value instanceof java.util.Date){
            return new java.sql.Time(((java.util.Date) value).getTime());
        }
        if(value instanceof Calendar){
            return new java.sql.Time(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;
        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
        } else if(value instanceof Number){
            longValue = ((Number) value).longValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)){
                return null;
            }
            if(isNumber(strVal)){
                longValue = Long.parseLong(strVal);
            } else{
                JSONScanner scanner = new JSONScanner(strVal);
                if(scanner.scanISO8601DateIfMatch(false)){
                    longValue = scanner.getCalendar().getTime().getTime();
                } else{
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }
            }
        }
        if(longValue <= 0){
            throw new JSONException("can not cast to Date, value : " + value); // TODO 忽略 1970-01-01 之前的时间处理？
        }
        return new java.sql.Time(longValue);
    }

    public static java.sql.Timestamp castToTimestamp(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Calendar){
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }
        if(value instanceof java.sql.Timestamp){
            return (java.sql.Timestamp) value;
        }
        if(value instanceof java.util.Date){
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }
        long longValue = 0;
        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
        } else if(value instanceof Number){
            longValue = ((Number) value).longValue();
        }
        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.endsWith(".000000000")){
                strVal = strVal.substring(0, strVal.length() - 10);
            } else if(strVal.endsWith(".000000")){
                strVal = strVal.substring(0, strVal.length() - 7);
            }

            if (strVal.length() == 29
                    && strVal.charAt(4) == '-'
                    && strVal.charAt(7) == '-'
                    && strVal.charAt(10) == ' '
                    && strVal.charAt(13) == ':'
                    && strVal.charAt(16) == ':'
                    && strVal.charAt(19) == '.') {
                int year = num(
                        strVal.charAt(0),
                        strVal.charAt(1),
                        strVal.charAt(2),
                        strVal.charAt(3));
                int month = num(
                        strVal.charAt(5),
                        strVal.charAt(6));
                int day = num(
                        strVal.charAt(8),
                        strVal.charAt(9));
                int hour = num(
                        strVal.charAt(11),
                        strVal.charAt(12));
                int minute = num(
                        strVal.charAt(14),
                        strVal.charAt(15));
                int second = num(
                        strVal.charAt(17),
                        strVal.charAt(18));
                int nanos = num(
                        strVal.charAt(20),
                        strVal.charAt(21),
                        strVal.charAt(22),
                        strVal.charAt(23),
                        strVal.charAt(24),
                        strVal.charAt(25),
                        strVal.charAt(26),
                        strVal.charAt(27),
                        strVal.charAt(28));
                return new java.sql.Timestamp(year - 1900, month - 1, day, hour, minute, second, nanos);
            }

            if(isNumber(strVal)){
                longValue = Long.parseLong(strVal);
            } else {
                JSONScanner scanner = new JSONScanner(strVal);
                if(scanner.scanISO8601DateIfMatch(false)){
                    longValue = scanner.getCalendar().getTime().getTime();
                } else{
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }
            }
        }
        if(longValue <= 0){
            throw new JSONException("can not cast to Timestamp, value : " + value);
        }
        return new java.sql.Timestamp(longValue);
    }

    static int num(char c0, char c1) {
        if (c0 >= '0'
                && c0 <= '9'
                && c1 >= '0'
                && c1 <= '9'
        ) {
            return (c0 - '0') * 10
                    + (c1 - '0');
        }

        return -1;
    }

    static int num(char c0, char c1, char c2, char c3) {
        if (c0 >= '0'
                && c0 <= '9'
                && c1 >= '0'
                && c1 <= '9'
                && c2 >= '0'
                && c2 <= '9'
                && c3 >= '0'
                && c3 <= '9'
        ) {
            return (c0 - '0') * 1000
                    + (c1 - '0') * 100
                    + (c2 - '0') * 10
                    + (c3 - '0');
        }

        return -1;
    }

    static int num(char c0, char c1, char c2, char c3, char c4, char c5, char c6, char c7, char c8) {
        if (c0 >= '0'
                && c0 <= '9'
                && c1 >= '0'
                && c1 <= '9'
                && c2 >= '0'
                && c2 <= '9'
                && c3 >= '0'
                && c3 <= '9'
                && c4 >= '0'
                && c4 <= '9'
                && c5 >= '0'
                && c5 <= '9'
                && c6 >= '0'
                && c6 <= '9'
                && c7 >= '0'
                && c7 <= '9'
                && c8 >= '0'
                && c8 <= '9'
        ) {
            return (c0 - '0') * 100000000
                    + (c1 - '0') * 10000000
                    + (c2 - '0') * 1000000
                    + (c3 - '0') * 100000
                    + (c4 - '0') * 10000
                    + (c5 - '0') * 1000
                    + (c6 - '0') * 100
                    + (c7 - '0') * 10
                    + (c8 - '0');
        }

        return -1;
    }

    public static boolean isNumber(String str){
        for(int i = 0; i < str.length(); ++i){
            char ch = str.charAt(i);
            if(ch == '+' || ch == '-'){
                if(i != 0){
                    return false;
                } 
            } else if(ch < '0' || ch > '9'){
                return false;
            }
        }
        return true;
    }

    public static Long castToLong(Object value){
        if(value == null){
            return null;
        }

        if(value instanceof BigDecimal){
            return longValue((BigDecimal) value);
        }

        if(value instanceof Number){
            return ((Number) value).longValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.indexOf(',') != -1){
                strVal = strVal.replaceAll(",", "");
            }
            try{
                return Long.parseLong(strVal);
            } catch(NumberFormatException ex){
                //
            }
            JSONScanner dateParser = new JSONScanner(strVal);
            Calendar calendar = null;
            if(dateParser.scanISO8601DateIfMatch(false)){
                calendar = dateParser.getCalendar();
            }
            dateParser.close();
            if(calendar != null){
                return calendar.getTimeInMillis();
            }
        }

        if(value instanceof Map){
            Map map = (Map) value;
            if(map.size() == 2
                    && map.containsKey("andIncrement")
                    && map.containsKey("andDecrement")){
                Iterator iter = map.values().iterator();
                iter.next();
                Object value2 = iter.next();
                return castToLong(value2);
            }
        }
        
        throw new JSONException("can not cast to long, value : " + value);
    }

    public static byte byteValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.byteValue();
        }

        return decimal.byteValueExact();
    }

    public static short shortValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.shortValue();
        }

        return decimal.shortValueExact();
    }

    public static int intValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.intValue();
        }

        return decimal.intValueExact();
    }

    public static long longValue(BigDecimal decimal) {
        if (decimal == null) {
            return 0;
        }

        int scale = decimal.scale();
        if (scale >= -100 && scale <= 100) {
            return decimal.longValue();
        }

        return decimal.longValueExact();
    }

    public static Integer castToInt(Object value){
        if(value == null){
            return null;
        }

        if(value instanceof Integer){
            return (Integer) value;
        }

        if(value instanceof BigDecimal){
            return intValue((BigDecimal) value);
        }

        if(value instanceof Number){
            return ((Number) value).intValue();
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.indexOf(',') != -1){
                strVal = strVal.replaceAll(",", "");
            }
            
            Matcher matcher = NUMBER_WITH_TRAILING_ZEROS_PATTERN.matcher(strVal);
            if(matcher.find()) {
                strVal = matcher.replaceAll("");
            }
            return Integer.parseInt(strVal);
        }

        if(value instanceof Boolean){
            return (Boolean) value ? 1 : 0;
        }
        if(value instanceof Map){
            Map map = (Map) value;
            if(map.size() == 2
                    && map.containsKey("andIncrement")
                    && map.containsKey("andDecrement")){
                Iterator iter = map.values().iterator();
                iter.next();
                Object value2 = iter.next();
                return castToInt(value2);
            }
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static byte[] castToBytes(Object value){
        if(value instanceof byte[]){
            return (byte[]) value;
        }
        if(value instanceof String){
            return IOUtils.decodeBase64((String) value);
        }
        throw new JSONException("can not cast to byte[], value : " + value);
    }

    public static Boolean castToBoolean(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Boolean){
            return (Boolean) value;
        }

        if(value instanceof BigDecimal){
            return intValue((BigDecimal) value) == 1;
        }

        if(value instanceof Number){
            return ((Number) value).intValue() == 1;
        }

        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if("true".equalsIgnoreCase(strVal) //
                    || "1".equals(strVal)){
                return Boolean.TRUE;
            }
            if("false".equalsIgnoreCase(strVal) //
                    || "0".equals(strVal)){
                return Boolean.FALSE;
            }
            if("Y".equalsIgnoreCase(strVal) //
                    || "T".equals(strVal)){
                return Boolean.TRUE;
            }
            if("F".equalsIgnoreCase(strVal) //
                    || "N".equals(strVal)){
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + value);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> clazz){
        return cast(obj, clazz, ParserConfig.getGlobalInstance());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T cast(Object obj, Class<T> clazz, ParserConfig config){
        if(obj == null){
            if(clazz == int.class){
                return (T) Integer.valueOf(0);
            } else if(clazz == long.class){
                return (T) Long.valueOf(0);
            } else if(clazz == short.class){
                return (T) Short.valueOf((short) 0);
            } else if(clazz == byte.class){
                return (T) Byte.valueOf((byte) 0);
            } else if(clazz == float.class){
                return (T) Float.valueOf(0);
            } else if(clazz == double.class){
                return (T) Double.valueOf(0);
            } else if(clazz == boolean.class){
                return (T) Boolean.FALSE;
            }
            return null;
        }

        if(clazz == null){
            throw new IllegalArgumentException("clazz is null");
        }

        if(clazz == obj.getClass()){
            return (T) obj;
        }

        if(obj instanceof Map){
            if(clazz == Map.class){
                return (T) obj;
            }

            Map map = (Map) obj;
            if(clazz == Object.class && !map.containsKey(JSON.DEFAULT_TYPE_KEY)){
                return (T) obj;
            }
            return castToJavaBean((Map<String,Object>) obj, clazz, config);
        }

        if(clazz.isArray()){
            if(obj instanceof Collection){
                Collection collection = (Collection) obj;
                int index = 0;
                Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                for(Object item : collection){
                    Object value = cast(item, clazz.getComponentType(), config);
                    Array.set(array, index, value);
                    index++;
                }
                return (T) array;
            }
            if(clazz == byte[].class){
                return (T) castToBytes(obj);
            }
        }

        if(clazz.isAssignableFrom(obj.getClass())){
            return (T) obj;
        }

        if(clazz == boolean.class || clazz == Boolean.class){
            return (T) castToBoolean(obj);
        }

        if(clazz == byte.class || clazz == Byte.class){
            return (T) castToByte(obj);
        }

        if(clazz == char.class || clazz == Character.class){
            return (T) castToChar(obj);
        }

        if(clazz == short.class || clazz == Short.class){
            return (T) castToShort(obj);
        }

        if(clazz == int.class || clazz == Integer.class){
            return (T) castToInt(obj);
        }

        if(clazz == long.class || clazz == Long.class){
            return (T) castToLong(obj);
        }

        if(clazz == float.class || clazz == Float.class){
            return (T) castToFloat(obj);
        }

        if(clazz == double.class || clazz == Double.class){
            return (T) castToDouble(obj);
        }

        if(clazz == String.class){
            return (T) castToString(obj);
        }

        if(clazz == BigDecimal.class){
            return (T) castToBigDecimal(obj);
        }

        if(clazz == BigInteger.class){
            return (T) castToBigInteger(obj);
        }

        if(clazz == Date.class){
            return (T) castToDate(obj);
        }

        if(clazz == java.sql.Date.class){
            return (T) castToSqlDate(obj);
        }

        if(clazz == java.sql.Time.class){
            return (T) castToSqlTime(obj);
        }

        if(clazz == java.sql.Timestamp.class){
            return (T) castToTimestamp(obj);
        }

        if(clazz.isEnum()){
            return castToEnum(obj, clazz, config);
        }

        if(Calendar.class.isAssignableFrom(clazz)){
            Date date = castToDate(obj);
            Calendar calendar;
            if(clazz == Calendar.class){
                calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            } else{
                try{
                    calendar = (Calendar) clazz.newInstance();
                } catch(Exception e){
                    throw new JSONException("can not cast to : " + clazz.getName(), e);
                }
            }
            calendar.setTime(date);
            return (T) calendar;
        }

        String className = clazz.getName();
        if(className.equals("javax.xml.datatype.XMLGregorianCalendar")){
            Date date = castToDate(obj);
            Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            calendar.setTime(date);
            return (T) CalendarCodec.instance.createXMLGregorianCalendar(calendar);
        }

        if(obj instanceof String){
            String strVal = (String) obj;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }

            if(clazz == java.util.Currency.class){
                return (T) java.util.Currency.getInstance(strVal);
            }

            if(clazz == java.util.Locale.class){
                return (T) toLocale(strVal);
            }

            if (className.startsWith("java.time.")) {
                String json = JSON.toJSONString(strVal);
                return JSON.parseObject(json, clazz);
            }
        }

        final ObjectDeserializer objectDeserializer = config.get(clazz);
        if (objectDeserializer != null) {
            String str = JSON.toJSONString(obj);
            return JSON.parseObject(str, clazz);
        }
        throw new JSONException("can not cast to : " + clazz.getName());
    }

    public static Locale toLocale(String strVal){
        String[] items = strVal.split("_");
        if(items.length == 1){
            return new Locale(items[0]);
        }
        if(items.length == 2){
            return new Locale(items[0], items[1]);
        }
        return new Locale(items[0], items[1], items[2]);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T castToEnum(Object obj, Class<T> clazz, ParserConfig mapping){
        try{
            if(obj instanceof String){
                String name = (String) obj;
                if(name.length() == 0){
                    return null;
                }

                if (mapping == null) {
                    mapping = ParserConfig.getGlobalInstance();
                }

                ObjectDeserializer deserializer = mapping.getDeserializer(clazz);
                if (deserializer instanceof EnumDeserializer) {
                    EnumDeserializer enumDeserializer = (EnumDeserializer) deserializer;
                    return (T) enumDeserializer.getEnumByHashCode(TypeUtils.fnv1a_64(name));
                }

                return (T) Enum.valueOf((Class<? extends Enum>) clazz, name);
            }

            if(obj instanceof BigDecimal){
                int ordinal = intValue((BigDecimal) obj);
                Object[] values = clazz.getEnumConstants();
                if(ordinal < values.length){
                    return (T) values[ordinal];
                }
            }

            if(obj instanceof Number){
                int ordinal = ((Number) obj).intValue();
                Object[] values = clazz.getEnumConstants();
                if(ordinal < values.length){
                    return (T) values[ordinal];
                }
            }
        } catch(Exception ex){
            throw new JSONException("can not cast to : " + clazz.getName(), ex);
        }
        throw new JSONException("can not cast to : " + clazz.getName());
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj, Type type, ParserConfig mapping){
        if(obj == null){
            return null;
        }
        if(type instanceof Class){
            return cast(obj, (Class<T>) type, mapping);
        }
        if(type instanceof ParameterizedType){
            return (T) cast(obj, (ParameterizedType) type, mapping);
        }
        if(obj instanceof String){
            String strVal = (String) obj;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
        }
        if(type instanceof TypeVariable){
            return (T) obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> T cast(Object obj, ParameterizedType type, ParserConfig mapping) {
        Type rawTye = type.getRawType();

        if(rawTye == List.class || rawTye == ArrayList.class){
            Type itemType = type.getActualTypeArguments()[0];
            if(obj instanceof List){
                List listObj = (List) obj;
                List arrayList = new ArrayList(listObj.size());

                for (int i = 0; i < listObj.size(); i++) {
                    Object item = listObj.get(i);

                    Object itemValue;
                    if (itemType instanceof Class) {
                        if (item != null && item.getClass() == JSONObject.class) {
                            itemValue = ((JSONObject) item).toJavaObject((Class<T>) itemType, mapping, 0);
                        } else {
                            itemValue = cast(item, (Class<T>) itemType, mapping);
                        }
                    } else {
                        itemValue = cast(item, itemType, mapping);
                    }

                    arrayList.add(itemValue);
                }
                return (T) arrayList;
            }
        }

        if(rawTye == Set.class || rawTye == HashSet.class //
                || rawTye == TreeSet.class //
                || rawTye == Collection.class //
                || rawTye == List.class //
                || rawTye == ArrayList.class){
            Type itemType = type.getActualTypeArguments()[0];
            if(obj instanceof Iterable){
                Collection collection;
                if(rawTye == Set.class || rawTye == HashSet.class){
                    collection = new HashSet();
                } else if(rawTye == TreeSet.class){
                    collection = new TreeSet();
                } else{
                    collection = new ArrayList();
                }
                for(Iterator it = ((Iterable) obj).iterator(); it.hasNext(); ){
                    Object item = it.next();

                    Object itemValue;
                    if (itemType instanceof Class) {
                        if (item != null && item.getClass() == JSONObject.class) {
                            itemValue = ((JSONObject) item).toJavaObject((Class<T>) itemType, mapping, 0);
                        } else {
                            itemValue = cast(item, (Class<T>) itemType, mapping);
                        }
                    } else {
                        itemValue = cast(item, itemType, mapping);
                    }

                    collection.add(itemValue);
                }
                return (T) collection;
            }
        }

        if(rawTye == Map.class || rawTye == HashMap.class){
            Type keyType = type.getActualTypeArguments()[0];
            Type valueType = type.getActualTypeArguments()[1];
            if(obj instanceof Map){
                Map map = new HashMap();
                for(Map.Entry entry : ((Map<?,?>) obj).entrySet()){
                    Object key = cast(entry.getKey(), keyType, mapping);
                    Object value = cast(entry.getValue(), valueType, mapping);
                    map.put(key, value);
                }
                return (T) map;
            }
        }
        if(obj instanceof String){
            String strVal = (String) obj;
            if(strVal.length() == 0){
                return null;
            }
        }

        Type[] actualTypeArguments = type.getActualTypeArguments();
        if (actualTypeArguments.length == 1) {
            Type argType = type.getActualTypeArguments()[0];
            if(argType instanceof WildcardType){
                return (T) cast(obj, rawTye, mapping);
            }
        }

        if (rawTye == Map.Entry.class && obj instanceof Map && ((Map) obj).size() == 1) {
            Map.Entry entry = (Map.Entry) ((Map) obj).entrySet().iterator().next();
            Object entryValue = entry.getValue();
            if (actualTypeArguments.length == 2 && entryValue instanceof Map) {
                Type valueType = actualTypeArguments[1];
                entry.setValue(
                        cast(entryValue, valueType, mapping)
                );
            }
            return (T) entry;
        }

        if (rawTye instanceof Class) {
            if (mapping == null) {
                mapping = ParserConfig.global;
            }
            ObjectDeserializer deserializer = mapping.getDeserializer(rawTye);
            if (deserializer != null) {
                String str = JSON.toJSONString(obj);
                DefaultJSONParser parser = new DefaultJSONParser(str, mapping);
                return (T) deserializer.deserialze(parser, type, null);
            }
        }

        throw new JSONException("can not cast to : " + type);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T castToJavaBean(Map<String,Object> map, Class<T> clazz, ParserConfig config){
        try{
            if(clazz == StackTraceElement.class){
                String declaringClass = (String) map.get("className");
                String methodName = (String) map.get("methodName");
                String fileName = (String) map.get("fileName");
                int lineNumber;
                {
                    Number value = (Number) map.get("lineNumber");
                    if(value == null) {
                        lineNumber = 0;
                    } else if (value instanceof BigDecimal) {
                        lineNumber = ((BigDecimal) value).intValueExact();
                    } else{
                        lineNumber = value.intValue();
                    }
                }
                return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
            }

            {
                Object iClassObject = map.get(JSON.DEFAULT_TYPE_KEY);
                if(iClassObject instanceof String){
                    String className = (String) iClassObject;
                    Class<?> loadClazz;
                    if(config == null){
                        config = ParserConfig.global;
                    }
                    loadClazz = config.checkAutoType(className, null);
                    if(loadClazz == null){
                        throw new ClassNotFoundException(className + " not found");
                    }
                    if(!loadClazz.equals(clazz)){
                        return (T) castToJavaBean(map, loadClazz, config);
                    }
                }
            }

            if(clazz.isInterface()){
                JSONObject object;
                if(map instanceof JSONObject){
                    object = (JSONObject) map;
                } else{
                    object = new JSONObject(map);
                }
                if(config == null){
                    config = ParserConfig.getGlobalInstance();
                }
                ObjectDeserializer deserializer = config.get(clazz);
                if(deserializer != null){
                    String json = JSON.toJSONString(object);
                    return JSON.parseObject(json, clazz);
                }
                return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{clazz}, object);
            }

            if(clazz == Locale.class){
                Object arg0 = map.get("language");
                Object arg1 = map.get("country");
                if(arg0 instanceof String){
                    String language = (String) arg0;
                    if(arg1 instanceof String){
                        String country = (String) arg1;
                        return (T) new Locale(language, country);
                    } else if(arg1 == null){
                        return (T) new Locale(language);
                    }
                }
            }

            if (clazz == String.class && map instanceof JSONObject) {
                return (T) map.toString();
            }

            if (clazz == JSON.class && map instanceof JSONObject) {
                return (T) map;
            }

            if (clazz == LinkedHashMap.class && map instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) map;
                Map innerMap = jsonObject.getInnerMap();
                if (innerMap instanceof LinkedHashMap) {
                    return (T) innerMap;
                } else {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    linkedHashMap.putAll(innerMap);
                }
            }

            if (clazz.isInstance(map)) {
                return (T) map;
            }

            if (clazz == JSONObject.class) {
                return (T) new JSONObject(map);
            }

            if (config == null) {
                config = ParserConfig.getGlobalInstance();
            }

            JavaBeanDeserializer javaBeanDeser = null;
            ObjectDeserializer deserializer = config.getDeserializer(clazz);
            if (deserializer instanceof JavaBeanDeserializer) {
                javaBeanDeser = (JavaBeanDeserializer) deserializer;
            }

            if(javaBeanDeser == null){
                throw new JSONException("can not get javaBeanDeserializer. " + clazz.getName());
            }
            return (T) javaBeanDeser.createInstance(map, config);
        } catch(Exception e){
            throw new JSONException(e.getMessage(), e);
        }
    }

    private static void addBaseClassMappings(){
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        Class<?>[] classes = new Class[]{
                Object.class,
                java.lang.Cloneable.class,
                loadClass("java.lang.AutoCloseable"),
                java.lang.Exception.class,
                java.lang.RuntimeException.class,
                java.lang.IllegalAccessError.class,
                java.lang.IllegalAccessException.class,
                java.lang.IllegalArgumentException.class,
                java.lang.IllegalMonitorStateException.class,
                java.lang.IllegalStateException.class,
                java.lang.IllegalThreadStateException.class,
                java.lang.IndexOutOfBoundsException.class,
                java.lang.InstantiationError.class,
                java.lang.InstantiationException.class,
                java.lang.InternalError.class,
                java.lang.InterruptedException.class,
                java.lang.LinkageError.class,
                java.lang.NegativeArraySizeException.class,
                java.lang.NoClassDefFoundError.class,
                java.lang.NoSuchFieldError.class,
                java.lang.NoSuchFieldException.class,
                java.lang.NoSuchMethodError.class,
                java.lang.NoSuchMethodException.class,
                java.lang.NullPointerException.class,
                java.lang.NumberFormatException.class,
                java.lang.OutOfMemoryError.class,
                java.lang.SecurityException.class,
                java.lang.StackOverflowError.class,
                java.lang.StringIndexOutOfBoundsException.class,
                java.lang.TypeNotPresentException.class,
                java.lang.VerifyError.class,
                java.lang.StackTraceElement.class,
                java.util.HashMap.class,
                java.util.Hashtable.class,
                java.util.TreeMap.class,
                java.util.IdentityHashMap.class,
                java.util.WeakHashMap.class,
                java.util.LinkedHashMap.class,
                java.util.HashSet.class,
                java.util.LinkedHashSet.class,
                java.util.TreeSet.class,
                java.util.ArrayList.class,
                java.util.concurrent.TimeUnit.class,
                java.util.concurrent.ConcurrentHashMap.class,
                java.util.concurrent.atomic.AtomicInteger.class,
                java.util.concurrent.atomic.AtomicLong.class,
                java.util.Collections.EMPTY_MAP.getClass(),
                java.lang.Boolean.class,
                java.lang.Character.class,
                java.lang.Byte.class,
                java.lang.Short.class,
                java.lang.Integer.class,
                java.lang.Long.class,
                java.lang.Float.class,
                java.lang.Double.class,
                java.lang.Number.class,
                java.lang.String.class,
                java.math.BigDecimal.class,
                java.math.BigInteger.class,
                java.util.BitSet.class,
                java.util.Calendar.class,
                java.util.Date.class,
                java.util.Locale.class,
                java.util.UUID.class,
                java.sql.Time.class,
                java.sql.Date.class,
                java.sql.Timestamp.class,
                java.text.SimpleDateFormat.class,
                com.alibaba.fastjson.JSONObject.class,
                com.alibaba.fastjson.JSONPObject.class,
                com.alibaba.fastjson.JSONArray.class,
        };
        for(Class clazz : classes){
            if(clazz == null){
                continue;
            }
            mappings.put(clazz.getName(), clazz);
        }
    }

    public static void clearClassMapping(){
        mappings.clear();
        addBaseClassMappings();
    }

    public static void addMapping(String className, Class<?> clazz) {
        mappings.put(className, clazz);
    }

    public static Class<?> loadClass(String className){
        return loadClass(className, null);
    }

    public static boolean isPath(Class<?> clazz){
        if(pathClass == null && !pathClass_error){
            try{
                pathClass = Class.forName("java.nio.file.Path");
            } catch(Throwable ex){
                pathClass_error = true;
            }
        }
        if(pathClass != null){
            return pathClass.isAssignableFrom(clazz);
        }
        return false;
    }

    public static Class<?> getClassFromMapping(String className){
        return mappings.get(className);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        return loadClass(className, classLoader, false);
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader, boolean cache) {
        if(className == null || className.length() == 0 || className.length() > 128){
            return null;
        }

        Class<?> clazz = mappings.get(className);
        if(clazz != null){
            return clazz;
        }

        if(className.charAt(0) == '['){
            Class<?> componentType = loadClass(className.substring(1), classLoader);
            return Array.newInstance(componentType, 0).getClass();
        }

        if(className.startsWith("L") && className.endsWith(";")){
            String newClassName = className.substring(1, className.length() - 1);
            return loadClass(newClassName, classLoader);
        }

        try{
            if(classLoader != null){
                clazz = classLoader.loadClass(className);
                if (cache) {
                    mappings.put(className, clazz);
                }
                return clazz;
            }
        } catch(Throwable e){
            e.printStackTrace();
            // skip
        }
        try{
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if(contextClassLoader != null && contextClassLoader != classLoader){
                clazz = contextClassLoader.loadClass(className);
                if (cache) {
                    mappings.put(className, clazz);
                }
                return clazz;
            }
        } catch(Throwable e){
            // skip
        }
        try{
            clazz = Class.forName(className);
            if (cache) {
                mappings.put(className, clazz);
            }
            return clazz;
        } catch(Throwable e){
            // skip
        }
        return clazz;
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> beanType //
            , Map<String,String> aliasMap //
            , PropertyNamingStrategy propertyNamingStrategy){
        return buildBeanInfo(beanType, aliasMap, propertyNamingStrategy, false);
    }

    public static SerializeBeanInfo buildBeanInfo(Class<?> beanType //
            , Map<String,String> aliasMap //
            , PropertyNamingStrategy propertyNamingStrategy //
            , boolean fieldBased //
    ){
        JSONType jsonType = TypeUtils.getAnnotation(beanType,JSONType.class);
        String[] orders = null;
        final int features;
        String typeName = null, typeKey = null;
        if(jsonType != null){
            orders = jsonType.orders();

            typeName = jsonType.typeName();
            if(typeName.length() == 0){
                typeName = null;
            }

            PropertyNamingStrategy jsonTypeNaming = jsonType.naming();
            if (jsonTypeNaming != PropertyNamingStrategy.CamelCase) {
                propertyNamingStrategy = jsonTypeNaming;
            }

            features = SerializerFeature.of(jsonType.serialzeFeatures());
            for(Class<?> supperClass = beanType.getSuperclass()
                ; supperClass != null && supperClass != Object.class
                    ; supperClass = supperClass.getSuperclass()){
                JSONType superJsonType = TypeUtils.getAnnotation(supperClass,JSONType.class);
                if(superJsonType == null){
                    break;
                }
                typeKey = superJsonType.typeKey();
                if(typeKey.length() != 0){
                    break;
                }
            }

            for(Class<?> interfaceClass : beanType.getInterfaces()){
                JSONType superJsonType = TypeUtils.getAnnotation(interfaceClass,JSONType.class);
                if(superJsonType != null){
                    typeKey = superJsonType.typeKey();
                    if(typeKey.length() != 0){
                        break;
                    }
                }
            }

            if(typeKey != null && typeKey.length() == 0){
                typeKey = null;
            }
        } else{
            features = 0;
        }
        // fieldName,field ，先生成fieldName的快照，减少之后的findField的轮询
        Map<String,Field> fieldCacheMap = new HashMap<String,Field>();
        ParserConfig.parserAllFieldToCache(beanType, fieldCacheMap);
        List<FieldInfo> fieldInfoList = fieldBased
                ? computeGettersWithFieldBase(beanType, aliasMap, false, propertyNamingStrategy) //
                : computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, false, propertyNamingStrategy);
        FieldInfo[] fields = new FieldInfo[fieldInfoList.size()];
        fieldInfoList.toArray(fields);
        FieldInfo[] sortedFields;
        List<FieldInfo> sortedFieldList;
        if(orders != null && orders.length != 0){
            sortedFieldList = fieldBased
                    ? computeGettersWithFieldBase(beanType, aliasMap, true, propertyNamingStrategy) //
                    : computeGetters(beanType, jsonType, aliasMap, fieldCacheMap, true, propertyNamingStrategy);
        } else{
            sortedFieldList = new ArrayList<FieldInfo>(fieldInfoList);
            Collections.sort(sortedFieldList);
        }
        sortedFields = new FieldInfo[sortedFieldList.size()];
        sortedFieldList.toArray(sortedFields);
        if(Arrays.equals(sortedFields, fields)){
            sortedFields = fields;
        }
        return new SerializeBeanInfo(beanType, jsonType, typeName, typeKey, features, fields, sortedFields);
    }

    public static List<FieldInfo> computeGettersWithFieldBase(
            Class<?> clazz, //
            Map<String,String> aliasMap, //
            boolean sorted, //
            PropertyNamingStrategy propertyNamingStrategy){
        Map<String,FieldInfo> fieldInfoMap = new LinkedHashMap<String,FieldInfo>();
        for(Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()){
            Field[] fields = currentClass.getDeclaredFields();
            computeFields(currentClass, aliasMap, propertyNamingStrategy, fieldInfoMap, fields);
        }
        return getFieldInfos(clazz, sorted, fieldInfoMap);
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, Map<String,String> aliasMap){
        return computeGetters(clazz, aliasMap, true);
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, Map<String,String> aliasMap, boolean sorted){
        JSONType jsonType = TypeUtils.getAnnotation(clazz,JSONType.class);
        Map<String,Field> fieldCacheMap = new HashMap<String,Field>();
        ParserConfig.parserAllFieldToCache(clazz, fieldCacheMap);
        return computeGetters(clazz, jsonType, aliasMap, fieldCacheMap, sorted, PropertyNamingStrategy.CamelCase);
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, //
                                                 JSONType jsonType, //
                                                 Map<String,String> aliasMap, //
                                                 Map<String,Field> fieldCacheMap, //
                                                 boolean sorted, //
                                                 PropertyNamingStrategy propertyNamingStrategy //
    ){
        Map<String,FieldInfo> fieldInfoMap = new LinkedHashMap<String,FieldInfo>();
        boolean kotlin = TypeUtils.isKotlin(clazz);
        // for kotlin
        Constructor[] constructors = null;
        Annotation[][] paramAnnotationArrays = null;
        String[] paramNames = null;
        short[] paramNameMapping = null;
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            String methodName = method.getName();
            int ordinal = 0, serialzeFeatures = 0, parserFeatures = 0;
            String label = null;
            if(Modifier.isStatic(method.getModifiers())){
                continue;
            }
            if(method.getReturnType().equals(Void.TYPE)){
                continue;
            }
            if(method.getParameterTypes().length != 0){
                continue;
            }
            if(method.getReturnType() == ClassLoader.class){
                continue;
            }

            if(methodName.equals("getMetaClass")
                    && method.getReturnType().getName().equals("groovy.lang.MetaClass")){
                continue;
            }
            if(methodName.equals("getSuppressed")
                    && method.getDeclaringClass() == Throwable.class){
                continue;
            }

            if(kotlin && isKotlinIgnore(clazz, methodName)){
                continue;
            }
            /**
             *  如果在属性或者方法上存在JSONField注解，并且定制了name属性，不以类上的propertyNamingStrategy设置为准，以此字段的JSONField的name定制为准。
             */
            Boolean fieldAnnotationAndNameExists = false;
            JSONField annotation = TypeUtils.getAnnotation(method, JSONField.class);
            if(annotation == null){
                annotation = getSuperMethodAnnotation(clazz, method);
            }
            if(annotation == null && kotlin){
                if(constructors == null){
                    constructors = clazz.getDeclaredConstructors();
                    Constructor creatorConstructor = TypeUtils.getKoltinConstructor(constructors);
                    if(creatorConstructor != null){
                        paramAnnotationArrays = TypeUtils.getParameterAnnotations(creatorConstructor);
                        paramNames = TypeUtils.getKoltinConstructorParameters(clazz);
                        if(paramNames != null){
                            String[] paramNames_sorted = new String[paramNames.length];
                            System.arraycopy(paramNames, 0, paramNames_sorted, 0, paramNames.length);

                            Arrays.sort(paramNames_sorted);
                            paramNameMapping = new short[paramNames.length];
                            for(short p = 0; p < paramNames.length; p++){
                                int index = Arrays.binarySearch(paramNames_sorted, paramNames[p]);
                                paramNameMapping[index] = p;
                            }
                            paramNames = paramNames_sorted;
                        }
                    }
                }
                if(paramNames != null && paramNameMapping != null && methodName.startsWith("get")){
                    String propertyName = decapitalize(methodName.substring(3));
                    int p = Arrays.binarySearch(paramNames, propertyName);
                    if (p < 0) {
                        for (int i = 0; i < paramNames.length; i++) {
                            if (propertyName.equalsIgnoreCase(paramNames[i])) {
                                p = i;
                                break;
                            }
                        }
                    }
                    if(p >= 0){
                        short index = paramNameMapping[p];
                        Annotation[] paramAnnotations = paramAnnotationArrays[index];
                        if(paramAnnotations != null){
                            for(Annotation paramAnnotation : paramAnnotations){
                                if(paramAnnotation instanceof JSONField){
                                    annotation = (JSONField) paramAnnotation;
                                    break;
                                }
                            }
                        }
                        if(annotation == null){
                            Field field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                            if(field != null){
                                annotation = TypeUtils.getAnnotation(field, JSONField.class);
                            }
                        }
                    }
                }
            }
            if(annotation != null){
                if(!annotation.serialize()){
                    continue;
                }
                ordinal = annotation.ordinal();
                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                parserFeatures = Feature.of(annotation.parseFeatures());
                if(annotation.name().length() != 0){
                    String propertyName = annotation.name();
                    if(aliasMap != null){
                        propertyName = aliasMap.get(propertyName);
                        if(propertyName == null){
                            continue;
                        }
                    }
                    FieldInfo fieldInfo = new FieldInfo(propertyName, method, null, clazz, null, ordinal,
                            serialzeFeatures, parserFeatures, annotation, null, label);
                    fieldInfoMap.put(propertyName, fieldInfo);
                    continue;
                }
                if(annotation.label().length() != 0){
                    label = annotation.label();
                }
            }
            if(methodName.startsWith("get")){
                if(methodName.length() < 4){
                    continue;
                }
                if(methodName.equals("getClass")){
                    continue;
                }
                if(methodName.equals("getDeclaringClass") && clazz.isEnum()){
                    continue;
                }
                char c3 = methodName.charAt(3);
                String propertyName;
                Field field = null;
                if(Character.isUpperCase(c3) //
                        || c3 > 512 // for unicode method name
                        ){
                    if(compatibleWithJavaBean){
                        propertyName = decapitalize(methodName.substring(3));
                    } else{
                        propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    }
                    propertyName = getPropertyNameByCompatibleFieldName(fieldCacheMap, methodName, propertyName, 3);
                } else if(c3 == '_'){
                    propertyName = methodName.substring(4);
                    field = fieldCacheMap.get(propertyName);
                    if (field == null) {
                        String temp = propertyName;
                        propertyName = methodName.substring(3);
                        field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                        if (field == null) {
                            propertyName = temp; //减少修改代码带来的影响
                        }
                    }
                } else if(c3 == 'f'){
                    propertyName = methodName.substring(3);
                } else if(methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))){
                    propertyName = decapitalize(methodName.substring(3));
                } else{
                    propertyName = methodName.substring(3);
                    field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                    if (field == null) {
                        continue;
                    }
                }
                boolean ignore = isJSONTypeIgnore(clazz, propertyName);
                if(ignore){
                    continue;
                }

                if (field == null) {
                    // 假如bean的field很多的情况一下，轮询时将大大降低效率
                    field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                }
                
                if(field == null && propertyName.length() > 1){
                    char ch = propertyName.charAt(1);
                    if(ch >= 'A' && ch <= 'Z'){
                        String javaBeanCompatiblePropertyName = decapitalize(methodName.substring(3));
                        field = ParserConfig.getFieldFromCache(javaBeanCompatiblePropertyName, fieldCacheMap);
                    }
                }
                JSONField fieldAnnotation = null;
                if(field != null){
                    fieldAnnotation = TypeUtils.getAnnotation(field, JSONField.class);
                    if(fieldAnnotation != null){
                        if(!fieldAnnotation.serialize()){
                            continue;
                        }
                        ordinal = fieldAnnotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                        parserFeatures = Feature.of(fieldAnnotation.parseFeatures());
                        if(fieldAnnotation.name().length() != 0){
                            fieldAnnotationAndNameExists = true;
                            propertyName = fieldAnnotation.name();
                            if(aliasMap != null){
                                propertyName = aliasMap.get(propertyName);
                                if(propertyName == null){
                                    continue;
                                }
                            }
                        }
                        if(fieldAnnotation.label().length() != 0){
                            label = fieldAnnotation.label();
                        }
                    }
                }
                if(aliasMap != null){
                    propertyName = aliasMap.get(propertyName);
                    if(propertyName == null){
                        continue;
                    }
                }
                if(propertyNamingStrategy != null && !fieldAnnotationAndNameExists){
                    propertyName = propertyNamingStrategy.translate(propertyName);
                }
                FieldInfo fieldInfo = new FieldInfo(propertyName, method, field, clazz, null, ordinal, serialzeFeatures, parserFeatures,
                        annotation, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
            if(methodName.startsWith("is")){
                if(methodName.length() < 3){
                    continue;
                }
                if(method.getReturnType() != Boolean.TYPE
                        && method.getReturnType() != Boolean.class){
                    continue;
                }
                char c2 = methodName.charAt(2);
                String propertyName;
                Field field = null;
                if(Character.isUpperCase(c2)){
                    if(compatibleWithJavaBean){
                        propertyName = decapitalize(methodName.substring(2));
                    } else{
                        propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
                    }
                    propertyName = getPropertyNameByCompatibleFieldName(fieldCacheMap, methodName, propertyName, 2);
                } else if(c2 == '_'){
                    propertyName = methodName.substring(3);
                    field = fieldCacheMap.get(propertyName);
                    if (field == null) {
                        String temp = propertyName;
                        propertyName = methodName.substring(2);
                        field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                        if (field == null) {
                            propertyName = temp;
                        }
                    }
                } else if(c2 == 'f'){
                    propertyName = methodName.substring(2);
                } else{
                    propertyName = methodName.substring(2);
                    field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                    if (field == null) {
                        continue;
                    }
                }
                boolean ignore = isJSONTypeIgnore(clazz, propertyName);
                if(ignore){
                    continue;
                }
                
                if(field == null) {
                    field = ParserConfig.getFieldFromCache(propertyName, fieldCacheMap);
                }
                
                if(field == null){
                    field = ParserConfig.getFieldFromCache(methodName, fieldCacheMap);
                }
                JSONField fieldAnnotation = null;
                if(field != null){
                    fieldAnnotation = TypeUtils.getAnnotation(field, JSONField.class);
                    if(fieldAnnotation != null){
                        if(!fieldAnnotation.serialize()){
                            continue;
                        }
                        ordinal = fieldAnnotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                        parserFeatures = Feature.of(fieldAnnotation.parseFeatures());
                        if(fieldAnnotation.name().length() != 0){
                            propertyName = fieldAnnotation.name();
                            if(aliasMap != null){
                                propertyName = aliasMap.get(propertyName);
                                if(propertyName == null){
                                    continue;
                                }
                            }
                        }
                        if(fieldAnnotation.label().length() != 0){
                            label = fieldAnnotation.label();
                        }
                    }
                }
                if(aliasMap != null){
                    propertyName = aliasMap.get(propertyName);
                    if(propertyName == null){
                        continue;
                    }
                }
                if(propertyNamingStrategy != null){
                    propertyName = propertyNamingStrategy.translate(propertyName);
                }
                //优先选择get
                if(fieldInfoMap.containsKey(propertyName)){
                    continue;
                }
                FieldInfo fieldInfo = new FieldInfo(propertyName, method, field, clazz, null, ordinal, serialzeFeatures, parserFeatures,
                        annotation, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }
        Field[] fields = clazz.getFields();
        computeFields(clazz, aliasMap, propertyNamingStrategy, fieldInfoMap, fields);
        return getFieldInfos(clazz, sorted, fieldInfoMap);
    }

    private static List<FieldInfo> getFieldInfos(Class<?> clazz, boolean sorted, Map<String,FieldInfo> fieldInfoMap){
        List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
        String[] orders = null;
        JSONType annotation = TypeUtils.getAnnotation(clazz,JSONType.class);
        if(annotation != null){
            orders = annotation.orders();
        }
        if(orders != null && orders.length > 0){
            LinkedHashMap<String,FieldInfo> map = new LinkedHashMap<String,FieldInfo>(fieldInfoList.size());
            for(FieldInfo field : fieldInfoMap.values()){
                map.put(field.name, field);
            }
            int i = 0;
            for(String item : orders){
                FieldInfo field = map.get(item);
                if(field != null){
                    fieldInfoList.add(field);
                    map.remove(item);
                }
            }
            for(FieldInfo field : map.values()){
                fieldInfoList.add(field);
            }
        } else{
            for(FieldInfo fieldInfo : fieldInfoMap.values()){
                fieldInfoList.add(fieldInfo);
            }
            if(sorted){
                Collections.sort(fieldInfoList);
            }
        }
        return fieldInfoList;
    }

    private static void computeFields(
            Class<?> clazz, //
            Map<String,String> aliasMap, //
            PropertyNamingStrategy propertyNamingStrategy, //
            Map<String,FieldInfo> fieldInfoMap, //
            Field[] fields){
        for(Field field : fields){
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }
            JSONField fieldAnnotation = TypeUtils.getAnnotation(field, JSONField.class);
            int ordinal = 0, serialzeFeatures = 0, parserFeatures = 0;
            String propertyName = field.getName();
            String label = null;
            if(fieldAnnotation != null){
                if(!fieldAnnotation.serialize()){
                    continue;
                }
                ordinal = fieldAnnotation.ordinal();
                serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                parserFeatures = Feature.of(fieldAnnotation.parseFeatures());
                if(fieldAnnotation.name().length() != 0){
                    propertyName = fieldAnnotation.name();
                }
                if(fieldAnnotation.label().length() != 0){
                    label = fieldAnnotation.label();
                }
            }
            if(aliasMap != null){
                propertyName = aliasMap.get(propertyName);
                if(propertyName == null){
                    continue;
                }
            }
            if(propertyNamingStrategy != null){
                propertyName = propertyNamingStrategy.translate(propertyName);
            }
            if(!fieldInfoMap.containsKey(propertyName)){
                FieldInfo fieldInfo = new FieldInfo(propertyName, null, field, clazz, null, ordinal, serialzeFeatures, parserFeatures,
                        null, fieldAnnotation, label);
                fieldInfoMap.put(propertyName, fieldInfo);
            }
        }
    }

    private static String getPropertyNameByCompatibleFieldName(Map<String,Field> fieldCacheMap, String methodName,
                                                               String propertyName, int fromIdx){
        if(compatibleWithFieldName){
            if(!fieldCacheMap.containsKey(propertyName)){
                String tempPropertyName = methodName.substring(fromIdx);
                return fieldCacheMap.containsKey(tempPropertyName) ? tempPropertyName : propertyName;
            }
        }
        return propertyName;
    }

    public static JSONField getSuperMethodAnnotation(final Class<?> clazz, final Method method){
        Class<?>[] interfaces = clazz.getInterfaces();
        if(interfaces.length > 0){
            Class<?>[] types = method.getParameterTypes();
            for(Class<?> interfaceClass : interfaces){
                for(Method interfaceMethod : interfaceClass.getMethods()){
                    Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                    if(interfaceTypes.length != types.length){
                        continue;
                    }
                    if(!interfaceMethod.getName().equals(method.getName())){
                        continue;
                    }
                    boolean match = true;
                    for(int i = 0; i < types.length; ++i){
                        if(!interfaceTypes[i].equals(types[i])){
                            match = false;
                            break;
                        }
                    }
                    if(!match){
                        continue;
                    }
                    JSONField annotation = TypeUtils.getAnnotation(interfaceMethod, JSONField.class);
                    if(annotation != null){
                        return annotation;
                    }
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if(superClass == null){
            return null;
        }
        if(Modifier.isAbstract(superClass.getModifiers())){
            Class<?>[] types = method.getParameterTypes();
            for(Method interfaceMethod : superClass.getMethods()){
                Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                if(interfaceTypes.length != types.length){
                    continue;
                }
                if(!interfaceMethod.getName().equals(method.getName())){
                    continue;
                }
                boolean match = true;
                for(int i = 0; i < types.length; ++i){
                    if(!interfaceTypes[i].equals(types[i])){
                        match = false;
                        break;
                    }
                }
                if(!match){
                    continue;
                }
                JSONField annotation = TypeUtils.getAnnotation(interfaceMethod, JSONField.class);
                if(annotation != null){
                    return annotation;
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName){
        JSONType jsonType = TypeUtils.getAnnotation(clazz,JSONType.class);
        if(jsonType != null){
            // 1、新增 includes 支持，如果 JSONType 同时设置了includes 和 ignores 属性，则以includes为准。
            // 2、个人认为对于大小写敏感的Java和JS而言，使用 equals() 比 equalsIgnoreCase() 更好，改动的唯一风险就是向后兼容性的问题
            // 不过，相信开发者应该都是严格按照大小写敏感的方式进行属性设置的
            String[] fields = jsonType.includes();
            if(fields.length > 0){
                for(int i = 0; i < fields.length; i++){
                    if(propertyName.equals(fields[i])){
                        return false;
                    }
                }
                return true;
            } else{
                fields = jsonType.ignores();
                for(int i = 0; i < fields.length; i++){
                    if(propertyName.equals(fields[i])){
                        return true;
                    }
                }
            }
        }
        if(clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null){
            return isJSONTypeIgnore(clazz.getSuperclass(), propertyName);
        }
        return false;
    }

    public static boolean isGenericParamType(Type type){
        if(type instanceof ParameterizedType){
            return true;
        }
        if(type instanceof Class) {
            Type superType = ((Class<?>) type).getGenericSuperclass();
            return superType != Object.class && isGenericParamType(superType);
        }
        return false;
    }

    public static Type getGenericParamType(Type type){
        if(type instanceof ParameterizedType){
            return type;
        }
        if(type instanceof Class){
            return getGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        return type;
    }

    public static Type unwrapOptional(Type type){
        if(!optionalClassInited){
            try{
                optionalClass = Class.forName("java.util.Optional");
            } catch(Exception e){
                // skip
            } finally{
                optionalClassInited = true;
            }
        }
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(parameterizedType.getRawType() == optionalClass){
                return parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    public static Class<?> getClass(Type type){
        if(type.getClass() == Class.class){
            return (Class<?>) type;
        }

        if(type instanceof ParameterizedType){
            return getClass(((ParameterizedType) type).getRawType());
        }

        if(type instanceof TypeVariable){
            Type boundType = ((TypeVariable<?>) type).getBounds()[0];
            if (boundType instanceof Class) {
                return (Class) boundType;
            }
            return getClass(boundType);
        }

        if(type instanceof WildcardType){
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if (upperBounds.length == 1) {
                return getClass(upperBounds[0]);
            }
        }

        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName, Field[] declaredFields){
        for(Field field : declaredFields){
            String itemName = field.getName();
            if(fieldName.equals(itemName)){
                return field;
            }

            char c0, c1;
            if (fieldName.length() > 2
                    && (c0 = fieldName.charAt(0)) >= 'a' && c0 <= 'z'
                    && (c1 = fieldName.charAt(1)) >= 'A' && c1 <= 'Z'
                    && fieldName.equalsIgnoreCase(itemName)) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null && superClass != Object.class){
            return getField(superClass, fieldName, superClass.getDeclaredFields());
        }
        return null;
    }

    /**
     * @deprecated
     */
    public static int getSerializeFeatures(Class<?> clazz){
        JSONType annotation = TypeUtils.getAnnotation(clazz,JSONType.class);
        if(annotation == null){
            return 0;
        }
        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz){
        JSONType annotation = TypeUtils.getAnnotation(clazz,JSONType.class);
        if(annotation == null){
            return 0;
        }
        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name){
        if(name == null || name.length() == 0){
            return name;
        }
        if(name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    static void setAccessible(AccessibleObject obj){
        if(!setAccessibleEnable){
            return;
        }
        if(obj.isAccessible()){
            return;
        }
        try{
            obj.setAccessible(true);
        } catch(AccessControlException error){
            setAccessibleEnable = false;
        }
    }

    public static Type getCollectionItemType(Type fieldType) {
        if (fieldType instanceof ParameterizedType) {
            return getCollectionItemType((ParameterizedType) fieldType);
        }
        if (fieldType instanceof Class<?>) {
            return getCollectionItemType((Class<?>) fieldType);
        }
        return Object.class;
    }

    private static Type getCollectionItemType(Class<?> clazz) {
        return clazz.getName().startsWith("java.")
                ? Object.class
                : getCollectionItemType(getCollectionSuperType(clazz));
    }

    private static Type getCollectionItemType(ParameterizedType parameterizedType) {
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (rawType == Collection.class) {
            return getWildcardTypeUpperBounds(actualTypeArguments[0]);
        }
        Class<?> rawClass = (Class<?>) rawType;
        Map<TypeVariable, Type> actualTypeMap = createActualTypeMap(rawClass.getTypeParameters(), actualTypeArguments);
        Type superType = getCollectionSuperType(rawClass);
        if (superType instanceof ParameterizedType) {
            Class<?> superClass = getRawClass(superType);
            Type[] superClassTypeParameters = ((ParameterizedType) superType).getActualTypeArguments();
            return superClassTypeParameters.length > 0
                    ? getCollectionItemType(makeParameterizedType(superClass, superClassTypeParameters, actualTypeMap))
                    : getCollectionItemType(superClass);
        }
        return getCollectionItemType((Class<?>) superType);
    }

    private static Type getCollectionSuperType(Class<?> clazz) {
        Type assignable = null;
        for (Type type : clazz.getGenericInterfaces()) {
            Class<?> rawClass = getRawClass(type);
            if (rawClass == Collection.class) {
                return type;
            }
            if (Collection.class.isAssignableFrom(rawClass)) {
                assignable = type;
            }
        }
        return assignable == null ? clazz.getGenericSuperclass() : assignable;
    }

    private static Map<TypeVariable, Type> createActualTypeMap(TypeVariable[] typeParameters, Type[] actualTypeArguments) {
        int length = typeParameters.length;
        Map<TypeVariable, Type> actualTypeMap = new HashMap<TypeVariable, Type>(length);
        for (int i = 0; i < length; i++) {
            actualTypeMap.put(typeParameters[i], actualTypeArguments[i]);
        }
        return actualTypeMap;
    }

    private static ParameterizedType makeParameterizedType(Class<?> rawClass, Type[] typeParameters, Map<TypeVariable, Type> actualTypeMap) {
        int length = typeParameters.length;
        Type[] actualTypeArguments = new Type[length];
        for (int i = 0; i < length; i++) {
            actualTypeArguments[i] = getActualType(typeParameters[i], actualTypeMap);
        }
        return new ParameterizedTypeImpl(actualTypeArguments, null, rawClass);
    }

    private static Type getActualType(Type typeParameter, Map<TypeVariable, Type> actualTypeMap) {
        if (typeParameter instanceof TypeVariable) {
            return actualTypeMap.get(typeParameter);
        } else if (typeParameter instanceof ParameterizedType) {
            return makeParameterizedType(getRawClass(typeParameter), ((ParameterizedType) typeParameter).getActualTypeArguments(), actualTypeMap);
        } else if (typeParameter instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(getActualType(((GenericArrayType) typeParameter).getGenericComponentType(), actualTypeMap));
        }
        return typeParameter;
    }

    private static Type getWildcardTypeUpperBounds(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            return upperBounds.length > 0 ? upperBounds[0] : Object.class;
        }
        return type;
    }

    public static Class<?> getCollectionItemClass(Type fieldType){
        if(fieldType instanceof ParameterizedType){
            Class<?> itemClass;
            Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
            if(actualTypeArgument instanceof WildcardType){
                WildcardType wildcardType = (WildcardType) actualTypeArgument;
                Type[] upperBounds = wildcardType.getUpperBounds();
                if(upperBounds.length == 1){
                    actualTypeArgument = upperBounds[0];
                }
            }
            if(actualTypeArgument instanceof Class){
                itemClass = (Class<?>) actualTypeArgument;
                if(!Modifier.isPublic(itemClass.getModifiers())){
                    throw new JSONException("can not create ASMParser");
                }
            } else{
                throw new JSONException("can not create ASMParser");
            }
            return itemClass;
        }
        return Object.class;
    }

    public static Type checkPrimitiveArray(GenericArrayType genericArrayType) {
        Type clz = genericArrayType;
        Type genericComponentType  = genericArrayType.getGenericComponentType();

        String prefix = "[";
        while (genericComponentType instanceof GenericArrayType) {
            genericComponentType = ((GenericArrayType) genericComponentType)
                    .getGenericComponentType();
            prefix += prefix;
        }

        if (genericComponentType instanceof Class<?>) {
            Class<?> ck = (Class<?>) genericComponentType;
            if (ck.isPrimitive()) {
                try {
                    if (ck == boolean.class) {
                        clz = Class.forName(prefix + "Z");
                    } else if (ck == char.class) {
                        clz = Class.forName(prefix + "C");
                    } else if (ck == byte.class) {
                        clz = Class.forName(prefix + "B");
                    } else if (ck == short.class) {
                        clz = Class.forName(prefix + "S");
                    } else if (ck == int.class) {
                        clz = Class.forName(prefix + "I");
                    } else if (ck == long.class) {
                        clz = Class.forName(prefix + "J");
                    } else if (ck == float.class) {
                        clz = Class.forName(prefix + "F");
                    } else if (ck == double.class) {
                        clz = Class.forName(prefix + "D");
                    }
                } catch (ClassNotFoundException e) {
                }
            }
        }

        return clz;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Collection createCollection(Type type) {
        Class<?> rawClass = getRawClass(type);
        Collection list;
        if(rawClass == AbstractCollection.class //
                || rawClass == Collection.class) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(HashSet.class)) {
            list = new HashSet();
        } else if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            list = new LinkedHashSet();
        } else if (rawClass.isAssignableFrom(TreeSet.class)) {
            list = new TreeSet();
        } else if(rawClass.isAssignableFrom(ArrayList.class)) {
            list = new ArrayList();
        } else if (rawClass.isAssignableFrom(EnumSet.class)) {
            Type itemType;
            if(type instanceof ParameterizedType){
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                itemType = Object.class;
            }
            list = EnumSet.noneOf((Class<Enum>) itemType);
        } else if (rawClass.isAssignableFrom(Queue.class) || rawClass.isAssignableFrom(Deque.class)){
            list = new LinkedList();
        } else {
            try {
                list = (Collection) rawClass.newInstance();
            } catch(Exception e) {
                throw new JSONException("create instance error, class " + rawClass.getName());
            }
        }
        return list;
    }

    public static Class<?> getRawClass(Type type) {
        if(type instanceof Class<?>){
            return (Class<?>) type;
        } else if(type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType)type).getRawType());
        } else if(type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length == 1) {
                return getRawClass(upperBounds[0]);
            } else {
                throw new JSONException("TODO");
            }
        } else{
            throw new JSONException("TODO");
        }
    }

    public static boolean isProxy(Class<?> clazz){
        for(Class<?> item : clazz.getInterfaces()){
            String interfaceName = item.getName();
            if(interfaceName.equals("net.sf.cglib.proxy.Factory") //
                    || interfaceName.equals("org.springframework.cglib.proxy.Factory")){
                return true;
            }
            if(interfaceName.equals("javassist.util.proxy.ProxyObject") //
                    || interfaceName.equals("org.apache.ibatis.javassist.util.proxy.ProxyObject")
                    ){
                return true;
            }
            if (interfaceName.equals("org.hibernate.proxy.HibernateProxy")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTransient(Method method){
        if(method == null){
            return false;
        }
        if(!transientClassInited){
            try{
                transientClass = (Class<? extends Annotation>) Class.forName("java.beans.Transient");
            } catch(Exception e){
                // skip
            } finally{
                transientClassInited = true;
            }
        }
        if(transientClass != null){
            Annotation annotation = TypeUtils.getAnnotation(method, transientClass);
            return annotation != null;
        }
        return false;
    }

    public static boolean isAnnotationPresentOneToMany(Method method) {
        if (method == null) {
            return false;
        }

        if (class_OneToMany == null && !class_OneToMany_error) {
            try {
                class_OneToMany = (Class<? extends Annotation>) Class.forName("javax.persistence.OneToMany");
            } catch (Throwable e) {
                // skip
                class_OneToMany_error = true;
            }
        }
        return class_OneToMany != null && method.isAnnotationPresent(class_OneToMany);

    }

    public static boolean isAnnotationPresentManyToMany(Method method) {
        if (method == null) {
            return false;
        }

        if (class_ManyToMany == null && !class_ManyToMany_error) {
            try {
                class_ManyToMany = (Class<? extends Annotation>) Class.forName("javax.persistence.ManyToMany");
            } catch (Throwable e) {
                // skip
                class_ManyToMany_error = true;
            }
        }
        return class_ManyToMany != null && (method.isAnnotationPresent(class_OneToMany) || method.isAnnotationPresent(class_ManyToMany));

    }

    public static boolean isHibernateInitialized(Object object){
        if(object == null){
            return false;
        }
        if(method_HibernateIsInitialized == null && !method_HibernateIsInitialized_error){
            try{
                Class<?> class_Hibernate = Class.forName("org.hibernate.Hibernate");
                method_HibernateIsInitialized = class_Hibernate.getMethod("isInitialized", Object.class);
            } catch(Throwable e){
                // skip
                method_HibernateIsInitialized_error = true;
            }
        }
        if(method_HibernateIsInitialized != null){
            try{
                Boolean initialized = (Boolean) method_HibernateIsInitialized.invoke(null, object);
                return initialized.booleanValue();
            } catch(Throwable e){
                // skip
            }
        }
        return true;
    }

    public static double parseDouble(String str) {
        final int len = str.length();
        if (len > 10) {
            return Double.parseDouble(str);
        }

        boolean negative = false;

        long longValue = 0;
        int scale = 0;
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch == '-' && i == 0) {
                negative = true;
                continue;
            }

            if (ch == '.') {
                if (scale != 0) {
                    return Double.parseDouble(str);
                }
                scale = len - i - 1;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                int digit = ch - '0';
                longValue = longValue * 10 + digit;
            } else {
                return Double.parseDouble(str);
            }
        }

        if (negative) {
            longValue = -longValue;
        }

        switch (scale) {
            case 0:
                return (double) longValue;
            case 1:
                return ((double) longValue) / 10;
            case 2:
                return ((double) longValue) / 100;
            case 3:
                return ((double) longValue) / 1000;
            case 4:
                return ((double) longValue) / 10000;
            case 5:
                return ((double) longValue) / 100000;
            case 6:
                return ((double) longValue) / 1000000;
            case 7:
                return ((double) longValue) / 10000000;
            case 8:
                return ((double) longValue) / 100000000;
            case 9:
                return ((double) longValue) / 1000000000;
        }

        return Double.parseDouble(str);
    }

    public static float parseFloat(String str) {
        final int len = str.length();
        if (len >= 10) {
            return Float.parseFloat(str);
        }

        boolean negative = false;

        long longValue = 0;
        int scale = 0;
        for (int i = 0; i < len; ++i) {
            char ch = str.charAt(i);
            if (ch == '-' && i == 0) {
                negative = true;
                continue;
            }

            if (ch == '.') {
                if (scale != 0) {
                    return Float.parseFloat(str);
                }
                scale = len - i - 1;
                continue;
            }

            if (ch >= '0' && ch <= '9') {
                int digit = ch - '0';
                longValue = longValue * 10 + digit;
            } else {
                return Float.parseFloat(str);
            }
        }

        if (negative) {
            longValue = -longValue;
        }

        switch (scale) {
            case 0:
                return (float) longValue;
            case 1:
                return ((float) longValue) / 10;
            case 2:
                return ((float) longValue) / 100;
            case 3:
                return ((float) longValue) / 1000;
            case 4:
                return ((float) longValue) / 10000;
            case 5:
                return ((float) longValue) / 100000;
            case 6:
                return ((float) longValue) / 1000000;
            case 7:
                return ((float) longValue) / 10000000;
            case 8:
                return ((float) longValue) / 100000000;
            case 9:
                return ((float) longValue) / 1000000000;
        }

        return Float.parseFloat(str);
    }

    public static long fnv1a_64_lower(String key){
        long hashCode = 0xcbf29ce484222325L;
        for(int i = 0; i < key.length(); ++i){
            char ch = key.charAt(i);
            if(ch == '_' || ch == '-'){
                continue;
            }
            if(ch >= 'A' && ch <= 'Z'){
                ch = (char) (ch + 32);
            }
            hashCode ^= ch;
            hashCode *= 0x100000001b3L;
        }
        return hashCode;
    }

    public static long fnv1a_64(String key){
        long hashCode = 0xcbf29ce484222325L;
        for(int i = 0; i < key.length(); ++i){
            char ch = key.charAt(i);
            hashCode ^= ch;
            hashCode *= 0x100000001b3L;
        }
        return hashCode;
    }

    public static boolean isKotlin(Class clazz) {
        if (kotlin_metadata == null && !kotlin_metadata_error) {
            try {
                kotlin_metadata = Class.forName("kotlin.Metadata");
            } catch (Throwable e) {
                kotlin_metadata_error = true;
            }
        }
        return kotlin_metadata != null && clazz.isAnnotationPresent(kotlin_metadata);
    }

    public static Constructor getKoltinConstructor(Constructor[] constructors){
        return getKoltinConstructor(constructors, null);
    }

    public static Constructor getKoltinConstructor(Constructor[] constructors, String[] paramNames){
        Constructor creatorConstructor = null;
        for(Constructor<?> constructor : constructors){
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (paramNames != null && parameterTypes.length != paramNames.length) {
                continue;
            }

            if(parameterTypes.length > 0 && parameterTypes[parameterTypes.length - 1].getName().equals("kotlin.jvm.internal.DefaultConstructorMarker")){
                continue;
            }
            if(creatorConstructor != null && creatorConstructor.getParameterTypes().length >= parameterTypes.length){
                continue;
            }
            creatorConstructor = constructor;
        }
        return creatorConstructor;
    }

    public static String[] getKoltinConstructorParameters(Class clazz){
        if(kotlin_kclass_constructor == null && !kotlin_class_klass_error){
            try{
                Class class_kotlin_kclass = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlin_kclass_constructor = class_kotlin_kclass.getConstructor(Class.class);
            } catch(Throwable e){
                kotlin_class_klass_error = true;
            }
        }
        if (kotlin_kclass_constructor == null){
            return null;
        }

        if (kotlin_kclass_getConstructors == null && !kotlin_class_klass_error) {
            try{
                Class class_kotlin_kclass = Class.forName("kotlin.reflect.jvm.internal.KClassImpl");
                kotlin_kclass_getConstructors = class_kotlin_kclass.getMethod("getConstructors");
            } catch(Throwable e){
                kotlin_class_klass_error = true;
            }
        }

        if (kotlin_kfunction_getParameters == null && !kotlin_class_klass_error) {
            try{
                Class class_kotlin_kfunction = Class.forName("kotlin.reflect.KFunction");
                kotlin_kfunction_getParameters = class_kotlin_kfunction.getMethod("getParameters");
            } catch(Throwable e){
                kotlin_class_klass_error = true;
            }
        }

        if (kotlin_kparameter_getName == null && !kotlin_class_klass_error) {
            try{
                Class class_kotlinn_kparameter = Class.forName("kotlin.reflect.KParameter");
                kotlin_kparameter_getName = class_kotlinn_kparameter.getMethod("getName");
            } catch(Throwable e){
                kotlin_class_klass_error = true;
            }
        }

        if (kotlin_error){
            return null;
        }

        try{
            Object constructor = null;
            Object kclassImpl = kotlin_kclass_constructor.newInstance(clazz);
            Iterable it = (Iterable) kotlin_kclass_getConstructors.invoke(kclassImpl);
            for(Iterator iterator = it.iterator(); iterator.hasNext(); iterator.hasNext()){
                Object item = iterator.next();
                List parameters = (List) kotlin_kfunction_getParameters.invoke(item);
                if (constructor != null && parameters.size() == 0) {
                    continue;
                }
                constructor = item;
            }

            if (constructor == null) {
                return null;
            }

            List parameters = (List) kotlin_kfunction_getParameters.invoke(constructor);
            String[] names = new String[parameters.size()];
            for(int i = 0; i < parameters.size(); i++){
                Object param = parameters.get(i);
                names[i] = (String) kotlin_kparameter_getName.invoke(param);
            }
            return names;
        } catch(Throwable e){
            e.printStackTrace();
            kotlin_error = true;
        }
        return null;
    }

    private static boolean isKotlinIgnore(Class clazz, String methodName) {
        if (kotlinIgnores == null && !kotlinIgnores_error) {
            try {
                Map<Class, String[]> map = new HashMap<Class, String[]>();
                Class charRangeClass = Class.forName("kotlin.ranges.CharRange");
                map.put(charRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class intRangeClass = Class.forName("kotlin.ranges.IntRange");
                map.put(intRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class longRangeClass = Class.forName("kotlin.ranges.LongRange");
                map.put(longRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class floatRangeClass = Class.forName("kotlin.ranges.ClosedFloatRange");
                map.put(floatRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                Class doubleRangeClass = Class.forName("kotlin.ranges.ClosedDoubleRange");
                map.put(doubleRangeClass, new String[]{"getEndInclusive", "isEmpty"});
                kotlinIgnores = map;
            } catch (Throwable error) {
                kotlinIgnores_error = true;
            }
        }
        if (kotlinIgnores == null) {
            return false;
        }
        String[] ignores = kotlinIgnores.get(clazz);
        return ignores != null && Arrays.binarySearch(ignores, methodName) >= 0;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> targetClass, Class<A> annotationClass){
        A targetAnnotation = targetClass.getAnnotation(annotationClass);

        Class<?> mixInClass = null;
        Type type = JSON.getMixInAnnotations(targetClass);
        if (type instanceof Class<?>) {
            mixInClass = (Class<?>) type;
        }

        if(mixInClass != null) {
            A mixInAnnotation = mixInClass.getAnnotation(annotationClass);
            if(mixInAnnotation == null && mixInClass.getAnnotations().length > 0){
                for(Annotation annotation : mixInClass.getAnnotations()){
                    mixInAnnotation = annotation.annotationType().getAnnotation(annotationClass);
                    if(mixInAnnotation != null){
                        break;
                    }
                }
            }
            if (mixInAnnotation != null) {
                return mixInAnnotation;
            }
        }

        if(targetAnnotation == null && targetClass.getAnnotations().length > 0){
            for(Annotation annotation : targetClass.getAnnotations()){
                targetAnnotation = annotation.annotationType().getAnnotation(annotationClass);
                if(targetAnnotation != null){
                    break;
                }
            }
        }
        return targetAnnotation;
    }

    public static <A extends Annotation> A getAnnotation(Field field, Class<A> annotationClass){
        A targetAnnotation = field.getAnnotation(annotationClass);

        Class<?> clazz = field.getDeclaringClass();
        A mixInAnnotation;
        Class<?> mixInClass = null;
        Type type = JSON.getMixInAnnotations(clazz);
        if (type instanceof Class<?>) {
            mixInClass = (Class<?>) type;
        }

        if (mixInClass != null) {
            Field mixInField = null;
            String fieldName = field.getName();
            // 递归从MixIn类的父类中查找注解（如果有父类的话）
            for (Class<?> currClass = mixInClass; currClass != null && currClass != Object.class;
                 currClass = currClass.getSuperclass()) {
                try {
                    mixInField = currClass.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    // skip
                }
            }
            if (mixInField == null) {
                return targetAnnotation;
            }
            mixInAnnotation = mixInField.getAnnotation(annotationClass);
            if (mixInAnnotation != null) {
                return mixInAnnotation;
            }
        }
        return targetAnnotation;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass){
        A targetAnnotation = method.getAnnotation(annotationClass);

        Class<?> clazz = method.getDeclaringClass();
        A mixInAnnotation;
        Class<?> mixInClass = null;
        Type type = JSON.getMixInAnnotations(clazz);
        if (type instanceof Class<?>) {
            mixInClass = (Class<?>) type;
        }

        if (mixInClass != null) {
            Method mixInMethod = null;
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 递归从MixIn类的父类中查找注解（如果有父类的话）
            for (Class<?> currClass = mixInClass; currClass != null && currClass != Object.class;
                 currClass = currClass.getSuperclass()) {
                try {
                    mixInMethod = currClass.getDeclaredMethod(methodName, parameterTypes);
                    break;
                } catch (NoSuchMethodException e) {
                    // skip
                }
            }
            if (mixInMethod == null) {
                return targetAnnotation;
            }
            mixInAnnotation = mixInMethod.getAnnotation(annotationClass);
            if (mixInAnnotation != null) {
                return mixInAnnotation;
            }
        }
        return targetAnnotation;
    }

    public static Annotation[][] getParameterAnnotations(Method method){
        Annotation[][] targetAnnotations = method.getParameterAnnotations();

        Class<?> clazz = method.getDeclaringClass();
        Annotation[][] mixInAnnotations;
        Class<?> mixInClass = null;
        Type type = JSON.getMixInAnnotations(clazz);
        if (type instanceof Class<?>) {
            mixInClass = (Class<?>) type;
        }

        if (mixInClass != null) {
            Method mixInMethod = null;
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 递归从MixIn类的父类中查找注解（如果有父类的话）
            for (Class<?> currClass = mixInClass; currClass != null && currClass != Object.class;
                 currClass = currClass.getSuperclass()) {
                try {
                    mixInMethod = currClass.getDeclaredMethod(methodName, parameterTypes);
                    break;
                } catch (NoSuchMethodException e) {
                    continue;
                }
            }
            if (mixInMethod == null) {
                return targetAnnotations;
            }
            mixInAnnotations = mixInMethod.getParameterAnnotations();
            if (mixInAnnotations != null) {
                return mixInAnnotations;
            }
        }
        return targetAnnotations;
    }

    public static Annotation[][] getParameterAnnotations(Constructor constructor){
        Annotation[][] targetAnnotations = constructor.getParameterAnnotations();

        Class<?> clazz = constructor.getDeclaringClass();
        Annotation[][] mixInAnnotations;
        Class<?> mixInClass = null;
        Type type = JSON.getMixInAnnotations(clazz);
        if (type instanceof Class<?>) {
            mixInClass = (Class<?>) type;
        }

        if (mixInClass != null) {
            Constructor mixInConstructor = null;
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            // 构建参数列表，因为内部类的构造函数需要传入外部类的引用
            List<Class<?>> enclosingClasses = new ArrayList<Class<?>>(2);
            for (Class<?> enclosingClass = mixInClass.getEnclosingClass(); enclosingClass != null; enclosingClass = enclosingClass.getEnclosingClass()) {
                enclosingClasses.add(enclosingClass);
            }
            int level = enclosingClasses.size();
            // 递归从MixIn类的父类中查找注解（如果有父类的话）
            for (Class<?> currClass = mixInClass; currClass != null && currClass != Object.class;
                 currClass = currClass.getSuperclass()) {
                try {
                    if (level != 0) {
                        Class<?>[] outerClassAndParameterTypes = new Class[level + parameterTypes.length];
                        System.arraycopy(parameterTypes, 0, outerClassAndParameterTypes, level, parameterTypes.length);
                        for (int i = level; i > 0 ; i--) {
                            outerClassAndParameterTypes[i - 1] = enclosingClasses.get(i - 1);
                        }
                        mixInConstructor = mixInClass.getDeclaredConstructor(outerClassAndParameterTypes);
                    } else {
                        mixInConstructor = mixInClass.getDeclaredConstructor(parameterTypes);
                    }
                    break;
                } catch (NoSuchMethodException e) {
                    level--;
                }
            }
            if (mixInConstructor == null) {
                return targetAnnotations;
            }
            mixInAnnotations = mixInConstructor.getParameterAnnotations();
            if (mixInAnnotations != null) {
                return mixInAnnotations;
            }
        }
        return targetAnnotations;
    }

    public static boolean isJacksonCreator(Method method) {
        if (method == null) {
            return false;
        }

        if (class_JacksonCreator == null && !class_JacksonCreator_error) {
            try {
                class_JacksonCreator = (Class<? extends Annotation>) Class.forName("com.fasterxml.jackson.annotation.JsonCreator");
            } catch (Throwable e) {
                // skip
                class_JacksonCreator_error = true;
            }
        }
        return class_JacksonCreator != null && method.isAnnotationPresent(class_JacksonCreator);
    }

    public static LocalDateTime castToLocalDateTime(Object value, String format) {
        if (value == null) {
            return null;
        }

        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(value.toString(), df);
    }
}
