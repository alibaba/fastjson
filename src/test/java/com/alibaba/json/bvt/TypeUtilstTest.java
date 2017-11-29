/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.json.bvt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class TypeUtilstTest extends TestCase {

    public void test_0() throws Exception {

        List<Person> personList = new ArrayList<Person>();
        {
            Person p = new Person();
            p.setF1(true);
            p.setF2(true);
            p.setF3((byte) 3);
            p.setF4((byte) 4);
            p.setF5((short) 5);
            p.setF6((short) 6);
            p.setF7(7);
            p.setF8(8);
            p.setF9(9L);
            p.setF10(10L);
            p.setF11(new BigInteger("12345678901234567890123456789012345678901234567890"));
            p.setF12(new BigDecimal("1234567890123456789012345678901234567890.1234567890"));
            p.setF13("F13");
            p.setF14(new Date());
            p.setF15(15);
            p.setF16(16F);
            p.setF17(17);
            p.setF18(18D);
            personList.add(p);
        }
        {
            Person person = new Person();
            personList.add(person);
        }

        String jsonString = JSON.toJSONString(personList);


        JSON.parseArray(jsonString, Person.class);
        // CGLibExtJSONParser parser = new CGLibExtJSONParser(text);
    }

    public static class Person {

        private boolean    f1;
        private Boolean    f2;
        private byte       f3;
        private Byte       f4;
        private short      f5;
        private Short      f6;
        private int        f7;
        private Integer    f8;
        private long       f9;
        private Long       f10;
        private BigInteger f11;
        private BigDecimal f12;
        private String     f13;
        private Date       f14;
        private float      f15;
        private Float      f16;
        private double     f17;
        private Double     f18;

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

        public Date getF14() {
            return f14;
        }

        public void setF14(Date f14) {
            this.f14 = f14;
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

    }
}
