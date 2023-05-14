package com.alibaba.json.bvt.taobao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class MTopTest extends TestCase {

    public void test_for_mtop() throws Exception {
        P0 p = new P0();
        p.model = new Model();

        ContextValueFilter valueFilter = new ContextValueFilter() {

            @Override
            public Object process(BeanContext context, Object object, String name, Object value) {


                if (value instanceof Model) {
                    Assert.assertEquals(P0.class, context.getBeanClass());
                    Assert.assertNotNull(context.getField());
                    Assert.assertNotNull(context.getMethod());
                    Assert.assertEquals("model", context.getName());
                    Assert.assertEquals(Model.class, context.getFieldClass());
                    Assert.assertEquals(Model.class, context.getFieldType());
                    Assert.assertEquals(SerializerFeature.WriteMapNullValue.mask, context.getFeatures());
                    
                    Field field = context.getField();
                    Assert.assertNotNull(field.getAnnotation(UrlIdentify.class));
                    Assert.assertNotNull(context.getAnnation(UrlIdentify.class));
                    
                    return value;
                }

                return value;
            }
        };

        JSON.toJSONString(p, valueFilter);
    }

    private static class P0 {

        @JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue)
        @UrlIdentify(schema = "xxxx")
        private Model model;

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

    }

    public static class Model {

        private int    id;
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

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface UrlIdentify {

        String schema();
    }
}
