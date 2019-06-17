package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONObject;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jiangyu on 2019-03-08 22:30.
 */
public class Java8InstantDeserializeFromMilliTest {

  @Test
  public void test() {
    MilliSecondObject time = new MilliSecondObject();
    time.setTime(System.currentTimeMillis());
    String timeJson = JSONObject.toJSONString(time);
    InstantObject instantObject = JSONObject.parseObject(timeJson, InstantObject.class);
    Assert.assertEquals(time.getTime(), instantObject.time.toEpochMilli());
  }


  public static class InstantObject {

    private Instant time;

    public Instant getTime() {
      return time;
    }

    public void setTime(Instant time) {
      this.time = time;
    }

  }

  public static class MilliSecondObject {

    private long time;

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }
  }

}
