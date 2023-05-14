package com.alibaba.json.bvt.support.spring.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class PageToJSONTest extends TestCase {
    public void test_page() throws Exception {
        List<Post> postList = new ArrayList<Post>();
        {
            postList.add(new Post(1001));
        }
        
        Page<Post> page = new PageImpl(postList);
        
        JSONObject obj = (JSONObject) JSON.toJSON(page);
        Assert.assertNotNull(obj);
        Assert.assertEquals(1, obj.getJSONArray("content").size());
    }
    
    public static class Post{
        public int id;
        
        public Post() {
            
        }
        
        public Post(int id) {
            this.id = id;
        }
    }
}
