package com.alibaba.json.bvt.ref;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;


public class RefTest15 extends TestCase {
    public void test_0 () throws Exception {
        List<Object> a = new ArrayList<Object>();
        List<Object> b = new ArrayList<Object>();
        List<Object> c = new ArrayList<Object>();
        List<Object> d = new ArrayList<Object>();
        
        a.add(b);
        a.add(c);
        a.add(d);
        
        b.add(a);
        b.add(c);
        b.add(d);
        
        c.add(a);
        c.add(b);
        c.add(d);
        
        d.add(a);
        d.add(b);
        d.add(c);
        
        String text = JSON.toJSONString(a);
        System.out.println(text);
    }
}
