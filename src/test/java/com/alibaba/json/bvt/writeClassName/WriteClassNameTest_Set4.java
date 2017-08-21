package com.alibaba.json.bvt.writeClassName;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import com.alibaba.fastjson.parser.ParserConfig;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WriteClassNameTest_Set4 extends TestCase {
    protected void setUp() throws Exception {
        ParserConfig.global.addAccept("com.alibaba.json.bvt.writeClassName.WriteClassNameTest_Set4");
    }

    public void test_list() throws Exception {
        A a = new A();
        LinkedHashSet<B> set = new LinkedHashSet<B>();
        set.add(new B());
        set.add(new B1());
        a.setList(set);
        String text = JSON.toJSONString(a, SerializerFeature.WriteClassName);
        System.out.println(text);
        Assert.assertEquals("{\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest_Set4$A\",\"list\":[{\"valueB\":100},{\"@type\":\"com.alibaba.json.bvt.writeClassName.WriteClassNameTest_Set4$B1\",\"valueB\":100,\"valueB1\":200}]}",
                            text);

        A a1 = (A) JSON.parse(text);

        Assert.assertEquals(2, a1.getList().size());
        Assert.assertTrue(new ArrayList<B>(a1.getList()).get(0) instanceof B);
        Assert.assertTrue(new ArrayList<B>(a1.getList()).get(1) instanceof B1);
    }

    public static class A {

        private LinkedHashSet<B> list;

        public LinkedHashSet<B> getList() {
            return list;
        }

        public void setList(LinkedHashSet<B> list) {
            this.list = list;
        }

    }

    public static class B {
        private int valueB = 100;
        public int getValueB() {
            return valueB;
        }
    }

    public static class B1 extends B {
        private int valueB1 = 200;
        public int getValueB1() {
            return valueB1;
        }
    }
}
