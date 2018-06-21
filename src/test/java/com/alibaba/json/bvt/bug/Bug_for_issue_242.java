package com.alibaba.json.bvt.bug;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class Bug_for_issue_242 extends TestCase {

    public void test_true() throws Exception {
        final String text = "{int1:\"NULL\",int2:\"null\",long1:NULL,long2:null, dou1:\"NULL\",dou2:\"null\",str1:\"NULL\",str2:NULL, bool2:\"NULL\",bool1:null}";
        VO vo = JSON.parseObject(text, VO.class);

        System.out.println(vo);
    }

    public static class VO {

        public int     int1;
        public int     int2;
        public long    long1;
        public long    long2;
        public double  dou1;
        public double  dou2;
        public boolean bool1;
        public boolean bool2;
        public String  str1;
        public String  str2;

        public VO(){

        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("int1 = ").append(int1)//
            .append(" int2 = ").append(int2)//
            .append(" long1 = ").append(long1)//
            .append(" long2 = ").append(long2)//
            .append(" dou1 = ").append(dou1)//
            .append(" dou2 = ").append(dou2)//
            .append(" bool1 = ").append(bool1)//
            .append(" bool2 = ").append(bool2)//
            .append(" str1 = ").append(str2)//
            .append(" str2 = ").append(str2);
            return sb.toString();
        }

        public int getInt1() {
            return int1;
        }

        public void setInt1(int int1) {
            this.int1 = int1;
        }

        public int getInt2() {
            return int2;
        }

        public void setInt2(int int2) {
            this.int2 = int2;
        }

        public long getLong1() {
            return long1;
        }

        public void setLong1(long long1) {
            this.long1 = long1;
        }

        public long getLong2() {
            return long2;
        }

        public void setLong2(long long2) {
            this.long2 = long2;
        }

        public double getDou1() {
            return dou1;
        }

        public void setDou1(double dou1) {
            this.dou1 = dou1;
        }

        public double getDou2() {
            return dou2;
        }

        public void setDou2(double dou2) {
            this.dou2 = dou2;
        }

        public boolean isBool1() {
            return bool1;
        }

        public void setBool1(boolean bool1) {
            this.bool1 = bool1;
        }

        public boolean isBool2() {
            return bool2;
        }

        public void setBool2(boolean bool2) {
            this.bool2 = bool2;
        }

        public String getStr1() {
            return str1;
        }

        public void setStr1(String str1) {
            this.str1 = str1;
        }

        public String getStr2() {
            return str2;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
        }

    }
}
