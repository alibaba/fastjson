package com.alibaba.json.bvt.jsonfield;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ASMJavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class JSONFieldTest_0 extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setF0(100);
        vo.setF1(101);
        vo.setF2(102);

        String text = JSON.toJSONString(vo);
        System.out.println(text);

        Assert.assertEquals("{\"f2\":102,\"f1\":101,\"f0\":100}", text);

        VO vo_decoded = JSON.parseObject(text, VO.class);

        Assert.assertEquals(vo.f0, vo_decoded.f0);
        Assert.assertEquals(vo.f1, vo_decoded.f1);
        Assert.assertEquals(vo.f2, vo_decoded.f2);

        JavaBeanDeserializer javaBeanDeser = null;
        
        ObjectDeserializer deser = ParserConfig.getGlobalInstance().getDeserializer(VO.class);
        if (deser instanceof ASMJavaBeanDeserializer) {
            javaBeanDeser = ((ASMJavaBeanDeserializer) deser).getInnterSerializer();
        } else {
            javaBeanDeser = (JavaBeanDeserializer) deser;
        }
        
        List<FieldDeserializer> fieldDeserList = javaBeanDeser.getSortedFieldDeserializers();
        Assert.assertEquals(3, fieldDeserList.size());
        Assert.assertEquals("f2", fieldDeserList.get(0).getFieldInfo().getName());
        Assert.assertEquals("f1", fieldDeserList.get(1).getFieldInfo().getName());
        Assert.assertEquals("f0", fieldDeserList.get(2).getFieldInfo().getName());
    }

    public static class VO {

        @JSONField(ordinal = 3)
        private int f0;

        @JSONField(ordinal = 2)
        private int f1;

        @JSONField(ordinal = 1)
        private int f2;

        public int getF0() {
            return f0;
        }

        public void setF0(int f0) {
            this.f0 = f0;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getF2() {
            return f2;
        }

        public void setF2(int f2) {
            this.f2 = f2;
        }

    }
}
