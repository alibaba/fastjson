package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import junit.framework.TestCase;


public class InterfaceParseTest extends TestCase {
    public void test_interface() throws Exception {
        VO vo = JSON.parseObject("{\"text\":\"abc\",\"b\":true}", VO.class);
        Assert.assertEquals("abc", vo.getText());
        Assert.assertEquals(Boolean.TRUE, vo.getB());
    }
    
    public static interface VO {
        void setText(String val);
        String getText();
        
        void setB(Boolean val);
        Boolean getB();
        
        void setI(int value);
        void setC(char value);
        void setS(short value);
        void setL(long value);
    }
}
