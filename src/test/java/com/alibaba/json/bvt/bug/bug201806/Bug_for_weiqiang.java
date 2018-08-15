package com.alibaba.json.bvt.bug.bug201806;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Bug_for_weiqiang extends TestCase {
    public void test_for_bug() throws Exception {
        SerializeWriter sw = new SerializeWriter();
        sw.config(SerializerFeature.WriteNullStringAsEmpty, Boolean.TRUE);
        JSONSerializer js = new JSONSerializer(sw);
        js.write(JSON.parseObject("{'operator':null, 'status':1}"));
        System.out.println(js);

        String json2 = JSON.toJSONString(JSON.parseObject("{'operator':null, 'status':1}"), SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(json2);
    }
}
