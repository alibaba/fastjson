package com.alibaba.json.bvt.writeAsArray;

import java.io.StringReader;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvt.writeAsArray.WriteAsArray_int_public.VO;

import junit.framework.TestCase;

public class WriteAsArray_long_public extends TestCase {

    public void test_0() throws Exception {
        VO vo = new VO();
        vo.setId(123);
        vo.setName("wenshao");

        String text = JSON.toJSONString(vo, SerializerFeature.BeanToArray);
        Assert.assertEquals("[123,\"wenshao\"]", text);
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(vo.getId(), vo2.getId());
        Assert.assertEquals(vo.getName(), vo2.getName());
    }
    
    public void test_1() throws Exception {
        String text = "[123 ,\"wenshao\"]";
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(123, vo2.getId());
        Assert.assertEquals("wenshao", vo2.getName());
    }
    
    public void test_2() throws Exception {
        String text = "[-123 ,\"wenshao\"]";
        VO vo2 = JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        Assert.assertEquals(-123, vo2.getId());
        Assert.assertEquals("wenshao", vo2.getName());
    }
    
    public void test_1_stream() throws Exception {
        String text = "[123 ,\"wenshao\"]";
        JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
        VO vo2 = reader.readObject(VO.class);
        Assert.assertEquals(123, vo2.getId());
        Assert.assertEquals("wenshao", vo2.getName());
    }
    
    public void test_2_stream() throws Exception {
        String text = "[-123 ,\"wenshao\"]";
        JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
        VO vo2 = reader.readObject(VO.class);
        Assert.assertEquals(-123, vo2.getId());
        Assert.assertEquals("wenshao", vo2.getName());
    }

    public void test_error() throws Exception {
        String text = "[123.,\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_stream() throws Exception {
        String text = "[123.,\"wenshao\"]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_1() throws Exception {
        String text = "[123:\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_stream_1() throws Exception {
        String text = "[123:\"wenshao\" ]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    
    public void test_error_2() throws Exception {
        String text = "[-123:\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_stream_2() throws Exception {
        String text = "[-123:\"wenshao\" ]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_overflow() throws Exception {
        String text = "[2147483649:\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_overflow_stream() throws Exception {
        String text = "[2147483649:\"wenshao\" ]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_value_notmatch() throws Exception {
        String text = "[true,\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_value_notmatch_stream() throws Exception {
        String text = "[true,\"wenshao\"]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_value_notmatch_2() throws Exception {
        String text = "[+,\"wenshao\"]";
        Exception error = null;
        try {
            JSON.parseObject(text, VO.class, Feature.SupportArrayToBean);
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }
    
    public void test_error_value_notmatch_2_stream() throws Exception {
        String text = "[+,\"wenshao\"]";
        Exception error = null;
        try {
            JSONReader reader = new JSONReader(new StringReader(text), Feature.SupportArrayToBean);
            reader.readObject(VO.class);
            reader.close();
        } catch (JSONException ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public static class VO {

        private long   id;
        private String name;

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
    }
}
