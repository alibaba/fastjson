package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Issue771 extends TestCase {

    public void test_for_issue() throws Exception {
        SerializeWriter writer = new SerializeWriter(null, JSON.DEFAULT_GENERATE_FEATURE, new SerializerFeature[0]);
        int defaultBufferSize = writer.getBufferLength();
        String encoded = JSON.toJSONString(new FooBar(defaultBufferSize));
        JSONObject decoded = (JSONObject) JSON.parse(encoded);
        JSONArray dataToEncode = decoded.getJSONArray("dataToEncode"); 
        Assert.assertEquals(5, dataToEncode.size());
        writer.close();
    }

    public static class FooBar {

        private List<String> dataToEncode;

        protected FooBar(int buffLen){
            dataToEncode = new ArrayList<String>();
            dataToEncode.add("foo");
            dataToEncode.add("bar");
            dataToEncode.add(new String(new char[buffLen]).replace('\0', 'a')); // create some texts to fill up & expand
                                                                                // the buffer
            dataToEncode.add("a wild special character appears: áőű"); // this will restart the list encoding (while the
                                                                       // count is committed at expand)
            dataToEncode.add("foobar");
        }

        public List<String> getDataToEncode() {
            return dataToEncode;
        }
    }
}
