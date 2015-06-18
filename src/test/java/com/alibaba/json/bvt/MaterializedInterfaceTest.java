package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class MaterializedInterfaceTest extends TestCase {
    
    public void test_parse() throws Exception {
        String text = "{\"id\":123, \"name\":\"chris\"}";
        Bean bean = JSON.parseObject(text, Bean.class);
        
        Assert.assertEquals(123, bean.getId());
        Assert.assertEquals("chris", bean.getName());
        
        String text2 = JSON.toJSONString(bean);
        System.out.println(text2);
    }

    public static interface Bean {
        int getId();

        void setId(int value);

        String getName();

        void setName(String value);
    }
}
