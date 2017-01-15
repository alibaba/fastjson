package com.alibaba.json.bvt.naming;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

import junit.framework.TestCase;

public class NamingSerTest extends TestCase {

    public void test_snake() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person_id\":1001}", text);

        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(model.personId, model2.personId);

//        Model model3 = JSON.parseObject(text, Model.class);
//        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_kebab() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.KebabCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person-id\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.KebabCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(model.personId, model2.personId);

//        Model model3 = JSON.parseObject(text, Model.class);
//        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_pascal() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"PersonId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_camel() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"personId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void test_camel_1() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;

        Model2 model = new Model2();
        model.PersonId_1 = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"personId_1\":1001}", text);

        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        Model2 model2 = JSON.parseObject(text, Model2.class, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
        Assert.assertEquals(model.PersonId_1, model2.PersonId_1);

        Model2 model3 = JSON.parseObject(text, Model2.class);
        Assert.assertEquals(model.PersonId_1, model3.PersonId_1);
    }

    public static class Model {

        public int personId;
    }

    public static class Model2 {

        public int PersonId_1;
    }
}
