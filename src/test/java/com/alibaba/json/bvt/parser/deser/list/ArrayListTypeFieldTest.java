package com.alibaba.json.bvt.parser.deser.list;

import java.util.ArrayList;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.Feature;

public class ArrayListTypeFieldTest extends TestCase {

    public void test_0() throws Exception {
        Entity entity = JSON.parseObject("{,,,list:[,,,{value:3}]}", Entity.class);
        Assert.assertEquals(3, entity.getList().get(0).getValue());
    }

    public void test_1() throws Exception {
        Entity entity = JSON.parseObject("{list:[{value:3}]}", Entity.class, 0, Feature.AllowUnQuotedFieldNames);
        Assert.assertEquals(3, entity.getList().get(0).getValue());
    }
    
    public void test_null() throws Exception {
        Entity entity = JSON.parseObject("{list:null}", Entity.class, 0, Feature.AllowUnQuotedFieldNames);
        Assert.assertEquals(null, entity.getList());
    }
    public void test_null2() throws Exception {
        Entity entity = JSON.parseObject("{list:[null]}", Entity.class, 0, Feature.AllowUnQuotedFieldNames);
        Assert.assertEquals(null, entity.getList().get(0));
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{list:{{value:3}]}", Entity.class, 0, Feature.AllowUnQuotedFieldNames);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    private static class Entity {

        private ArrayList<VO> list;

        public ArrayList<VO> getList() {
            return list;
        }

        public void setList(ArrayList<VO> list) {
            this.list = list;
        }

    }

    public static class VO {

        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
