package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Issue_748 extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.Issue_748.");
    }

    public void testJsonObjectWithClassName() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", "value1");
        jsonObject.put("key2", "value2");
        DataObject dataObject = new DataObject();
        dataObject.setValue(jsonObject);
        String jsonStr = JSON.toJSONString(dataObject, SerializerFeature.QuoteFieldNames,
                                           SerializerFeature.SkipTransientField, SerializerFeature.WriteClassName);

//        System.out.println("parse之前:" + jsonStr);

        DataObject obj = (DataObject) JSON.parse(jsonStr, Feature.IgnoreNotMatch);
        Assert.assertNotNull(obj.value);
        Assert.assertNotNull(obj.value.get("key1"));
        Assert.assertNotNull(obj.value.get("key2"));
    }

    public static class DataObject {

        private JSONObject value;

        public DataObject(){
        }

        public JSONObject getValue() {
            return value;
        }

        public void setValue(JSONObject value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "DataObject{" + "value=" + value + '}';
        }
    }

}
