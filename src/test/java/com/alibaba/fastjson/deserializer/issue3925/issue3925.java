package com.alibaba.fastjson.deserializer.issue3925;

import com.alibaba.fastjson.JSONObject;
import lombok.Value;
import org.junit.Test;
import com.alibaba.fastjson.JSON;

public class issue3925 {
    // Related to issue #3925 https://github.com/alibaba/fastjson/issues/3925
    @Value
    public static class SomeVO {
        Long id;
    }
    @Value
    public static class SomeV1 {
        Short id;
    }


    @Test
    public void testLong(){
        //Test long type
        final String txt = "{\"id\":42L}";
        SomeVO a = JSON.parseObject(txt, SomeVO.class); // OK
        JSONObject jo = JSON.parseObject(txt);
        SomeVO b = jo.toJavaObject(SomeVO.class); //com.alibaba.fastjson.JSONException: create instance error
        System.out.println(a);
        System.out.println(b);
    }
    @Test
    public void testShort(){
        //Test short type
        final String txt = "{\"id\":11S}";
        SomeV1 a = JSON.parseObject(txt, SomeV1.class); // OK
        JSONObject jo = JSON.parseObject(txt);
        SomeV1 b = jo.toJavaObject(SomeV1.class); //com.alibaba.fastjson.JSONException: create instance error
        System.out.println(a);
        System.out.println(b);
    }

}
