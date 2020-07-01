package com.alibaba.json.bvt.issue_2900;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

public class Issue2962 extends TestCase {
    private TimeZone original = TimeZone.getDefault();

    @Override
    public void tearDown () {
        TimeZone.setDefault(original);
        JSON.defaultTimeZone = original;
    }

    public void test_dates_different_timeZones() {
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone timeZone = TimeZone.getTimeZone(id);
            TimeZone.setDefault(timeZone);
            JSON.defaultTimeZone = timeZone;

            Calendar cal = Calendar.getInstance();
            Date now = cal.getTime();

            VO vo = new VO();
            vo.date = now;

            String json = JSON.toJSONString(vo);
            VO result = JSON.parseObject(json, VO.class);
            assertEquals(vo.date, result.date);

            // with iso-format
            json = JSON.toJSONString(vo, SerializerFeature.UseISO8601DateFormat);
            System.out.println(id + " " + json);
            result = JSON.parseObject(json, VO.class);
            assertEquals(JSON.toJSONString(vo.date), JSON.toJSONString(result.date));
        }
    }

    public static class VO {
        public Date date;
    }
}
