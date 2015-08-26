/*
 * Copyright 2015 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.json.bvt.bug;

import java.text.DateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class Bug_376_for_iso8601 extends TestCase {

    public void test_fix() {

        String s = "{date: \"2015-07-22T19:13:42Z\"}";
        String s2 = "{date: \"2015-07-22T19:13:42.000Z\"}";

        MyObj o = JSON.parseObject(s, MyObj.class, Feature.AllowISO8601DateFormat);
        MyObj o2 = JSON.parseObject(s2, MyObj.class, Feature.AllowISO8601DateFormat);

        System.out.println(DateFormat.getDateTimeInstance().format(o.getDate()));
        System.out.println(DateFormat.getDateTimeInstance().format(o2.getDate()));

        // 修复之前输出
        // 2015-7-22 19:13:42
        // 2015-7-23 3:13:42

        // 修复之后输出
        // 2015-7-23 3:13:42
        // 2015-7-23 3:13:42

    }

    static class MyObj {

        private Date date;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

}
