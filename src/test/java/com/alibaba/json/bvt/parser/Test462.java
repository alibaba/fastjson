package com.alibaba.json.bvt.parser;


import junit.framework.TestCase;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import java.math.BigInteger;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class Test462 extends TestCase {

    @Test
    public void test_462() throws Exception {
        String test_text = "{a:123456789123456789123456789123456}";
        DefaultJSONParser parser = new DefaultJSONParser(test_text);
        parser.config(Feature.UseBigInteger, false);
        Map<String,Object> ret = (Map<String,Object>) parser.parse();
        for( Map.Entry<String,Object> en : ret.entrySet() ){
            System.out.println("BigInteger.class="+BigInteger.class);
            System.out.println("en.getValue().getClass()="+en.getValue().getClass());
            Assert.assertNotEquals(BigInteger.class, en.getValue().getClass());
        }
    }
     
}
