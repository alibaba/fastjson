package com.alibaba.json.bvt.serializer.filters;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

import junit.framework.TestCase;

public class MTopFilterTest extends TestCase {
    public void test_0() throws Exception {
        Model model = new Model();
        model.id = 1001;
        
        ValueFilter valueFilter = new ValueFilter() {

            @Override
            public Object process(Object object, String name, Object value) {
                System.out.printf("%s_%s\n", name, value);
                return value;
            }
        };

        String jsonString = JSON.toJSONString(model, valueFilter);
        Assert.assertEquals("{\"id\":1001}", jsonString);
    }

    
    public static class Model {
        private int id;

        
        public int getId() {
            return id;
        }

        
        public void setId(int id) {
            this.id = id;
        }
        
    }
}
