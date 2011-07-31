package com.alibaba.json.test.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class TestEntity {

    private boolean       f1;
    private Boolean       f2;
    private byte          f3;
    private Byte          f4;
    private short         f5;
    private Short         f6;
    private int           f7;
    private Integer       f8;
    private long          f9;
    private Long          f10;
    private BigInteger    f11;
    private BigDecimal    f12;
    private String        f13;
    private Object        f14;
    private float         f15;
    private Float         f16;
    private double        f17;
    private Double        f18;
    private Date          d1;
    private Date          d2;

    private String        a1;
    private String        a2;
    private String        a3;

    private List<Boolean> l0 = new ArrayList<Boolean>();
    private List<Byte>    l1 = new ArrayList<Byte>();
    private List<Short>   l2 = new ArrayList<Short>();
    private List<Integer> l3 = new ArrayList<Integer>();
    private List<Long>    l4 = new ArrayList<Long>();

    @JSONField(name = "A1")
    public String getA1() {
        return a1;
    }

    @JSONField(name = "A1")
    public void setA1(String a1) {
        this.a1 = a1;
    }

    @JSONField(serialize = false)
    public String getA2() {
        return a2;
    }

    @JSONField(deserialize = false)
    public void setA2(String a2) {
        this.a2 = a2;
    }

    @JSONField(serialize = true)
    public String getA3() {
        return a3;
    }

    @JSONField(deserialize = true)
    public void setA3(String a3) {
        this.a3 = a3;
    }

    public static TestEntity getInstance_0() {
        return instance_0;
    }

    public boolean isF1() {
        return f1;
    }

    public void setF1(boolean f1) {
        this.f1 = f1;
    }

    public Boolean getF2() {
        return f2;
    }

    public void setF2(Boolean f2) {
        this.f2 = f2;
    }

    public byte getF3() {
        return f3;
    }

    public void setF3(byte f3) {
        this.f3 = f3;
    }

    public Byte getF4() {
        return f4;
    }

    public void setF4(Byte f4) {
        this.f4 = f4;
    }

    public short getF5() {
        return f5;
    }

    public void setF5(short f5) {
        this.f5 = f5;
    }

    public Short getF6() {
        return f6;
    }

    public void setF6(Short f6) {
        this.f6 = f6;
    }

    public int getF7() {
        return f7;
    }

    public void setF7(int f7) {
        this.f7 = f7;
    }

    public Integer getF8() {
        return f8;
    }

    public void setF8(Integer f8) {
        this.f8 = f8;
    }

    public long getF9() {
        return f9;
    }

    public void setF9(long f9) {
        this.f9 = f9;
    }

    public Long getF10() {
        return f10;
    }

    public void setF10(Long f10) {
        this.f10 = f10;
    }

    public BigInteger getF11() {
        return f11;
    }

    public void setF11(BigInteger f11) {
        this.f11 = f11;
    }

    public BigDecimal getF12() {
        return f12;
    }

    public void setF12(BigDecimal f12) {
        this.f12 = f12;
    }

    public String getF13() {
        return f13;
    }

    public void setF13(String f13) {
        this.f13 = f13;
    }

    public Object getF14() {
        return f14;
    }

    public void setF14(Object f14) {
        this.f14 = f14;
    }

    public List<Boolean> getL0() {
        return l0;
    }

    public void setL0(List<Boolean> l0) {
        this.l0 = l0;
    }

    public List<Byte> getL1() {
        return l1;
    }

    public void setL1(List<Byte> l1) {
        this.l1 = l1;
    }

    public List<Short> getL2() {
        return l2;
    }

    public void setL2(List<Short> l2) {
        this.l2 = l2;
    }

    public List<Integer> getL3() {
        return l3;
    }

    public void setL3(List<Integer> l3) {
        this.l3 = l3;
    }

    public List<Long> getL4() {
        return l4;
    }

    public void setL4(List<Long> l4) {
        this.l4 = l4;
    }

    public float getF15() {
        return f15;
    }

    public void setF15(float f15) {
        this.f15 = f15;
    }

    public Float getF16() {
        return f16;
    }

    public void setF16(Float f16) {
        this.f16 = f16;
    }

    public double getF17() {
        return f17;
    }

    public void setF17(double f17) {
        this.f17 = f17;
    }

    public Double getF18() {
        return f18;
    }

    public void setF18(Double f18) {
        this.f18 = f18;
    }

    public Date getD1() {
        return d1;
    }

    public void setD1(Date d1) {
        this.d1 = d1;
    }

    public Date getD2() {
        return d2;
    }

    public void setD2(Date d2) {
        this.d2 = d2;
    }

    public static TestEntity instance_0;

    static {
        instance_0 = new TestEntity();
        instance_0.setF1(true);
        instance_0.setF2(Boolean.TRUE);
        instance_0.setF3((byte) 123);
        instance_0.setF4((byte) 123);
        instance_0.setF5((short) 123);
        instance_0.setF6((short) 123);
        instance_0.setF7((int) 123);
        instance_0.setF8((int) 123);
        instance_0.setF9((long) 123);
        instance_0.setF10((long) 123);
        instance_0.setF11(new BigInteger("123"));
        instance_0.setF12(new BigDecimal("123"));
        instance_0.setF13("abc");
        instance_0.setF14(null);
        instance_0.setF15(12.34F);
        instance_0.setF16(12.34F);
        instance_0.setF17(12.345D);
        instance_0.setF18(12.345D);
    }
}
