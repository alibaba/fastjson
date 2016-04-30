package com.alibaba.json.bvt.serializer.filters;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.BeforeFilter;

public class AfterFilterTest extends TestCase {
    public void test_afterFilter() throws Exception {
        AfterFilter filter = new AfterFilter() {
            
            @Override
            public void writeAfter(Object object) {
                this.writeKeyValue("id", 123);
            }
        };
        Assert.assertEquals("{\"id\":123}",JSON.toJSONString( new VO(), filter));
    }
    
    public void test_afterFilter2() throws Exception {
        AfterFilter filter = new AfterFilter() {
            
            @Override
            public void writeAfter(Object object) {
                this.writeKeyValue("id", 123);
                this.writeKeyValue("name", "wenshao");
            }
        };
        Assert.assertEquals("{\"id\":123,\"name\":\"wenshao\"}", JSON.toJSONString(new VO(), filter));
    }
    
    private static class VO {
        
    }
}
