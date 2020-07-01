package com.alibaba.json.bvt.writeClassName;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MapTest extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.writeClassName.MapTest");
    }

    public void test_map() throws Exception {
        VO vo = new VO();
        vo.getValue().put("1", "AA");
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
        
        System.out.println(text);
        
        VO vo2 = (VO) JSON.parse(text);
        
        Assert.assertEquals(vo.getValue(), vo2.getValue());
    }
    
    public void test_map_2() throws Exception {
        VO vo = new VO();
        vo.setValue(new TreeMap<String, Object>());
        vo.getValue().put("1", "AA");
        
        String text = JSON.toJSONString(vo, SerializerFeature.WriteClassName);
        
        System.out.println(text);
        
        VO vo2 = (VO) JSON.parse(text);
        
        Assert.assertEquals(vo.getValue(), vo2.getValue());
    }

    private static class VO {

        private Map<String, Object> value = new HashMap<String, Object>();

        public Map<String, Object> getValue() {
            return value;
        }

        public void setValue(Map<String, Object> value) {
            this.value = value;
        }

    }
}
