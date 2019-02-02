package com.alibaba.json.bvt.naming;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

import junit.framework.TestCase;

public class PropertyNamingStrategyTest extends TestCase {

    public void testSnakeCase() {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person_id\":1001}", text);

        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void testKebabCase() {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.KebabCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person-id\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.KebabCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void testLowerCaseWithDots() {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.LowerCaseWithDots;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"person.id\":1001}", text);

        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.LowerCaseWithDots;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void testPascalCase() {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"PersonId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public void testCamelCase() {
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;

        Model model = new Model();
        model.personId = 1001;
        String text = JSON.toJSONString(model, config);
        Assert.assertEquals("{\"personId\":1001}", text);
        
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        Model model2 = JSON.parseObject(text, Model.class, parserConfig);
        Assert.assertEquals(model.personId, model2.personId);

        Model model3 = JSON.parseObject(text, Model.class);
        Assert.assertEquals(model.personId, model3.personId);
    }

    public static class Model {

        public int personId;
    }
}
