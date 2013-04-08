package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_Jay_1 extends TestCase {
	public void test_bug() throws Exception {
		JSON.parseObject("{\"body\":{\"coupons\":[{\"couponTypeId\":\"81c07c7c-7b88-4f5c-9d1e-e6f16e2ae36d\",\"editor\":\"ADMIN\",\"organizationPartyId\":\"00\",\"statusId\":\"COUPON_CREATED\",\"editorName\":\"超级管理员\",\"couponCode\":\"02\",\"creatorName\":\"超级管理员\",\"id\":\"d686bc04-a9d5-4f84-977a-8bfbb4fa9fe3\",\"fromDate\":\"2013-03-11 00:00:00\",\"creator\":\"ADMIN\",\"displayName\":\"02\",\"createTime\":\"2013-03-12 13:14:05\",\"updateTime\":\"2013-03-12 13:14:05\",\"organizationName\":\"X、X\"}],\"event\":\"activate\"}}");
	}
}
