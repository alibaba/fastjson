package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class JSONFeidDemo2 extends TestCase {

	public void test_0() throws Exception {
		Z_OA_MM_PR_INFO_IN in = new Z_OA_MM_PR_INFO_IN();
		in.setIM_PREQ_NO("111111");
		TB_PR_INFO t1 = new TB_PR_INFO("t1");
		TB_PR_INFO t2 = new TB_PR_INFO("t2");
		List<TB_PR_INFO> tb_pr_infos = new ArrayList<TB_PR_INFO>();
		tb_pr_infos.add(t1);
		tb_pr_infos.add(t2);
		in.setTB_PR_INFO(tb_pr_infos);

		String text = JSON.toJSONString(in);
		System.out.println(text);

		assertEquals(
				"{\"IM_PREQ_NO\":\"111111\",\"TB_PR_INFO\":[{\"PREQ_NO\":\"t1\"},{\"PREQ_NO\":\"t2\"}]}",
				text);

	}

	public void test_1() throws Exception {
		String text = "{\"IM_PREQ_NO\":\"111111\",\"TB_PR_INFO\":[{\"pREQ_NO\":\"t1\"},{\"pREQ_NO\":\"t2\"}]}";
		Z_OA_MM_PR_INFO_IN in = JSON
				.parseObject(text, Z_OA_MM_PR_INFO_IN.class);
		assertEquals("111111", in.getIM_PREQ_NO());
		assertNotNull(in.getTB_PR_INFO());

	}

	public static class Z_OA_MM_PR_INFO_IN {
		@JSONField(name = "IM_PREQ_NO")
		private String IM_PREQ_NO;
		@JSONField(name = "TB_PR_INFO")
		private List<TB_PR_INFO> TB_PR_INFO;

		public List<JSONFeidDemo2.TB_PR_INFO> getTB_PR_INFO() {
			return TB_PR_INFO;
		}

		public void setTB_PR_INFO(List<JSONFeidDemo2.TB_PR_INFO> TB_PR_INFO) {
			this.TB_PR_INFO = TB_PR_INFO;
		}

		public String getIM_PREQ_NO() {
			return IM_PREQ_NO;
		}

		public void setIM_PREQ_NO(String IM_PREQ_NO) {
			this.IM_PREQ_NO = IM_PREQ_NO;
		}
	}

	public static class TB_PR_INFO {
		@JSONField(name = "PREQ_NO")
		private String PREQ_NO;

		public TB_PR_INFO() {
		}

		public TB_PR_INFO(String PREQ_NO) {
			this.PREQ_NO = PREQ_NO;
		}
		@JSONField(name = "PREQ_NO")
		public String getPREQ_NO() {
			return PREQ_NO;
		}

		public void setPREQ_NO(String PREQ_NO) {
			this.PREQ_NO = PREQ_NO;
		}
	}
}
