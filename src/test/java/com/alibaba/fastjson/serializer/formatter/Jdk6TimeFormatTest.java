package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangyu on 2019-03-09 00:57.
 */
public class Jdk6TimeFormatTest {


  @Test
  public void dateTest() {
    Calendar date = Calendar.getInstance();
    date.set(2019, Calendar.JANUARY, 1, 1, 1, 1);
    date.set(Calendar.MILLISECOND, 0);
    Time time = new Time(date);
    JSONObject json = (JSONObject) JSONObject.toJSON(time);
    System.out.println(json.toJSONString());
    Assert.assertEquals(date.getTime().toString(), json.getString("date1"));
    Assert.assertEquals("2019", json.getString("date2"));
    Assert.assertEquals("2019-01", json.getString("date3"));
    Assert.assertEquals("2019-01-01", json.getString("date4"));
    Assert.assertEquals("2019-01-01 01", json.getString("date5"));
    Assert.assertEquals("2019-01-01 01:01:01", json.getString("date6"));
    Assert.assertEquals("1546275661000", json.getString("date7"));
    Assert.assertEquals("2019-01-01 01", json.getString("date8"));
    Assert.assertEquals("2019-01-01 01:01:01", json.getString("date9"));
    Assert.assertEquals("2019-01-01", json.getString("date10"));
    Assert.assertEquals("2019-01-01 01", json.getString("date11"));
    Assert.assertEquals("2019-01-01 01:01:01.0", json.getString("date12"));
    Assert.assertEquals("2019-01-01 01", json.getString("date13"));
    Assert.assertEquals("01:01:01", json.getString("date14"));
    Assert.assertEquals("2019-01-01 01", json.getString("date15"));
    Assert.assertEquals("1546275661000", json.getString("date16"));
  }

  static class Time {

    public Time() {
    }

    public Time(Calendar time) {
      Date date = time.getTime();
      this.date1 = date;
      this.date2 = date;
      this.date3 = date;
      this.date4 = date;
      this.date5 = date;
      this.date6 = date;
      this.date16 = date;

      this.date7 = time;
      this.date8 = time;
      this.date9 = time;

      this.date10 = new java.sql.Date(date.getTime());
      this.date11 = new java.sql.Date(date.getTime());
      this.date12 = new java.sql.Timestamp(date.getTime());
      this.date13 = new java.sql.Timestamp(date.getTime());
      this.date14 = new java.sql.Time(date.getTime());
      this.date15 = new java.sql.Time(date.getTime());
    }

    private Date date1;
    @JSONField(format = "yyyy")
    private Date date2;
    @JSONField(format = "yyyy-MM")
    private Date date3;
    @JSONField(format = "yyyy-MM-dd")
    private Date date4;
    @JSONField(format = "yyyy-MM-dd hh")
    private Date date5;
    @JSONField(format = "yyyy-MM-dd hh:MM:ss")
    private Date date6;
    private Calendar date7;
    @JSONField(format = "yyyy-MM-dd hh")
    private Calendar date8;
    @JSONField(format = "yyyy-MM-dd hh:MM:ss")
    private Calendar date9;

    private java.sql.Date date10;
    @JSONField(format = "yyyy-MM-dd hh")
    private java.sql.Date date11;

    private java.sql.Timestamp date12;
    @JSONField(format = "yyyy-MM-dd hh")
    private java.sql.Timestamp date13;
    private java.sql.Time date14;
    @JSONField(format = "yyyy-MM-dd hh")
    private java.sql.Time date15;
    @JSONField(format = "unixtime")
    private Date date16;

    public Date getDate1() {
      return date1;
    }

    public void setDate1(Date date1) {
      this.date1 = date1;
    }

    public Date getDate2() {
      return date2;
    }

    public void setDate2(Date date2) {
      this.date2 = date2;
    }

    public Date getDate3() {
      return date3;
    }

    public void setDate3(Date date3) {
      this.date3 = date3;
    }

    public Date getDate4() {
      return date4;
    }

    public void setDate4(Date date4) {
      this.date4 = date4;
    }

    public Date getDate5() {
      return date5;
    }

    public void setDate5(Date date5) {
      this.date5 = date5;
    }

    public Date getDate6() {
      return date6;
    }

    public void setDate6(Date date6) {
      this.date6 = date6;
    }

    public Calendar getDate7() {
      return date7;
    }

    public void setDate7(Calendar date7) {
      this.date7 = date7;
    }

    public Calendar getDate8() {
      return date8;
    }

    public void setDate8(Calendar date8) {
      this.date8 = date8;
    }

    public Calendar getDate9() {
      return date9;
    }

    public void setDate9(Calendar date9) {
      this.date9 = date9;
    }

    public java.sql.Date getDate10() {
      return date10;
    }

    public void setDate10(java.sql.Date date10) {
      this.date10 = date10;
    }

    public java.sql.Date getDate11() {
      return date11;
    }

    public void setDate11(java.sql.Date date11) {
      this.date11 = date11;
    }

    public Timestamp getDate12() {
      return date12;
    }

    public void setDate12(Timestamp date12) {
      this.date12 = date12;
    }

    public Timestamp getDate13() {
      return date13;
    }

    public void setDate13(Timestamp date13) {
      this.date13 = date13;
    }

    public java.sql.Time getDate14() {
      return date14;
    }

    public void setDate14(java.sql.Time date14) {
      this.date14 = date14;
    }

    public java.sql.Time getDate15() {
      return date15;
    }

    public void setDate15(java.sql.Time date15) {
      this.date15 = date15;
    }

    public Date getDate16() {
      return date16;
    }

    public void setDate16(Date date16) {
      this.date16 = date16;
    }
  }
}
