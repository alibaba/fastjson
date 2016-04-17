package com.alibaba.json.bvt.serializer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONLexer;

public class SpecicalStringTest extends TestCase {
    public void test_0 () throws Exception {
        String text;
        {
            JSONObject json = new JSONObject();
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", "张三");
            
            json.put("text", JSON.toJSONString(map));
            
            text = JSON.toJSONString(json);
        }
        
        Assert.assertEquals("{\"text\":\"{\\\"name\\\":\\\"张三\\\"}\"}", text);
    }
    
    public void test_string2() throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append('"');
        for (int i = 0; i < 200; ++i) {
            buf.append("\\\\\\/\\b\\f\\n\\r\\t\\u" + Integer.toHexString('中'));
        }
        buf.append('"');

        String text = buf.toString();

        JSONLexer lexer = new JSONLexer(text.toCharArray(), text.length());
        lexer.nextToken();

        Assert.assertEquals(0, pos(lexer));

        lexer.stringVal();

        // Assert.assertEquals("\"\\\\\\/\\b\\f\\n\\r\\t中\"",
        // JSON.toJSONString(stringVal));

    }
    
    int pos(JSONLexer lexer) throws Exception {
        Field field = JSONLexer.class.getDeclaredField("pos");
        field.setAccessible(true);
        return field.getInt(lexer);
    }
}
