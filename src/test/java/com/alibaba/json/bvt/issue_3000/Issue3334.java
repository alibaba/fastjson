package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author ：Nanqi
 * @Date ：Created in 00:26 2020/7/9
 */
public class Issue3334 extends TestCase {
    public void test_for_issue_1() throws Exception {
        String simpleJSON = "{\"name\":\"nanqi\",\"ageInt\":999999999999999999999999999999999" +
                ",\"ageInteger\":999999999999999999999999999999999" +
                ",\"ageString\":998" +
                ",\"date\":\"20200709000000\"}";
        Simple simple = JSON.parseObject(simpleJSON, Simple.class);
        assertEquals(0, simple.getAgeInt());
        assertNull(simple.getAgeInteger());
        assertEquals(0.0, simple.getAgeDouble());
        assertNull(simple.getAgeFloat());
        assertEquals("nanqi", simple.getName());
    }

    public void test_for_issue_2() {
        String simpleJSON = "{\"name\":\"nanqi\",\"ageInt\":999999999999999999999999999999999" +
                ",\"ageInteger\":999999999999999999999999999999999" +
                ",\"ageString\":998" +
                ",\"date\":\"nanqi\"}";
        try {
            JSON.parseObject(simpleJSON, Simple.class);
        } catch (Exception e) {
            assertEquals(e.getClass(), JSONException.class);
        }
    }

    static class Simple implements Serializable {
        private String name;

        @JSONField(ignoreException = true)
        private int ageInt;

        @JSONField(ignoreException = true)
        private Integer ageInteger;

        @JSONField(ignoreException = true)
        private String ageString;

        @JSONField(ignoreException = true)
        private double ageDouble;

        @JSONField(ignoreException = true)
        private Float ageFloat;

        private Date date;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public int getAgeInt() {
            return ageInt;
        }

        public void setAgeInt(int ageInt) {
            this.ageInt = ageInt;
        }

        public Integer getAgeInteger() {
            return ageInteger;
        }

        public void setAgeInteger(Integer ageInteger) {
            this.ageInteger = ageInteger;
        }

        public String getAgeString() {
            return ageString;
        }

        public void setAgeString(String ageString) {
            this.ageString = ageString;
        }

        public double getAgeDouble() {
            return ageDouble;
        }

        public void setAgeDouble(double ageDouble) {
            this.ageDouble = ageDouble;
        }

        public Float getAgeFloat() {
            return ageFloat;
        }

        public void setAgeFloat(Float ageFloat) {
            this.ageFloat = ageFloat;
        }
    }
}
