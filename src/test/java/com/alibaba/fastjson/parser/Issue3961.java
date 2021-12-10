package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.JSONScanner;
import com.diffblue.deeptestutils.Reflector;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Issue3961 {
    private String string;

    /**
     *
     */
    @Test
    public void testDouble() throws Throwable {
        ArrayList<Map<String, Object>> list = new ArrayList<>();

        HashMap<String, Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age","20");
        System.out.println(map);
        list.add(map);
        System.out.println(map);
        list.add(map);
        System.out.println(map);
        list.add(map);

        System.out.println(list);
//         //start of new method
//        String s = "[";
//        for (int i = 0; i < list.size(); i++) {
//            s += "{";
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                 s +="\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",";
//            }
//            s = s.substring(0, s.length() - 1);
//            s += i == list.size() -1 ? "}" : "}, ";
//        }
//        s += "]";
//       // end of new method (return s;)
//        System.out.println(s);

//        Object o = JSONObject.parse("[{\"name\":\"张三\",\"age\":\"20\"},{\"name\":\"张三\",\"age\":\"20\"}]");
//        System.out.println(o);
//        String jsonString = JSON.toJSONString(o);//the root cause of the issue, create toJSONStringFromList(list)
//        // [{"name":"张三","age":"20"},{"name":"张三","age":"20"},{"$ref":"$[0]"}]
//        System.out.print(jsonString);
//        List<Map<String, Object>> list1 = JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
//        });
//        Map<String, Object> map2 = list1.get(1);
//        for (Map.Entry<String, Object> requestParam : map2.entrySet()) {
//            // key:$ref value:$[0]
//            System.out.print("key:"+requestParam.getKey()+" value:"+requestParam.getValue());
//        }

       // Assert.assertEquals("{\"rank\":2.4444444E7}", JSON.toJSONStringScientificNotation(jo1, "rank"));
    }

}



