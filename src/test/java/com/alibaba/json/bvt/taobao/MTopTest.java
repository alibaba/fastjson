package com.alibaba.json.bvt.taobao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.FieldContext;

import junit.framework.TestCase;

public class MTopTest extends TestCase {
    
    public void test_for_mtop() throws Exception {
        P0 p = new P0();
        p.model = new Model();
        
        ContextValueFilter valueFilter = new ContextValueFilter() {
            @Override
            public Object process(FieldContext context, Object object, String name, Object value) {
                // TODO Auto-generated method stub
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
        private String url;

        
        public String getUrl() {
            return url;
        }

        
        public void setUrl(String url) {
            this.url = url;
        }
        
        
    }
    
    public static @interface UrlIdentify {
        String schema();
    }
}
