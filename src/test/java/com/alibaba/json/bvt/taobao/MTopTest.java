package com.alibaba.json.bvt.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.BeanContext;

import junit.framework.TestCase;

public class MTopTest extends TestCase {
    
    public void test_for_mtop() throws Exception {
        P0 p = new P0();
        p.model = new Model();
        
        ContextValueFilter valueFilter = new ContextValueFilter() {
            @Override
            public Object process(BeanContext context, Object object, String name, Object value) {
                Class<?> objectClass = context.getBeanClass();
                UrlIdentify annotation = context.getAnnation(UrlIdentify.class);
                
                if (value instanceof Model) {
                    return value;
                }
                
                return value;
            }
        };
        
        JSON.toJSONString(p, valueFilter);
    }

    private static class P0 {
        @UrlIdentify(schema="xxxx")
        private Model model;

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

    }

    public static class Model {
        private int id;
        private String url;

        
        public String getUrl() {
            return url;
        }

        
        public void setUrl(String url) {
            this.url = url;
        }


        
        public int getId() {
            return id;
        }


        
        public void setId(int id) {
            this.id = id;
        }
        
        
    }
    
    public static @interface UrlIdentify {
        String schema();
    }
}
