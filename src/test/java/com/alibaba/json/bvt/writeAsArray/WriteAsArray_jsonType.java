package com.alibaba.json.bvt.writeAsArray;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_jsonType extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("wenshao");
        
        Parent parent = new Parent();
        parent.setVo(vo);

        String text = JSON.toJSONString(parent);
        Assert.assertEquals("{\"vo\":[123,\"wenshao\"]}", text);
        VO vo2 = JSON.parseObject(text, Parent.class).getVo();
        Assert.assertEquals(vo.getId(), vo2.getId());
        Assert.assertEquals(vo.getName(), vo2.getName());
    }

    public static class Parent {
        private VO vo;

        public VO getVo() {
            return vo;
        }

        public void setVo(VO vo) {
            this.vo = vo;
        }

    }

    @JSONType(serialzeFeatures=SerializerFeature.BeanToArray, parseFeatures=Feature.SupportArrayToBean)
    public static class VO {
        @JSONField(ordinal=1)
        private int    id;
        
        @JSONField(ordinal=2)
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
