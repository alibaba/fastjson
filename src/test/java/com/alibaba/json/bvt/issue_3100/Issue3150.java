package com.alibaba.json.bvt.issue_3100;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue3150 extends TestCase {
    public void test_for_issue() throws Exception {
        MyRefAfterFilter refAfterFilterTest = new MyRefAfterFilter();

        List<Item> items = new ArrayList<Item>(2);
        Category category = new Category("category");
        items.add(new Item("item1",category));
        items.add(new Item("item2",category));

//        System.out.println(JSON.toJSONString(items));
        System.out.println(JSON.toJSONString(items, refAfterFilterTest));

    }

    public static class MyRefAfterFilter extends AfterFilter {

        private Category category = new Category("afterFilterCategory");

        @Override
        public void writeAfter(Object object) {
            if(object instanceof  Item){
                this.writeKeyValue("afterFilterCategory", category);
            }
        }

    }

    public static class Item {

        private String name;

        private Category category;


        public Item(String name,Category category){
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public Category getcategory() {
            return category;
        }
    }

    public static class Category {

        private String name;

        public Category(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
