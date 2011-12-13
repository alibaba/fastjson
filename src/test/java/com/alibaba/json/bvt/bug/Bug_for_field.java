package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Bug_for_field extends TestCase {

    public void test_annotation() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        
        String text = JSON.toJSONString(vo);
        
        System.out.println(text);
    }

    public static class VO {

        @JSONField(name = "ID", serialzeFeatures={SerializerFeature.WriteClassName})
        private long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }
}
