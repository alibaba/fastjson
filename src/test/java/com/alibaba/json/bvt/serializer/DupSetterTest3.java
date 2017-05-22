/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-03-01 00:55 创建
 *
 */
package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import junit.framework.TestCase;

public class DupSetterTest3 extends TestCase {

    public void testEnum() {
        VO enumTest = new VO();
        enumTest.status = 3;
        String json = JSONObject.toJSONString(enumTest);
        JSONObject.parseObject(json, VO.class);
    }

    public static class VO {

        public Integer status;

        @JSONField(name = "status")
        public Integer status2;
    }
}
