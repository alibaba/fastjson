package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_101_for_rongganlin_case2 extends TestCase {

    public void test_for_bug() throws Exception {
        Structure structure = new Structure();
        List<Group> groups = new ArrayList<Group>();

        List<Element> elemA = new ArrayList<Element>();
        elemA.add(new Text().set("t.a", "v.t.a"));
        elemA.add(new Image().set("i.a", "v.i.a"));
        groups.add(new Group().set(elemA));

        List<Element> elemB = new ArrayList<Element>();
        elemB.add(new Text().set("t.b", "v.t.b"));
        elemB.add(new Image().set("i.b", "v.i.b"));
        groups.add(new Group().set(elemB));

        structure.groups = groups;

        String text = JSON.toJSONString(structure);
        System.out.println(text);
        Structure structure2 = JSON.parseObject(text, Structure.class);
        
        Assert.assertEquals(structure.groups.size(), structure2.groups.size());
        System.out.println(JSON.toJSONString(structure2));
    }

    public static class Structure {

        public List<Group> groups;
    }

    public static class Group implements Object {

        public List<Element> elements;

        public Group set(List<Element> items) {
            this.elements = items;
            return this;
        }
    }

    public static interface Object {
    }

    public static abstract class Element {

        public String key, value;

        public Element set(String key, String value) {
            this.key = key;
            this.value = value;
            return this;
        }
    }

    public static class Text extends Element {
    }

    public static class Image extends Element {
    }
}
