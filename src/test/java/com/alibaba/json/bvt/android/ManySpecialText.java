package com.alibaba.json.bvt.android;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class ManySpecialText extends TestCase {
    public void test_0() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 1000 * 10; ++i) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        
        Bean bean = new Bean();
        bean.actionValue = JSON.toJSONString(map);
        
        String text = JSON.toJSONString(bean);
        
        Bean bean2 = JSON.parseObject(text, Bean.class);
        Assert.assertEquals(bean.actionValue, bean2.actionValue);
    }
    
    public static class Bean {
        public String actionValue;
    }
}
