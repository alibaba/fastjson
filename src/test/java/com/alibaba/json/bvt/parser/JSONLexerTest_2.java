package com.alibaba.json.bvt.parser;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;

public class JSONLexerTest_2 extends TestCase {

    public void test_0() throws Exception {
        VO vo = (VO) JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO\"}", VO.class);
        Assert.assertNotNull(vo);
    }

    public void test_1() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO1\"}", VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_2() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$A\"}", VO.class);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_a() throws Exception {
        P a = JSON.parseObject("{\"vo\":{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO\"}}", P.class);
        Assert.assertNotNull(a);
    }

    public void test_list() throws Exception {
        List<VO> list = JSON.parseObject("[{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO\"}]",
                                         new TypeReference<List<VO>>() {
                                         });
        Assert.assertNotNull(list);
        Assert.assertNotNull(list.get(0));
    }

    public void test_list_2() throws Exception {
        List<VO> list = JSON.parseObject("[{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO\"},{}]",
                                         new TypeReference<List<VO>>() {
                                         });
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());
        Assert.assertNotNull(list.get(0));
        Assert.assertNotNull(list.get(1));
    }

    public void test_error() throws Exception {
        Exception error = null;
        try {
            JSON.parseObject("[{\"@type\":\"com.alibaba.json.bvt.parser.JSONLexerTest_2$VO\"}[]",
                             new TypeReference<List<VO>>() {
                             });
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class P {

        private VO vo;

        public VO getVo() {
            return vo;
        }

        public void setVo(VO vo) {
            this.vo = vo;
        }

    }

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    public static class VO1 {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class A {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
