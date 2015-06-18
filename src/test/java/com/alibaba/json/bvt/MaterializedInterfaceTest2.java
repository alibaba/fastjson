package com.alibaba.json.bvt;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;

public class MaterializedInterfaceTest2 extends TestCase {
    
    public void test_parse() throws Exception {
        String text = "{\"id\":123, \"name\":\"chris\"}";
        JSONObject object = JSON.parseObject(text);
        
        Bean bean = TypeUtils.cast(object, Bean.class, null);
        
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
