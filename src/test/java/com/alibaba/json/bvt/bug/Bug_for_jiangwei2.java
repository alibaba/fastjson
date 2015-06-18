package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class Bug_for_jiangwei2 extends TestCase {
	public void test_for_jiangwei() throws Exception {
//		String str = "﻿[2,'韩国篮球联赛','仁川大象(男篮)','首尔SK骑士 男篮',['大/小',3],'总进球 : 138.5 @ 0-0','','大','0.66','',1,25,200,1,0,0,'True','False',0,'','','',0,0,19819905,1,'h',145528,0]";
//		JSONArray array = JSON.parseArray(str);
		String str = "[]";
		str = "﻿[]";
		JSONArray array = JSON.parseArray(str);
	}
}
