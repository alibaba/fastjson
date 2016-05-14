package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Bug_for_issue_630 extends TestCase {

    public void test_for_issue_null() throws Exception {
        Model model = new Model();
        model.id = 123;
        model.name = null;
        model.modelName = null;
        model.isFlay = false;
//        model.persons = new ArrayList<Person>();
//        model.persons.add(new Person());
        
        String str = JSON.toJSONString(model, SerializerFeature.BeanToArray);
//        System.out.println(str);
        JSON.parseObject(str, Model.class, Feature.SupportArrayToBean);
    }
    
    public void test_for_issue_empty() throws Exception {
        Model model = new Model();
        model.id = 123;
        model.name = null;
        model.modelName = null;
        model.isFlay = false;
        model.persons = new ArrayList<Person>();
//        model.persons.add(new Person());
        
        String str = JSON.toJSONString(model, SerializerFeature.BeanToArray);
//        System.out.println(str);
        JSON.parseObject(str, Model.class, Feature.SupportArrayToBean);
    }
    
    public void test_for_issue_one() throws Exception {
        Model model = new Model();
        model.id = 123;
        model.name = null;
        model.modelName = null;
        model.isFlay = false;
        model.persons = new ArrayList<Person>();
        model.persons.add(new Person());
        
        String str = JSON.toJSONString(model, SerializerFeature.BeanToArray);
//        System.out.println(str);
        JSON.parseObject(str, Model.class, Feature.SupportArrayToBean);
    }

    public static class Model {

        public int     id;
        public String  name;
        public String  modelName;
        public boolean isFlay;
        public List<Person>    persons;// = new ArrayList<Person>();
    }

    public static class Person {

        public int    id;
        public String name;
    }
}
