package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_jinghui70 extends TestCase {

    public static abstract class IdObject<I> {

        private I id;

        public I getId() {
            return id;
        }

        public void setId(I id) {
            this.id = id;
        }
    }

    public static class Child extends IdObject<Long> {

    }
    
    public void test_generic() throws Exception {
        String str = "{\"id\":0}";
        
        Child child = JSON.parseObject(str, Child.class);
        Assert.assertEquals(Long.class, child.getId().getClass());
    }
}
