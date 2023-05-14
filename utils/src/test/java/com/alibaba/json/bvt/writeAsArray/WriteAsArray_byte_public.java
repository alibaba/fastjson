package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_byte_public extends TestCase {
    public void test_0 () throws Exception {
        VO vo = new VO();
        vo.setId((byte)123);
        vo.setName("wenshao");
        
        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[123,\"wenshao\"]", text);
    }
    
    public static class VO {
        private byte id;
        private String name;

        public byte getId() {
            return id;
        }

        public void setId(byte id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
