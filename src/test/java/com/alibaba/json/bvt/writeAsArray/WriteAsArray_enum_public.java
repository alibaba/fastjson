package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_enum_public extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(Type.AA);
        vo.setName("wenshao");

        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[\"AA\",\"wenshao\"]", text);
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo.getId(), vo2.getId());
        Assert.assertEquals(vo.getName(), vo2.getName());
    }

    public static class VO {

        private Type   id;
        private String name;

        public Type getId() {
            return id;
        }

        public void setId(Type id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static enum Type {
        A, B, C, D, AA, BB, CC
    }
}
