package com.alibaba.json.bvt.parser.deser.generic;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class GenericArrayTest4 extends TestCase {
    
    public void test_generic() throws Exception {
        VO vo = new VO();
        vo.values = new Pair[] {null, null};
        
        String text = JSON.toJSONString(vo);
//        VO vo1 = JSON.parseObject(text, new TypeReference<VO<Number, String>>(){} );
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertNotNull(vo1.values);
        Assert.assertEquals(2, vo1.values.length);
//        Assert.assertEquals("a", vo1.values[0]);
//        Assert.assertEquals("b", vo1.values[1]);
    }

    public static class A<T extends Number, S> {
        public Pair<? extends T, ? extends S>[] values;
    }
    
    public static class VO extends A<Number, String> {
        
    }
    
    public static class Pair<A, B> {
        public A a;
        public B b;
    }
}

