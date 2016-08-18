package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;

import junit.framework.TestCase;

public class Bug_for_issue_465 extends TestCase {
    public void test_for_issue() throws Exception {
        String json = "[\"abc\",\"efg\",\"sss\",[1,2]]";
        
        TestBean testBean = JSON.parseObject(json, TestBean.class);
        Assert.assertEquals("abc", testBean.name);
        Assert.assertEquals("efg", testBean.country);
        Assert.assertEquals("sss", testBean.password);
        Assert.assertEquals(2, testBean.location.length);
        Assert.assertEquals(1, testBean.location[0]);
        Assert.assertEquals(2, testBean.location[1]);
    }
    
    public void f_test_for_issue_private() throws Exception {
        String json = "[\"abc\",\"efg\",\"sss\",[1,2]]";
        
        TestBean1 testBean = JSON.parseObject(json, TestBean1.class);
        Assert.assertEquals("abc", testBean.name);
        Assert.assertEquals("efg", testBean.country);
        Assert.assertEquals("sss", testBean.password);
        Assert.assertEquals(2, testBean.location.length);
        Assert.assertEquals(1, testBean.location[0]);
        Assert.assertEquals(2, testBean.location[1]);
    }
    
    @JSONType(parseFeatures = Feature.SupportArrayToBean)
    public static class TestBean {
      private String name;

      private String password;

      private String country;

      private int[] location;

      public String getName() {
        return name;
      }

      @JSONField(ordinal = 0)
      public void setName(String name) {
        this.name = name;
      }

      public String getPassword() {
        return password;
      }

      @JSONField(ordinal = 2)
      public void setPassword(String password) {
        this.password = password;
      }

      public String getCountry() {
        return country;
      }

      @JSONField(ordinal = 1)
      public void setCountry(String country) {
        this.country = country;
      }

      public int[] getLocation() {
        return location;
      }

      @JSONField(ordinal = 3)
      public void setLocation(int[] location) {
        this.location = location;
      }
    }
    
    @JSONType(parseFeatures = Feature.SupportArrayToBean)
    private static class TestBean1 {
      private String name;

      private String password;

      private String country;

      private int[] location;

      public String getName() {
        return name;
      }

      @JSONField(ordinal = 0)
      public void setName(String name) {
        this.name = name;
      }

      public String getPassword() {
        return password;
      }

      @JSONField(ordinal = 2)
      public void setPassword(String password) {
        this.password = password;
      }

      public String getCountry() {
        return country;
      }

      @JSONField(ordinal = 1)
      public void setCountry(String country) {
        this.country = country;
      }

      public int[] getLocation() {
        return location;
      }

      @JSONField(ordinal = 3)
      public void setLocation(int[] location) {
        this.location = location;
      }
    }
}
