package com.alibaba.json.bvt.serializer.date;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.Date;

public class DateTest4 extends TestCase {

    public void test_date() throws Exception {
        assertNotNull(
                JSON.parseObject(
                        "{\"gmtCreate\":\"1970-01-01 00:00:00\"}"
                        , VO.class)
                        .gmtCreate
        );

        assertNotNull(
                JSON.parseObject(
                        "{\"gmtCreate\":\"1970-01-01 00:00:00.000\"}"
                        , VO.class)
                        .gmtCreate
        );

        assertNotNull(
                JSON.parseObject(
                        "{\"gmtCreate\":\"1960-01-01 00:00:00.000\"}"
                        , VO.class)
                        .gmtCreate
        );
    }

    public static class VO {
        public Date gmtCreate;
    }
}
