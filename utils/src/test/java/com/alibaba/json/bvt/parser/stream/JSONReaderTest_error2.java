package com.alibaba.json.bvt.parser.stream;

import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.Assert;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;

public class JSONReaderTest_error2 extends TestCase {
    private static Object context;
    private static Field stateField;
    
    public void test_read() throws Exception {
        Field field = JSONReader.class.getDeclaredField("context");
        field.setAccessible(true);
        ;

        JSONReader reader = new JSONReader(new StringReader("[{}]"));
        reader.config(Feature.AllowArbitraryCommas, true);

        reader.startArray();

        context = field.get(reader);
        stateField = context.getClass().getDeclaredField("state");
        stateField.setAccessible(true);
        

        {
            Exception error = null;
            try {
                reader.readObject(VO.class);
            } catch (Exception ex) {
                error = ex;
            }
            Assert.assertNotNull(error);
        }
    }
    
    public static class VO {
        public VO() {
            try {
                stateField.set(context, -1);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
