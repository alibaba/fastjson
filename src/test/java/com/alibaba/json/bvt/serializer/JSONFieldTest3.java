package com.alibaba.json.bvt.serializer;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

public class JSONFieldTest3 extends TestCase {
	public void test_jsonField() throws Exception {
		VO vo = new VO();

		vo.setId(123);
		vo.setFlag(true);

		String text = JSON.toJSONString(vo);
		Assert.assertEquals("{\"id\":123}", text);
	}

	public static class VO {
		private int id;

		@JSONField(serialize = false)
		private boolean _flag;
		
		@JSONField(serialize = false)
		private int _id2;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public boolean isFlag() {
			return _flag;
		}

		public void setFlag(boolean flag) {
			this._flag = flag;
		}

        
        public int getId2() {
            return _id2;
        }

        
        public void setId2(int id2) {
            this._id2 = id2;
        }
		
		
	}
}
