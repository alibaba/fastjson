package com.alibaba.json.bvt.issue_2200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue2201 extends TestCase  {
    public void test_for_issue() throws Exception {
        ParserConfig.getGlobalInstance().register("M2001", Model.class);

        String json = "{\"@type\":\"M2001\",\"id\":3}";
        Model m = (Model) JSON.parseObject(json, Object.class);
        assertEquals(3, m.id);
    }

    public static class Model {
        public int id;
    }
}
