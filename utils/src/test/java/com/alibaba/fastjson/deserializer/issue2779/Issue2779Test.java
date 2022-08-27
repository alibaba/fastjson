package com.alibaba.fastjson.deserializer.issue2779;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

// https://github.com/alibaba/fastjson/issues/2779
public class Issue2779Test {
    @Test
    public void canDeserializeLargeJavaBean() {
        JSON.parseObject("{}", LargeJavaBean.class);
    }
}
