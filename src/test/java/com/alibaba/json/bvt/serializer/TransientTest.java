package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

public class TransientTest extends TestCase {

    public void test_transient() throws Exception {
        Category parent = new Category();
        parent.setName("Parent");

        Category child = new Category();
        child.setName("child");

        parent.addChild(child);

        String text = JSON.toJSONString(parent);
        System.out.println(text);
        Map</**fieldName*/String , Field> fieldCacheMap =new HashMap<String, Field>();
        ParserConfig.parserAllFieldToCache(Category.class, fieldCacheMap);  
        Assert.assertNotNull(ParserConfig.getFieldFromCache("name", fieldCacheMap));
        Assert.assertNull(ParserConfig.getFieldFromCache("abc",fieldCacheMap));
    }

    public static class Category {

        private String             name;
        private transient Category parent;

        private List<Category>     children = new ArrayList<Category>();

        public void addChild(Category child) {
            children.add(child);
            child.setParent(this);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category getParent() {
            return parent;
        }

        public void setParent(Category parent) {
            this.parent = parent;
        }

        public List<Category> getChildren() {
            return children;
        }

        public void setChildren(List<Category> children) {
            this.children = children;
        }

    }
}
