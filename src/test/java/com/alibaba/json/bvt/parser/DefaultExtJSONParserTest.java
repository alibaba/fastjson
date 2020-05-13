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
package com.alibaba.json.bvt.parser;

import static com.alibaba.fastjson.util.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.util.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.util.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.util.TypeUtils.castToByte;
import static com.alibaba.fastjson.util.TypeUtils.castToDate;
import static com.alibaba.fastjson.util.TypeUtils.castToDouble;
import static com.alibaba.fastjson.util.TypeUtils.castToFloat;
import static com.alibaba.fastjson.util.TypeUtils.castToInt;
import static com.alibaba.fastjson.util.TypeUtils.castToLong;
import static com.alibaba.fastjson.util.TypeUtils.castToShort;
import static com.alibaba.fastjson.util.TypeUtils.castToString;

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;

import junit.framework.TestCase;

public class DefaultExtJSONParserTest extends TestCase {

    public void test_parseObject() {
        new DefaultJSONParser("".toCharArray(), 0, ParserConfig.getGlobalInstance(), 0).close();
        User user = new User();
        user.setName("校长");
        user.setAge(3);
        user.setSalary(new BigDecimal("123456789.0123"));

        String jsonString = JSON.toJSONString(user);

        System.out.println(jsonString);

        JSON.parseObject(jsonString);

        DefaultJSONParser parser = new DefaultJSONParser(jsonString);
        User user1 = new User();
        parser.parseObject(user1);

        Assert.assertEquals(user.getAge(), user1.getAge());
        Assert.assertEquals(user.getName(), user1.getName());
        Assert.assertEquals(user.getSalary(), user1.getSalary());
    }

    public void testCastCalendar() throws Exception {
        Calendar c = Calendar.getInstance();
        Date d = TypeUtils.castToDate(c);
        Assert.assertEquals(c.getTime(), d);
    }

    public void testCast() throws Exception {
        new TypeUtils();
        DefaultJSONParser parser = new DefaultJSONParser("");

        Assert.assertNull(castToByte(null));
        Assert.assertNull(castToShort(null));
        Assert.assertNull(castToInt(null));
        Assert.assertNull(castToLong(null));
        Assert.assertNull(castToBigInteger(null));
        Assert.assertNull(castToBigDecimal(null));
        Assert.assertNull(castToFloat(null));
        Assert.assertNull(castToDouble(null));
        Assert.assertNull(castToBoolean(null));
        Assert.assertNull(castToDate(null));
        Assert.assertNull(castToString(null));

        Assert.assertEquals(12, castToByte("12").intValue());
        Assert.assertEquals(1234, castToShort("1234").intValue());
        Assert.assertEquals(1234, castToInt("1234").intValue());
        Assert.assertEquals(1234, castToLong("1234").intValue());
        Assert.assertEquals(1234, castToBigInteger("1234").intValue());
        Assert.assertEquals(1234, castToBigDecimal("1234").intValue());
        Assert.assertEquals(1234, castToFloat("1234").intValue());
        Assert.assertEquals(1234, castToDouble("1234").intValue());

        Assert.assertEquals(12, castToByte(12).intValue());
        Assert.assertEquals(1234, castToShort(1234).intValue());
        Assert.assertEquals(1234, castToInt(1234).intValue());
        Assert.assertEquals(1234, castToLong(1234).intValue());
        Assert.assertEquals(1234, castToBigInteger(1234).intValue());
        Assert.assertEquals(1234, castToBigDecimal(1234).intValue());
        Assert.assertEquals(1234, castToFloat(1234).intValue());
        Assert.assertEquals(1234, castToDouble(1234).intValue());

        Assert.assertEquals(Boolean.TRUE, castToBoolean(true));
        Assert.assertEquals(Boolean.FALSE, castToBoolean(false));
        Assert.assertEquals(Boolean.TRUE, castToBoolean(1));
        Assert.assertEquals(Boolean.FALSE, castToBoolean(0));
        Assert.assertEquals(Boolean.TRUE, castToBoolean("true"));
        Assert.assertEquals(Boolean.FALSE, castToBoolean("false"));

        long time = System.currentTimeMillis();
        Assert.assertEquals(time, castToDate(new Date(time)).getTime());
        Assert.assertEquals(time, castToDate(time).getTime());
        Assert.assertEquals(time, castToDate(Long.toString(time)).getTime());

        Assert.assertEquals("true", castToString("true"));
        Assert.assertEquals("true", castToString(true));
        Assert.assertEquals("123", castToString(123));

        Assert.assertEquals(new BigDecimal("2"), castToBigDecimal("2"));
        Assert.assertEquals(new BigDecimal("2"), castToBigDecimal(new BigInteger("2")));

    }

    public void test_casterror2() {
        DefaultJSONParser parser = new DefaultJSONParser("");
        {
            Exception error = null;
            try {
                castToByte(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToShort(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToInt(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToLong(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToFloat(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToDouble(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBigInteger(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBigDecimal(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToDate(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBoolean(new Object());
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    public void test_casterror() {
        DefaultJSONParser parser = new DefaultJSONParser("");

        {
            Exception error = null;
            try {
                castToByte("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToShort("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToInt("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToLong("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToFloat("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToDouble("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBigInteger("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBigDecimal("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToDate("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                castToBoolean("xx");
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    @SuppressWarnings("rawtypes")
    public void test_parseArrayWithType() throws Exception {

        Method method = DefaultExtJSONParserTest.class.getMethod("f", Collection.class, Collection.class, Collection.class, Collection.class, Collection.class,
                                                                 Collection.class, Collection.class);
        Type[] types = method.getGenericParameterTypes();
        {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            Assert.assertEquals(true, ((List) parser.parseArrayWithType(types[0])).get(0) instanceof Map);
        }
        {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            Assert.assertEquals(true, ((List) parser.parseArrayWithType(types[1])).get(0) instanceof User);
        }
        {
            Exception error = null;
            try {
                String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseArrayWithType(types[2]);
                ;
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            Assert.assertEquals(true, ((List) parser.parseArrayWithType(types[3])).get(0) instanceof User);
        }
        {
            Exception error = null;
            try {
                String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseArrayWithType(types[4]);
                ;
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            Assert.assertEquals(true, ((List) parser.parseArrayWithType(types[5])).get(0) instanceof User);
        }

        {
            Exception error = null;
            try {
                String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseArrayWithType(types[6]);
                ;
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    public void test_parseArrayWithType_error_1() throws Exception {
        Method method = DefaultExtJSONParserTest.class.getMethod("f", Collection.class, Collection.class, Collection.class, Collection.class, Collection.class,
                                                                 Collection.class, Collection.class);
        Type[] types = method.getGenericParameterTypes();

        Exception error = null;
        try {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            parser.parseArrayWithType(types[6]);
            ;
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static <T extends Object & Comparable<? super T>, T1 extends User> void f(Collection<?> p0, Collection<? extends User> p1,
                                                                                     Collection<? super User> p2, Collection<User> p3, Collection<T> p4,
                                                                                     Collection<T1> p5, Collection<T[]> p6) {

    }

    public void test_not_match() throws Exception {
        String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123, \"kxxx\":33}]";
        DefaultJSONParser parser = new DefaultJSONParser(text);
        Assert.assertEquals(true, (parser.parseArray(User.class).get(0) instanceof User));
    }

    public void test_not_match_error() throws Exception {
        Exception error = null;
        try {
            String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123, \"kxxx\":33}]";
            DefaultJSONParser parser = new DefaultJSONParser(text);
            parser.config(Feature.IgnoreNotMatch, false);
            Assert.assertEquals(true, (parser.parseArray(User.class).get(0) instanceof User));
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error() throws Exception {
        {
            Exception error = null;
            try {
                String text = "[{\"old\":false,\"name\":\"校长\",\"age\":3,\"salary\":123456789.0123]";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseArray(User.class);
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                String text = "{\"reader\":3}";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseObject(ErrorObject.class);
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
        {
            Exception error = null;
            try {
                String text = "{\"name\":3}";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseObject(ErrorObject2.class);
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    public static class ErrorObject {

        private Reader reader;

        public Reader getReader() {
            return reader;
        }

        public void setReader(Reader reader) {
            this.reader = reader;
        }
    }

    public static class ErrorObject2 {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            throw new UnsupportedOperationException();
        }

    }

    public void test_error2() throws Exception {
        {
            Exception error = null;
            try {
                String text = "{}";
                DefaultJSONParser parser = new DefaultJSONParser(text);
                parser.parseArray(User.class);
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }

    public static class User {

        private String     name;
        private int        age;
        private BigDecimal salary;
        private Date       birthdate;
        private boolean    old;

        public boolean isOld() {
            return old;
        }

        public void setOld(boolean old) {
            this.old = old;
        }

        public Date getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setage(int age) {
            this.age = age;
        }

        public void set(int age) {
            throw new UnsupportedOperationException();
        }

        public void get(int age) {
            throw new UnsupportedOperationException();
        }

        public void is(int age) {
            throw new UnsupportedOperationException();
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public static void setFF() {

        }

        void setXX() {

        }
    }
}
