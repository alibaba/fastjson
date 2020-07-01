package com.alibaba.fastjson.deserializer.issue2711;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Test;

public class TestIssue {
    @Test
    public void testDeserializeGenericsUnwrapped() {
        PageRequest<User> req = new PageRequest<User>();
        req.setData(new User(1L, "jack"));
        req.setFrom(10);
        req.setSize(20);
        String s = JSON.toJSONString(req);
        System.out.println(s);

        PageRequest<User> newReq = JSON.parseObject(s, new TypeReference<PageRequest<User>>() {});
        Assert.assertNotNull(newReq);
    }
}