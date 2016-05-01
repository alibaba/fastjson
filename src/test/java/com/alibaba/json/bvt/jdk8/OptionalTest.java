package com.alibaba.json.bvt.jdk8;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class OptionalTest extends TestCase {
    public void test_optional() throws Exception {
        Optional<Integer> val = Optional.of(3);
        
        String text = JSON.toJSONString(val);
        
        Assert.assertEquals("3", text);
        
        Optional<Integer> val2 = JSON.parseObject(text, new TypeReference<Optional<Integer>>() {});
        Assert.assertEquals(val.get(), val2.get());
    }
    
    public void test_optionalInt_present() throws Exception {
        String text = JSON.toJSONString(OptionalInt.empty());
        
        Assert.assertEquals("null", text);
    }
    
    public void test_optionalInt() throws Exception {
        OptionalInt val = OptionalInt.of(3);
        
        String text = JSON.toJSONString(val);
        
        Assert.assertEquals("3", text);
        
        OptionalInt val2 = JSON.parseObject(text, OptionalInt.class);
        Assert.assertEquals(val.getAsInt(), val2.getAsInt());
    }
    
    public void test_optionalLong_present() throws Exception {
        String text = JSON.toJSONString(OptionalLong.empty());
        
        Assert.assertEquals("null", text);
    }
    
    public void test_optionalLong() throws Exception {
        OptionalLong val = OptionalLong.of(3);
        
        String text = JSON.toJSONString(val);
        
        Assert.assertEquals("3", text);
        
        OptionalLong val2 = JSON.parseObject(text, OptionalLong.class);
        Assert.assertEquals(val.getAsLong(), val2.getAsLong());
    }
    
    public void test_optionalDouble_present() throws Exception {
        String text = JSON.toJSONString(OptionalDouble.empty());
        
        Assert.assertEquals("null", text);
    }
    
    public void test_optionalDouble() throws Exception {
        OptionalDouble val = OptionalDouble.of(3.1D);
        
        String text = JSON.toJSONString(val);
        
        Assert.assertEquals("3.1", text);
        
        OptionalDouble val2 = JSON.parseObject(text, OptionalDouble.class);
        Assert.assertEquals(Double.toString(val.getAsDouble()), Double.toString(val2.getAsDouble()));
    }
}
