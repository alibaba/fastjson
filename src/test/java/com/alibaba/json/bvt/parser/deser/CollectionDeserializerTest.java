package com.alibaba.json.bvt.parser.deser;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class CollectionDeserializerTest extends TestCase {
    public void test_set() throws Exception {
        Assert.assertEquals(HashSet.class, JSON.parseObject("{value:[]}", VO.class).getValue().getClass());
        Assert.assertEquals(HashSet.class, JSON.parseObject("[]", Set.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", VO.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", Set.class));
    }
    
    public void test_hashset() throws Exception {
        Assert.assertEquals(HashSet.class, JSON.parseObject("{value:[]}", V1.class).getValue().getClass());
        Assert.assertEquals(HashSet.class, JSON.parseObject("[]", HashSet.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V1.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", HashSet.class));
    }
    
    public void test_linkedhashset() throws Exception {
        Assert.assertEquals(LinkedHashSet.class, JSON.parseObject("{value:[]}", V2.class).getValue().getClass());
        Assert.assertEquals(LinkedHashSet.class, JSON.parseObject("[]", LinkedHashSet.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V2.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", LinkedHashSet.class));
    }
    
    public void test_treeset() throws Exception {
        Assert.assertEquals(TreeSet.class, JSON.parseObject("{value:[]}", V3.class).getValue().getClass());
        Assert.assertEquals(TreeSet.class, JSON.parseObject("[]", TreeSet.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V3.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", TreeSet.class));
    }
    
    public void test_vector() throws Exception {
        Assert.assertEquals(Vector.class, JSON.parseObject("{value:[]}", V4.class).getValue().getClass());
        Assert.assertEquals(Vector.class, JSON.parseObject("[]", Vector.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V4.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", Vector.class));
    }
    
    public void test_AbstractList() throws Exception {
        Assert.assertEquals(ArrayList.class, JSON.parseObject("{value:[]}", V5.class).getValue().getClass());
        Assert.assertEquals(ArrayList.class, JSON.parseObject("[]", AbstractList.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V5.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", AbstractList.class));
    }
    
    public void test_AbstractCollection() throws Exception {
        Assert.assertEquals(ArrayList.class, JSON.parseObject("{value:[]}", V6.class).getValue().getClass());
        Assert.assertEquals(ArrayList.class, JSON.parseObject("[]", AbstractCollection.class).getClass());
        Assert.assertEquals(null, JSON.parseObject("{value:null}", V6.class).getValue());
        Assert.assertEquals(null, JSON.parseObject("null", AbstractCollection.class));
    }
    
    public static class VO {

        private Set value;

        public Set getValue() {
            return value;
        }

        public void setValue(Set value) {
            this.value = value;
        }

    }

    public static class V1 {

        private HashSet value;

        public HashSet getValue() {
            return value;
        }

        public void setValue(HashSet value) {
            this.value = value;
        }

    }
    
    public static class V2 {

        private LinkedHashSet value;

        public LinkedHashSet getValue() {
            return value;
        }

        public void setValue(LinkedHashSet value) {
            this.value = value;
        }

    }
    
    public static class V3 {

        private TreeSet value;

        public TreeSet getValue() {
            return value;
        }

        public void setValue(TreeSet value) {
            this.value = value;
        }

    }
    
    public static class V4 {
        
        private Vector value;
        
        public Vector getValue() {
            return value;
        }
        
        public void setValue(Vector value) {
            this.value = value;
        }
        
    }
    
    public static class V5 {
        
        private AbstractList value;
        
        public AbstractList getValue() {
            return value;
        }
        
        public void setValue(AbstractList value) {
            this.value = value;
        }
        
    }
    
    public static class V6 {
        
        private AbstractCollection value;
        
        public AbstractCollection getValue() {
            return value;
        }
        
        public void setValue(AbstractCollection value) {
            this.value = value;
        }
        
    }
}
