package com.zero;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONString;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.TypeUtils;
import com.zero.entity.Data;
import com.zero.entity.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTest {
    @Test
    public void simpleJson(){
        List<Person> personList = new ArrayList<Person>();
        personList.add(new Person("lili"));
        Map jsonMap = new HashMap();
        jsonMap.put("child",JSON.toJSONString(new Person("lucy")));
        jsonMap.put("list",JSON.toJSONString(personList));
        jsonMap.put("name",1);
        jsonMap.put("value",2);
        String json = JSON.toJSONString(jsonMap);
        Data data = JSON.parseObject(json,Data.class);
        assert data.getName().equals("1");
        String parsedJSON = JSON.toJSONString(data, new ValueFilter() {
            public Object process(Object object, String name, Object value) {
                JSONString string = TypeUtils.getField(object.getClass(),name,object.getClass().getDeclaredFields()).getAnnotation(JSONString.class);
                if(string!=null){
                    return JSON.toJSONString(value);
                }
                return value;
            }
        });
        System.out.println(parsedJSON);
    }
}
