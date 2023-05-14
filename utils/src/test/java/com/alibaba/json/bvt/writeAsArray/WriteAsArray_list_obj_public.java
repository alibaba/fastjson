package com.alibaba.json.bvt.writeAsArray;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteAsArray_list_obj_public extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("wenshao");
        vo.getValues().add(new A());

        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[123,\"wenshao\",[[0]]]", text);
        
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo.getId(), vo2.getId());
        Assert.assertEquals(vo.getName(), vo2.getName());
        Assert.assertEquals(vo.getValues().size(), vo2.getValues().size());
        Assert.assertEquals(vo.getValues().get(0).getClass(), vo2.getValues().get(0).getClass());
        Assert.assertEquals(vo.getValues().get(0).getValue(), vo2.getValues().get(0).getValue());
    }

    public static class VO {

        private long         id;
        private String       name;
        private List<A> values = new ArrayList<A>();

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<A> getValues() {
            return values;
        }

        public void setValues(List<A> values) {
            this.values = values;
        }

    }
    
    public static class A {
        private int value;

        
        public int getValue() {
            return value;
        }

        
        public void setValue(int value) {
            this.value = value;
        }
        
        
    }
}
