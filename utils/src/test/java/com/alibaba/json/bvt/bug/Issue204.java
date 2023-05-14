package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;

public class Issue204 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        
        SerializeFilter filter = null;
        JSON.toJSONString(vo, SerializeConfig.getGlobalInstance(), filter);
        JSON.toJSONString(vo, SerializeConfig.getGlobalInstance(), new SerializeFilter[0]);
    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
