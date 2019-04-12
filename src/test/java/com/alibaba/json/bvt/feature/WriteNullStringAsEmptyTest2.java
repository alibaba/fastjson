package com.alibaba.json.bvt.feature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 14/03/2017.
 */
public class WriteNullStringAsEmptyTest2 extends TestCase {
    public void test_features() throws Exception {
        Model model = new Model();
        String json = JSON.toJSONString(model);
        assertEquals("{\"id\":\"\"}", json);
    }


    public static class Model {
        @JSONField(serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
        public String id;
    }
}
