package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：Nanqi
 * @Date ：Created in 11:29 2020/8/16
 */
public class Issue3402 extends TestCase {
    public void test_for_issue() throws Exception {
        List<SimpleEnum> simpleLists = new ArrayList();
        simpleLists.add(SimpleEnum.A);
        simpleLists.add(SimpleEnum.B);

        String simpleListsString = JSON.toJSONString(simpleLists, SerializerFeature.WriteEnumUsingToString);
        List<SimpleEnum> simples = JSONObject.parseArray(simpleListsString, SimpleEnum.class);
        assertNull(simples.get(0));

        simpleListsString = JSON.toJSONString(simpleLists, SerializerFeature.WriteEnumUsingName);
        simples = JSONObject.parseArray(simpleListsString, SimpleEnum.class);
        assertNotNull(simples.get(0));

        simpleListsString = JSON.toJSONString(simpleLists);
        simples = JSONObject.parseArray(simpleListsString, SimpleEnum.class);
        assertNotNull(simples.get(0));
    }

    public void test_for_issue2() throws Exception {
        Simple simple = new Simple();
        simple.setEnum1(SimpleEnum.A);
        simple.setEnum2(SimpleEnum.B);

        assertEquals("{\"enum1\":\"Simple{name='A', unit='台'}\",\"enum2\":\"B\"}", JSON.toJSONString(simple));
    }

    public void test_for_issue3() throws Exception {
        Simple2 simple = new Simple2();
        simple.setEnum1(SimpleEnum.A);
        simple.setEnum2(SimpleEnum.B);

        assertEquals("{\"enum1\":\"Simple{name='A', unit='台'}\",\"enum2\":\"Simple{name='B', unit='人'}\"}", JSON.toJSONString(simple));
    }

    public enum SimpleEnum implements Serializable {
        A("A", "台"),
        B("B", "人"),
        ;

        private String name;
        private String unit;


        SimpleEnum(String name, String unit) {
            this.name = name;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Simple{" +
                    "name='" + name + '\'' +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }

    public class Simple implements Serializable {
        @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
        private SimpleEnum enum1;

        @JSONField(serialzeFeatures = SerializerFeature.WriteEnumUsingName)
        private SimpleEnum enum2;

        public SimpleEnum getEnum1() {
            return enum1;
        }

        public void setEnum1(SimpleEnum enum1) {
            this.enum1 = enum1;
        }

        public SimpleEnum getEnum2() {
            return enum2;
        }

        public void setEnum2(SimpleEnum enum2) {
            this.enum2 = enum2;
        }
    }

    @JSONType(serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    public class Simple2 implements Serializable {
        private SimpleEnum enum1;

        private SimpleEnum enum2;

        public SimpleEnum getEnum1() {
            return enum1;
        }

        public void setEnum1(SimpleEnum enum1) {
            this.enum1 = enum1;
        }

        public SimpleEnum getEnum2() {
            return enum2;
        }

        public void setEnum2(SimpleEnum enum2) {
            this.enum2 = enum2;
        }
    }
}
