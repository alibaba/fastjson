package com.alibaba.json.bvt.parser.deser.generic;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class GenericArrayTest2 extends TestCase {
    
    public void test_generic() throws Exception {
        VO vo = new VO();
        vo.values = new String[] {"a", "b"};
        
        String text = JSON.toJSONString(vo);
        VO vo1 = JSON.parseObject(text, VO.class);
        Assert.assertNotNull(vo1.values);
        Assert.assertEquals(2, vo1.values.length);
        Assert.assertEquals("a", vo1.values[0]);
        Assert.assertEquals("b", vo1.values[1]);
    }

    public static class A<T> {
        public T[] values;
    }
    
    public static class VO extends A {
        
    }
}

