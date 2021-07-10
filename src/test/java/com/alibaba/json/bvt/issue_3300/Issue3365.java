package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Issue3365 {
    public JSONObject init_json() {
        // 第三层的数据
        Map<String, Object> layer3Ele1 = new HashMap<String, Object>();
        Map<String, Object> layer3Ele2 = new LinkedHashMap<String, Object>();
        // 第二层的数据
        List<Object> layer2Ele = new ArrayList<Object>();
        layer2Ele.add(layer3Ele1);
        layer2Ele.add(layer3Ele2);
        // 第一层的数据
        JSONObject layer1Ele = new JSONObject();
        layer1Ele.put("layer2Ele", layer2Ele);
        return layer1Ele;
    }
    public JSONArray init_json_4_layer(){
        JSONObject layer1 = init_json();
        JSONArray will_return = new JSONArray();
        will_return.add(layer1);
        return will_return;//
    }
    @Test
    public void test_for_issue() {
        JSONObject iJson = init_json();

        JSONArray jsonObject = iJson.getJSONArray("layer2Ele");
        //1.2.46 遍历时iJarray存储的元素类型是 JSONObject
        //1.2.68 遍历时iJarray存储的元素类型是 JSONArray
        for (Object o : jsonObject) {
            System.out.println(o instanceof JSONArray);
            System.out.println(o instanceof JSONObject);
            System.out.println(o.getClass().getName());
            System.out.println(o);
        }
        for (Object ele : jsonObject) {
            JSONObject ele_object = (JSONObject) ele;
            System.out.println(ele_object.toString());
        }
    }
    @Test
    public void test_for_deeper_layer() {
        // CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/3365
        JSONArray layer0 = init_json_4_layer();
        JSONObject iJson = (JSONObject) layer0.get(0);
        JSONArray jsonObject = iJson.getJSONArray("layer2Ele");
        //1.2.46 遍历时iJarray存储的元素类型是 JSONObject
        //1.2.68 遍历时iJarray存储的元素类型是 JSONArray
        for (Object o : jsonObject) {
            System.out.println(o instanceof JSONArray);
            System.out.println(o instanceof JSONObject);
            System.out.println(o.getClass().getName());
            System.out.println(o);
        }
        for (Object ele : jsonObject) {
            JSONObject ele_object = (JSONObject) ele;
            System.out.println(ele_object.toString());
        }
    }
}