package com.alibaba.json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ArrayRefTest2 extends TestCase {
    
    public void test_0() throws Exception {
        String text;
        {
            List<Group> groups = new ArrayList<Group>();
            
            Group g0 = new Group(0);
            Group g1 = new Group(1);
            Group g2 = new Group(2);
            
            groups.add(g0);
            groups.add(g1);
            groups.add(g2);
            groups.add(g0);
            groups.add(g1);
            groups.add(g2);
            
            text = JSON.toJSONString(groups);
        }
        
        System.out.println(text);
        
        Group[] groups = JSON.parseObject(text, new TypeReference<Group[]>() {});
        Assert.assertEquals(6, groups.length);
        
        Assert.assertNotNull(groups[0]);
        Assert.assertNotNull(groups[1]);
        Assert.assertNotNull(groups[2]);
        Assert.assertNotNull(groups[3]);
        Assert.assertNotNull(groups[4]);
        Assert.assertNotNull(groups[5]);
        
        Assert.assertEquals(0, groups[0].getId());
        Assert.assertEquals(1, groups[1].getId());
        Assert.assertEquals(2, groups[2].getId());
        Assert.assertEquals(0, groups[3].getId());
        Assert.assertEquals(1, groups[4].getId());
        Assert.assertEquals(2, groups[5].getId());
    }

    public static class Group {

        private int id;

        public Group(){

        }

        public Group(int id){
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
        public String toString() {
            return "{id:" + id + "}";
        }

    }
}
