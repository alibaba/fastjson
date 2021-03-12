package com.alibaba.fastjson.deserializer.issues3671;

import com.alibaba.fastjson.JSONValidator;
import org.junit.Assert;
import org.junit.Test;

/**
 * https://github.com/alibaba/fastjson/issues/3671
 *
 * @author ryc
 * @date 2021/03/12 15:40
 */
public class TestIssue3671 {

    @Test
    public void testIssue3671() {
        String json = "[{\n" +
                "    \"filters\": [],\n" +
                "    \"id\": \"baidu_route2\",\n" +
                "    \"order\": 0,\n" +
                "    \"predicates\": [{\n" +
                "        \"args\": {\n" +
                "            \"pattern\": \"/baidu/**\"\n" +
                "        },\n" +
                "        \"name\": \"Path\"\n" +
                "    }],\n" +
                "    \"uri\": \"https://www.baidu.com\"\n" +
                "}]\n ";
        Assert.assertTrue(JSONValidator.from(json).validate());
    }

}
