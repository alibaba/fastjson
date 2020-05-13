package com.alibaba.json.demo;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.ValueFilter;

public class FilterDemo extends TestCase {

    public void test_secure() throws Exception {

        ValueFilter filter = new ValueFilter() {

            public Object process(Object source, String name, Object value) {
                if (name.equals("name")) {
                    return "WSJ";
                }
                return value;
            }
        };

        NameFilter nameFilter = new NameFilter() {
            public String process(Object source, String name, Object value) {
                if (name.equals("id")) {
                    return "ID";
                }
                return name;
            }
        };

        String text = "{\"id\":123,\"name\":\"WJH\"}";

        Object object = JSON.parse(text);

        SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.getValueFilters().add(filter);
        serializer.getNameFilters().add(nameFilter);

        serializer.write(object);

        String outText = out.toString();
        System.out.println(outText);
    }
}
