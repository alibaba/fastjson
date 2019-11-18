package com.alibaba.fastjson.serializer.issue2885;

import static org.junit.Assert.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author juliuszhang
 * @since 2019年11月18日
 * <p>
 * https://github.com/alibaba/fastjson/issues/2885
 */
public class TestIssue2885 {

    @Test
    public void test() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("A", 0);
        map.put("B", null);
        String expectResult = "{\"A\":0,\"B\":0}";
        assertEquals(expectResult, JSON.toJSONString(map, SerializerFeature.WriteNullNumberAsZero));

        //check WriteMapNullValue logic
        String expectResult2 = "{\"A\":0,\"B\":null}";
        assertEquals(expectResult2,JSON.toJSONString(map,SerializerFeature.WriteMapNullValue));
    }

}
