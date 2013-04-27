package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.Bean;
import com.alibaba.json.bvtVO.Page;

import junit.framework.TestCase;


public class Bug_for_jial10802 extends TestCase {
    public void test_for_jial10802() throws Exception {
        Page<Bean> page = new Page<Bean>();
        page.setCount(1);
        List<Bean> items = new ArrayList<Bean>();
        Bean item = new Bean();
        item.setId(1);
        item.setName("name");
        item.setDesc("desc");
        items.add(item);
        page.setItems(items);
        String json = JSON.toJSONString(page, SerializerFeature.PrettyFormat);

        Page<Bean> jsonPage = JSON.parseObject(json, new TypeReference<Page<Bean>>() {
        });
        System.out.println(jsonPage.getItems().get(0).getName());
    }
}
