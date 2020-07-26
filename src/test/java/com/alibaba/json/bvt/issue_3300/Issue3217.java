package com.alibaba.json.bvt.issue_3300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class Issue3217 extends TestCase {

    public void test_for_issue() throws Exception {
        RefAfterFilterTest refAfterFilterTest = new RefAfterFilterTest();

        List<Item> items = new ArrayList<Item>(2);
        Category category = new Category("category");
        items.add(new Item("item1",category));
        items.add(new Item("item2",category));

        System.out.println(JSON.toJSONString(items,refAfterFilterTest));
    }

    public static class RefAfterFilterTest extends AfterFilter {

        private Category category = new Category("afterFilterCategory");

        @Override
        public void writeAfter(Object object) {

            if (object instanceof Item) {

                this.writeKeyValue("afterFilterCategory", category);
                /*多加一个属性报错,原因是category是object也触发了writeAfter,当前线程变量serializer被设置为null了serializerLocal.set(null);
                 *这两个write换个顺序就不会报错
                 */
                this.writeKeyValue("afterFilterTwo", "two");

            }
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

    public static class Item {

        private String name;

        private Category category;

        private String barcode;


        public Item(String name,Category category){
            this.name = name;
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

    }
}
