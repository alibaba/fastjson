package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue1474 extends TestCase {
    public void test_for_issue() throws Exception {
        Map<String,Object> extraData = new HashMap<String,Object>();
        extraData.put("ext_1", null);
        extraData.put("ext_2", null);

        People p = new People();
        p.setId("001");
        p.setName("顾客");
        p.setExtraData(extraData);

        assertEquals("{\"id\":\"001\",\"name\":\"顾客\"}", JSON.toJSONString(p));
    }

    @JSONType(asm = false)
    static class People{
        private String name;
        private String id;
        @JSONField(unwrapped=true)
        private Object extraData;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public Object getExtraData() {
            return extraData;
        }
        public void setExtraData(Object extraData) {
            this.extraData = extraData;
        }
    }
}
