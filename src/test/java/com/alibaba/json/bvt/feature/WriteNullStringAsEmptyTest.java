package com.alibaba.json.bvt.feature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 14/03/2017.
 */
public class WriteNullStringAsEmptyTest extends TestCase {
    public void test_features() throws Exception {
        PropertyFilter filter = new PropertyFilter() {

            public boolean apply(Object object, String name, Object value) {
                if (value == null && object instanceof Model && "id".equals(name)) {
                    return false;
                }
                return true;
            }
        };

        Model model = new Model();
        String json = JSON.toJSONString(model, filter, SerializerFeature.WriteNullStringAsEmpty);
        System.out.println(json);
    }


    private static class Model {
        @JSONField(serialzeFeatures = SerializerFeature.WriteNullStringAsEmpty)
        public String id;
    }
}
