package com.alibaba.json.bvt.bug;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_chengchao extends TestCase {

    public void test_0() throws Exception {
        SerializerFeature[] features = { SerializerFeature.WriteMapNullValue,

        SerializerFeature.WriteEnumUsingToString, SerializerFeature.SortField };

        Entity entity = new Entity();
        JSON.toJSONString(entity, features);
    }

    private static class Entity {

        private TimeUnit unit;

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }

    }
}
