package com.alibaba.fastjson.deserializer.issue4070;

import com.alibaba.fastjson.JSONPath;
import org.junit.Test;

import java.math.BigInteger;

public class issue4070 {
    // Related to issue #4070 https://github.com/alibaba/fastjson/issues/4070
    @Test
    public void Test_0(){
        // test item include b
        TestClass item = new TestClass(1,new Object());
        BigInteger b = BigInteger.valueOf(1);
        Boolean flag = JSONPath.containsValue(item, "$.num", b);
        System.out.println(flag);
    }
    @Test
    public void Test_1(){
        // test item not include b
        TestClass item = new TestClass(2,new Object());
        BigInteger b = BigInteger.valueOf(1);
        Boolean flag = JSONPath.containsValue(item, "$.num", b);
        System.out.println(flag);
    }
    public static class TestClass {
        private Integer num;
        private Object value;

        public TestClass() {
        }

        public TestClass(Integer num, Object value) {
            this.num = num;
            this.value = value;
        }

        public Integer getNum() {
            return num;
        }

        public Object getValue() {
            return value;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
