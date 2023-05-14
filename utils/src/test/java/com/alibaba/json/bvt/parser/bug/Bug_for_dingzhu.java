package com.alibaba.json.bvt.parser.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;
import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Bug_for_dingzhu extends TestCase {
    public void test_0() throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("notExitAfterVid", false);
        params.put("VIData", "{\"config\":{\"noTokens\":\"Y\",\"stopReport\":\"Y\"}");
        StupidObject so = new StupidObject();
        so.setParams(params);
        SerializeFilter[] filters = new SerializeFilter[] { new DumbValueFilter()};
        String jsonString = JSON.toJSONString(so, new SerializeConfig(), filters,
                SerializerFeature.NotWriteDefaultValue, SerializerFeature.IgnoreErrorGetter,
                SerializerFeature.QuoteFieldNames);
    }

    private class DumbValueFilter implements ContextValueFilter {
        public Object process(BeanContext context, Object object, String name, Object value) {
            if (context == null) {
                return object;
            }

            Field field = context.getField();
            return value;
        }
    }

    private class StupidObject {
        private Map<String, Object> params;

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }
    }
}
