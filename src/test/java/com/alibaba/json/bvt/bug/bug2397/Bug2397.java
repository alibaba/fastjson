package com.alibaba.json.bvt.bug.bug2397;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import junit.framework.TestCase;
import org.junit.Assert;

public class Bug2397 extends TestCase {
    public void test_for_bug(){
        String jsonStr = "{\"items\":[{\"id\":1,\"name\":\"kata\"},{\"id\":2,\"name\":\"kata2\"}],\"items2\":[{\"id\":3,\"name\":\"kata\"},{\"id\":4,\"name\":\"kata2\"}],\"itemArray\":[{\"id\":5,\"name\":\"kata\"},{\"id\":6,\"name\":\"kata2\"}]}";
        TestReply testReply = JSON.parseObject(jsonStr, new TypeReference<TestReply>() {
        });

        Assert.assertEquals(testReply.getItems().get(0).getId() , 1);
    }
}
