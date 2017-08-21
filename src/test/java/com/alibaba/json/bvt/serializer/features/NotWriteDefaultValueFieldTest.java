package com.alibaba.json.bvt.serializer.features;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 01/04/2017.
 */
public class NotWriteDefaultValueFieldTest extends TestCase {
    public void test_not_write_default() throws Exception {
        assertEquals("{}", JSON.toJSONString(new Model(0)));
        assertEquals("{\"id\":1}", JSON.toJSONString(new Model(1)));
    }

    public static class Model {

        @JSONField(serialzeFeatures = SerializerFeature.NotWriteDefaultValue)
        public int id;

        public Model(int id) {
            this.id = id;
        }
    }
}
