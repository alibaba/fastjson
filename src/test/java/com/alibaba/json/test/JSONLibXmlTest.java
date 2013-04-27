package com.alibaba.json.test;

import junit.framework.TestCase;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class JSONLibXmlTest extends TestCase {
    public void test_xml() throws Exception {
        XMLSerializer xmlSerializer = new XMLSerializer();
        
        JSONObject json = new JSONObject();
        json.put("id", 123);
        json.put("name", "jobs");
        json.put("flag", true);
        
        JSONArray items = new JSONArray();
        items.add("x");
        items.add(234);
        items.add(false);
        json.put("items", items);
        
        String text = xmlSerializer.write(json);
        System.out.println(text);
    }
}
