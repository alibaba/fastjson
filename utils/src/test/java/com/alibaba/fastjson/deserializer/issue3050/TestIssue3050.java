package com.alibaba.fastjson.deserializer.issue3050;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.deserializer.issue3050.beans.Person;
import com.alibaba.fastjson.parser.Feature;
import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/alibaba/fastjson/issues/3050
 *
 * @author yangy
 * @since 2020年05月03日
 */
public class TestIssue3050 {

    @Test
    public void testIssue3050() {
        String jsonStr = "{\"name\":5, \"address\":\"beijing\", \"id\":\"100\", \"age\":10}";
        Person person = JSON.parseObject(jsonStr, Person.class, Feature.InitStringFieldAsEmpty);
        Assert.assertEquals("5", person.getName());
        Assert.assertEquals("beijing", person.getAddress());
        Assert.assertEquals("100", person.getId());
        Assert.assertEquals(10, person.getAge());
    }

}
