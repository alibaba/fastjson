package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Issue69 extends TestCase {

    public void test_for_issue() throws Exception {
        VO vo = new VO();
        vo.a = new Entry();
        vo.b = vo.a;
        
        String text = JSON.toJSONString(vo);
        System.out.println(text);
    }

    @JSONType(serialzeFeatures={SerializerFeature.DisableCircularReferenceDetect})
    public static class VO {

        private Entry a;

        private Entry b;

        public Entry getA() {
            return a;
        }

        public void setA(Entry a) {
            this.a = a;
        }

        public Entry getB() {
            return b;
        }

        public void setB(Entry b) {
            this.b = b;
        }

    }

    public static class Entry {
        private int id;

        
        public int getId() {
            return id;
        }

        
        public void setId(int id) {
            this.id = id;
        }
        
        
    }
}
