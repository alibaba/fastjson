package com.alibaba.json.bvt.bug;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Bug_for_zhongyin extends TestCase {

    public void test_entity() throws Exception {
        for (char c = '\u0000'; c < '\u0020'; c++) {
            String s = String.valueOf(c) + "entity";
            String jsons = JSON.toJSONString(new VO(s));
            System.out.println(jsons);
            VO v = JSON.parseObject(jsons, VO.class);
            Assert.assertEquals(s, v.getName());
        }
    }

    public void test_map() throws Exception {
        for (char c = '\u0000'; c < '\u0020'; c++) {
            String s = String.valueOf(c) + "map";
            String jsons = JSON.toJSONString(Collections.singletonMap("value", s));
            System.out.println(jsons);

            JSONObject o = JSON.parseObject(jsons);
            Assert.assertEquals(s, o.getString("value"));
        }
    }
    
    public  void test_0() throws Exception {
        String hex = "41544D20E58F96E78EB0EFBC8DE993B6E88194E5908CE59F8E1A20E4BD9BE5B1B1E5B882E7A685E59F8EE58CBAE7A596E5BA99E8B7AF201A33331A20E58FB7E799BEE88AB1E5B9BFE59CBAE9A696E5B182201A";
        String result = getHexStr(hex);
        
        Map <String , Object>map = new HashMap<String, Object>();
        map.put("aaa" , result);
        String stringV = JSON.toJSONString(map);
        System.out.println(stringV);
        JSONObject o = JSON.parseObject(stringV);
        System.out.println(o.getString("aaa"));
        
    }
    
    private String getHexStr(String hex) throws UnsupportedEncodingException {
        byte []bytes = new byte[hex.length() / 2];
        for(int i = 0 ; i < bytes.length ; i++) {
            String v = hex.substring(i * 2 , i * 2 + 2);
            bytes[i] = (byte)Integer.parseInt(v , 16);
        }
        String str = new String(bytes , "UTF-8");
        System.out.println(str);
        return str;
    }

    public static class VO {

        private String name;

        public VO(){

        }

        public VO(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
