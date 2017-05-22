package com.alibaba.json.bvt.bug;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSONReader;

public class Issue143 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{\"rec\":[{},{}]}";
        
        JsonStroe store = new JsonStroe();
        
        JSONReader reader = new JSONReader(new StringReader(text));
        reader.startObject();
        
        String key = reader.readString();
        
        Assert.assertEquals("rec", key);
        reader.startArray();
        
        List<KeyValue> list = new ArrayList<KeyValue>();
        while(reader.hasNext()) {
            KeyValue keyValue = reader.readObject(KeyValue.class);
            list.add(keyValue);
        }
        store.setRec(list);
        
        reader.endArray();
        
        reader.endObject();
        
        reader.close();
    }

    public static class JsonStroe {

        private List rec = new ArrayList();

        public void setRec(List items) {
            this.rec = items;
        }

        public List getRec() {
            return rec;
        }
    }

    public static class KeyValue {

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
