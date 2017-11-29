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

import org.junit.Assert;

import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;

public class DupSetterTest6 extends TestCase {

    public void testDupSetter() {
        VO vo = new VO();
        vo.status = 3;
        String json = JSONObject.toJSONString(vo);
        VO vo2 = JSONObject.parseObject(json, VO.class);
        Assert.assertEquals(3, vo2.status3);
    }

    public static class VO {

        public int status;
        private int status2;
        private int status3;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status2 = status;
        }

        public void setStatus(String status) {
            this.status3 = Integer.parseInt(status);
        }
    }
}
