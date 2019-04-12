package com.alibaba.json.bvt.compatible.jsonlib;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CompatibleTest0 extends TestCase {

    protected void setUp() throws Exception {
        System.out.println();
    }

    public void test_0() throws Exception {
        Map<String, Object> obj = new HashMap<String, Object>();
        assertEquals(toCompatibleJSONString(obj), toJSONLibString(obj));
    }

    public void test_1() throws Exception {
        VO vo = new VO();
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_2() throws Exception {
        V1 vo = new V1();
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    // {"media":{"size":58982400,"format":"video/mpg4","uri":"http://javaone.com/keynote.mpg","title":"Javaone Keynote","width":640,"height":480,"duration":18000000,"bitrate":262144,"persons":["Bill Gates","Steve Jobs"],"player":"JAVA"}{"images":[{"size":"LARGE","uri":"http://javaone.com/keynote_large.jpg","title":"Javaone Keynote","width":1024,"height":768},{"size":"SMALL","uri":"http://javaone.com/keynote_small.jpg","title":"Javaone Keynote","width":320,"height":240}]}

    public void test_3() throws Exception {
        V1 vo = new V1();
        vo.setDate(new Date());
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_4() throws Exception {
        V1 vo = new V1();
        vo.setF2('中');
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_5() throws Exception {
        V2 vo = new V2();
        vo.setF1(0.2f);
        vo.setF2(33.3);
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_6() throws Exception {
        V2 vo = new V2();
        vo.setF1(0.1f);
        vo.setF2(33.3);
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_7() throws Exception {
        V2 vo = new V2();
        vo.setF2(0.1D);
        vo.setF1(33.3f);
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_8() throws Exception {
        V3 vo = new V3();
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_9() throws Exception {
        V4 vo = new V4();
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_10() throws Exception {
        Object vo = null;
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public void test_11() throws Exception {
        Object vo = new HashMap();
        assertEquals(toCompatibleJSONString(vo), toJSONLibString(vo));
    }

    public static void assertEquals(String fastJSON, String jsonLib) {
        System.out.println("fastjson: " + fastJSON);
        System.out.println("json-lib: " + jsonLib);
        Assert.assertEquals(JSON.parse(fastJSON), JSON.parse(jsonLib));
    }

private static final SerializeConfig   mapping;
static {
    mapping = new SerializeConfig();
    mapping.put(Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
}

private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
                                                  };

// 序列化为和JSON-LIB兼容的字符串
public static String toCompatibleJSONString(Object object) {

    return JSON.toJSONString(object, mapping, features);
}

    public static String toJSONLibString(Object object) {
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(object);
        return obj.toString();
    }

    public static class V4 {

        private Map<String, Object> items;

        public Map<String, Object> getItems() {
            return items;
        }

        public void setItems(Map<String, Object> items) {
            this.items = items;
        }

    }

    public static class V3 {

        private List<String> items;

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

    }

    public static class V2 {

        private float  f1;
        private double f2;

        private Float  f3;
        private Double f4;

        public float getF1() {
            return f1;
        }

        public void setF1(float f1) {
            this.f1 = f1;
        }

        public double getF2() {
            return f2;
        }

        public void setF2(double f2) {
            this.f2 = f2;
        }

        public Float getF3() {
            return f3;
        }

        public void setF3(Float f3) {
            this.f3 = f3;
        }

        public Double getF4() {
            return f4;
        }

        public void setF4(Double f4) {
            this.f4 = f4;
        }

    }

    public static class V1 {

        private Boolean   f1;
        private Character f2;
        private String    f3;
        private Date      date;

        private boolean   f4;
        private char      f5;

        public Boolean getF1() {
            return f1;
        }

        public void setF1(Boolean f1) {
            this.f1 = f1;
        }

        public Character getF2() {
            return f2;
        }

        public void setF2(Character f2) {
            this.f2 = f2;
        }

        public String getF3() {
            return f3;
        }

        public void setF3(String f3) {
            this.f3 = f3;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public boolean isF4() {
            return f4;
        }

        public void setF4(boolean f4) {
            this.f4 = f4;
        }

        public char getF5() {
            return f5;
        }

        public void setF5(char f5) {
            this.f5 = f5;
        }

    }

    public static class VO {

        private int          id;
        private String       name;
        private BigDecimal   salary;
        private List<String> items;

        private Byte         f1;
        private Short        f2;
        private Integer      f3;
        private Long         f4;
        private BigInteger   f5;
        private BigDecimal   f6;

        private byte         f7;
        private short        f8;
        private int          f9;
        private long         f10;

        public Byte getF1() {
            return f1;
        }

        public void setF1(Byte f1) {
            this.f1 = f1;
        }

        public Short getF2() {
            return f2;
        }

        public void setF2(Short f2) {
            this.f2 = f2;
        }

        public Integer getF3() {
            return f3;
        }

        public void setF3(Integer f3) {
            this.f3 = f3;
        }

        public Long getF4() {
            return f4;
        }

        public void setF4(Long f4) {
            this.f4 = f4;
        }

        public BigInteger getF5() {
            return f5;
        }

        public void setF5(BigInteger f5) {
            this.f5 = f5;
        }

        public BigDecimal getF6() {
            return f6;
        }

        public void setF6(BigDecimal f6) {
            this.f6 = f6;
        }

        public byte getF7() {
            return f7;
        }

        public void setF7(byte f7) {
            this.f7 = f7;
        }

        public short getF8() {
            return f8;
        }

        public void setF8(short f8) {
            this.f8 = f8;
        }

        public int getF9() {
            return f9;
        }

        public void setF9(int f9) {
            this.f9 = f9;
        }

        public long getF10() {
            return f10;
        }

        public void setF10(long f10) {
            this.f10 = f10;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

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

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

    }
}
