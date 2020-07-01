package com.alibaba.json.bvt.bug;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Bug_for_rendong extends TestCase {

	public void test_0() throws Exception {
		String text = "{\"BX-20110613-1739\":{\"repairNum\":\"BX-20110613-1739\",\"set\":[{\"employNum\":\"a1027\",\"isConfirm\":false,\"isReceive\":false,\"state\":11}]},\"BX-20110613-1749\":{\"repairNum\":\"BX-20110613-1749\",\"set\":[{\"employNum\":\"a1027\",\"isConfirm\":false,\"isReceive\":true,\"state\":1}]}}";

		Map<String, TaskMobileStatusBean> map = JSON.parseObject(text,
				new TypeReference<Map<String, TaskMobileStatusBean>>() {
				});

		Assert.assertEquals(2, map.size());
		// System.out.println(JSON.toJSONString(map,
		// SerializerFeature.PrettyFormat));
	}

	public static class TaskMobileStatusBean {

		private String repairNum;

		private Set<PeopleTaskMobileStatusBean> set = new HashSet<PeopleTaskMobileStatusBean>();

		public String getRepairNum() {
			return repairNum;
		}

		public void setRepairNum(String repairNum) {
			this.repairNum = repairNum;
		}

		public Set<PeopleTaskMobileStatusBean> getSet() {
			return set;
		}

		public void setSet(Set<PeopleTaskMobileStatusBean> set) {
			this.set = set;
		}

	}

	public static class PeopleTaskMobileStatusBean {

		private String employNum;
		private Boolean isConfirm;
		private Boolean isReceive;
		private int state;

		public String getEmployNum() {
			return employNum;
		}

		public void setEmployNum(String employNum) {
			this.employNum = employNum;
		}

		public Boolean getIsConfirm() {
			return isConfirm;
		}

		public void setIsConfirm(Boolean isConfirm) {
			this.isConfirm = isConfirm;
		}

		public Boolean getIsReceive() {
			return isReceive;
		}

		public void setIsReceive(Boolean isReceive) {
			this.isReceive = isReceive;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

	}

}
