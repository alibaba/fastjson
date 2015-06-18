package com.alibaba.json.bvt;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ArmoryTest extends TestCase {

    public void test_0() throws Exception {
        List<Object> message = new ArrayList<Object>();
        MessageHead head = new MessageHead();
        
        
        MessageBody body = new MessageBody();
        body.getItems().add(new Item());
        
        message.add(head);
        message.add(body);

        String text = JSON.toJSONString(message, SerializerFeature.SortField, SerializerFeature.UseSingleQuotes);
        Assert.assertEquals("[{},{'items':[{'id':0,'name':'xx'}]}]", text);
    }
    
    public static class Item {
        private int id;
        private String name = "xx";
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
    }

    public static class MessageHead {

    }

    public static class MessageBody {

        private List<Object> items = new ArrayList<Object>();

        public List<Object> getItems() {
            return items;
        }

        public void setItems(List<Object> items) {
            this.items = items;
        }

    }
}
