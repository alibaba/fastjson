package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.OfferRankResultVO;

public class Bug_for_vikingschow extends TestCase {
	public void test_for_vikingschow() throws Exception {
		OfferRankResultVO vo = new OfferRankResultVO();
		String text = JSON.toJSONString(vo);
		JSON.parseObject(text, OfferRankResultVO.class);
	}
}
