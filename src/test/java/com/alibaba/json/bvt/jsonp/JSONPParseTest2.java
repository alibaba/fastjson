package com.alibaba.json.bvt.jsonp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 21/02/2017.
 */
public class JSONPParseTest2 extends TestCase {
    public void test_f() throws Exception {
        String text = "parent.callback ({'id':1, 'name':'idonans'} );   /**/ ";

        JSONPObject jsonpObject = JSON.parseObject(text, JSONPObject.class);
        assertEquals("parent.callback", jsonpObject.getFunction());

        assertEquals(1, jsonpObject.getParameters().size());
        JSONObject param = (JSONObject) jsonpObject.getParameters().get(0);
        assertEquals(1, param.get("id"));
        assertEquals("idonans", param.get("name"));

        String json = JSON.toJSONString(jsonpObject, SerializerFeature.MapSortField);
        assertEquals("parent.callback({\"id\":1,\"name\":\"idonans\"})", json);
    }
}
