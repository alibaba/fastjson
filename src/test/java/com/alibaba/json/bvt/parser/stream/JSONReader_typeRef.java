package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;

public class JSONReader_typeRef extends TestCase {
    public void test_array() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[{\"id\":123}]"));
        
        List<VO> list = reader.readObject(new TypeReference<List<VO>>() {}.getType());
        
        Assert.assertEquals(123, list.get(0).getId());
        
        reader.close();
    }
    
    public void test_array_1() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[[{\"id\":123}]]"));
        
        reader.startArray();
        List<VO> list = reader.readObject(new TypeReference<List<VO>>() {}.getType());
        
        Assert.assertEquals(123, list.get(0).getId());
        
        reader.endArray();
        
        reader.close();
    }
    
    public void test_array_2() throws Exception {
        JSONReader reader = new JSONReader(new StringReader("[[{\"id\":123}]]"));
        
        reader.startArray();
        List<VO> list = reader.readObject(new TypeReference<List<VO>>() {});
        
        Assert.assertEquals(123, list.get(0).getId());
        
        reader.endArray();
        
        reader.close();
    }
    
    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
