package com.alibaba.fastjson.serializer.formatter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangyu on 2019-03-09 00:57.
 */
public class Jdk8TimeFormatTest {

  @Test
  public void jdk8TimeTest() {
    Jdk8Time time = new Jdk8Time(Instant.parse("2019-01-01T01:01:01.000Z"));
    JSONObject json = (JSONObject) JSONObject.toJSON(time);
    System.out.println(json.toJSONString());
    Assert.assertEquals("2019-01-01T01:01:01Z",json.getString("time") );
    Assert.assertEquals("2019-01-01",json.getString("time1") );
    Assert.assertEquals("2019-01-01 09:01:01.000",json.getString("time2") );
    Assert.assertEquals("2019-01-01",json.getString("time3") );
    Assert.assertEquals("2019-01-01 09:01:01.000+0800",json.getString("time4") );
    Assert.assertEquals("2019-01-01",json.getString("time5"));
    Assert.assertEquals("2019-01-01 09:01:01",json.getString("time6") );
    Assert.assertEquals("2019-01-01",json.getString("time7") );
    Assert.assertEquals("2019-01",json.getString("time8") );
    Assert.assertEquals("1546304461000",json.getString("time9") );
  }

  static class Jdk8Time {

    public Jdk8Time() {
    }

    public Jdk8Time(Instant instant) {
      this.time = instant;
      this.time1 = instant;
      this.time2 = instant;
      this.time3 = ZonedDateTime.ofInstant(instant, JSON.defaultTimeZone.toZoneId());
      this.time4 = ZonedDateTime.ofInstant(instant, JSON.defaultTimeZone.toZoneId());
      this.time5 = LocalDateTime.ofInstant(instant, JSON.defaultTimeZone.toZoneId());
      this.time9 = ZonedDateTime.ofInstant(instant, JSON.defaultTimeZone.toZoneId());
      this.time6 = LocalDateTime.ofInstant(instant, JSON.defaultTimeZone.toZoneId());
      this.time7 = LocalDate.from(time3);
      this.time8 = LocalDate.from(time3);
    }
    @JSONField(format = "yyyy-MM-dd")
    private Long otherType;
    private Instant time;
    @JSONField(format = "yyyy-MM-dd")
    private Instant time1;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Instant time2;
    @JSONField(format = "yyyy-MM-dd")
    private ZonedDateTime time3;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss.SSSZ")
    private ZonedDateTime time4;
    @JSONField(format = "yyyy-MM-dd")
    private LocalDateTime time5;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time6;
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate time7;
    @JSONField(format = "yyyy-MM")
    private LocalDate time8;
    @JSONField(format = "unixtime")
    private ZonedDateTime time9;

    public Long getOtherType() {
      return otherType;
    }

    public void setOtherType(Long otherType) {
      this.otherType = otherType;
    }

    public Instant getTime() {
      return time;
    }

    public void setTime(Instant time) {
      this.time = time;
    }

    public Instant getTime1() {
      return time1;
    }

    public void setTime1(Instant time1) {
      this.time1 = time1;
    }

    public Instant getTime2() {
      return time2;
    }

    public void setTime2(Instant time2) {
      this.time2 = time2;
    }

    public ZonedDateTime getTime3() {
      return time3;
    }

    public void setTime3(ZonedDateTime time3) {
      this.time3 = time3;
    }

    public ZonedDateTime getTime4() {
      return time4;
    }

    public void setTime4(ZonedDateTime time4) {
      this.time4 = time4;
    }

    public LocalDateTime getTime5() {
      return time5;
    }

    public void setTime5(LocalDateTime time5) {
      this.time5 = time5;
    }

    public LocalDateTime getTime6() {
      return time6;
    }

    public void setTime6(LocalDateTime time6) {
      this.time6 = time6;
    }

    public LocalDate getTime7() {
      return time7;
    }

    public void setTime7(LocalDate time7) {
      this.time7 = time7;
    }

    public LocalDate getTime8() {
      return time8;
    }

    public void setTime8(LocalDate time8) {
      this.time8 = time8;
    }

    public ZonedDateTime getTime9() {
      return time9;
    }

    public void setTime9(ZonedDateTime time9) {
      this.time9 = time9;
    }
  }

}
