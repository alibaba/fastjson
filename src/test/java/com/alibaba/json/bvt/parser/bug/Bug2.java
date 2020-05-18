package com.alibaba.json.bvt.parser.bug;

import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Bug2 extends TestCase {

    public void test_0() throws Exception {
        String text = "{children:[{id:3}]}";
        Page page = JSON.parseObject(text, Page.class);
        Assert.assertEquals(1, page.getChildren().size());

        Assert.assertEquals(JSONObject.class, page.getChildren().get(0).getClass());
    }

    public void test_1() throws Exception {
        String text = "{children:['aa']}";
        Page page = JSON.parseObject(text, Page.class);
        Assert.assertEquals(1, page.getChildren().size());

        Assert.assertEquals(String.class, page.getChildren().get(0).getClass());
    }

    public static class Page<T> {

        private List<T> children;

        public List<T> getChildren() {
            return children;
        }

        public void setChildren(List<T> children) {
            this.children = children;
        }

    }

    public static class Result {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
