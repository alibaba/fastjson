package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_yanpei4 extends TestCase {
    public void test_for_issue() throws Exception {
        String valueText = JSON.toJSONString("a\"Puck\"");
        System.out.println("valueText : " + valueText);
        
        RPCAckBody body1 = new RPCAckBody();
        body1.actionValue = valueText;
        
        String bodyString = JSON.toJSONString(body1);
        System.out.println(bodyString);
        
         RPCAckBody body2 = JSON.parseObject(bodyString, RPCAckBody.class);
         
         System.out.println(body1.actionValue);
         System.out.println(body2.actionValue);
         Assert.assertEquals(body1.actionValue, body2.actionValue);
     }
     
     public static class RPCAckBody {
         public String actionValue;
     }
}
