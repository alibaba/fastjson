package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue3217 extends TestCase {
    public void testException(){
        MyException myException = new MyException();
        myException.enumTest = EnumTest.FIRST;
        TestClass testClass = new TestClass();
        testClass.setMyException(myException);

        String jsonString = JSON.toJSONString(testClass, SerializerFeature.NotWriteDefaultValue);
        System.out.println(jsonString);

        TestClass testClass1 = JSON.parseObject(jsonString, TestClass.class);
        System.out.println(testClass1);
    }

    public static enum EnumTest{
        FIRST("111","111"),
        SECOND("222","222");
        private String key;
        private String value;

        EnumTest(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class MyException extends Exception {
        private EnumTest enumTest;

        public EnumTest getEnumTest() {
            return enumTest;
        }

        public void setEnumTest(EnumTest enumTest) {
            this.enumTest = enumTest;
        }
    }

    public static class TestClass{
        private MyException myException;

        public MyException getMyException() {
            return myException;
        }

        public void setMyException(MyException myException) {
            this.myException = myException;
        }
    }
}
