package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_cduym extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Bug_for_cduym");
    }

    @SuppressWarnings("rawtypes")
    public void test0() {

        List<A> as = new ArrayList<A>();

        A a1 = new A();
        a1.setA(1000);
        a1.setB(2000l);
        a1.setC("xxx");

        as.add(a1);
        as.add(a1);

        String text = JSON.toJSONString(as, SerializerFeature.WriteClassName);
        System.out.println(text);
        List<?> target = (List) JSON.parseObject(text, Object.class);
        
        Assert.assertSame(target.get(0), target.get(1));
    }
    
    public void test1() {
        
        List<A> as = new ArrayList<A>();
        
        A a1 = new A();
        a1.setA(1000);
        a1.setB(2000l);
        a1.setC("xxx");
        
        as.add(a1);
        as.add(a1);
        
        Demo o = new Demo();
        o.setAs(as);
        
        String text = JSON.toJSONString(o, SerializerFeature.WriteClassName);
        System.out.println(text);
        Demo target = (Demo) JSON.parseObject(text, Object.class);
        
        Assert.assertSame(((List)target.getAs()).get(0), ((List)target.getAs()).get(1));
    }
    

    public static class Demo {

        private Object as;

        public Object getAs() {
            return as;
        }

        public void setAs(Object as) {
            this.as = as;
        }

    }

    private static class A {

        private Integer a;
        private Long    b;

        private String  c;

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }

        public Long getB() {
            return b;
        }

        public void setB(Long b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

    }
}
