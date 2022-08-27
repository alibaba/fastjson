package com.alibaba.json.bvtVO;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Main {
    public static void main(String[] args) {
        Page<Bean> page = new Page<Bean>();
        page.setCount(1);
        List<Bean> items = new ArrayList<Bean>();
        Bean item = new Bean();
        item.setId(1);
        item.setName("name");
        item.setDesc("desc");
        items.add(item);
        page.setItems(items);
        String json = JSON.toJSONString(page);

        Page<Bean> jsonPage = JSON.parseObject(json, new TypeReference<Page<Bean>>() {
        });
        System.out.println(jsonPage.getItems().get(0).getName());
    }
}
