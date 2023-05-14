package com.alibaba.fastjson.serializer.issue3473;

import java.sql.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * package com.alibaba.fastjson.serializer.issue3473 <br/>
 * description: java.sql.Date序列化测试 <br/>
 * Copyright 2019 thunisoft, Inc. All rights reserved.
 *
 * @author fanzhongwei
 * @date 20-9-29
 */
public class SerializeWriterJavaSqlDateTest {

    private Map<String, Object> data = new HashMap<String, Object>(1, 1);

    @Before
    public void before() throws ParseException {
        data.put("sqlDate", new Date(DateUtils.parseDate("2020-09-29", "yyyy-MM-dd")
            .getTime()));
    }

    @Test
    public void yyyy_MM_dd_HH_mm_ss_test() {
        String json = JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat);
        Assert.assertEquals("{\"sqlDate\":\"2020-09-29 00:00:00\"}", json);
    }

    @Test
    public void yyyy_MM_dd_test() {
        String json = JSON.toJSONString(data);
        Assert.assertEquals("{\"sqlDate\":\"2020-09-29\"}", json);
    }
}
