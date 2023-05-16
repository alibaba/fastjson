package com.alibaba.json.bvt.issue_3300;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

/**
 * Test of Deserializer customized for special enum
 *
 * @author zhaohuihua
 * @version 20211219
 */
public class Issue3372 extends TestCase {

    protected void setUp() {
        // Register a customized Deserializer for UserState
        ParserConfig.getGlobalInstance().putDeserializer(UserState.class, new CodeEnumDeserializer());
    }

    public void testConvert() {
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum("NORMAL", UserState.class));
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum("1", UserState.class));
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum(1, UserState.class));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum("LOCKED", UserState.class));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum("2", UserState.class));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum(2, UserState.class));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum("UNACTIVATED", UserState.class));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum("3", UserState.class));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum(3, UserState.class));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum("LOGOFF", UserState.class));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum("9", UserState.class));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum(9, UserState.class));
    }

    public void testException() {
        try {
            TypeUtils.castToEnum(0, UserState.class, null);
        } catch (Exception e) {
            assertEquals(JSONException.class, e.getClass());
        }
        try {
            TypeUtils.castToEnum("AAA", UserState.class, null);
        } catch (Exception e) {
            assertEquals(JSONException.class, e.getClass());
        }
    }

    /** Enum interface with customized code. **/
    protected interface CodeEnum {
        int code();
    }

    /** User state enum class **/
    protected enum UserState implements CodeEnum {

        /** 1.Normal **/
        NORMAL(1),
        /** 2.Locked **/
        LOCKED(2),
        /** 3.Unactivated **/
        UNACTIVATED(3),
        /** 9.Logoff **/
        LOGOFF(9);

        private final int code;

        UserState(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }
    }

    /** Returns a special Deserializer class for a customized code. **/
    protected static class CodeEnumDeserializer implements ObjectDeserializer {

        @SuppressWarnings("unchecked")
        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            JSONLexer lexer = parser.lexer;
            if (lexer.token() == JSONToken.NULL) {
                lexer.nextToken(JSONToken.COMMA);
                return null;
            }

            Class<?> targetClass = TypeUtils.getRawClass(type);
            if (!CodeEnum.class.isAssignableFrom(targetClass)) {
                throw new JSONException("CodeEnumDeserializer do not support " + targetClass.getName());
            }
            if (!Enum.class.isAssignableFrom(targetClass)) {
                throw new JSONException("CodeEnumDeserializer do not support " + targetClass.getName());
            }
            Enum<?>[] ordinalEnums = (Enum<?>[]) targetClass.getEnumConstants();
            if (lexer.token() == JSONToken.LITERAL_INT) {
                BigDecimal number = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) convertFromNumber(number, ordinalEnums, targetClass);
            } else if (lexer.token() == JSONToken.LITERAL_FLOAT) {
                BigDecimal number = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) convertFromNumber(number, ordinalEnums, targetClass);
            } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                String string = lexer.stringVal();
                if (isDigitString(string)) {
                    BigDecimal number = new BigDecimal(string);
                    return (T) convertFromNumber(number, ordinalEnums, targetClass);
                } else {
                    return (T) convertFromString(string, ordinalEnums, targetClass);
                }
            }

            Object value = parser.parse();
            String msg = "can not cast " + value + " to : " + targetClass.getName();
            throw new JSONException(msg);
        }

        private Enum<?> convertFromNumber(BigDecimal number, Enum<?>[] enums, Class<?> type) {
            // integer part
            BigInteger integer = number.toBigInteger();
            boolean isIntegral = number.compareTo(new BigDecimal(integer)) == 0;
            if (!isIntegral) {
                // Is not an integer
                String msg = "can not cast " + number.toPlainString() + " to : " + type.getName();
                throw new JSONException(msg);
            }
            BigInteger minInteger = BigInteger.valueOf(Integer.MIN_VALUE);
            BigInteger maxInteger = BigInteger.valueOf(Integer.MAX_VALUE);
            if (integer.compareTo(minInteger) < 0 || integer.compareTo(maxInteger) > 0) {
                // Out of integer range
                throw new JSONException("can not cast " + integer + "to : " + type.getName());
            }
            // search by code
            int targetValue = number.intValue();
            for (Enum<?> item : enums) {
                if (((CodeEnum) item).code() == targetValue) {
                    return item;
                }
            }
            throw new JSONException("can not cast " + integer + "to : " + type.getName());
        }

        private Enum<?> convertFromString(String string, Enum<?>[] enums, Class<?> type) {
            if (string.length() == 0) {
                return null;
            }
            // search by name
            for (Enum<?> item : enums) {
                if (item.name().equals(string)) {
                    return item;
                }
            }
            throw new JSONException("can not cast " + string + "to : " + type.getName());
        }

        private boolean isDigitString(String string) {
            if (string == null || string.length() == 0) {
                return false;
            }
            for (int i = 0, z = string.length(); i < z; i++) {
                char c = string.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            return true;
        }

        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }
}
