package com.alibaba.fastjson.deserializer;

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
 * 针对特殊枚举定制的反序列化处理类测试
 *
 * @author zhaohuihua
 * @version 20200728
 */
public class EnumCustomizedDeserializerTest extends TestCase {

    public static void main(String[] args) {
        EnumCustomizedDeserializerTest test = new EnumCustomizedDeserializerTest();
        test.setUp();
        test.testConvert();
        test.testException();
    }

    protected void setUp() {
        // 给UserState注册定制的反序列化处理类
        ParserConfig.getGlobalInstance().putDeserializer(UserState.class, new UserStateDeserializer());
    }

    public void testConvert() {
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum("NORMAL", UserState.class, null));
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum("1", UserState.class, null));
        assertEquals(UserState.NORMAL, TypeUtils.castToEnum(1, UserState.class, null));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum("LOCKED", UserState.class, null));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum("2", UserState.class, null));
        assertEquals(UserState.LOCKED, TypeUtils.castToEnum(2, UserState.class, null));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum("UNACTIVATED", UserState.class, null));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum("3", UserState.class, null));
        assertEquals(UserState.UNACTIVATED, TypeUtils.castToEnum(3, UserState.class, null));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum("LOGOFF", UserState.class, null));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum("9", UserState.class, null));
        assertEquals(UserState.LOGOFF, TypeUtils.castToEnum(9, UserState.class, null));
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

    /** 用户状态 **/
    protected static enum UserState {

        /** 1.正常 **/
        NORMAL(1),
        /** 2.锁定 **/
        LOCKED(2),
        /** 3.待激活 **/
        UNACTIVATED(3),
        /** 9.已注销 **/
        LOGOFF(9);

        private int code;

        UserState(int code) {
            this.code = code;
        }

        public int code() {
            return this.code;
        }
    }

    /** 用户状态专用反序列化处理类(数字根据code转换) **/
    protected static class UserStateDeserializer implements ObjectDeserializer {

        public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            JSONLexer lexer = parser.lexer;
            if (lexer.token() == JSONToken.NULL) {
                lexer.nextToken(JSONToken.COMMA);
                return null;
            }

            Class<T> targetClass = (Class<T>) TypeUtils.getRawClass(type);
            if (targetClass != UserState.class) {
                throw new JSONException("UserStateDeserializer do not support " + targetClass.getName());
            }
            if (lexer.token() == JSONToken.LITERAL_INT) {
                BigDecimal number = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) convertFromNumber(number);
            } else if (lexer.token() == JSONToken.LITERAL_FLOAT) {
                BigDecimal number = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                return (T) convertFromNumber(number);
            } else if (lexer.token() == JSONToken.LITERAL_STRING) {
                String string = lexer.stringVal();
                if (isDigitString(string)) {
                    BigDecimal number = new BigDecimal(string);
                    return (T) convertFromNumber(number);
                } else {
                    return (T) convertFromString(string);
                }
            }

            Object value = parser.parse();
            String msg = value + " can not cast to : " + targetClass.getName();
            throw new JSONException(msg);
        }

        private UserState convertFromNumber(BigDecimal number) {
            // 整数部分
            BigInteger integer = number.toBigInteger();
            // 是不是整数
            boolean isIntegral = number.compareTo(new BigDecimal(integer)) == 0;
            if (!isIntegral) { // 不是整数
                String msg = number.toPlainString() + " can not cast to : " + UserState.class.getName();
                throw new JSONException(msg);
            }
            BigInteger minInteger = BigInteger.valueOf(Integer.MIN_VALUE);
            BigInteger maxInteger = BigInteger.valueOf(Integer.MAX_VALUE);
            if (integer.compareTo(minInteger) < 0 || integer.compareTo(maxInteger) > 0) {
                // 不在Integer范围内
                throw new JSONException(integer + " can not cast to : " + UserState.class.getName());
            }
            // 根据code转换
            int targetValue = number.intValue();
            for (UserState item : UserState.values()) {
                if (item.code() == targetValue) {
                    return item;
                }
            }
            throw new JSONException(integer + " can not cast to : " + UserState.class.getName());
        }

        private UserState convertFromString(String string) {
            if (string.length() == 0) {
                return null;
            }
            // 根据name转换
            for (UserState item : UserState.values()) {
                if (item.name().equals(string)) {
                    return item;
                }
            }
            throw new JSONException(string + " can not cast to : " + UserState.class.getName());
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
            return JSONToken.UNDEFINED; // name or ordinal
        }

    }
}
