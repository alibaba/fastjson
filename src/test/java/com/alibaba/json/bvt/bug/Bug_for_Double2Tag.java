package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_Double2Tag extends TestCase {
	public void test_double() throws Exception {
		Double2Tag tag = new Double2Tag();
		String str = JSON.toJSONString(tag);
		JSON.parseObject(str, Double2Tag.class);
	}
	
	public static class Double2Tag {
		public String data_time;
		public String data_id;
		public String hour_id;
		public String minute_id;
		public String tag3_id;
		public double ali_fee;
		public double total_ali_fee;
		public long seller_cnt;

		public Double2Tag() {
			ali_fee = 0.0;
			total_ali_fee = 0.0;
			seller_cnt = 0;
		}

		public String getData_time() {
			return data_time;
		}

		public void setData_time(String data_time) {
			this.data_time = data_time;
		}

		public String getData_id() {
			return data_id;
		}

		public void setData_id(String data_id) {
			this.data_id = data_id;
		}

		public String getHour_id() {
			return hour_id;
		}

		public void setHour_id(String hour_id) {
			this.hour_id = hour_id;
		}

		public String getMinute_id() {
			return minute_id;
		}

		public void setMinute_id(String minute_id) {
			this.minute_id = minute_id;
		}

		public String getTag3_id() {
			return tag3_id;
		}

		public void setTag3_id(String tag3_id) {
			this.tag3_id = tag3_id;
		}

		public double getAli_fee() {
			return ali_fee;
		}

		public void setAli_fee(double ali_fee) {
			this.ali_fee = ali_fee;
		}

		public double getTotal_ali_fee() {
			return total_ali_fee;
		}

		public void setTotal_ali_fee(double total_ali_fee) {
			this.total_ali_fee = total_ali_fee;
		}

		public long getSeller_cnt() {
			return seller_cnt;
		}

		public void setSeller_cnt(long seller_cnt) {
			this.seller_cnt = seller_cnt;
		}

	}
}
