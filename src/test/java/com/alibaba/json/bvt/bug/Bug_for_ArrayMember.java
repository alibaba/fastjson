package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;

public class Bug_for_ArrayMember extends TestCase {
    public void test_arrayMember() throws Exception {
        A a = new A();
        a.setValues(new B[] {new B()});
        
        String text = JSON.toJSONString(a);
        
        Assert.assertEquals("{\"values\":[{}]}", text);
        Assert.assertEquals("{}", JSON.toJSONString(new A()));
        Assert.assertEquals("null", JSON.toJSONString(new A().getValues()));
        
        Assert.assertEquals("[]", JSON.toJSONString(new A[0]));
        Assert.assertEquals("[{},{}]", JSON.toJSONString(new A[] {new A(), new A()}));
    }

    public static class A {

        private B[] values;

        public B[] getValues() {
            return values;
        }

        public void setValues(B[] values) {
            this.values = values;
        }

    }

    public static class B {

    }
}
