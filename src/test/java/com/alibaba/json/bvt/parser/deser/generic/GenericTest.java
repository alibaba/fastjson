package com.alibaba.json.bvt.parser.deser.generic;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class GenericTest extends TestCase {
    
    public void test_0 () throws Exception {
       B b = JSON.parseObject("{\"data\":[1,2,3]}", B.class);
       b.get(0);
    }

    public static abstract class A<T> {
        T[] data;
        
        public A() {
            
        }
        

        
        public T[] getData() {
            return data;
        }


        
        public void setData(T[] data) {
            this.data = data;
        }
    }
    
    public static class B extends A<Long> {
        public B() {
        }
        
        public Long get(int index) {
            Long l = data[index];
            return l;
        }
    }
    
    public static class C<T> {
        private T[] data;
        
        public C(T[] data) {
            this.data = data;
        }

        
        public T[] getData() {
            return data;
        }
        
    }
}
